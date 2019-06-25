import java.io.IOException;
import java.security.GeneralSecurityException;


public class Main
{
    /**
     * Starts the program.
     * @param args
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static void main(String[] args) throws IOException, GeneralSecurityException
    {
        Controller controller = new Controller();
        controller.start();
    }
}
