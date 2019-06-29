import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.System;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

public class Controller
{
    private Authenticator authenticator;
    private Visualizer visualizer;
    private EmailLoader emailLoader;
    private SpamFilter spamFilter;
    private FileManager fileManager;

    /**
     * Constructor of the controller, initializes authenticator, visualizer and emailLoader
     */
    public Controller()
    {
        authenticator = new Authenticator();
        visualizer = new Visualizer();
        emailLoader = new EmailLoader();
        fileManager = new FileManager();
    }

    /**
     * Starts the program.
     * Main method of the controller who constructs other classes and call their methods.
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public void start() throws IOException, GeneralSecurityException
    {
        boolean goBack;
        boolean continueLoop;
        String messageSpam = "";
        ArrayList<Email> unreadEmails;

        while(true)
        {
            continueLoop = true;
            goBack = false;
            if(authenticator.isAuthenticated() == false)
            {
                // If the user is not authenticated, it will show the start app interface.
                visualizer.showStartApp();
                switch (visualizer.readConsoleString())
                {
                    case "2":
                    case "salir":
                        System.exit(0);
                    case"1":
                    case"autenticarse":
                    {
                        authenticator.logIn();
                    }
                }
            }
            else
            {
                // To initialize the gmail service.
                authenticator.logIn();
            }
            spamFilter = new SpamFilter();

            while (continueLoop)
            {
                visualizer.showMainMenu();
                switch (visualizer.readConsoleString())
                {
                    // Configuration
                    case "1":
                    case "configure":
                    {
                        visualizer.showConfigurationMenu();
                        while (!goBack)
                        {
                            switch (visualizer.readConsoleString())
                            {
                                case "1":
                                case "change spam probability":
                                case "probability":
                                    visualizer.showMessage("Enter the new spam probability");
                                    try {
                                        double probability = visualizer.readConsoleDouble();
                                        while (!(probability >= 0 && probability <= 1)) {
                                            visualizer.showMessage("Enter a value between 0 and 1");
                                            probability = visualizer.readConsoleDouble();
                                        }
                                        spamFilter.setSpamProbability(probability);
                                        visualizer.showMessage("Number saved");
                                    }
                                    catch (Exception o)
                                    {
                                        visualizer.showMessage("Wrong value, the operation wasn't completed");
                                    }
                                    break;
                                case "2":
                                case "change threshold":
                                case "threshold":
                                    visualizer.showMessage("Enter the new threshold");
                                    try {
                                        double threshold = visualizer.readConsoleDouble();
                                        while (!(threshold >= 0 && threshold <= 1)) {
                                            visualizer.showMessage("Enter a value between 0 and 1");
                                            threshold = visualizer.readConsoleDouble();
                                        }

                                        spamFilter.setSpamThreshold(threshold);
                                        visualizer.showMessage("Number saved");
                                    }
                                    catch(Exception o)
                                    {
                                        visualizer.showMessage("Wrong value, the operation wasn't completed");
                                    }
                                    break;
                                case "3":
                                case "change number of emails for training":
                                case "number of emails":
                                    visualizer.showMessage("Enter the new number of emails");
                                    try {
                                        int size = visualizer.readConsonleInt();
                                        while (size < 0) {
                                            visualizer.showMessage("Enter a value bigger than 0");
                                            size = visualizer.readConsonleInt();
                                        }

                                        spamFilter.setEmailAmount(size);
                                        visualizer.showMessage("Number saved");
                                    }
                                    catch (Exception o)
                                    {
                                        visualizer.showMessage("Wrong value, the operation wasn't completed");
                                    }
                                    break;
                                case "4":
                                case "show current configuration":
                                case "configuration":
                                    visualizer.showConfiguration(spamFilter.getSpamProbability(), spamFilter.getSpamThreshold(), spamFilter.getEmailAmount());
                                    break;
                                case "5":
                                case "go back":
                                    goBack = true;
                                    break;
                            }
                        }
                        goBack = false;
                        break;
                    }
                    // Train
                    case "2":
                    {
                        visualizer.showMessage("Training...\n");
                        try
                        {
                            spamFilter.train(emailLoader.getSpam(authenticator.getService()), emailLoader.getNotSpam(authenticator.getService()));
                        }
                        catch (Exception o)
                        {
                            if (o.getMessage().equals("Training canceled, you need more emails\n"))
                            {
                                visualizer.showMessage(o.getMessage());
                            }
                            else
                            {
                                //"System was unable to find emails"
                                if(o.getMessage().equals("System was unable to find emails"))
                                {
                                    visualizer.showMessage(o.getMessage());
                                }
                                    else{
                                if (o.getMessage().equals("www.googleapis.com")) {
                                    visualizer.showMessage("Connection error");
                                } else {
                                    visualizer.showMessage("Error, the training was canceled\n");
                                }
                            }
                            }
                        }
                        break;
                    }
                    // Show training data.
                    case "3":
                    {
                        if(spamFilter.isTrained() == true)
                        {
                            visualizer.showTrainingData(spamFilter.getWordsProbabilities());
                        }
                        else
                        {
                            visualizer.showMessage("Training necessary");
                        }

                        break;
                    }
                    // Get unread messages.
                    case "4":
                    {
                        if(spamFilter.isTrained() == true)
                        {
                            visualizer.showMessage("Getting unread emails...\n");
                            // Iterate through all unread messages.
                            try
                            {
                                unreadEmails = emailLoader.getUnreadEmail(authenticator.getService());

                                if (unreadEmails.isEmpty())
                                {
                                    visualizer.showMessage("No unread emails");
                                }
                                else
                                {
                                    for (Email email : unreadEmails)
                                    {
                                        // Print snippet and whether is spam or not (calling spam filter).
                                        if (spamFilter.determineEmail(email))
                                        {
                                            messageSpam = "[SPAM] ";
                                        }
                                        else
                                        {
                                            messageSpam = "[NOT SPAM] ";
                                        }
                                        visualizer.showMessage(messageSpam + email.getSnippet());
                                    }
                                }
                            }
                            catch (Exception o)
                            {
                                visualizer.showMessage("Error, unread emails wasn't gotten");
                            }
                        }
                        else
                        {
                            visualizer.showMessage("Training necessary");
                        }
                        break;
                    }
                    // Close session.
                    case "5":
                    {
                        authenticator.closeSession();
                        continueLoop = false;
                        break;
                    }
                    // Exit.
                    case "6":
                    case "leave":
                        System.exit(0);
                }
            }
        }
    }
}