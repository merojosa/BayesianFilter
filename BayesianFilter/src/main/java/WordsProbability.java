import java.io.Serializable;

public class WordsProbability  implements Serializable {



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

    private int totalSpamEmails;

    private float spamProbability;

    private float notSpamProbability;

    public WordsProbability(String word, int totalSpam, int totalSpamEmails, float spamProbability, float notSpamProbability) {
        this.word = word;
        this.totalSpam = totalSpam;
        this.totalSpamEmails = totalSpamEmails;
        this.spamProbability = spamProbability;
        this.notSpamProbability = notSpamProbability;
    }

    public WordsProbability() {
    }

    public int getTotalSpam() {
        return totalSpam;
    }

    public void setTotalSpam(int totalSpam) {
        this.totalSpam = totalSpam;
    }

    public int getTotalSpamEmails() {
        return totalSpamEmails;
    }

    public void setTotalSpamEmails(int totalSpamEmails) {
        this.totalSpamEmails = totalSpamEmails;
    }

    public float getSpamProbability() {
        return spamProbability;
    }

    public void setSpamProbability(float spamProbability) {
        this.spamProbability = spamProbability;
    }

    public float getNotSpamProbability() {
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