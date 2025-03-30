import java.util.regex.Pattern;
import java.util.regex.*;

import java.io.IOException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.util.Scanner;

//coutns number of words up to and including the stopword
//if stop = null, count all the words
// text = text to process, stop = word to stop counting to
// returns the word count

//throws InvalidStopwordException if stop word is not found in text
//throws TooSmallText if count is less than 5

public class WordCounter {
    
    public int processTest(StringBuffer text, String stop) throws InvalidStopwordException, TooSmallText {
        
        if (text == null) {
            text = new StringBuffer();
        }
    

        Pattern regex = Pattern.compile("[a-zA-Z0-9']+");
        Matcher regexMatcher = regex.matcher(text);

        int count = 0;
        boolean foundStop = (stop == null);

        while (regexMatcher.find()) {
            String word = regexMatcher.group(); //?
            count++;

            if (stop != null && word.equals(stop)) {
                foundStop = true;
                break;
            }
        }

        if (!foundStop) {
            throw new InvalidStopwordException("Stopword " + stop + " was not found in the text");
        }

        if (count < 5) {
            throw new TooSmallText("The text contains less than 5 words ( " + count + " )");
        }

        return count;
    }

    public StringBuffer processFile(String path) throws EmptyFileException {
        Scanner scanner = new Scanner(System.in);
        File file = new File(path);

        while (!file.exists() || !file.isFile()) { //exception handling
            System.out.println("file not found, please enter a valid path to the file");
            path = scanner.nextLine();
            file = new File(path);
        }

        StringBuffer stuff = new StringBuffer(); //review StringBuffer

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stuff.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error reading file, enter a valid path to the file");
            path = scanner.nextLine();
            try {
                return processFile(path);
            } catch (EmptyFileException a) {
                throw a;
            }
        }

        if (stuff.length() == 0) {
            throw new EmptyFileException("empty file");
        }

        return stuff;

    }

}