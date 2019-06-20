import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.client.util.Base64;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.jsoup.select.Elements;

import java.lang.String;import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;


public class GmailQuickstart {
    private static final String APPLICATION_NAME = "Gmail API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GmailQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void main(String... args) throws IOException, GeneralSecurityException {



        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Print the labels in the user's account.
        String user = "me";


        ListLabelsResponse listResponse = service.users().labels().list(user).execute();
        List<Label> labels = listResponse.getLabels();
        if (labels.isEmpty()) {
            System.out.println("No labels found.");
        } else {
            System.out.println("Labels:");
            for (Label label : labels) {
                System.out.printf("- %s\n", label.getName());
            }

            System.out.println("Pruebas\n");
        }

        ListMessagesResponse spamlist = service.users().messages().list(user).setQ("is:spam").execute();
        List<Message>  messages = spamlist.getMessages();

        if (messages.isEmpty()) {
            System.out.println("No messages found.");
        } else {
            System.out.println("Messages:");
            for (Message message : messages) {
                System.out.printf("- %s\n", service.users().messages().get(user, message.getId()).execute().getSnippet());
            }

        }


        if (messages.isEmpty()) {
            System.out.println("No messages found.");
        } else {
            System.out.println("Messages:");
            for (Message message : messages) {
                //codigo base
                // https://stackoverflow.com/questions/29553887/gmail-apis-decoding-the-body-of-the-message-java-android
                Message currentMessage = service.users().messages().get("me", message.getId()).execute();
                String body = "";
                String mimeType = currentMessage.getPayload().getMimeType();
                List<MessagePart> parts = currentMessage.getPayload().getParts();
                if (mimeType.contains("alternative")) {
                    System.out.println("entering alternative loop");
                    for (MessagePart part : parts) {
                        body = new String(Base64.decodeBase64(part.getBody().getData().getBytes()));

                    }
                    System.out.println("unparsed");
                    System.out.println(body);
                    System.out.println("\n parsed");
                    String tmp  = parse(body);
                    evaluateEmail(tmp);
                    System.out.println(tmp);
                }



                //System.out.println("Message body: " + currentMessage.decodeRaw());

            }






        }





        //evaluateEmail("hola hola hola,hola/adios but");





        /*

        for(Map.Entry<String,String> actualWord: tmp.entrySet())
        {
            System.out.println("llave: "+actualWord.getKey()+" valor: "+actualWord.getValue()+'\n');
        }

         */


    }

    public static void evaluateEmail(String email)
    {
        double total =0;
        Map<String,WordsProbability> wordsProbabilities = new HashMap<String,WordsProbability>(){{}};
        String[] emailWords = email.split(" |,|<|!|:|-|¡|_|/");
        int numWord = emailWords.length;
        Map<String,String>tmp = getListasComunes();

        for(int counter = 0;counter < emailWords.length;counter++)
        {
            WordsProbability word = new WordsProbability();

            if(tmp.get(emailWords[counter])==null) {
                if (wordsProbabilities.get(emailWords[counter]) == null) {
                    wordsProbabilities.put(emailWords[counter], word);
                    total++;
                } else {
                    total++;
                    word.setWordAmount(wordsProbabilities.get(emailWords[counter]).getWordAmount() + 1);
                    wordsProbabilities.put(emailWords[counter], word);
                }
                System.out.println("\npalabra:" + emailWords[counter]);
            }

        }

        saveWordsProbability(wordsProbabilities);

        loadWordsProbability();

        //imprimir map
        for(Map.Entry<String,WordsProbability> actualWord: wordsProbabilities.entrySet())
        {
            System.out.println("Palabra: "+actualWord.getKey()+" cantidad: "+actualWord.getValue().getWordAmount()+'\n');
            System.out.println("Total"+total);
            double proba = (double)actualWord.getValue().getWordAmount()/total;

            System.out.println("Probabilidad de la palabra"+proba);
        }



    }

    public static void saveWordsProbability(Map<String,WordsProbability>wordsProbabilities)
    {

        try {
            FileOutputStream file = new FileOutputStream("trainning.dat");
            ObjectOutputStream os = new ObjectOutputStream(file);

            for(Map.Entry<String,WordsProbability> actualWord: wordsProbabilities.entrySet())
            {
                os.writeObject(actualWord.getKey());
                os.writeObject(actualWord.getValue());
                //https://www.youtube.com/watch?v=YzwiuRDgSSY
                //https://stackoverflow.com/questions/8371398/reading-from-a-binary-file-object
                //https://docs.oracle.com/javase/7/docs/api/java/io/ObjectInputStream.html#readObject()
                //https://www.ranks.nl/stopwords
            }
            file.close();
        }
        catch (Exception o)
        {
            System.out.println("No se pudo crear el archivo");
        }

    }

    public static void loadWordsProbability()
    {
        try
        {
            FileInputStream file = new FileInputStream("trainning.dat");
            ObjectInputStream is = new ObjectInputStream(file);

            while (true)
            {
                Object objectReaded;
                objectReaded = is.readObject();
                WordsProbability tmpWord = (WordsProbability)objectReaded;

                System.out.println("Palabra"+tmpWord.getWord());
                System.out.println("cantidad"+tmpWord.getWordAmount());


            }
        }
        catch (Exception o)
        {
            System.out.println("No se pudo abir el archivo");
        }



    }

    public static  Map<String,String> getListasComunes()
    {
        Map<String,String> palabrasLeidas = new HashMap<String,String>(){{}};

        try {
            String word;
            FileReader file = new FileReader("lista.txt");
            BufferedReader buffer = new BufferedReader(file);
            while ((word = buffer.readLine()) != null) {
                //System.out.println(cadena);
                palabrasLeidas.put(word,word);
            }
            buffer.close();
            file.close();
        }
        catch(Exception e)
        {

        }
        return palabrasLeidas;
    }


    public static String parse(String html)
    {
        String returnValue = "";
        Document document = Jsoup.parse(html);
        Elements elements = document.getAllElements();

        for(Element element : elements)
        {
            String tmp = element.ownText();
            returnValue = returnValue.concat(tmp);
            returnValue = returnValue.concat(" ");
        }

        return returnValue;
    }



}