import java.util.List;
import java.util.Map;

public class SpamFilter
{
    private float spamProbability;
    private float spamThreshold;
    private int emailAmount;
    private Map<String, WordsProbability> wordsProbabilities;
    private FileManager fileManager;

    public boolean determineEmail(Email email)
    {
        return true;
    }

    public void train(List<Email> notSpam, List<Email> spam)
    {

    }

    public void changeConfiguration()
    {

    }
}
