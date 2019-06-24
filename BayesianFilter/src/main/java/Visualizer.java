import java.util.Map;
import java.util.Scanner;

public class Visualizer
{
    private Scanner scanner;
    private final int SIZE_WORD = 16;

    public Visualizer() {
        scanner = new Scanner(System.in);
    }

    public void showStartApp()
    {
        System.out.println("Bienvenido a la aplicacion de Filtro Bayesiano \n Seleccione una opcion \n[1]Autenticarse\n[2]Salir" );
    }
    public void showMainMenu()
    {
        System.out.println("Seleccione una opción\n[1]Configurar\n[2]Entrenar\n[3]Mostrar datos\n[4]Obtener correos nuevos\n[5]Cerrar sesion\n[6]Salir");
    }

    public void showConfigurationMenu()
    {
        System.out.println("Seleccione una opcion\n[1]Cambiar probabilidad de 'SPAM'\n[2]Cambiar 'SPAM Threshold'\n[3]Cambiar el tamanio del conjunto de entrenamiento\n[4]Mostrar configuracion\n[5]Regresar");
    }

    public void showConfiguration(float spamProbability, float spamThreshold, int emailAmount)
    {
        System.out.println("Probabilidad de spam: "+spamProbability+"\nLimite de spam: "+spamThreshold+"\nMinimo de cantidad de correos: "+emailAmount);
    }

    public void showTrainingData(Map<String, WordsProbability> wordsProbabilities)
    {
        if(wordsProbabilities.size() > 0)
        {
            System.out.println("\n----------------------------------------------------------------------------------");
            System.out.printf("| %-" + SIZE_WORD + "s| %-15s | %-13s | %-10s | %-13s |", "Palabra", "Correos totales", "Cantidad spam", "Prob. spam", "Prob. no spam");
            String word = "";
            for (Map.Entry<String, WordsProbability> mapWord : wordsProbabilities.entrySet())
            {
                System.out.println("\n----------------------------------------------------------------------------------");

                // If the lenght word is bigger than SIZE_WORD, cut it.
                if(mapWord.getKey().length() > SIZE_WORD)
                {
                    word = mapWord.getKey().substring(0, SIZE_WORD - 1);
                }
                else
                {
                    word = mapWord.getKey();
                }

                System.out.printf("| %-" + SIZE_WORD + "s| %-15s | %-13s | %-10s | %-13s |",
                        word,
                        mapWord.getValue().getTotalEmails(),
                        mapWord.getValue().getTotalSpam(),
                        mapWord.getValue().getSpamProbability(),
                        mapWord.getValue().getNotSpamProbability());

            }
            System.out.println("\n----------------------------------------------------------------------------------");
            System.out.println("Total de palabras: " + wordsProbabilities.size());
        }
        else
        {
            System.out.printf("No hay correos nuevos.");
        }

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
            System.out.println("No se pudo realizar la oprecion porque el valor es invalido");
        }
        return returnValue;
    }

    public int readConsonleInt()
    {
        int returnValue = 0;
        try {
            returnValue = Integer.valueOf(scanner.next());
        }
        catch(NumberFormatException e) {
            System.out.println("No se pudo realizar la oprecion porque el valor es invalido");
        }
        return returnValue;
    }

    public void showMessage(String message)
    {
        System.out.println(message);
    }
}
