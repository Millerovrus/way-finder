public class Way {
    private int start;
    private int finish;
    private double edge;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public double getEdge() {
        return edge;
    }

    public void setEdge(double edge) {
        this.edge = edge;
    }

    @Override
    public String toString() {
        return "Way{" +
                "start=" + start +
                ", finish=" + finish +
                ", edge=" + edge +
                '}';
    }

    public Way(int start, int finish) {
        this.start = start;
        this.finish = finish;
    }
}
