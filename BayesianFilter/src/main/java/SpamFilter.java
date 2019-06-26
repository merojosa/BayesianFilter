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

    public SpamFilter(){
        fileManager = new FileManager();
        try{
            wordsProbabilities = fileManager.loadWordsProbability();
        }
        catch (Exception o)
        {
            /*
            if(o.getMessage()==null || o.getMessage().equals("WordsProbability cannot be cast to java.lang.String"))
            {
                System.out.println("Se termino cargar la informaicon del entrenamiento.\n");
            }
            else
            {
                System.out.println("No se cargo ninguna informacion sobre el entrenamiento");
            }
            */

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

        double result = spamProbabilities/(spamProbabilities + notSpamProbabilities);

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
        for (String word : text.split("\\s+[^a-zA-z]*|[^a-zA-z]+\\s*"))
        {
            // Do things only if the word exists in the spam filter and if the word was not added
            if(getWordsProbabilities().containsKey(word) == true && computedWords.contains(word) == false)
            {
                singleWord = getWordsProbabilities().get(word);
                if(singleWord.getSpamProbability() > 0 && singleWord.getNotSpamProbability() > 0)
                {
                    computedWords.add(word);
                    spamProbabilities *= singleWord.getSpamProbability();
                    notSpamProbabilities *= singleWord.getNotSpamProbability();
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
        if(spam.size()+notSpam.size()>=emailAmount) {
            HashSet<String> commonWords = fileManager.getStopWords();
            Map<String, WordsProbability> wordsProbabilities = new HashMap<String, WordsProbability>() {{
            }};

            for (int i = 0; i < spam.size(); i++) {
                String emailsText = "";
                emailsText = emailsText.concat(spam.get(i).getSnippet());
                emailsText = emailsText.concat(" ");
                emailsText = emailsText.concat(spam.get(i).getSubject());
                emailsText = emailsText.concat(" ");
                emailsText = emailsText.concat(spam.get(i).getBody());
                String[] emailWords = emailsText.split("[^a-zA-Z'áéíóúàèìòùäëïöü]+");
                int numWord = emailWords.length;
                HashSet<String> countedWords = countedWords = new HashSet<String>();
                for (int counter = 0; counter < emailWords.length; counter++) {
                    if (emailWords[counter].length() > 2)
                    {
                        WordsProbability word = new WordsProbability();
                        emailWords[counter] = emailWords[counter].toLowerCase();
                        if (!commonWords.contains(emailWords[counter])) {
                            if (wordsProbabilities.get(emailWords[counter]) == null) {
                                word.setTotalSpam(1);
                                word.setSpamProbability(new Double(1) / spam.size());
                                word.setWord(emailWords[counter]);
                                wordsProbabilities.put(emailWords[counter], word);
                                countedWords.add(emailWords[counter]);
                            } else {
                                word = wordsProbabilities.get(emailWords[counter]);
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

            for (int j = 0; j < notSpam.size(); j++) {
                String emailsText = "";
                emailsText = emailsText.concat(notSpam.get(j).getSubject());
                emailsText = emailsText.concat(" ");
                emailsText = emailsText.concat(notSpam.get(j).getBody());
                String[] emailWords = emailsText.split("[^a-zA-Z'áéíóúàèìòùäëïöü]+");
                int numWord = emailWords.length;
                HashSet<String> countedWords = countedWords = new HashSet<String>();
                for (int counter = 0; counter < emailWords.length; counter++) {
                    if (emailWords[counter].length() > 2) {
                        WordsProbability word = new WordsProbability();
                        emailWords[counter] = emailWords[counter].toLowerCase();
                        if (!commonWords.contains(emailWords[counter])) {
                            if (wordsProbabilities.get(emailWords[counter]) == null) {
                                word.setTotalEmails(1);
                                word.setNotSpamProbability(new Double(1) / notSpam.size());
                                word.setWord(emailWords[counter]);
                                wordsProbabilities.put(emailWords[counter], word);
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
            wordsProbabilities.remove("");
            wordsProbabilities.remove("''");

        for (Map.Entry<String, WordsProbability> entry : wordsProbabilities.entrySet()) {
            System.out.println("clave: " + entry.getKey() + " ,palabra: " + entry.getValue().getWord()
            +" ,proba spam:  "+entry.getValue().getSpamProbability() + ", proba de no spam:  "+entry.getValue().getNotSpamProbability()
            + ",cantidad en no spam:"+entry.getValue().getTotalEmails()
            );
        }

            fileManager.saveWordsProbability(wordsProbabilities);
        }
        else{throw new Exception("Se cancelo el entrenamiento porque se necesitan mas correos para entrenar el sistema");}
    }

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
}
