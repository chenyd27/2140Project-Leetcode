package com.leetcode.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseLeetcodeContent {

    private final static int CONTENT_SKIP_CHAR_N = 28;     // Empirical skip character (useless
    private final static String STOP_WORD_FILE = "./leetcodeSearch/src/main/java/com/leetcode/utils/stopword.txt";
    private static HashSet<String> stopWords = new HashSet<>();

    public static void loadStopWords()
    {
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(STOP_WORD_FILE));
            String line = reader.readLine();

            while (line != null) {
                stopWords.add(line);
                // read next line
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String parseContent(String content)
    {
        StringBuffer stringBuffer = new StringBuffer();
        String[] split = content.toLowerCase().split(" ");
        for (int i = CONTENT_SKIP_CHAR_N; i < split.length; i++)
        {
            // remove the regular stop words
            if (!stopWords.contains(split[i]))
            {
                // to handle the code part
                if (i != split.length - 1 && split[i].equals("class") && split[i+1].equals("solution"))
                {
                    // fast forward to first "return";
                    while(i < split.length && !split[i].equals("return")) i++;
                    i++;
                }
                // to handle the long latex
                else if (split[i].equals("\\begin{aligned}")) {
                    // fast forward to first ending sign
                    while(i < split.length && !split[i].equals("\\end{aligned}")) i++;
                }
                else if (!split[i].startsWith("\\"))
                {
                    // remove all the symbols other than letter of the alphabet or hyphen
                    String[] split2 = split[i].split("[^a-zA-Z-]");
                    // remove stopwords again to remove the meaningless variable names and expressions in the solutions
                    for (String s : split2)
                    {
                        if (!stopWords.contains(s) && s.length() > 0) {
                            stringBuffer.append(s);
                            stringBuffer.append(' ');
                        }
                    }

                }
            }
        }
        return stringBuffer.toString();
    }

    // a test of the method on single json file
    public static void main(String args[])
    {
        String content = "";
        try {
            FileInputStream fis = new FileInputStream("./leetcodeSearch/src/main/java/com/leetcode/utils/Temp.json");
            byte[] buffer = new byte[10];
            StringBuilder sb = new StringBuilder();
            while (fis.read(buffer) != -1) {
                sb.append(new String(buffer));
                buffer = new byte[10];
            }
            fis.close();
            content = sb.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        ParseLeetcodeContent.loadStopWords();
        String parsedStr = ParseLeetcodeContent.parseContent(content);
        System.out.println(parsedStr);
    }
}
