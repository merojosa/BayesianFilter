import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;


public class EmailLoader
{
    private HtmlParser htmlParser;
    private final String USER_ID = "me";

    public EmailLoader()
    {
        htmlParser = new HtmlParser();
    }
    public List<Email> getSpam(Gmail service)
    {
        try {
            ListMessagesResponse spamlist = service.users().messages().list(USER_ID).setQ("is:spam").execute();
            List<Message> messages = spamlist.getMessages();
        }
        catch(Exception e){}

        return null;
    }
    public void getNotSpam()
    {

    }

    public ArrayList<Email> getUnreadEmail(Gmail service)  throws IOException
    {
        // Make a request to recieve unread messages.
        Gmail.Users.Messages.List request = service.users().messages().list(USER_ID);
        request.setQ("is:unread");
        ListMessagesResponse listMessagesResponse = request.execute();

        // Get unread messages.
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
                email = getEmail(service, USER_ID, message.getId());
                emailsList.add(email);
            }
            return emailsList;
        }
    }

    // Extract snippet, from, body and footer.
    private Email getEmail(Gmail service, String userId, String messageId)
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
