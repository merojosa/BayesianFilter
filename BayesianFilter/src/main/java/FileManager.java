import java.io.FileReader;
import java.io.File;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;


public class FileManager
{
    public ArrayList<Double> loadTrainingConfiguration() throws IOException
    {
        ArrayList<Double>config = new ArrayList<Double>(3);
        File configFile = new File("files/config.txt");
        BufferedReader buffer = new BufferedReader(new FileReader(configFile));
        String read;
        while ((read = buffer.readLine()) != null)
        {
            config.add(Double.valueOf(read));
        }
        buffer.close();
        return config;
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
            Object objectReaded = null;
            boolean read = false;
            while (!read)
            {
                try {
                    objectReaded = is.readObject();
                }
                catch(Exception o)
                {
                    read = true;
                }
                if(!read) {
                    String key = (String) objectReaded;

                    objectReaded = is.readObject();
                    WordsProbability currentWord = (WordsProbability) objectReaded;

                    wordsProbabilities.put(key, currentWord);
                    /*
                    System.out.println("Palabra " + currentWord.getWord());
                    System.out.println("proba spam : " + currentWord.getSpamProbability());
                    System.out.println("proba notspam : " + currentWord.getNotSpamProbability() + "\n");
                    */

                }
            }
        }
        catch (Exception o)
        {
            if(o.getMessage()==null || o.getMessage().equals("WordsProbability cannot be cast to java.lang.String"))
            {
                System.out.println("Se termino de leer el archivo.\n");
            }
            else
                {
                System.out.println("No se pudo abir el archivo");
                }
        }
        return wordsProbabilities;
    }

    public void saveTrainingData(double spamProbability, double spamThreshold, int emailAmount) throws IOException
    {
        FileWriter writer = new FileWriter("files/config.txt");
        BufferedWriter buffer = new BufferedWriter(writer);
        buffer.write(new String().valueOf(spamProbability));
        buffer.newLine();
        buffer.write(new String().valueOf(spamThreshold));
        buffer.newLine();
        buffer.write(new String().valueOf(emailAmount));
        buffer.close();
        writer.close();
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
