import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class SpamFilter
{

    private double spamProbability;
    private double spamThreshold;

    // Determine.
    private double spamProbabilities;
    private double notSpamProbabilities;

    private int emailAmount;
    private Map<String, WordsProbability> wordsProbabilities;
    private FileManager fileManager;


    /**
     * Constructor of the class SpamFilter. Loads the traning.
     */
    public SpamFilter(){
        fileManager = new FileManager();
        try
        {
            wordsProbabilities = fileManager.loadWordsProbability();
        }
        catch (Exception o)
        {
            wordsProbabilities = new HashMap<String, WordsProbability>();
        }
        try
        {
            this.changeConfiguration();
        }
        catch(Exception o)
        {
            spamProbability = 0.3;
            spamThreshold = 0.9;
            emailAmount = 50;
        }
    }

    /**
     * Determines if an email is spam or not.
     * Requires the system to be trained using the method train(List<Email> spam,List<Email> notSpam).
     * @param email
     * @return
     */
    public boolean determineEmail(Email email)
    {
        spamProbabilities = 1.0;
        notSpamProbabilities = 1.0;

        HashSet<String> computedWords = new HashSet<String>();

        // Iterate through body, subject and from.
        multiplyProbabilities(email.getBody(), computedWords);
        multiplyProbabilities(email.getSubject(), computedWords);
        multiplyProbabilities(email.getFrom(), computedWords);

        double result = spamProbabilities /
                (spamProbabilities + notSpamProbabilities);

        if(result < spamThreshold)  // If it is less than the threshold, is not spam.
        {
            return false;
        }
        else                        // Is spam.
        {
            return true;
        }
    }

    /**
     * Multiplies all probabilities given a text
     * The results is saved in spamProbabilities and notSpamProbabilities
     * Requieres the training.
     * @param text
     * @param computedWords
     */
    private void multiplyProbabilities(String text, HashSet computedWords)
    {
        WordsProbability singleWord = null;

        // Word by word, includes letters only.
        for (String word : text.split("[^a-zA-Z'áéíóúàèìòùäëïöü]+"))
        {
            // Do things only if the word exists in the spam filter and if the word was not added
            if(wordsProbabilities.containsKey(word) == true && computedWords.contains(word) == false)
            {
                singleWord = getWordsProbabilities().get(word);
                if(singleWord.getSpamProbability() > 0 && singleWord.getNotSpamProbability() > 0)
                {
                    computedWords.add(word);
                    spamProbabilities *= singleWord.getSpamProbability() * spamProbability;
                    notSpamProbabilities *= singleWord.getNotSpamProbability() * (1 - spamProbability);
                }
            }
        }
    }

    /**
     * Gets the text of important parts of the email
     * @param email
     * @return
     */
    private String[] splitEmail(Email email)
    {
        String emailsText = "";
        emailsText = emailsText.concat(email.getSnippet());
        emailsText = emailsText.concat(" ");
        emailsText = emailsText.concat(email.getSubject());
        emailsText = emailsText.concat(" ");
        emailsText = emailsText.concat(email.getBody());
        return emailsText.split("[^a-zA-Z'áéíóúàèìòùäëïöü]+");
    }

    /**
     * Count the words of spam email and add them to the training
     * @param commonWords
     * @param spam
     */
    private void trainWithSpam(HashSet<String> commonWords, List<Email> spam)
    {
        for (int i = 0; i < spam.size(); i++) {
            String[] emailWords = this.splitEmail(spam.get(i));
            int numWord = emailWords.length;
            HashSet<String> countedWords = countedWords = new HashSet<String>();
            for (int counter = 0; counter < emailWords.length; counter++) {
                if (emailWords[counter].length() > 2) {
                    emailWords[counter] = emailWords[counter].toLowerCase();
                    if (!commonWords.contains(emailWords[counter])) {
                        if (wordsProbabilities.get(emailWords[counter]) == null) {
                            wordsProbabilities.put(emailWords[counter], new WordsProbability(emailWords[counter], 1, 0, new Double(1) / spam.size(), 0));
                            countedWords.add(emailWords[counter]);
                        } else {
                            WordsProbability word = wordsProbabilities.get(emailWords[counter]);
                            if (!countedWords.contains(emailWords[counter])) {
                                countedWords.add(emailWords[counter]);
                                word.setTotalSpam(wordsProbabilities.get(emailWords[counter]).getTotalSpam() + 1);
                                word.setWordAmount(wordsProbabilities.get(emailWords[counter]).getWordAmount() + 1);
                            }
                            word.setSpamProbability(new Double(wordsProbabilities.get(emailWords[counter]).getTotalSpam()) / spam.size());
                            wordsProbabilities.put(emailWords[counter], word);
                        }
                    }
                }

            }
        }
    }

    /**
     * Count the words of a non spam email and add them to the training
     * @param commonWords
     * @param notSpam
     */
    private void trainWithNotSpam(HashSet<String> commonWords, List<Email> notSpam)
    {
        for (int j = 0; j < notSpam.size(); j++) {
            String[] emailWords = this.splitEmail(notSpam.get(j));
            int numWord = emailWords.length;
            HashSet<String> countedWords = countedWords = new HashSet<String>();
            for (int counter = 0; counter < emailWords.length; counter++) {
                if (emailWords[counter].length() > 2) {
                    WordsProbability word = new WordsProbability();
                    emailWords[counter] = emailWords[counter].toLowerCase();
                    if (!commonWords.contains(emailWords[counter])) {
                        if (wordsProbabilities.get(emailWords[counter]) == null) {
                            wordsProbabilities.put(emailWords[counter], new WordsProbability(emailWords[counter], 0, 1, 0, new Double(1) / notSpam.size()));
                            countedWords.add(emailWords[counter]);
                        } else {
                            word = wordsProbabilities.get(emailWords[counter]);
                            if (!countedWords.contains(emailWords[counter])) {
                                countedWords.add(emailWords[counter]);
                                word.setTotalEmails(wordsProbabilities.get(emailWords[counter]).getTotalEmails() + 1);
                                word.setWordAmount(wordsProbabilities.get(emailWords[counter]).getWordAmount() + 1);
                            }
                            word.setNotSpamProbability(new Double(wordsProbabilities.get(emailWords[counter]).getTotalEmails()) / notSpam.size());
                            wordsProbabilities.put(emailWords[counter], word);
                        }
                    }

                }
            }
        }
    }

    /**
     * Trains the system so it can determine if an email is spam or not.
     * @param spam
     * @param notSpam
     * @throws Exception
     */
    public void train(List<Email> spam,List<Email> notSpam) throws Exception
    {
        wordsProbabilities.clear();
        if(spam.size()+notSpam.size()>=emailAmount) {
            if(spam!=null||notSpam!=null) {
                HashSet<String> commonWords = fileManager.getStopWords();
                this.trainWithSpam(commonWords, spam);
                this.trainWithNotSpam(commonWords, notSpam);
                wordsProbabilities.remove("");
                wordsProbabilities.remove("''");
                for (Map.Entry<String, WordsProbability> entry : wordsProbabilities.entrySet()) {
                    System.out.println("clave: " + entry.getKey() + " ,palabra: " + entry.getValue().getWord()
                            + " ,proba spam:  " + entry.getValue().getSpamProbability() + ", proba de no spam:  " + entry.getValue().getNotSpamProbability()
                            + ",cantidad en no spam:" + entry.getValue().getTotalEmails()
                    );
                }

                fileManager.saveWordsProbability(wordsProbabilities);
            }
            else{
                    throw new Exception("System was unable to find emails");
                }
        }
        else{
                throw new Exception("Training canceled, you need more emails\n");
            }
    }

    /**
     * Changes the default configuration with the configuration loaded from the user's file.
     * @throws IOException
     */
    public void changeConfiguration() throws IOException
    {
        ArrayList<Double> config =  fileManager.loadTrainingConfiguration();
        spamProbability = config.get(0);
        spamThreshold = config.get(1);
        emailAmount = (int)Math.round(config.get(2));
    }

    public double getSpamProbability() {
        return spamProbability;
    }

    public double getSpamThreshold() {
        return spamThreshold;
    }

    public int getEmailAmount() {
        return emailAmount;
    }

    public void setSpamProbability(double spamProbability) throws IOException {

        this.spamProbability = spamProbability;
        fileManager.saveTrainingData(this.spamProbability,this.spamThreshold,this.emailAmount);
    }

    public void setSpamThreshold(double spamThreshold) throws IOException{
        this.spamThreshold = spamThreshold;
        fileManager.saveTrainingData(this.spamProbability,this.spamThreshold,this.emailAmount);
    }

    public void setEmailAmount(int emailAmount) throws IOException{
        this.emailAmount = emailAmount;
        fileManager.saveTrainingData(this.spamProbability,this.spamThreshold,this.emailAmount);
    }

    public Map<String, WordsProbability> getWordsProbabilities() {
        return wordsProbabilities;
    }

    public void setWordsProbabilities(Map<String, WordsProbability> wordsProbabilities) {
        this.wordsProbabilities = wordsProbabilities;
    }

    /**
     * Checks if the system is already trained
     * @return
     */
    public boolean isTrained()
    {
        if(fileManager.fileExists("tokens/training.dat") == true)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}