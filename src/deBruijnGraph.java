
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class deBruijnGraph {
	private String text;
	private List<String> nodes;
	private int base;
	private String agct = "AGCT";
	private int[][] IN;
	private int[][] OUT;
	public generateHash g;
	
	public deBruijnGraph(String text) {
       this.text = text;
       nodes = new ArrayList<String>();
    }
	//build de Bruijn graph from input string
	public String[] buildGraph (int k) throws IOException {
		int length = text.length();
		String[] subSeqs = new String[length - k + 1];
		for(int i=0; i <= length - k; i++){
			String fromNode = text.substring(i, i + k - 1);
			String toNode = text.substring(i + 1, i + k);		
			subSeqs[i] = fromNode + " -> " + toNode;
			if(!nodes.contains(fromNode)) nodes.add(fromNode);
			if(!nodes.contains(toNode)) nodes.add(toNode);
			
		}
		g = new generateHash(this.nodes.toArray(new String[nodes.size()]));
		this.base = g.generate();
		g.showMap();
		subSeqs = glueVertices(subSeqs, k - 1);
		File f = new File("data/BruijinGraph.txt");
		FileWriter out = new FileWriter(f);
		for(int i = 0; i <= length - k; i++){
			if(subSeqs[i].charAt(0) != ' '){
				out.write( subSeqs[i] + "\n");	
			}
		}
		out.close();
		
		return this.nodes.toArray(new String[nodes.size()]);
	}
	//build de Bruijn graph from input kmers
	public String[] buildGraphfromKmers (String[] kmers) throws IOException {
		int number = kmers.length;
		int length = kmers[0].length();
		String[] subSeqs = new String[number];
		
		for(int i=0; i<number; i++){
			String fromNode = kmers[i].substring(0, length-1);
			String toNode = kmers[i].substring(1);		
			subSeqs[i] = fromNode +" -> " + toNode;	
			if(!nodes.contains(fromNode)) nodes.add(fromNode);
			if(!nodes.contains(toNode)) nodes.add(toNode);
		}
		g = new generateHash(this.nodes.toArray(new String[nodes.size()]));
		this.base = g.generate();
		g.showMap();
		subSeqs = glueVertices(subSeqs, length-1);
		File f = new File("data/DBGfromKmers.txt");
		FileWriter out = new FileWriter(f);	
		for(int i=0; i<number; i++){	
			if(subSeqs[i].charAt(0) != ' '){
				out.write( subSeqs[i] +"\n");	
			}
		}
		out.close();
		
		return this.nodes.toArray(new String[nodes.size()]);
	}
	
	//Glue all vertices that have the same label
	private String[] glueVertices(String[] subSeqs, int k) {
		OUT = new int[nodes.size()][4];
		IN = new int[nodes.size()][4];
		Arrays.sort(subSeqs);
		int l = subSeqs.length;
		String aftStr = subSeqs[0].substring(0, k);
		String fromNode = aftStr;
		String toNode = subSeqs[0].substring(k + 4, k + 4 + k);
		OUT[(int) g.hashFunction(base, fromNode)][agct.indexOf(toNode.charAt(k - 1))] = 1;
		IN[(int) g.hashFunction(base, toNode)][agct.indexOf(fromNode.charAt(0))] = 1;
		for(int i = 1; i < l; i++){
			aftStr = subSeqs[i].substring(0, k);
			fromNode = aftStr;
			toNode = subSeqs[i].substring(k + 4, k + 4 + k);
			OUT[(int) g.hashFunction(base, fromNode)][agct.indexOf(toNode.charAt(k - 1))] = 1;
			IN[(int) g.hashFunction(base, toNode)][agct.indexOf(fromNode.charAt(0))] = 1;
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
	public int getBase() {
		return this.base;
	}
	public int[][] getIN() {
		return this.IN;
	}
	public int[][] getOUT() {
		return this.OUT;
	}
}