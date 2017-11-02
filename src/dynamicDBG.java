import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class dynamicDBG {
	
	public static void main(String[] args) throws IOException{
//**********build DBG from input string:
		
//		Scanner ScanLines = new Scanner(new File("data/datasetSmall.txt"));
//		String seq = ScanLines.nextLine();
//		ScanLines.close();
//		deBruijnGraph dbg = new deBruijnGraph(seq);
//		dbg.buildGraph(4);
		
//**********build DBG from K-mers:
		
		Scanner ScanLines = new Scanner(new File("data/dataset1.txt"));
		ArrayList<String> lines = new ArrayList<String>();
		while(ScanLines.hasNextLine()){
			lines.add(ScanLines.nextLine());
		}
		ScanLines.close();
		deBruijnGraph dbg = new deBruijnGraph("");
		String[] kmers = lines.toArray(new String[0]);
//		dbg.buildGraphfromKmers(kmers);
		
//**********generate hash from n K-mers:	
		
		String[] s = {"asda","ddda","fsda","fffa","asdb"};
//		generate rabin karp hash value for kmers
		generateHash g = new generateHash(kmers);
    	g.showMap();
	}
}