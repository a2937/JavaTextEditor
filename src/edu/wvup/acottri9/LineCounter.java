package edu.wvup.acottri9;

import java.io.File;
import java.io.FileNotFoundException;
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
     * Count int.
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
           // if (!line.trim().startsWith("//") && !line.trim().startsWith("/*") && !line.trim().startsWith("*/") && !line.trim().startsWith("*") && !line.isEmpty())
           // {
            //     lineCount++;
          //  }

        }

        return charCount;

    }
}
