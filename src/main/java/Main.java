import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Edge> edges = new ArrayList<>();
        String startPoint = "Воронеж";
        edges.add(new Edge("Воронеж", "Москва", 26));
        edges.add(new Edge("Воронеж", "Волгоград", 42));
        edges.add(new Edge("Воронеж", "Липецк", 15));
        edges.add(new Edge("Воронеж", "Курск", 29));
        edges.add(new Edge("Воронеж", "Орел", 25));
        edges.add(new Edge("Москва", "Воронеж", 7));
        edges.add(new Edge("Москва", "Волгоград", 16));
        edges.add(new Edge("Москва", "Липецк", 1));
        edges.add(new Edge("Москва", "Курск", 30));
        edges.add(new Edge("Москва", "Орел", 25));
        edges.add(new Edge("Волгоград", "Воронеж", 20));
        edges.add(new Edge("Волгоград", "Москва", 13));
        edges.add(new Edge("Волгоград", "Липецк", 35));
        edges.add(new Edge("Волгоград", "Курск", 5));
        edges.add(new Edge("Волгоград", "Орел", 0));
        edges.add(new Edge("Липецк", "Воронеж", 21));
        edges.add(new Edge("Липецк", "Москва", 16));
        edges.add(new Edge("Липецк", "Волгоград", 25));
        edges.add(new Edge("Липецк", "Курск", 18));
        edges.add(new Edge("Липецк", "Орел", 21));
        edges.add(new Edge("Курск", "Воронеж", 12));
        edges.add(new Edge("Курск", "Москва", 46));
        edges.add(new Edge("Курск", "Волгоград", 27));
        edges.add(new Edge("Курск", "Липецк", 48));
        edges.add(new Edge("Курск", "Орел", 5));
        edges.add(new Edge("Орел", "Воронеж", 23));
        edges.add(new Edge("Орел", "Москва", 5));
        edges.add(new Edge("Орел", "Волгоград", 5));
        edges.add(new Edge("Орел", "Липецк", 9));
        edges.add(new Edge("Орел", "Курск", 5));

        Kommivoyazher k = new Kommivoyazher();

        List<Edge> finalResult= k.startSearch(edges, startPoint);
        double price = 0;
        for (Edge edge : finalResult) {
            System.out.println(edge.toString());
            price += edge.getWeight();
        }
        System.out.println("Стоимость поездки " + price);

    }
}
