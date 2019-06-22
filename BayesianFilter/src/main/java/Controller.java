import java.io.IOException;
import java.lang.System;
import java.security.GeneralSecurityException;

public class Controller
{
    private Authenticator authenticator;
    private Visualizer visualizer;
    private SpamFilter spamFilter;

    public Controller()
    {
        authenticator = new Authenticator();
        visualizer = new Visualizer();
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
                                case "5":
                                    try {
                                        authenticator.closeSession();
                                        visualizer.showMessage("Se cerro la sesion exitosamente");
                                    }
                                    catch(Exception e)
                                    {
                                        visualizer.showMessage("Hubo un problema al cerrar sesion.");
                                    }
                                    Main.main(null);
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