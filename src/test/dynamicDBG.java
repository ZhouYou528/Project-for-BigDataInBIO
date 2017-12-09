package test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import debruijnGraph.deBruijnGraph;

public class dynamicDBG {
	
	public static void main(String[] args) throws Exception{
		
//**********build DBG from input string:
		
//		Scanner ScanLines = new Scanner(new File("data/datasetSmall.txt"));
//		String seq = ScanLines.nextLine();
//		ScanLines.close();
//		deBruijnGraph dbg = new deBruijnGraph(seq);
//		dbg.buildGraph(4);
		

//**********preparing input data:	
		//test data set
//		Scanner ScanLines = new Scanner(new File("data/dataset1.txt"));
//		ArrayList<String> lines = new ArrayList<String>();
//		while(ScanLines.hasNextLine()){
//			lines.add(ScanLines.nextLine());
//		}
//		ScanLines.close();
		deBruijnGraph dbg = new deBruijnGraph("");
		//String[] kmers = lines.toArray(new String[0]);
		
		//Sample test data: GAGGTGAGTGAGGTC
		//String[] kmers = {"GAGGTG","AGGTGA","GGTGAG","GTGAGT","TGAGTG","GAGTGA","AGTGAG","GTGAGG","TGAGGT","GAGGTC"};
		System.out.println("******************************** Input ********************************\n");
		long time3 = System.currentTimeMillis();
		System.out.println("Start reading fastq file...");
		fastqReader fr = new fastqReader(args[0]);
		//fastqReader fr = new fastqReader("/Users/youzhou/Documents/BigDataInBIO/projectCode/dynamicDeBruijnGraph/data/test.fastq");
		int k = Integer.parseInt(args[1]);
		//int k = 51;
		String[] kmers = fr.generateKmer(k);
		System.out.println("Done!");
		time3 = System.currentTimeMillis() - time3;
		System.out.println("fastq file reading time: " + time3 + " ms\n");

//**********build DBG from K-mers:
		System.out.println("********************************* DBG *********************************\n");
		long time1 = System.currentTimeMillis();
		String[] nodes = dbg.buildGraphfromKmers(kmers);
		time1 = System.currentTimeMillis() - time1;
		System.out.println("Done constructing de Bruijn Graph");
		System.out.println("de Bruijn Graph constructing time: " + time1 + " ms\n");
		
//**********generate hash from n K-mers:	
			
		int base = dbg.getBase();
		System.out.println("base: " + base);
		System.out.println("PRIME: " + dbg.g.getPrime()+ "\n");
		
		//for testing: hash part
		//long res = dbg.g.hashFunction(base, "GATAGTCCAAA");
		//System.out.println("hash(GATAGTCCAAA): "+ res);
		//IN and OUT
		//int[][] IN = dbg.getIN();
		//int[][] OUT = dbg.getOUT();
		
		//construct forest
		System.out.println("******************************** Forest ********************************\n");
		long time = System.currentTimeMillis();
		dbg.forestConstruction(k*3/5);
		time = System.currentTimeMillis() - time;
		System.out.println("Covering forest constructing time: " + time + " ms\n");
		//membership query
		System.out.println("**************************** Membership query *****************************\n");
		long time2 = System.currentTimeMillis();
		int fault = 0;
		//50 nodes in the DBG
		for (int i = 0; i < 50; i++) {
			String s = nodes[i];
			boolean res1 = dbg.isMember(s);
			if (!res1)
				fault++;
		}
		//50 nodes not in the DBG
		for (int i = 0; i < 50; i++) {
			String s = kmers[i];
			boolean res2 = dbg.isMember(s);
			if (res2)
				fault++;
		}
		time2 = System.currentTimeMillis() - time2;
		System.out.println("Number of membership query: 100");
		System.out.println("total Membership query time: " + time2 + " ms " + "Accuracy: " + 100 * (50 - fault) / 50 + "%\n");
		
		System.out.println("***************************** Dynamic Edge ******************************\n");
		//delete an edge
		System.out.println("Delete an edge between TGAGG and GAGGT: ");
		boolean res = dbg.deleteEdge("TGAGG", "GAGGT");
		if(res) System.out.println("Success!");
		
		System.out.println("Delete an edge between " + nodes[0] + " and " + nodes[10]+": ");
		res = dbg.deleteEdge(nodes[0], nodes[10]);
		if(res) System.out.println("Success!");
		
		System.out.println("Delete an edge between " + nodes[0] + " and " + nodes[1]+": ");
		res = dbg.deleteEdge(nodes[0], nodes[1]);
		if(res) System.out.println("Success!");
		
		//add an edge
		System.out.println("Add an edge between TGAGG and GAGGT: ");
		res = dbg.addEdge("TGAGG", "GAGGT");
		if(res) System.out.println("Success!");
		
		System.out.println("Add an edge between " + nodes[0] + " and " + nodes[10]+": ");
		res = dbg.addEdge(nodes[0], nodes[10]);
		if(res) System.out.println("Success!");
		
		System.out.println("Add an edge between " + nodes[0] + " and " + nodes[1]+": ");
		res = dbg.addEdge(nodes[0], nodes[1]);
		if(res) System.out.println("Success!");
		
	}
}