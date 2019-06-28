public class Email {

    private String snippet;
    private String from;
    private String body;
    private String subject;

    /**
     * Constructor of the class Email
     *
     * @param snippet
     * @param from
     * @param body
     * @param subject
     */
    public Email(String snippet, String from, String body, String subject) {
        this.snippet = snippet;
        this.from = from;
        this.body = body;
        this.subject = subject;
    }

    /**
     * Empty constructor of the class Email
     */
    public Email() {
        snippet = "";
        from = "";
        body = "";
        subject = "";
    }

    /**
     * Returns the attribute from
     *
     * @return from
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the attribute body
     * @return body
     */
    public String getBody() {
        return body;
    }

    /**
     * Returns the attribute subject
     * @return subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Returns the attribute
     * @return snippet
     */
    public String getSnippet() {
        return snippet;
    }

    /**
     * Changes the value of the attribute from
     * @param from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Changes the value of the attribute body
     * @param body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Changes the value of the attribute subject
     * @param subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Changes the value of the attribute snippet
     * @param snippet
     */
    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

}
