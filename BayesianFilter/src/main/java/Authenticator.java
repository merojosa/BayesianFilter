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

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.io.File;
import java.io.InputStreamReader;
import static java.util.logging.Level.*;

public class Authenticator
{
    private static final String APPLICATION_NAME = "Spam Filter";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_READONLY);
    private Gmail service;

    /**
     * Logs in the user with google's credentials.
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public void logIn() throws IOException, GeneralSecurityException
    {
        // Disable warnings.
        final java.util.logging.Logger buggyLogger = java.util.logging.Logger.getLogger(FileDataStoreFactory.class.getName());
        buggyLogger.setLevel(SEVERE);

        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    }

    /**
     * Closes the opened session of the app by deleting the StoredCredentials file.  Moreover,
     * deletes training.dat and config.txt.
     */
    public void closeSession()
    {
        // Delete StoredCredential
        File file = new File(TOKENS_DIRECTORY_PATH + "/StoredCredential");
        file.delete();

        // Delete training data
        File trainingFile = new File(TOKENS_DIRECTORY_PATH + "/training.dat");
        trainingFile.delete();

        // Delete config file.
        File configFile = new File("files/config.txt");
        configFile.delete();
    }

    /**
     * Makes a request to google's service to use the user's credentials.
     * @param HTTP_TRANSPORT
     * @return the user's credentials.
     * @throws IOException
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException
    {
        // Load client secrets.
        InputStream in = Authenticator.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null)
        {
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

    public Gmail getService()
    {
        return service;
    }

    /**
     * Determines if the user is authenticated by checking the existence of StoredCredential file.
     * @return true if the user is authenticated, otherwise, not.
     */
    public boolean isAuthenticated()
    {
        File file = new File(TOKENS_DIRECTORY_PATH + "/StoredCredential");

        // If exists, the user is already authenticated.
        return file.exists();
    }
}
