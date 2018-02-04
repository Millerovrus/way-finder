public class Way {
    private String start;
    private String finish;

    @Override
    public String toString() {
        return "Way{" +
                "start='" + start + '\'' +
                ", finish='" + finish + '\'' +
                '}';
    }

    public Way(String start, String finish) {
        this.start = start;
        this.finish = finish;
    }

    public String getStart() {

        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }
}
