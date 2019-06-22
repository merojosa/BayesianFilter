import java.io.IOException;
import java.lang.System;
import java.security.GeneralSecurityException;

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
        spamFilter = new SpamFilter();
    }

    public void start() throws IOException, GeneralSecurityException
    {
        visualizer.showStartApp();
        boolean goBack = false;
        while(true)
        {
            switch (visualizer.readConsoleString())
            {
                case "2":
                case "salir":
                    System.exit(0);
                case"1":
                case"autenticarse":
                    authenticator.logIn();
                    while(true)
                    {
                        visualizer.showMainMenu();
                        switch (visualizer.readConsoleString())
                        {
                            // Configuration
                            case "1":
                            case "configurar":
                            {
                                visualizer.showConfigurationMenu();
                                while (!goBack) {
                                    switch (visualizer.readConsoleString()) {
                                        case "1":
                                        case "cambiar probabilidad de 'spam'":
                                        case "probabilidad":
                                            visualizer.showMessage("Ingrese el nuevo valor de la probabilidad");
                                            float probability = visualizer.readConsoleFloat();
                                            while(!(probability >= 0 && probability<=1))
                                            {
                                                visualizer.showMessage("Ingrese un valor entre 0 y 1");
                                                probability = visualizer.readConsoleFloat();
                                            }
                                            spamFilter.setSpamProbability(probability);
                                            visualizer.showMessage("El valor fue guardado");
                                            break;
                                        case "2":
                                        case"cambiar 'spam threshold":
                                        case"limite":
                                        case"threshold":
                                            visualizer.showMessage("Ingrese el nuevo valor del threshold");
                                            float threshold = visualizer.readConsoleFloat();
                                            while(!(threshold >= 0 && threshold<=1))
                                            {
                                                visualizer.showMessage("Ingrese un valor entre 0 y 1");
                                                threshold = visualizer.readConsoleFloat();
                                            }
                                            spamFilter.setSpamThreshold(threshold);
                                            break;
                                        case "3":
                                        case"Cambiar el tamanio del conjunto de entrenamiento":
                                        case"tamanio":
                                        case"tamaño":
                                        case"conjunto:":
                                        case"entrenamiento":
                                            visualizer.showMessage("Ingrese el nuevo valor del tamanio");
                                            int size = visualizer.readConsonleInt();
                                            while(size<0)
                                            {
                                                visualizer.showMessage("Ingrese un numero mayor a 0");
                                                size = visualizer.readConsonleInt();
                                            }
                                            spamFilter.setEmailAmount(size);
                                            break;
                                        case "4":
                                        case "mostrar configuracion":
                                            visualizer.showConfiguration(spamFilter.getSpamProbability(),spamFilter.getSpamThreshold(),spamFilter.getEmailAmount());
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
                            // Training
                            case "2":
                            {


                                break;
                            }
                            // Show training data.
                            case "3":
                            {
                                break;
                            }
                            // Get unread messages.
                            case "4":
                            {
                                visualizer.showEmail("Cargando correos nuevos...\n");
                                // Iterate through all unread messages.
                                for(Email email : emailLoader.getUnreadEmail(authenticator.getService()))
                                {
                                    // Print snippet and whether is spam or not (calling spam filter).
                                    visualizer.showEmail("[????] " + email.getSnippet());
                                }
                                visualizer.showEmail("\nSeleccione cualquier tecla para continuar.");
                                visualizer.readConsoleString();
                                break;
                            }
                            // Closse session.
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
}