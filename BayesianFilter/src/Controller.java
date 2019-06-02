import java.lang.System;

public class Controller {
    public static void main(String[] args) {
        Visualizer visualizer = new Visualizer();
        visualizer.showStartApp();
        while(true)
        {
            switch (visualizer.readConsoleString()) {
                case "salir":
                    System.exit(0);
                    break;
                case "Salir":
                    System.exit(0);
                    break;
                case "2":
                    System.exit(0);
                case"1":
                        visualizer.showMainMenu();
                        while(true)
                        {
                            switch (visualizer.readConsoleString())
                            {
                                case "salir":
                                    System.exit(0);
                                    break;
                                case "Salir":
                                    System.exit(0);
                                    break;
                                case "6":
                                    System.exit(0);
                                case "5":
                                    main(null);
                                    //Ingresar aqui el codigo para limpiar datos o cerrar sesion
                                    System.exit(0);
                                    break;
                                case"7":
                                    visualizer.readConsoleFloat();
                                    break;
                            }
                        }
            }
        }
    }

}