package reference;


public class testRabinKarp {
	
    /** 
     * Takes a pattern string and an input string as command-line arguments;
     * searches for the pattern string in the text string; and prints
     * the first occurrence of the pattern string in the text string.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String pat = "abc";
        String txt = "hfhalskdhfalkjhsekrjhlksjabcdhrlakjhdfjahdfjalhsjdhfahsdfkjhasd";

        rabinKarp searcher = new rabinKarp(pat);
        int offset = searcher.search(txt);

        // print results
        System.out.println("text:    " + txt);

        // from brute force search method 1
        System.out.print("pattern: ");
        for (int i = 0; i < offset; i++)
        	System.out.print(" ");
        System.out.println(pat);
        System.out.println(offset);
    }
	
}