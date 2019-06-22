import java.io.IOException;
import java.lang.System;
import java.security.GeneralSecurityException;

public class Controller
{
    private Authenticator authenticator;
    private Visualizer visualizer;
    private EmailLoader emailLoader;

    public Controller()
    {
        authenticator = new Authenticator();
        visualizer = new Visualizer();
        emailLoader = new EmailLoader();
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
                            case"1":
                            case"configurar":
                            {
                                visualizer.showConfigurationMenu();
                                while (!goBack)
                                {
                                    switch (visualizer.readConsoleString()) {
                                        case "1":
                                            break;
                                        case "2":
                                            break;
                                        case "3":
                                            break;
                                        case "4":
                                            break;
                                        case"regresar":
                                            visualizer.showMainMenu();
                                            goBack=true;
                                            break;
                                    }
                                }
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
                                visualizer.printMessage("Cargando correos nuevos...\n");
                                // Iterate through all unread messages.
                                for(Email email : emailLoader.getUnreadEmail(authenticator.getService()))
                                {
                                    // Print snippet and whether is spam or not (calling spam filter).
                                    visualizer.showEmail(email.getSnippet() + " ????");
                                }
                                visualizer.printMessage("\nSeleccione cualquier tecla para continuar.");
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
                            case"7":
                            {
                                visualizer.readConsoleFloat();
                                break;
                            }
                            case "6":
                            case "salir":
                                System.exit(0);
                        }
                    }
            }
        }
    }
}