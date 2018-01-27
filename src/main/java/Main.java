public class Main {
    public static void main(String[] args) {

        Kommivoyazher k = new Kommivoyazher();

        //исходные данные
        double inf = 1.0/0.0;

        double[][] matrix = {{0, 1, 2, 3, 4, 5},
                {1, inf, 20, 18, 12, 8},
                {2, 5, inf, 14, 7, 11},
                {3, 12, 18, inf, 6, 11},
                {4, 11, 17, 11, inf, 12},
                {5, 5, 5, 5, 5, inf}
        };

        //инициализация массива путей
        k.finishWayInitialization(matrix.length);
        //сохраняем начальную матрицу, потому что потом из нее будем вытаскивать значения
        k.saveMatrix(matrix);

        while (!k.isStopSearch()) {
            System.out.println("\nНовый проход!");

            System.out.print("\n\nНачальная матрица:");
            k.print(matrix);

            System.out.println("\n\nМассив минимумов по строкам:");
            k.rowsReduction(matrix);

            System.out.print("\n\nМатрица после редукции строк:");
            k.print(matrix);

            System.out.println("\n\nМассив минимумов по столбцам: ");
            k.columnsReduction(matrix);

            System.out.print("\n\nМатрица после редукции столбцов:");
            k.print(matrix);

            matrix = k.matrixReduction(matrix);
            System.out.print("\n\nМатрица после редукции матрицы:");
            k.print(matrix);
        }

        k.wayToString();
    }
}
