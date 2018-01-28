import java.util.ArrayList;
import java.util.List;

public class Kommivoyazher {
    private static final double INF = 1.0/0.0;
    private boolean stopSearch = false;
    private double[][] initialMatrix;
    private double[][] tempMatrix;
    private List<Way> tempWays = new ArrayList<>();
    private List<Way> foundWays = new ArrayList<>();
    private int max_i = Integer.MIN_VALUE;
    private int max_j = Integer.MIN_VALUE;

    //вывод матрицы
    private void print(double m[][]) {
        for (int i = 0; i < m.length; i++) {
            System.out.println();
            for (int j = 0; j < m.length; j++) {
                if (m[i][j] == INF) {
                    System.out.print("INF" + "\t\t");
                } else {
                    if (i == 0 || j == 0) {
                        System.out.print((int) m[i][j] + "\t\t");
                    } else {
                        System.out.print(m[i][j] + "\t\t");
                    }

                }
            }
        }
    }

    //вывод одномерного массива (минимумов по строкам и столбцам)
    private void print(double m[]) {
        for (int i = 0; i < m.length; i++) {
            System.out.print(m[i] + " ");
        }
    }

    //нахождение минимальных элементов всех строк. Проходим по строкам и в каждой строке ищем минимальный элемент. Записываем его в массив min_i
    private double[] findMinimumsOfRows(double m[][]) {
        double[] min_i = new double[m.length-1];
        for (int i = 1; i < m.length; i++) {
            double min = INF;
            for (int j = 1; j < m.length; j++) {
                if (m[i][j] < min) {
                    min = m[i][j];
                }
                if (j == m.length - 1) {
                    min_i[i-1]=min;
                }
            }
        }
        return min_i;
    }

    //нахождение минимальных элементов всех столбцов. Проходим по столбцам и в каждом столбце ищем минимальный элемент. Записываем его в массив min_j
    private double[] findMinimumsOfColumns(double m[][]) {
        double[] min_j = new double[m.length-1];
        for (int i = 1; i < m.length; i++) {
            double min = INF;
            for (int j = 1; j <  m.length+1-1; j++) {           //m.length+1-1 чтобы не ругалось на дубликат
                if (m[j][i] < min) {
                    min = m[j][i];
                }
                if (j == m.length - 1) {
                    min_j[i-1] = min;
                }
            }
        }
        return min_j;
    }

    //редукция строк. Из каждой строки матрицы вычитаем соответствующий этой строке минимальный элемент
    private void rowsReduction(double m[][]) {
        double[] min_i = findMinimumsOfRows(m);
        print(min_i);
        for (int i = 1; i < m.length; i++) {
            for (int j = 1; j < m.length; j++) {
                m[i][j]-=min_i[i-1];
            }
        }
    }

    //редукция столбцов. Из каждого столбца матрицы вычитаем соответствующий этому столбцу минимальный элемент
    private void columnsReduction(double m[][]) {
        double[] min_j = findMinimumsOfColumns(m);
        print(min_j);
        for (int i = 1; i < m.length; i++) {
            for (int j = 1; j < m.length; j++) {
                m[j][i]-=min_j[i-1];
            }
        }
    }

    //нахождение оценки. Для каждого нулевого элемента ищем его оценку, которая равна сумме минимального значения по строке и столбцу (сам элемент в рассчет не берем)
    private double findEvaluation(double m[][], int const_i, int const_j) {
        double min_i = INF;
        for (int i = 1; i < m.length; i++) {
            if (m[i][const_j] < min_i && i != const_i) {
                min_i = m[i][const_j];
            }
        }
        double min_j = INF;
        for (int j = 1; j < m.length; j++) {
            if (m[const_i][j] < min_j && j != const_j) {
                min_j = m[const_i][j];
            }
        }
        return min_i+min_j;
    }

    private void findEvaluationMatrix(double m[][]){
        //оценочная матрица
        double[][] evaluationMatrix = new double[m.length-1][m.length-1];

        for (int i = 1; i < m.length; i++) {
            for (int j = 1; j < m.length; j++) {
                if (m[i][j] == 0) {
                    evaluationMatrix[i-1][j-1] = findEvaluation(m, i, j);
                } else {
                    evaluationMatrix[i-1][j-1] = -INF;
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
                    max_i=i+1;
                    max_j=j+1;
                }
            }
        }
    }

    //составляем временный путь и смотрим в нем подциклы
    private boolean checkSubcycle(double m[][]) {
        boolean checkForCreateNewWay = true;
        if (tempWays.size() == 0) {
            tempWays.add(new Way((int) m[max_i][0], (int) m[0][max_j]));
            checkForCreateNewWay = false;
        } else {
            for (Way tempWay : tempWays) {
                if (m[max_i][0] == tempWay.getFinish() && m[0][max_j] == tempWay.getStart()) {
                    return false;
                } else {
                    if (m[max_i][0] == tempWay.getFinish()) {
                        tempWay.setFinish((int) m[0][max_j]);
                        checkForCreateNewWay = false;
                        break;
                    } else {
                        if (m[0][max_j] == tempWay.getStart()) {
                            tempWay.setStart((int) m[max_i][0]);
                            checkForCreateNewWay = false;
                            break;
                        }
                    }
                }
            }
        }
        if (checkForCreateNewWay) {
            tempWays.add(new Way((int) m[max_i][0], (int) m[0][max_j]));
        } else {
            for (int i = 0; i < tempWays.size(); i++) {
                for (int j = 0; j < tempWays.size(); j++) {
                    if (tempWays.get(i).getFinish() == tempWays.get(j).getStart()) {
                        tempWays.get(i).setFinish(tempWays.get(j).getFinish());
                        tempWays.remove(j);
                        break;
                    }
                }
            }
        }
        return true;
    }

    //редукция матрицы. Ищем оценочкую матрицу, состоящую из оценок для нулей. Находим максимальную оценку. Ту строку и столбец, где располагается максимальная оценка
    //вычеркиваем. Элемент, соответствующий обратному пути (если он есть) делаем равным INF
    private double[][] matrixReduction(double m[][]) {
        //обратный путь, если он существует, делаем равным INF
        for (int j = 0; j < m.length; j++) {
            if (m[0][j] == m[max_i][0]) {
                for (int i = 0; i < m.length; i++) {
                    if (m[i][0] == m[0][max_j]) {
                        m[i][j] = INF;
                    }
                }
            }
        }

        //добавление start и finish в массив foundWays
        foundWays.add(new Way((int)m[max_i][0], (int)m[0][max_j]));

        //удаление строки и столбца, в которой максимальный оценочный элемент
        for (int i = 0; i < m.length; i++) {
            for (int j = max_j; j < m.length-1; j++) {
                m[i][j] = m[i][j+1];
            }
        }
        for (int i = max_i; i < m.length-1; i++) {
            for (int j = 0; j < m.length; j++) {
                m[i][j] = m[i+1][j];
            }
        }
        double[][] newMatrix = new double[m.length-1][m.length-1];
        for (int i = 0; i < m.length-1; i++) {
            for (int j = 0; j < m.length-1; j++) {
                newMatrix[i][j]=m[i][j];
            }
        }

        //флаг остановки программы, когда размер матрицы 2
        if (newMatrix.length == 2) {
            stopSearch = true;
            foundWays.add(new Way((int)newMatrix[1][0], (int)newMatrix[0][1]));
        }
        return newMatrix;
    }

    //сохраняем заданную матрицу, чтобы потом получить из нее edges
    public void saveInitialMatrix(double m[][]) {
        initialMatrix = new double[m.length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                initialMatrix[i][j] = m[i][j];
            }

        }
    }

    private void saveTempMatrix(double m[][]) {
        tempMatrix = new double[m.length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                tempMatrix[i][j] = m[i][j];
            }

        }
    }

    private double[][] getTempMatrix(){
        tempMatrix[max_i][max_j] = INF;
        return tempMatrix;
    }

    public void installEdgesForFoundWays() {
        for (Way foundWay : foundWays) {
            int iEdge = -1, jEdge = -1;
            for (int j = 0; j < initialMatrix.length; j++) {
                if (initialMatrix[j][0] == foundWay.getStart()) {
                    iEdge = j;
                }
                if (initialMatrix[0][j] == foundWay.getFinish()) {
                    jEdge = j;
                }
            }
            foundWay.setEdge(initialMatrix[iEdge][jEdge]);
        }
    }

    public void printFoundWays() {
        double sum = 0;
        System.out.println("\n\nНайденные пути");
        for (Way foundWay : foundWays) {
            System.out.println(foundWay.toString());
            sum+=foundWay.getEdge();
        }
        System.out.println("Общая длина маршрута = " + sum);
    }

    public void startSearch(double m[][]){
        boolean needSave = true;
        while (!stopSearch) {
            System.out.println("\n\nНовый проход!");

            System.out.print("\nНачальная матрица:");
            print(m);

            if (needSave) {
                saveTempMatrix(m);
            }

            System.out.println("\n\nМассив минимумов по строкам:");
            rowsReduction(m);

            System.out.print("\n\nМатрица после редукции строк:");
            print(m);

            System.out.println("\n\nМассив минимумов по столбцам: ");
            columnsReduction(m);

            System.out.print("\n\nМатрица после редукции столбцов:");
            print(m);

            findEvaluationMatrix(m);

            if (checkSubcycle(m)){
                needSave = true;
                m = matrixReduction(m);
                System.out.print("\n\nМатрица после редукции матрицы:");
                print(m);
            } else {
                System.out.println("\n\nОбнаружен подцикл! Проделываем последний шаг еще раз заменив ребро на M");
                needSave = false;
                m = getTempMatrix();
                startSearch(m);

            }

        }
    }
}