import java.util.Scanner;
public class Visualizer {
    private Scanner scanner;

    public Visualizer() {
        scanner = new Scanner(System.in);
    }

    public void showStartApp()
    {
        System.out.println("Bienvenido a la aplicacion de Filtro Bayesiano \n Seleccione una opcion \n[1]Autenticarse\n[2]Salir" );
    }
    public void showMainMenu()
    {
        System.out.println("Seleccione una opcion\n[1]Configurar\n[2]Entrenar\n[3]Mostrar datos\n[4]Obetener correo nuevo\n[5]Cerrar sesion\n[6]Salir\n[7]Prueba");
    }
    public String readConsoleString()
    {
        return scanner.next();
    }
    public Float readConsoleFloat()
    {
        float returnValue = 0;
        try {
            returnValue = Float.valueOf(scanner.next());
        }
        catch(NumberFormatException e) {
        System.out.println("Ingrese un numero decimal");
    }
        return returnValue;
    }
}
