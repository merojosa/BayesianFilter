import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;


public class EmailLoader
{
    private HtmlParser htmlParser;
    private final String USER_ID = "me";

    public EmailLoader()
    {
        htmlParser = new HtmlParser();
    }

    /**
     * Calls getEmails with the query "is:spam" to get spam emails.
     * @param service
     * @return ArrayList<Email>
     */
    public List<Email> getSpam(Gmail service)
    {
        try
        {
            return getEmails(service, "is:spam");
        }
        catch (Exception o)
        {
            System.out.println("Hubo un problema al obtener los correos de spam.");
            return null;
        }
    }

    /**
     * Calls getEmails with the query "is:inbox" to get the inbox emails.
     * @param service
     * @return ArrayList<Email>
     */
    public List<Email> getNotSpam(Gmail service)
    {
        try
        {
            return getEmails(service, "is:inbox");
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Calls getEmails with the query "is:unread" to get unread emails.
     * @param service
     * @return  ArrayList<Email>
     * @throws IOException
     */
    public ArrayList<Email> getUnreadEmail(Gmail service)  throws IOException
    {
        try
        {
            return  getEmails(service, "is:unread");
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Gets a list of emails acording the given query.
     * @param service
     * @param emailQuery
     * @return ArrayList<Email>
     * @throws IOException
     */
    private ArrayList<Email> getEmails(Gmail service, String emailQuery) throws IOException
    {
        // Make a request to recieve unread messages.
        Gmail.Users.Messages.List request = service.users().messages().list(USER_ID);
        request.setQ(emailQuery);
        ListMessagesResponse listMessagesResponse = request.execute();

        // Get emails acording the email type.
        List<Message> unreadMessagesApi = listMessagesResponse.getMessages();
        ArrayList<Email> emailsList = new ArrayList<>();

        if (unreadMessagesApi == null)
        {
            return emailsList;
        }
        else
        {
            Email email;
            for (Message message : unreadMessagesApi)
            {
                email = getWholeEmail(service, USER_ID, message.getId());
                emailsList.add(email);
            }
            return emailsList;
        }
    }

    /**
     * Extracts the snippet, from, and body.
     * @param service
     * @param userId
     * @param messageId
     * @return Email
     * @throws IOException
     */
    private Email getWholeEmail(Gmail service, String userId, String messageId)
            throws IOException
    {
        Message message = service.users().messages().get(userId, messageId).execute();

        Email email = new Email();
        email.setSnippet(message.getSnippet());

        int count = 0;
        String from = "";
        String subject = "";

        // Get body, from and subject.
        List<MessagePart> parts = message.getPayload().getParts();

        // If parts is not null, you can access the body via part (the html type).  But you can't via message.
        if(parts != null)
        {
            for (MessagePart part : parts)
            {
                if(part.getMimeType().equals("text/html"))
                {
                    email.setBody(htmlParser.parseHtmlToPlainText(
                            new String(Base64.decodeBase64(part.getBody().getData().getBytes()))));

                    count = 0;
                    from = "";
                    subject = "";
                }
            }
        }
        else
        {
            // If parts is null, you can access the body via the message itself.
            email.setBody(htmlParser.parseHtmlToPlainText(
                    new String(Base64.decodeBase64(message.getPayload().getBody().getData().getBytes()))));

        }

        // Search from and subject.
        while(count < message.getPayload().getHeaders().size() && (from.equals("") || subject.equals("")))
        {
            if( message.getPayload().getHeaders().get(count).getName().equals("From"))
            {
                from = message.getPayload().getHeaders().get(count).getValue();
                email.setFrom(from);
            }
            else if(message.getPayload().getHeaders().get(count).getName().equals("Subject"))
            {
                subject = message.getPayload().getHeaders().get(count).getValue();
                email.setSubject(subject);
            }
            ++count;
        }
        return email;
    }
}
