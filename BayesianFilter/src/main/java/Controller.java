import java.lang.System;

public class Controller
{
    public static void main(String[] args)
    {
        Visualizer visualizer = new Visualizer();
        visualizer.showStartApp();
        boolean goBack = false;
        while(true)
        {
            switch (visualizer.readConsoleString()) {
                case "2":
                case "salir":
                    System.exit(0);
                case"1":
                case"autenticarse":
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
                                    visualizer.showMessage("opcion 5 ejecutada");
                                    Authenticator authenticator = new Authenticator();
                                    try {
                                        authenticator.closeSesion();
                                    }
                                    catch(Exception e)
                                    {
                                        visualizer.showMessage("Hubo un problema al cerrar sesión.");
                                    }
                                    main(null);
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