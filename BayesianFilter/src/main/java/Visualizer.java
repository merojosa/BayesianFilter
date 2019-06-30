import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Visualizer
{
    private Scanner scanner;
    private final int SIZE_WORD = 16;

    /**
     * Constructor of Visualizer
     * Initialize Scanner
     */
    public Visualizer() {
        scanner = new Scanner(System.in);
    }

    /**
     * Shows the start menu for the user to be able to log in of the application.
     */
    public void showStartApp()
    {
        System.out.println("Bayesian Filter, Sebastian - Jose Andres \nChoose one option\n[1]Login \n[2]Exit");
    }

    /**
     * Shows the main menu of the application for the user to be able to use it.
     */
    public void showMainMenu()
    {
        System.out.println("Choose one option\n[1]Configure\n[2]Train\n[3]Show training data\n[4]Get unread emails\n[5]Logout\n[6]Exit");
    }

    /**
     * Shows the configuration menu.
     */
    public void showConfigurationMenu()
    {
        System.out.println("Choose one option\n[1]Change spam probability\n[2]Change threshold\n[3]Change number of emails for training\n[4]Show current configuration\n[5]Go back");
    }

    /**
     * Shows the configuration the system is using
     * @param spamProbability
     * @param spamThreshold
     * @param emailAmount
     */
    public void showConfiguration(double spamProbability, double spamThreshold, int emailAmount)
    {
        System.out.println("Spam probability: "+spamProbability+"\nThreshold: "+spamThreshold+"\nNumber of emails: "+emailAmount);
    }

    /**
     * Shows the traning data.
     * @param wordsProbabilities
     */
    public void showTrainingData(Map<String, WordsProbability> wordsProbabilities)
    {
        if(wordsProbabilities.size() > 0)
        {
            System.out.println("\n----------------------------------------------------------------------------------");
            System.out.printf("| %-" + SIZE_WORD + "s| %-15s | %-13s | %-10s | %-13s |", "Word", "Total emails", "Total spam", "Spam prob", "Not spam prob");
            String word = "";
            for (Map.Entry<String, WordsProbability> mapWord : wordsProbabilities.entrySet())
            {
                if(mapWord.getValue().getSpamProbability() > 0 && mapWord.getValue().getNotSpamProbability() > 0)
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

                    System.out.printf("| %-" + SIZE_WORD + "s| %-15d | %-13d | %-10f | %-13f |",
                            word,
                            mapWord.getValue().getTotalEmails(),
                            mapWord.getValue().getTotalSpam(),
                            mapWord.getValue().getSpamProbability(),
                            mapWord.getValue().getNotSpamProbability());
                }

            }
            System.out.println("\n----------------------------------------------------------------------------------");
            System.out.println("Total words: " + wordsProbabilities.size());
        }
        else
        {
            System.out.printf("No unread emails");
        }

    }

    /**
     * Reads a string from the consonle
     * @return what the user types.
     */
    public String readConsoleString()
    {
        return scanner.next().toLowerCase();
    }

    /**
     * Reads a double from the console
     * @return what the user types.
     */
    public Double readConsoleDouble() throws Exception
    {
        return Double.valueOf(scanner.next());
    }

    /**
     * Reads an Int from the console
     * @return what the user types.
     */
    public int readConsonleInt() throws Exception
    {
        return Integer.valueOf(scanner.next());
    }

    /**
     * Displays a messae in the console
     * @param message
     */
    public void showMessage(String message)
    {
        System.out.println(message);
    }
}
