import java.io.Serializable;

public class WordsProbability  implements Serializable
{
    //quitar para proyecto
    private int wordAmount =1;

    public int getWordAmount() {
        return wordAmount;
    }

    public void setWordAmount(int wordAmount) {
        this.wordAmount = wordAmount;
    }

    //

    private String word;

    private int totalSpam;

    private int totalEmails;

    private double spamProbability;

    private double notSpamProbability;

    public WordsProbability(String word, int totalSpam, int totalEmails, double spamProbability, double notSpamProbability)
    {
        this.word = word;
        this.totalSpam = totalSpam;
        this.totalEmails = totalEmails;
        this.spamProbability = spamProbability;
        this.notSpamProbability = notSpamProbability;
    }

    public WordsProbability()
    {
    }

    public int getTotalSpam() {
        return totalSpam;
    }

    public void setTotalSpam(int totalSpam) {
        this.totalSpam = totalSpam;
    }

    public int getTotalEmails() {
        return totalEmails;
    }

    public void setTotalEmails(int totalEmails) {
        this.totalEmails = totalEmails;
    }

    public double getSpamProbability() {
        return spamProbability;
    }

    public void setSpamProbability(float spamProbability) {
        this.spamProbability = spamProbability;
    }

    public double getNotSpamProbability() {
        return notSpamProbability;
    }

    public void setNotSpamProbability(float notSpamProbability) {
        this.notSpamProbability = notSpamProbability;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

}
