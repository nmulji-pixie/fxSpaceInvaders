package edu.vanier.ufo.helpers;

public class StringHelper {
    public static String toTitleCase(String x) {
        StringBuilder titleCaseBuilder = new StringBuilder();
        boolean nextIsTitle = true;
        
        for (char c : x.toCharArray()) {
            if (!Character.isAlphabetic(c))
                nextIsTitle = true;
            else if (nextIsTitle) {
                c = Character.toTitleCase(c);
                nextIsTitle = false;
            }
            
            titleCaseBuilder.append(c);
        }
        
        return titleCaseBuilder.toString();
    }
}
