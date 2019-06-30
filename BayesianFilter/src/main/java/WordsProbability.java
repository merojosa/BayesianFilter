import java.io.Serializable;

public class WordsProbability  implements Serializable
{
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
        this.totalSpam = 0;
        this.totalEmails = 0;
    }

    public int getTotalSpam() { return totalSpam; }

    public void setTotalSpam(int totalSpam) { this.totalSpam = totalSpam; }

    public int getTotalEmails() { return totalEmails; }

    public void setTotalEmails(int totalEmails) { this.totalEmails = totalEmails; }

    public double getSpamProbability() { return spamProbability; }

    public void setSpamProbability(double spamProbability) { this.spamProbability = spamProbability; }

    public double getNotSpamProbability() { return notSpamProbability; }

    public void setNotSpamProbability(double notSpamProbability) { this.notSpamProbability = notSpamProbability; }

    public String getWord() { return word; }

    public void setWord(String word) { this.word = word; }
}
