package edu.wvup.acottri9;

/**
 * A class containing methods to encrypt and
 * decrypt text.
 */
public class CypherTools
{
    /**
     * Encrypts some text with a Caesar cypher.
     *
     * @param text  the text
     * @param shift the shift
     * @return the encrypted text
     */
    public static String encryptText(String text, int shift)
    {
        if(text == null || text.isEmpty())
        {
            return "";
        }
        char[] textArea = text.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char ch: textArea)
        {
            if(Character.isWhitespace(ch))
            {
                builder.append(ch);
            }
            else
            {
                builder.append((char)(ch + shift));
            }
        }

        return builder.toString();
    }

    /**
     * Decrypt some text with a Caesar Cypher algorithmn.
     *
     * @param text  the encrypted text
     * @param shift the shift
     * @return the decrypted text
     */
    public static String decryptText(String text, int shift)
    {
        if(text == null || text.isEmpty())
        {
            return "";
        }
        char[] textArea = text.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char ch: textArea)
        {
            if(Character.isWhitespace(ch))
            {
                builder.append(ch);
            }
            else
            {
                builder.append((char)(ch - shift));
            }
        }

        return builder.toString();
    }
}
