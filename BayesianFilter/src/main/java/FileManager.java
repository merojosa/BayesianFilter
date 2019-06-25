import java.io.FileReader;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.HashSet;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class FileManager
{
    public List<Double> loadTrainingConfiguration()
    {
        return null;
    }

    /**
     * Reads a file with the most used words of the spanish and english language to return a Hashset with them.
     * The file most be found on the direction files/StopWords.txt
     * The method also change the words to lower case.
     * @return stopWords
     */
    public HashSet<String> getStopWords()
    {
        HashSet<String> stopWords= stopWords = new HashSet<String>();
        try
        {
            FileReader fileReader = new FileReader("files/StopWords.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                stopWords.add(line.toLowerCase());
            }

            bufferedReader.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        return stopWords;
    }

    /**
     * Read the file with the training.
     * The file most be found on the direction tokens/training.dat
     * @return wordsProbabilities
     */
    public Map<String, WordsProbability> loadWordsProbability()
    {
        Map<String,WordsProbability> wordsProbabilities = new HashMap<String,WordsProbability>(){{}};
        try
        {
            FileInputStream file = new FileInputStream("tokens/training.dat");
            ObjectInputStream is = new ObjectInputStream(file);
            while (true)
            {
                Object objectReaded;

                objectReaded = is.readObject();
                String key = (String) objectReaded;

                objectReaded = is.readObject();
                WordsProbability currentWord=(WordsProbability)objectReaded;

                wordsProbabilities.put(key, currentWord);
/*
                System.out.println("Palabra " + currentWord.getWord());
                System.out.println("proba spam : " + currentWord.getSpamProbability());
                System.out.println("proba notspam : " + currentWord.getNotSpamProbability()+"\n");
*/
            }
        }
        catch (Exception o)
        {
            System.out.println("No se pudo abir el archivo");
        }
        return wordsProbabilities;
    }

    public void saveTrainingData(double spamProbability, double spamThreshold, int emailAmount)
    {

    }

    /**
     * Writes or creates a file with the training.
     * You can find the file in the direction tokens/training.dat
     * @param wordsProbability
     */
    public void saveWordsProbability(Map<String, WordsProbability> wordsProbability)
    {
        try {
            FileOutputStream file = new FileOutputStream("tokens/training.dat");
            ObjectOutputStream os = new ObjectOutputStream(file);
            for(Map.Entry<String,WordsProbability> actualWord: wordsProbability.entrySet())
            {
                os.writeObject(actualWord.getKey());
                os.writeObject(actualWord.getValue());
            }
            file.close();
        }
        catch (Exception o)
        {
            System.out.println("Hubo un problema al guardar los datos del entrenamiento.");
        }
    }
}
