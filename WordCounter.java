import java.util.regex.*;

import java.io.IOException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//coutns number of words up to and including the stopword
//if stop = null, count all the words
// text = text to process, stop = word to stop counting to
// returns the word count

//throws InvalidStopwordException if stop word is not found in text
//throws TooSmallText if count is less than 5

public class WordCounter {
    
    public static int processText(StringBuffer text, String stop) throws InvalidStopwordException, TooSmallText {
        if (text == null) {
            text = new StringBuffer();
        }
        Pattern regex = Pattern.compile("[a-zA-Z0-9']+");
        Matcher regexMatcher = regex.matcher(text);
        
        List<String> words = new ArrayList<>();
        while (regexMatcher.find()) {
            words.add(regexMatcher.group());
        }
        int totalWords = words.size();

        if (totalWords < 5) {
            throw new TooSmallText("Only found " + totalWords + " words.");
        }

        if (stop == null) {
            return totalWords;
        } else {
            int count = 0;
            boolean foundStop = false;

            for (String word : words) {
                count++;
                if (word.equals(stop)) {
                    foundStop = true;
                    break;
                }
            }
            if (!foundStop) {
                throw new InvalidStopwordException("Couldn't find stopword: " + stop);
            }
            return count;
        }
    }

    public static StringBuffer processFile(String path) throws EmptyFileException {
        Scanner scanner = new Scanner(System.in);
        File file = new File(path);
        StringBuffer stuff = new StringBuffer();
        boolean fileRead = false;
    
        try {
            while (!fileRead) {
                if (!file.exists() || !file.isFile()) {
                    System.out.println("file not found, enter a valid path to the file");
                    path = scanner.nextLine();
                    file = new File(path);
                    continue;
                }
    
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stuff.append(line);
                    }
                    fileRead = true;
                } catch (IOException e) {
                    System.out.println("error reading file, enter a valid path to the file");
                    path = scanner.nextLine();
                    file = new File(path);
                }
            }
    
            if (stuff.length() == 0) {
                throw new EmptyFileException(path + " was empty");
            }
            return stuff;
        } finally {
            scanner.close(); 
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        WordCounter counter = new WordCounter();
        StringBuffer text = new StringBuffer();
        String stop = null;

        if (args.length >= 2) { // check if at least 2 command line arguments are present, then set stop to the second arg
            stop = args[1];
        }

        try {
            int option = 0;
            boolean validOption = false;
            
            while (!validOption) {
                System.out.println("choose an option, 1: process file, 2: process text: ");
                try {
                    option = Integer.parseInt(scanner.nextLine());
                    if (option != 1 && option != 2) {
                        System.out.println("option has to be 1 or 2");
                        continue;
                    }
                    validOption = true;
                } catch (NumberFormatException e) {
                    System.out.println("enter a valid integer");
                }
            }

            if (option == 1) { //option 1
                String filePath = (args.length >= 1) ? args[0] : "";
                if (filePath.isEmpty()) {
                    System.out.println("enter the file path: ");
                    filePath = scanner.nextLine();
                }

                try {
                    text = WordCounter.processFile(filePath);
                } catch (EmptyFileException e) {
                    System.out.println(e.getMessage());
                    //continues with empty text
                }
            } else { //option 2
                System.out.println("Enter the text: ");
                String line;
                while (!(line = scanner.nextLine()).isEmpty()) {
                    text.append(line).append("\n");
                }
            }

            // process text with stopword
            try {
                int wordCount = WordCounter.processText(text, stop);
                System.out.println("Found " + wordCount + " words.");
            } catch (InvalidStopwordException e) {
                System.out.println(e.toString());
                System.out.println("Enter a new stopping word: ");
                stop = scanner.nextLine();
                
                try {
                    int wordCount = WordCounter.processText(text, stop); 
                    System.out.println("Found " + wordCount + " words.");
                } catch (InvalidStopwordException e1) { 
                    System.out.println("Stop " + stop + " not found, processing ending");
                } catch (TooSmallText e2) {
                    System.out.println(e2.toString());
                }
            } catch (TooSmallText e) {
                System.out.println(e.toString());
            }
        } finally {
            scanner.close(); 
        }
    }
}