import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
        /*
        HashSet<String> commonWords = fileManager.getStopWords();
        for(int i = 0;i<notSpam.size();i++)
        {
            String emailsText = "";
            emailsText = emailsText.concat(notSpam.get(i).getSnippet());
            emailsText = emailsText.concat(" ");
            emailsText = emailsText.concat( notSpam.get(i).getSubject());
            emailsText = emailsText.concat(" ");
            emailsText = emailsText.concat( notSpam.get(i).getBody());



            double total =0;
            Map<String,WordsProbability> wordsProbabilities = new HashMap<String,WordsProbability>(){{}};
            String[] emailWords = emailsText.split(" |,|<|!|:|-|ยก|_|/");
            int numWord = emailWords.length;

            for(int counter = 0;counter < emailWords.length;counter++)
            {
                WordsProbability word = new WordsProbability();

                if(!commonWords.contains(emailWords[counter]))
                {
                    if (wordsProbabilities.get(emailWords[counter]) == null) {
                        wordsProbabilities.put(emailWords[counter], word);
                        total++;
                    } else {
                        total++;
                        word.setWordAmount(wordsProbabilities.get(emailWords[counter]).getWordAmount() + 1);
                        wordsProbabilities.put(emailWords[counter], word);
                    }
                    //System.out.println("\npalabra:" + emailWords[counter]);
                }

            }


        }
        for(int j = 0;j<spam.size();j++)
        {}*/
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
