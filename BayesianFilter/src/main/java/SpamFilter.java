import java.util.List;
import java.util.Map;

public class SpamFilter
{

    private float spamProbability;
    private float spamThreshold;
    private int emailAmount;
    private Map<String, WordsProbability> wordsProbabilities;
    private FileManager fileManager;

    public SpamFilter(){
        spamProbability = (float)0.3;
        spamThreshold = (float)0.9;
        emailAmount = 50;
    }

    // True if is spam.
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

    public float getSpamProbability() {
        return spamProbability;
    }

    public float getSpamThreshold() {
        return spamThreshold;
    }

    public int getEmailAmount() {
        return emailAmount;
    }

    public void setSpamProbability(float spamProbability) {
        this.spamProbability = spamProbability;
    }

    public void setSpamThreshold(float spamThreshold) {
        this.spamThreshold = spamThreshold;
    }

    public void setEmailAmount(int emailAmount) {
        this.emailAmount = emailAmount;
    }
}
