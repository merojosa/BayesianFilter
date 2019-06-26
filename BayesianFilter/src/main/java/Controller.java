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

    public Controller()
    {
        authenticator = new Authenticator();
        visualizer = new Visualizer();
        emailLoader = new EmailLoader();
    }

    public void start() throws IOException, GeneralSecurityException
    {
        boolean goBack = false;
        String messageSpam = "";
        ArrayList<Email> unreadEmails;

        while(true)
        {
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
            try {
                spamFilter = new SpamFilter();
            }
            catch (Exception o){}
            while (true)
            {
                visualizer.showMainMenu();
                switch (visualizer.readConsoleString())
                {
                    // Configuration
                    case "1":
                    case "configurar": {
                        visualizer.showConfigurationMenu();
                        while (!goBack) {
                            switch (visualizer.readConsoleString()) {
                                case "1":
                                case "cambiar probabilidad de 'spam'":
                                case "probabilidad":
                                    visualizer.showMessage("Ingrese el nuevo valor de la probabilidad");
                                    double probability = visualizer.readConsoleDouble();
                                    while (!(probability >= 0 && probability <= 1)) {
                                        visualizer.showMessage("Ingrese un valor entre 0 y 1");
                                        probability = visualizer.readConsoleDouble();
                                    }
                                    spamFilter.setSpamProbability(probability);
                                    visualizer.showMessage("El valor fue guardado");
                                    break;
                                case "2":
                                case "cambiar 'spam threshold":
                                case "limite":
                                case "threshold":
                                    visualizer.showMessage("Ingrese el nuevo valor del threshold");
                                    double threshold = visualizer.readConsoleDouble();
                                    while (!(threshold >= 0 && threshold <= 1)) {
                                        visualizer.showMessage("Ingrese un valor entre 0 y 1");
                                        threshold = visualizer.readConsoleDouble();
                                    }
                                    spamFilter.setSpamThreshold(threshold);
                                    break;
                                case "3":
                                case "Cambiar el tamanio del conjunto de entrenamiento":
                                case "tamanio":
                                case "tamaño":
                                case "conjunto:":
                                case "entrenamiento":
                                    visualizer.showMessage("Ingrese el nuevo valor del tamanio");
                                    int size = visualizer.readConsonleInt();
                                    while (size < 0) {
                                        visualizer.showMessage("Ingrese un numero mayor a 0");
                                        size = visualizer.readConsonleInt();
                                    }
                                    spamFilter.setEmailAmount(size);
                                    break;
                                case "4":
                                case "mostrar configuracion":
                                    visualizer.showConfiguration(spamFilter.getSpamProbability(), spamFilter.getSpamThreshold(), spamFilter.getEmailAmount());
                                    break;
                                case "5":
                                case "regresar":
                                    visualizer.showMainMenu();
                                    goBack = true;
                                    break;
                            }
                        }
                        break;
                    }
                    // Train
                    case "2": {
                        visualizer.showMessage("Entrenando el sistema...\n");
                        try {
                            spamFilter.train(emailLoader.getSpam(authenticator.getService()), emailLoader.getNotSpam(authenticator.getService()));
                        } catch (Exception o) {
                            if (o.getMessage().equals("Se cancelo el entrenamiento porque se necesitan mas correos para entrenar el sistema.\n")) {
                                visualizer.showMessage(o.getMessage());
                            } else {
                                if (o.getMessage().equals("www.googleapis.com")) {
                                    visualizer.showMessage("No se pudo establecer la conexion con el servidor de google.");
                                } else {
                                    visualizer.showMessage("Ocurrio un error y no se pudo entrenar el sistema.\n");
                                }
                            }
                        }
                        break;
                    }
                    // Show training data.
                    case "3":
                        {
                        visualizer.showTrainingData(spamFilter.getWordsProbabilities());
                        visualizer.showMessage("\n");
                        break;
                    }
                    // Get unread messages.
                    case "4": {
                        visualizer.showMessage("Obteniendo correos nuevos...\n");
                        // Iterate through all unread messages.
                        try {
                            unreadEmails = emailLoader.getUnreadEmail(authenticator.getService());

                            if (unreadEmails.isEmpty()) {
                                visualizer.showMessage("No hay correos nuevos.");
                            } else {
                                for (Email email : unreadEmails) {
                                    // Print snippet and whether is spam or not (calling spam filter).
                                    if (spamFilter.determineEmail(email)) {
                                        messageSpam = "[SPAM] ";
                                    } else {
                                        messageSpam = "[NOT SPAM] ";
                                    }
                                    visualizer.showMessage(messageSpam + email.getSnippet());
                                }
                            }
                        }
                        catch (Exception o) {
                            if (o.getMessage().equals("www.googleapis.com")) {
                                visualizer.showMessage("No se pudo establecer la conexion con el servidor de google.");
                            } else {
                                visualizer.showMessage("Hubo un problema al obtener correos.");
                            }
                        }
                        break;
                    }
                    // Close session.
                    case "5":
                    {
                        authenticator.closeSession();
                        start();
                        System.exit(0);
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