package dto;

public class TaskContent {
    private String body;
    private StringBuffer history;

    public TaskContent(String body, StringBuffer history) {
        this.body = body;
        this.history = history;
    }

    public TaskContent (){}

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public StringBuffer getHistory() {
        return history;
    }

    public void setHistory(StringBuffer history) {
        this.history = history;
    }
}
