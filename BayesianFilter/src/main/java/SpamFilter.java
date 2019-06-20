import java.util.Map;

public class SpamFilter
{
    private float spamProbability;
    private float spamThreshold;
    private int emailAmount;
    private Map<String, WordsProbability> wordsProbabilities;
    private FileManager fileManager;
}
