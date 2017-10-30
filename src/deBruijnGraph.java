import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;


public class deBruijnGraph {
	private String text;
	
	public deBruijnGraph(String text) {
       this.text = text;
    }
	//build de Bruijn graph from input string
	public void buildGraph (int k) throws IOException {
		int length = text.length();
		String[] subSeqs = new String[length - k + 1];
		for(int i=0; i <= length - k; i++){
			String fromNode = text.substring(i, i + k - 1);
			String toNode = text.substring(i + 1, i + k);		
			subSeqs[i] = fromNode + " -> " + toNode;	
		}
		subSeqs = glueVertices(subSeqs, k - 1);
		File f = new File("data/BruijinGraph.txt");
		FileWriter out = new FileWriter(f);
		for(int i = 0; i <= length - k; i++){
			if(subSeqs[i].charAt(0) != ' '){
				out.write( subSeqs[i] + "\n");	
			}
		}
		out.close();
	}
	//build de Bruijn graph from input kmers
	public void buildGraphfromKmers (String[] kmers) throws IOException {
		int number = kmers.length;
		int length = kmers[0].length();
		String[] subSeqs = new String[number];
		
		for(int i=0; i<number; i++){
			String fromNode = kmers[i].substring(0, length-1);
			String toNode = kmers[i].substring(1);		
			subSeqs[i] = fromNode +" -> " + toNode;	
		}
		subSeqs = glueVertices(subSeqs, length-1);
		File f = new File("data/DBGfromKmers.txt");
		FileWriter out = new FileWriter(f);	
		for(int i=0; i<number; i++){	
			if(subSeqs[i].charAt(0) != ' '){
				out.write( subSeqs[i] +"\n");	
			}
		}
		out.close();
	}
	
	//Glue all vertices that have the same label
	private static String[] glueVertices(String[] subSeqs, int k) {
		Arrays.sort(subSeqs);
		int l = subSeqs.length;
		for(int i = 1; i < l; i++){
			String aftStr = subSeqs[i].substring(0, k);
			
			for(int j = 0; j < i; j++){
				String befStr = subSeqs[j].substring(0, k);
				
				if(befStr.equals(aftStr)){
					subSeqs[j] += "," + subSeqs[i].substring(k+4);
					subSeqs[i] = "    " +subSeqs[i];
				}
			}
		}
		
		return subSeqs;		
	} 

}