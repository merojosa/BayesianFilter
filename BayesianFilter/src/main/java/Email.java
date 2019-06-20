public class Email {
    
    private String header;
    private String body;
    private String footer;
    private String snippet;

    public Email() {
    }

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public String getFooter() {
        return footer;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

}
