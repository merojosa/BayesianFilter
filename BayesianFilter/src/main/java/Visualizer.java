import java.util.Scanner;
public class Visualizer {
    private Scanner scanner;

    public Visualizer() {
        scanner = new Scanner(System.in);
    }

    public void showStartApp()
    {
        System.out.println("Bienvenido a la aplicación de Filtro Bayesiano \n Seleccione una opción \n[1]Autenticarse\n[2]Salir" );
    }
    public void showMainMenu()
    {
        System.out.println("Seleccione una opción\n[1]Configurar\n[2]Entrenar\n[3]Mostrar datos\n[4]Obtener correo nuevo\n[5]Cerrar sesión\n[6]Salir\n[7]Prueba");
    }

    public void showConfigurationMenu()
    {
        System.out.println("Seleccione una opción\n[1]Cambiar probabilidad de 'SPAM'\n[2]Cambiar 'SPAM Threshold'\n[3]Cambiar el tamaño del conjunto de entrenamiento'\n[4]Regresar");
    }

    public String readConsoleString()
    {
        return scanner.next().toLowerCase();
    }

    public Float readConsoleFloat()
    {
        float returnValue = 0;
        try {
            returnValue = Float.valueOf(scanner.next());
        }
        catch(NumberFormatException e) {
            System.out.println("Ingrese un número decimal");
        }
        return returnValue;
    }

    public void showMessage(String message)
    {
        System.out.println(message);
    }
}
