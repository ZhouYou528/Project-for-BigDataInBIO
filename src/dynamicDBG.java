import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class dynamicDBG {
	
	public static void main(String[] args) throws IOException{
//		Scanner ScanLines = new Scanner(new File("data/datasetSmall.txt"));
//		String seq = ScanLines.nextLine();
//		ScanLines.close();
//		deBruijnGraph dbg = new deBruijnGraph(seq);
//		dbg.buildGraph(4);
		Scanner ScanLines = new Scanner(new File("data/dataset1.txt"));
		ArrayList<String> lines = new ArrayList<String>();
		while(ScanLines.hasNextLine()){
			lines.add(ScanLines.nextLine());
		}
		ScanLines.close();
		String[] kmers = lines.toArray(new String[0]);
//		dbg.buildGraphfromKmers(kmers);
		
//		String[] s = {"asda","ddda","fsda","fffa"};
//		generate rabin karp hash value for kmers
		generateHash g = new generateHash(kmers);
    	g.showMap();
	}
}