import java.util.*;

public class Kommivoyazher {
    private static final double INF = 1.0/0.0;
    private boolean stopSearch = false;
    private double[][] adjacencyMatrix;
    private double[][] tempMatrix;
    private List<Way> foundWays = new ArrayList<>();
    private List<Way> tempWays = new ArrayList<>();
    private int max_i = Integer.MIN_VALUE;
    private int max_j = Integer.MIN_VALUE;
    private List<String> startingPoints = new ArrayList<>();
    private List<String> destinationPoints = new ArrayList<>();

    //вывод матрицы смежности
    private void printAdjacencyMatrix() {
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                if (adjacencyMatrix[i][j] == INF) {
                    System.out.print("INF" + "\t\t");
                } else {
                    System.out.print(adjacencyMatrix[i][j] + "\t\t");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    //вывод одномерных массивов
    private void print(double m[]) {
        for (int i = 0; i < m.length; i++) {
            System.out.print(m[i] + " ");
        }
        System.out.println("\n");
    }

    //составление матрицы смежности
    private void buildAdjacencyMatrix(List<Edge> edges){
        Set<String> setPoints = new LinkedHashSet<>(); // множество городов без повторений

        // из данных ребер сохраняем все города
        for (Edge edge : edges) {
            setPoints.add(edge.getStartPoint());
        }
        for (String setPoint : setPoints) {
            startingPoints.add(setPoint);
            destinationPoints.add(setPoint);
        }

        //инициализируем матрицу смежности
        adjacencyMatrix = new double[startingPoints.size()][startingPoints.size()];

        //заполняем матрицу смежности
        for (Edge edge : edges) {
            for (int i = 0; i < startingPoints.size(); i++) {
                if (edge.getStartPoint().equals(startingPoints.get(i))){
                    for (int j = 0; j < destinationPoints.size(); j++) {
                        if (edge.getDestinationPoint().equals(destinationPoints.get(j))){
                            adjacencyMatrix[i][j] = edge.getWeight();
                        }
                    }
                }
            }
        }
        //пересечения городов полагаем равным бесконечности
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            adjacencyMatrix[i][i] = INF;
        }
    }

    //находим минимальные значения из каждой строки матрицы
    private double[] findMinimumsOfRows() {
        double[] min_i = new double[adjacencyMatrix.length];
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            double min = INF;
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                if (adjacencyMatrix[i][j] < min) {
                    min = adjacencyMatrix[i][j];
                }
            }
            min_i[i]=min;
        }
        return min_i;
    }

    //находим минимальные значения из каждого столбца
    private double[] findMinimumsOfColumns() {
        double[] min_j = new double[adjacencyMatrix.length];
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            double min = INF;
            for (int j = 0; j <  adjacencyMatrix.length+1-1; j++) {           //m.length+1-1 чтобы не ругалось на дубликат
                if (adjacencyMatrix[j][i] < min) {
                    min = adjacencyMatrix[j][i];
                }
            }
            min_j[i] = min;
        }
        return min_j;
    }

    //редукция строк. Из каждой строки матрицы вычитаем соответствующий этой строке минимальный элемент
    private void rowsReduction() {
        double[] min_i = findMinimumsOfRows();
        print(min_i);
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                adjacencyMatrix[i][j]-=min_i[i];
            }
        }
    }

    //редукция столбцов. Из каждого столбца матрицы вычитаем соответствующий этому столбцу минимальный элемент
    private void columnsReduction() {
        double[] min_j = findMinimumsOfColumns();
        print(min_j);
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                adjacencyMatrix[j][i]-=min_j[i];
            }
        }
    }

    //нахождение оценки. Для каждого нулевого элемента ищем его оценку, которая равна сумме минимального значения по строке и столбцу (сам элемент в рассчет не берем)
    private double findEvaluation(int const_i, int const_j) {
        double min_i = INF;
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if (adjacencyMatrix[i][const_j] < min_i && i != const_i) {
                min_i = adjacencyMatrix[i][const_j];
            }
        }
        double min_j = INF;
        for (int j = 0; j < adjacencyMatrix.length; j++) {
            if (adjacencyMatrix[const_i][j] < min_j && j != const_j) {
                min_j = adjacencyMatrix[const_i][j];
            }
        }
        return min_i+min_j;
    }

    private void findEvaluationMatrix(){
        //оценочная матрица
        double[][] evaluationMatrix = new double[adjacencyMatrix.length][adjacencyMatrix.length];

        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                if (adjacencyMatrix[i][j] == 0) {
                    evaluationMatrix[i][j] = findEvaluation(i, j);
                } else {
                    evaluationMatrix[i][j] = -INF;
                }
            }
        }
        //нахождение максимального элемента оценочной матрицы. Запоминаем номер строки и столбца
        double maxElement = -INF;
        max_i = Integer.MIN_VALUE;
        max_i = Integer.MIN_VALUE;

        for (int i = 0; i < evaluationMatrix.length; i++) {
            for (int j = 0; j < evaluationMatrix.length; j++) {
                if (evaluationMatrix[i][j] >= maxElement) {
                    maxElement = evaluationMatrix[i][j];
                    max_i=i;
                    max_j=j;
                }
            }
        }
    }

    //редукция матрицы. Ищем оценочкую матрицу, состоящую из оценок для нулей. Находим максимальную оценку. Ту строку и столбец, где располагается максимальная оценка
    //вычеркиваем. Элемент, соответствующий обратному пути (если он есть) делаем равным INF
    private void matrixReduction() {
        //обратный путь, если он существует, делаем равным INF
        for (int j = 0; j < adjacencyMatrix.length; j++) {
            if (destinationPoints.get(j).equals(startingPoints.get(max_i))) {
                for (int i = 0; i < adjacencyMatrix.length; i++) {
                    if (startingPoints.get(i).equals(destinationPoints.get(max_j))) {
                        adjacencyMatrix[i][j] = INF;
                    }
                }
            }
        }

        //добавление start и finish в массив foundWays
        foundWays.add(new Way(startingPoints.get(max_i), destinationPoints.get(max_j)));
        startingPoints.remove(max_i);
        destinationPoints.remove(max_j);

        //удаление строки и столбца, в которой максимальный оценочный элемент
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = max_j; j < adjacencyMatrix.length-1; j++) {
                adjacencyMatrix[i][j] = adjacencyMatrix[i][j+1];
            }
        }
        for (int i = max_i; i < adjacencyMatrix.length-1; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                adjacencyMatrix[i][j] = adjacencyMatrix[i+1][j];
            }
        }
        double[][] newMatrix = new double[adjacencyMatrix.length-1][adjacencyMatrix.length-1];
        for (int i = 0; i < newMatrix.length; i++) {
            for (int j = 0; j < newMatrix.length; j++) {
                newMatrix[i][j]=adjacencyMatrix[i][j];
            }
        }

        //флаг остановки программы, когда размер матрицы 1
        if (newMatrix.length == 1) {
            stopSearch = true;
            foundWays.add(new Way(startingPoints.get(0), destinationPoints.get(0)));
        }

        adjacencyMatrix = newMatrix;
    }
    private void saveTempMatrix() {
        tempMatrix = new double[adjacencyMatrix.length][adjacencyMatrix.length];
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                tempMatrix[i][j] = adjacencyMatrix[i][j];
            }
        }
    }

    //составляем временный путь и смотрим в нем подциклы
    private boolean checkSubcycle() {
        boolean checkForCreateNewWay = true;
        if (tempWays.size() == 0) {
            tempWays.add(new Way(startingPoints.get(max_i), destinationPoints.get(max_j)));
            checkForCreateNewWay = false;
        } else {
            for (Way tempWay : tempWays) {
                if (startingPoints.get(max_i).equals(tempWay.getFinish()) && destinationPoints.get(max_j).equals(tempWay.getStart())) {
                    return false;
                } else {
                    if (startingPoints.get(max_i).equals(tempWay.getFinish())) {
                        tempWay.setFinish(destinationPoints.get(max_j));
                        checkForCreateNewWay = false;
                        break;
                    } else {
                        if (destinationPoints.get(max_j).equals(tempWay.getStart())) {
                            tempWay.setStart(startingPoints.get(max_i));
                            checkForCreateNewWay = false;
                            break;
                        }
                    }
                }
            }
        }
        if (checkForCreateNewWay) {
            tempWays.add(new Way(startingPoints.get(max_i), destinationPoints.get(max_j)));
        } else {
            for (int i = 0; i < tempWays.size(); i++) {
                for (int j = 0; j < tempWays.size(); j++) {
                    if (tempWays.get(i).getFinish().equals(tempWays.get(j).getStart())) {
                        tempWays.get(i).setFinish(tempWays.get(j).getFinish());
                        tempWays.remove(j);
                        break;
                    }
                }
            }
        }
        return true;
    }

    private List<Edge> getFinalResult(String startPoint, List<Edge> edges){
        List<Edge> finalResult = new LinkedList<>();
        do {
            for (Way foundWay : foundWays) {
                if (startPoint.equals(foundWay.getStart())) {
                    for (Edge edge : edges) {
                        if (edge.getStartPoint().equals(foundWay.getStart()) && edge.getDestinationPoint().equals(foundWay.getFinish())) {
                            finalResult.add(edge);
                            startPoint = foundWay.getFinish();
                        }
                    }
                }
            }
        } while (!finalResult.get(0).getStartPoint().equals(finalResult.get(finalResult.size()-1).getDestinationPoint()));
        return finalResult;
    }

    private void search(){
        boolean needSave = true;
        while (!stopSearch) {
            printAdjacencyMatrix();
            if (needSave) {
                saveTempMatrix();
            }
            findMinimumsOfRows();
            rowsReduction();
            printAdjacencyMatrix();
            findMinimumsOfColumns();
            columnsReduction();
            printAdjacencyMatrix();
            findEvaluationMatrix();

            if (checkSubcycle()){
                needSave = true;
                matrixReduction();
            } else {
                System.out.println("\n\nОбнаружен подцикл! Проделываем последний шаг еще раз заменив ребро на INF");
                needSave = false;
                tempMatrix[max_i][max_j] = INF;
                adjacencyMatrix = tempMatrix;
                search();

            }
        }
    }

    public void startSearch(List<Edge> edges){
        buildAdjacencyMatrix(edges);
        search();
        List<Edge> finalResult = getFinalResult("Воронеж", edges);
        double price = 0;
        for (Edge edge : finalResult) {
            System.out.println(edge.toString());
            price += edge.getWeight();
        }
        System.out.println("Стоимость поездки " + price);
    }
}