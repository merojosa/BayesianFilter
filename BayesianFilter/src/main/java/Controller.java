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
                    case "configurar":
                    {
                        visualizer.showConfigurationMenu();
                        while (!goBack)
                        {
                            switch (visualizer.readConsoleString())
                            {
                                case "1":
                                case "cambiar probabilidad de 'spam'":
                                case "probabilidad":
                                    visualizer.showMessage("Ingrese el nuevo valor de la probabilidad");
                                    try {
                                        double probability = visualizer.readConsoleDouble();
                                        while (!(probability >= 0 && probability <= 1)) {
                                            visualizer.showMessage("Ingrese un valor entre 0 y 1");
                                            probability = visualizer.readConsoleDouble();
                                        }
                                        spamFilter.setSpamProbability(probability);
                                        visualizer.showMessage("Se guardo el valor");
                                    }
                                    catch (Exception o)
                                    {
                                        visualizer.showMessage("No se pudo realizar la operacion porque el valor es invalido");
                                    }
                                    break;
                                case "2":
                                case "cambiar 'spam' threshold":
                                case "limite":
                                case "threshold":
                                    visualizer.showMessage("Ingrese el nuevo valor del threshold");
                                    try {
                                        double threshold = visualizer.readConsoleDouble();
                                        while (!(threshold >= 0 && threshold <= 1)) {
                                            visualizer.showMessage("Ingrese un valor entre 0 y 1");
                                            threshold = visualizer.readConsoleDouble();
                                        }

                                        spamFilter.setSpamThreshold(threshold);
                                        visualizer.showMessage("Se guardo el valor");
                                    }
                                    catch(Exception o)
                                    {
                                        visualizer.showMessage("No se pudo realizar la operacion porque el valor es invalido");
                                    }
                                    break;
                                case "3":
                                case "Cambiar el tamaño del conjunto de entrenamiento":
                                case "tamanio":
                                case "tamaño":
                                case "conjunto:":
                                case "entrenamiento":
                                    visualizer.showMessage("Ingrese el nuevo valor del tamaño");
                                    try {
                                        int size = visualizer.readConsonleInt();
                                        while (size < 0) {
                                            visualizer.showMessage("Ingrese un numero mayor a 0");
                                            size = visualizer.readConsonleInt();
                                        }

                                        spamFilter.setEmailAmount(size);
                                        visualizer.showMessage("Se guardo el valor");
                                    }
                                    catch (Exception o)
                                    {
                                        visualizer.showMessage("No se pudo realizar la oprecion porque el valor es invalido");
                                    }
                                    break;
                                case "4":
                                case "mostrar configuracion":
                                    visualizer.showConfiguration(spamFilter.getSpamProbability(), spamFilter.getSpamThreshold(), spamFilter.getEmailAmount());
                                    break;
                                case "5":
                                case "regresar":
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
                        visualizer.showMessage("Entrenando el sistema...\n");
                        try
                        {
                            spamFilter.train(emailLoader.getSpam(authenticator.getService()), emailLoader.getNotSpam(authenticator.getService()));
                        }
                        catch (Exception o)
                        {
                            if (o.getMessage().equals("Se cancelo el entrenamiento porque se necesitan mas correos para entrenar el sistema.\n"))
                            {
                                visualizer.showMessage(o.getMessage());
                            }
                            else
                            {
                                if (o.getMessage().equals("www.googleapis.com"))
                                {
                                    visualizer.showMessage("No se pudo establecer la conexion con el servidor de google.");
                                } else
                                {
                                    visualizer.showMessage("Ocurrio un error y no se pudo entrenar el sistema.\n");
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
                            visualizer.showMessage("Entrenamiento necesario");
                        }

                        break;
                    }
                    // Get unread messages.
                    case "4":
                    {
                        if(spamFilter.isTrained() == true)
                        {
                            visualizer.showMessage("Obteniendo correos nuevos...\n");
                            // Iterate through all unread messages.
                            try
                            {
                                unreadEmails = emailLoader.getUnreadEmail(authenticator.getService());

                                if (unreadEmails.isEmpty())
                                {
                                    visualizer.showMessage("No hay correos nuevos.");
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
                                visualizer.showMessage("Hubo un problema al obtener correos.");
                            }
                        }
                        else
                        {
                            visualizer.showMessage("Entrenamiento necesario");
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
                    case "salir":
                        System.exit(0);
                }
            }
        }
    }
}