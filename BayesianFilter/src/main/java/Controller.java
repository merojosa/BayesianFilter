import java.io.IOException;
import java.lang.System;
import java.security.GeneralSecurityException;

public class Controller
{
    private Authenticator authenticator;
    private Visualizer visualizer;

    public Controller()
    {
        authenticator = new Authenticator();
        visualizer = new Visualizer();
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
                    if(authenticator.logIn() == true)
                    {
                        visualizer.showMainMenu();
                        while(true)
                        {
                            switch (visualizer.readConsoleString()) {
                                case "1":
                                case "configurar":
                                    visualizer.showConfigurationMenu();
                                    while (!goBack) {
                                        switch (visualizer.readConsoleString()) {
                                            case "1":
                                                break;
                                            case "2":
                                                break;
                                            case "3":
                                                break;
                                            case "4":
                                            case "regresar":
                                                visualizer.showMainMenu();
                                                goBack = true;
                                                break;
                                        }
                                    }
                                    break;
                                case "5":
                                    Authenticator authenticator = new Authenticator();
                                    try {
                                        authenticator.closeSesion();
                                        visualizer.showMessage("Se cerro la sesion exitosamente");
                                    }
                                    catch(Exception e)
                                    {
                                        visualizer.showMessage("Hubo un problema al cerrar sesion.");
                                    }
                                    main(null);
                                    start();
                                    System.exit(0);
                                    break;
                                case "7":
                                    visualizer.readConsoleFloat();
                                    break;
                                case "6":
                                case "salir":

                                    System.exit(0);
                            }
                        }
                    }
            }
        }
    }
}