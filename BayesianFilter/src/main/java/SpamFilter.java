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
        fileManager = new FileManager();
        //try {
            wordsProbabilities = fileManager.loadWordsProbability();
            /*
        }
        catch(Exception o){}*/
        spamProbability = (float)0.3;
        spamThreshold = (float)0.9;
        emailAmount = 50;
    }

    /**
     * Determines if an email is spam or not.
     * Requires the system to be trained using the method train(List<Email> spam,List<Email> notSpam).
     * @param email
     * @return
     */
    public boolean determineEmail(Email email)
    {
        return true;
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
                double total = 0;
                String[] emailWords = emailsText.split("\\s+[^a-zA-z]*|[^a-zA-z]+\\s*");
                int numWord = emailWords.length;
                HashSet<String> countedWords = countedWords = new HashSet<String>();
                for (int counter = 0; counter < emailWords.length; counter++) {
                    WordsProbability word = new WordsProbability();
                    emailWords[counter] = emailWords[counter].toLowerCase();
                    if (!commonWords.contains(emailWords[counter])) {
                        if (wordsProbabilities.get(emailWords[counter]) == null) {
                            word.setTotalSpam(1);
                            word.setSpamProbability(new Double(1) / spam.size());
                            word.setWord(emailWords[counter]);
                            wordsProbabilities.put(emailWords[counter], word);
                            countedWords.add(emailWords[counter]);
                            total++;
                        } else {
                            total++;
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

            for (int j = 0; j < notSpam.size(); j++) {
                String emailsText = "";
                emailsText = emailsText.concat(notSpam.get(j).getSubject());
                emailsText = emailsText.concat(" ");
                emailsText = emailsText.concat(notSpam.get(j).getBody());

                if (emailsText.contains("aerosol")) {
                    String a = "";
                }

                double total = 0;
                String[] emailWords = emailsText.split("\\s+[^a-zA-z]*|[^a-zA-z]+\\s*");
                int numWord = emailWords.length;
                HashSet<String> countedWords = countedWords = new HashSet<String>();
                for (int counter = 0; counter < emailWords.length; counter++) {
                    WordsProbability word = new WordsProbability();
                    emailWords[counter] = emailWords[counter].toLowerCase();
                    if (!commonWords.contains(emailWords[counter])) {
                        if (wordsProbabilities.get(emailWords[counter]) == null) {
                            word.setTotalEmails(1);
                            word.setNotSpamProbability(new Double(1) / notSpam.size());
                            word.setWord(emailWords[counter]);
                            wordsProbabilities.put(emailWords[counter], word);
                            countedWords.add(emailWords[counter]);
                            //quitar esta variable de total?
                            total++;
                        } else {
                            total++;
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
            wordsProbabilities.remove("");
        /*
        for (Map.Entry<String, WordsProbability> entry : wordsProbabilities.entrySet()) {
            System.out.println("clave: " + entry.getKey() + " ,palabra: " + entry.getValue().getWord()
            +" ,proba spam:  "+entry.getValue().getSpamProbability() + ", proba de no spam:  "+entry.getValue().getNotSpamProbability()
            + ",cantidad en no spam:"+entry.getValue().getTotalEmails()
            );
        }
        */
            fileManager.saveWordsProbability(wordsProbabilities);
        }
        else{throw new Exception("Se cancelo el entrenamiento porque se necesitan mas correos para entrenar el sistema");}
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
