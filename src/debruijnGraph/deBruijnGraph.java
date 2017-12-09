package debruijnGraph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;


public class deBruijnGraph {
	private String text;
	private List<String> nodes;
	private int base;
	private String agct = "AGCT";
	private int[][] IN;
	private int[][] OUT;
	public generateHash g;
	public forest f;
	private int alpha;//forest parameter
	
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
		//g.showMap();
		subSeqs = glueVertices(subSeqs, k - 1);
		//File f = new File("data/BruijinGraph.txt");
		//FileWriter out = new FileWriter(f);
//		for(int i = 0; i <= length - k; i++){
//			if(subSeqs[i].charAt(0) != ' '){
//				out.write( subSeqs[i] + "\n");	
//			}
//		}
//		out.close();
		
		return this.nodes.toArray(new String[nodes.size()]);
	}
	//build de Bruijn graph from input kmers
	public String[] buildGraphfromKmers (String[] kmers) throws IOException {
		
		int number = kmers.length;
		int length = kmers[0].length();
		System.out.println("Constructing de Bruijn Graph: k = " + (length - 1) + " N = " + number);
		System.out.println("Please wait... ");
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
		//g.showMap();
		subSeqs = glueVertices(subSeqs, length-1);
//		File f = new File("data/DBGfromKmers.txt");
//		FileWriter out = new FileWriter(f);	
//		for(int i=0; i<number; i++){	
//			if(subSeqs[i].charAt(0) != ' '){
//				out.write( subSeqs[i] +"\n");	
//			}
//		}
//		out.close();
		
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
			OUT[(int) g.hashFunction(base, fromNode)][agct.indexOf(toNode.charAt(k - 1)) == -1 ? 1 : agct.indexOf(toNode.charAt(k - 1))] = 1;
			IN[(int) g.hashFunction(base, toNode)][agct.indexOf(fromNode.charAt(0)) == -1 ? 1 : agct.indexOf(fromNode.charAt(0))] = 1;
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
	//get base value
	public int getBase() {
		return this.base;
	}
	//get IN matrix
	public int[][] getIN() {
		return this.IN;
	}
	//get OUT matrix
	public int[][] getOUT() {
		return this.OUT;
	}
	//construct the covering forest based on alpha value
	public void forestConstruction(int alpha) {
		System.out.println("Constructing covering forest...");
		String[] kmers = this.nodes.toArray(new String[nodes.size()]);
		f = new forest(kmers.length);
		this.alpha = alpha;
		Set<String> unvisited = new HashSet<String>();
		for(int i = 0; i < kmers.length; i++) {
			unvisited.add(kmers[i]);
		}
		String root = kmers[0];
		int[] height = new int[kmers.length];
		Queue<String> queue = new LinkedList<String>();
		queue.add(root);
		unvisited.remove(root);
		f.addToRoot(root);
		Map<Integer, Integer> rootMap = new HashMap<Integer, Integer>();
		rootMap.put((int) g.hashFunction(base, root), (int) g.hashFunction(base, root));
		//BFS
		while(!queue.isEmpty()) {
			List<String> neighbors = new ArrayList<String>();
			String cur = queue.poll();
			int hashed1 = (int) g.hashFunction(base, cur);
			
			this.getNeighbors(cur,hashed1, neighbors);
			for(int i = 0; i < neighbors.size(); i++) {
				String curNeighbor = neighbors.get(i);
				int hashed2 = (int) g.hashFunction(base, curNeighbor);
				if(unvisited.contains(curNeighbor)) {
					queue.add(curNeighbor);
					unvisited.remove(curNeighbor);
					height[hashed2] = height[hashed1] + 1;
					if(height[hashed2] <= 3 * alpha) {
						if(curNeighbor.substring(1).equals(cur.substring(0, cur.length() - 1))) {
							f.setParent(hashed2, cur.charAt(cur.length() - 1), false);
							rootMap.put(hashed2, rootMap.get(hashed1));
						} else if(curNeighbor.substring(0, curNeighbor.length() - 1).equals(cur.substring(1))) {
							f.setParent(hashed2, cur.charAt(0), true);
							rootMap.put(hashed2, rootMap.get(hashed1));
						} else {
							System.out.println("Setting parents ERROR!");
						}
		
					} else {
						if(curNeighbor.substring(1).equals(cur.substring(0, cur.length() - 1))) {
							f.setParent(hashed2, cur.charAt(cur.length() - 1), false);
							rootMap.put(hashed2, rootMap.get(hashed1));
						} else if(curNeighbor.substring(0, curNeighbor.length() - 1).equals(cur.substring(1))) {
							f.setParent(hashed2, cur.charAt(0), true);
							rootMap.put(hashed2, rootMap.get(hashed1));
						} else {
							System.out.println("Setting parents ERROR!");
						}
						String cutOffRoot = f.traverseUp(curNeighbor, this.g, this.base, alpha);
						int hash3 = (int) g.hashFunction(base, cutOffRoot);
						rootMap.put(hash3, hash3);
						f.addToRoot(cutOffRoot);
						f.setParent(hash3, '\0', false);
						String kmer = curNeighbor;
						int hashkmer = hashed2;
						int h = alpha;
						
						while(kmer != null){
							rootMap.put(hashkmer,hash3);
							height[hashkmer] = h;
							h--;
							kmer = f.getParent(kmer, hashkmer);
							if(kmer != null) {
								hashkmer = (int) g.hashFunction(this.base, kmer);
								
							}
						}

					}
					
				}
			}
			if(queue.size() == 0 && unvisited.size() != 0) {
				String next = "";
				for(String s : unvisited) {
					if(s != null) {
						next = s;
						break;
					}
				}
				int nextHash = (int) g.hashFunction(base, next);
				rootMap.put(nextHash, nextHash);
				queue.add(next);
				f.addToRoot(next);
			}
		}
		//f.setTreeHeight(nodes, this.g, this.base);
		//for testing
		int[] treeHeight = new int[kmers.length];
		for(int i = 0; i < height.length; i++) {
	
			if(height[i] > treeHeight[rootMap.get(i)]) {
				treeHeight[rootMap.get(i)] = height[i];
			}
		}
		int sum = 0, count = 0;
		for(int i = 0; i < treeHeight.length; i++) {
			if(treeHeight[i] != 0) {
				sum += treeHeight[i];
				count++;
			}
		}
		List<String> res1 = f.getRoots();
		System.out.println("Done constructing forest!");
		System.out.println("Number of trees: " + res1.size() + " Average tree height: " + sum/count);
	}
	//get all neighbors of a node in undirected graph
	public void getNeighbors(String cur, int hashed, List<String> neighbors){
		for(int i = 0; i < 4; i++) {
			if(OUT[hashed][i] == 1) neighbors.add(cur.substring(1) + this.agct.charAt(i));
			if(IN[hashed][i] == 1) neighbors.add(this.agct.charAt(i) + cur.substring(0, cur.length() - 1));
		}
	}
	//membership query
	public boolean isMember(String kmer) {
		String current = kmer;
		int hashed = (int) g.hashFunction(this.base, kmer);
		//hashed: 1...n-1
		if(hashed >= this.nodes.size()) return false;
		int count = 0;
		while(f.getParent(current, hashed) != null) {
			count++;
			String prev = current;
			int prevHash = hashed;
			if(count > 2 * this.alpha + 10) return false;	
			current = f.getParent(current, hashed);	
			hashed = (int) g.hashFunction(this.base, current);
			if(hashed >= this.nodes.size()) return false;
			//check if parent is legit
			boolean firstlast = f.getfirstlast()[prevHash];
			
			if(firstlast) {
				//parent -> child
				if(IN[prevHash][this.agct.indexOf(current.charAt(0))] != 1 || 
				   OUT[hashed][this.agct.indexOf(prev.charAt(prev.length() - 1))] != 1) {
				   return false;
				}
			}else{
				//child -> parent
				if(OUT[prevHash][this.agct.indexOf(current.charAt(current.length() - 1))] != 1 || 
				   IN[hashed][this.agct.indexOf(prev.charAt(0))] != 1) {
				   return false;
				}
			}
	
		}
		if(f.getRoots().contains(current)) return true;
		return false;
	}
	public boolean addEdge(String v, String u) {
		//check membership
		if(!this.isMember(v) || !this.isMember(u)) {
			System.out.println("Dynamically adding edge failed: Node not exists");
			return false;
		}
		//check whether the edge can be added legitimately
		if(!v.substring(1).equals(u.substring(0, u.length() - 1)) &&
		   !v.substring(0, v.length() - 1).equals(v.substring(1))) {
			   System.out.println("Dynamically adding edge failed: Edge is not legit");
			   return false;
		   }
		
		int hashedV = (int) g.hashFunction(this.base, v);
		int hashedU = (int) g.hashFunction(this.base, u);
		
		//check whether the edge already exists
		if(OUT[hashedV][this.agct.indexOf(u.charAt(u.length() - 1))] == 1) {
			System.out.println("Dynamically adding edge failed: Edge already exists");
			String s = f.traverseUp("GAGTG", this.g, this.base, 1);
			System.out.println(s);
			return false;	
		}
		
		
		//update IN and OUT matrices
		OUT[hashedV][this.agct.indexOf(u.charAt(u.length() - 1))] = 1;
		IN[hashedU][this.agct.indexOf(v.charAt(0))] = 1;
		
		String rootV = f.getRoot(v, g, this.base);
		int rootHashV = (int) g.hashFunction(this.base, rootV);
		int heightV = f.getTreeHeight(rootHashV);
		
		String rootU = f.getRoot(u, g, this.base);	
		int rootHashU = (int) g.hashFunction(this.base, rootU);
		int heightU = f.getTreeHeight(rootHashU);
		//System.out.println("RootV: " + rootV + " HeightV: " + heightV);
		//System.out.println("RootU: " + rootU + " HeightU: " + heightU);
			
		if(rootV.equals(rootU)) {
			System.out.println("Same tree, no need to merge");
			return true;
		}
		
		
		return true;
		
	}
	public boolean deleteEdge(String v, String u) {
		
		//check membership
		if(!this.isMember(v) || !this.isMember(u)) {
			System.out.println("Dynamically deleting edge failed: Node not exists");
			return false;
		}
		
		//check whether the edge can be added legitimately
		if(!v.substring(1).equals(u.substring(0, u.length() - 1)) &&
		   !v.substring(0, v.length() - 1).equals(v.substring(1))) {
			System.out.println("Dynamically adding edge failed: Edge is not legit");
			return false;
		}
		
		String rootV = f.getRoot(v, g, this.base);
		int hashedV = (int) g.hashFunction(this.base, v);
		
		String rootU = f.getRoot(u, g, this.base);	
		int hashedU = (int) g.hashFunction(this.base, u);
		
		//check in forest for this edge
		if(f.getParent(v, hashedV)!= null && f.getParent(v, hashedV).equals(u)) {
			f.addToRoot(v);
			f.setParent(hashedV, '\0', false);
			//v -> u
			if(v.substring(1).equals(u.substring(0, u.length() - 1))) {
				OUT[hashedV][this.agct.indexOf(u.charAt(u.length() - 1))] = 0;
				IN[hashedU][this.agct.indexOf(v.charAt(0))] = 0;
			}
			//u -> v
			if(u.substring(1).equals(v.substring(0, u.length() - 1))) {
				OUT[hashedU][this.agct.indexOf(v.charAt(v.length() - 1))] = 0;
				IN[hashedV][this.agct.indexOf(u.charAt(0))] = 0;
			}
		}else if(f.getParent(u, hashedU)!= null && f.getParent(u, hashedU).equals(v)) {
			f.addToRoot(u);
			f.setParent(hashedU, '\0', false);
			//v -> u
			if(v.substring(1).equals(u.substring(0, u.length() - 1))) {
				OUT[hashedV][this.agct.indexOf(u.charAt(u.length() - 1))] = 0;
				IN[hashedU][this.agct.indexOf(v.charAt(0))] = 0;
			}
			//u -> v
			if(u.substring(1).equals(v.substring(0, u.length() - 1))) {
				OUT[hashedU][this.agct.indexOf(v.charAt(v.length() - 1))] = 0;
				IN[hashedV][this.agct.indexOf(u.charAt(0))] = 0;
			}
		}else{
			//this edge is not in the forest, do nothing about forest
			//v -> u
			if(v.substring(1).equals(u.substring(0, u.length() - 1))) {
				OUT[hashedV][this.agct.indexOf(u.charAt(u.length() - 1))] = 0;
				IN[hashedU][this.agct.indexOf(v.charAt(0))] = 0;
			}
			//u -> v
			if(u.substring(1).equals(v.substring(0, u.length() - 1))) {
				OUT[hashedU][this.agct.indexOf(v.charAt(v.length() - 1))] = 0;
				IN[hashedV][this.agct.indexOf(u.charAt(0))] = 0;
			}
			
		}
		return true;
	}
	
}













