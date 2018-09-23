package edu.wvup.acottri9;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The type Line counter.
 */
public class LineCounter
{

    /**
     * Count int.
     *
     * @param sourceFile the source file
     * @return the int
     */
    public static int CountLinesInFile(String sourceFile)
    {
        int lineCount = 0;
        File file = new File(sourceFile);
        StringBuilder completeString = new StringBuilder();
        Scanner newScanner;
        try
        {
            newScanner = new Scanner(file);
            while ( newScanner.hasNextLine( ) )
            {
                completeString.append(newScanner.nextLine()).append("\n");
            }
            newScanner.close();
        }
        catch (FileNotFoundException e)
        {
            System.err.println(sourceFile + " not found");
        }

        String[] lines = completeString.toString().split("\n");


        for(String line : lines)
        {
            if (!line.trim().startsWith("//") && !line.trim().startsWith("/*") && !line.trim().startsWith("*/") && !line.trim().startsWith("*") && !line.isEmpty())
            {
                lineCount++;
            }
        }

        return lineCount;

    }


    /**
     * Counts lines in a code file.
     *
     * @param rawLines : the string it contains
     * @return the amount of lines of proper code
     */
    public static int CountLines(String rawLines)
    {
        int lineCount = 0;

        String[] lines = rawLines.split("\n");


        for(String line : lines)
        {
            if (!line.trim().startsWith("//") && !line.trim().startsWith("/*") && !line.trim().startsWith("*/") && !line.trim().startsWith("*") && !line.isEmpty())
            {
                lineCount++;
            }
        }

        return lineCount;

    }

    /**
     * Returns a map containing a count
     * of a how many occurrences of each vowel are in
     * a given string.
     *
     * @param input an string to return
     * @return a Map with each vowel as the key, and the count
     * as a value.
     */
    public static Map<Character, Integer> countVowels(String input)
    {
        Map<Character, Integer> vowelCount = new HashMap<Character, Integer>();

        vowelCount.put('a',0);
        vowelCount.put('e',0);
        vowelCount.put('i',0);
        vowelCount.put('o',0);
        vowelCount.put('u',0);
        for (char ch: input.toCharArray())
        {
            switch (ch)
            {
                case 'a':
                    vowelCount.put('a', vowelCount.get('a') + 1);
                    break;
                case 'e':
                    vowelCount.put('e', vowelCount.get('e') + 1);
                    break;
                case 'i':
                    vowelCount.put('i', vowelCount.get('i') + 1);
                    break;
                case 'o':
                    vowelCount.put('o', vowelCount.get('o') + 1);
                    break;
                case 'u':
                    vowelCount.put('u', vowelCount.get('u') + 1);
                    break;
                default:
                    break;
            }
        }
        return vowelCount;
    }

    /**
     * Counts the number of times a
     * character appears in a string
     *
     * @param str the string to read
     * @param ch  the character to look for
     * @return the number of occurances.
     */
    public static int countACharacter(String str, char ch)
    {
        int amount = 0;
        for (char letter: str.toCharArray())
        {
           if(ch == letter)
           {
              amount++;
           }
        }
        return amount;
    }

    /**
     * Counts the amount of characters in a line
     *
     * @param rawLines : a raw string
     * @return the amount of characters
     */
    public static int CountCharacters(String rawLines)
    {
        int charCount = 0;

        String[] lines = rawLines.split("\n");


        for(String line : lines)
        {
            charCount += line.trim().length();
        }

        return charCount;

    }
}
