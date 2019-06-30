public class Email
{

    private String snippet;
    private String from;
    private String body;
    private String subject;

    public Email()
    {
        snippet = "";
        from = "";
        body = "";
        subject = "";
    }

    public String getFrom() {
        return from;
    }

    public String getBody() {
        return body;
    }

    public String getSubject() { return subject; }

    public String getSnippet() {
        return snippet;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

}
