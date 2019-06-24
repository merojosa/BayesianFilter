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
    public List<Float> loadTrainingConfiguration()
    {
        return null;
    }


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

    public void saveTrainingData(float spamProbability, float spamThreshold, int emailAmount)
    {

    }

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
