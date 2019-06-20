import com.google.api.services.gmail.Gmail;
import java.util.List;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;


public class EmailLoader {



    public EmailLoader() {
    }
    public List<Email> getSpam(Gmail service)
    {
        try {
            ListMessagesResponse spamlist = service.users().messages().list("me").setQ("is:spam").execute();
            List<Message> messages = spamlist.getMessages();
        }
        catch(Exception e){}

        return null;
    }
    public void getNotSpam()
    {

    }
}
