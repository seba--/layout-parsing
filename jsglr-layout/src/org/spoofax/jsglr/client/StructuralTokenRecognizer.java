package org.spoofax.jsglr.client;

public class StructuralTokenRecognizer {
    
    private final static String[] closingTokens={"}", ")", "]|", ">", "|", "]", "*/"};
    private final static String[] openingTokens={"{", "(", "|[", "<", "|", "["};
    private final static String[] separatorTokens={",", ";", "<+", "+>", "+", ">", "<", "=>", "->", "&&", "&", "||", "|"};
    
    public boolean isScopeClosingLine(String s){
        return startsWith(s, closingTokens);
    }
    
    public boolean isScopeOpeningLine(String s){
        return startsWith(s, openingTokens);
    }
    
    public boolean isSeparatorStartedLine(String s){
        return startsWith(s, separatorTokens);
    }

    private boolean startsWith(String s, String[] tokens) {
        for (int i = 0; i < tokens.length; i++) {
            if(s.startsWith(tokens[i]))
                return true;
        }
        return false;
    }
    
    private boolean endsWith(String s, String[] tokens) {
        s = s.trim();
        for (int i = 0; i < tokens.length; i++) {
            if(s.endsWith(tokens[i]))
                return true;
        }
        return false;
    }

    public int separatorIndent(String lineContent) {
        String line = lineContent.trim();
        int length=line.length();
        for (int i = 0; i < separatorTokens.length; i++) {
            if(line.startsWith(separatorTokens[i])){
                line=line.substring(separatorTokens[i].length());
                line = line.trim();
            }
        }
        return length-line.length();
    }

    public boolean isSeparatorEndingLine(String line) {
        return endsWith(line, separatorTokens);
    }
    
    public char[] removeSeparatorAtTheEnd(String aLine) {
        String line = aLine.trim();
        for (int i = 0; i < separatorTokens.length; i++) {
            if(line.endsWith(separatorTokens[i])){
                String toParse=line.substring(0, line.length()-separatorTokens[i].length());
                return toParse.toCharArray();
            }
        }
        return new char[0];
    }

}
