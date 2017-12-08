package debruijnGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class forest {
	public List<String> roots;
	public char[] parent;
	public boolean[] firstlast;
	public int[] treeHeight;
	forest(int num) {
		this.roots = new ArrayList<String>();
		this.parent = new char[num];
		this.firstlast = new boolean[num];//true:first false:last
		this.treeHeight = new int[num];
	}
	public void addToRoot(String kmer) {
		roots.add(kmer);
	}
	public void removeFromRoot(String kmer) {
		roots.remove(kmer);
	}
	public boolean isRoot(String kmer){
		return roots.contains(kmer);
	}
	public List<String> getRoots() {
		return this.roots;
	}
	public void setParent(int c, char p, boolean in) {
		parent[c] = p;
		firstlast[c] = in;
	}
	public char[] getParents() {
		return this.parent;
	}
	public String getParent(String kmer, int hashed) {
		if(this.parent[hashed] == '\0') return null;
		if(firstlast[hashed]) {
			return this.parent[hashed] + kmer.substring(0, kmer.length() - 1);
		}
		return kmer.substring(1) + this.parent[hashed];
	}
	public boolean[] getfirstlast() {
		return this.firstlast;
	}
	//return the height of the tree
	public int getHeight(String kmer, generateHash g, int base) {
		int height = 0;
		String current = kmer;
		int hashed = (int) g.hashFunction(base, current);
		while(this.parent[hashed]!= '\0') {
			height++;
			current = this.getParent(current, hashed);
			hashed = (int) g.hashFunction(base, current);
		}
		return height;
	}
	//return the root of the tree
	public String getRoot(String kmer, generateHash g, int base) {
		int hashed = (int) g.hashFunction(base, kmer);
		while(this.parent[hashed]!= '\0') {
			kmer = this.getParent(kmer, hashed);
			hashed = (int) g.hashFunction(base, kmer);
		}
		return kmer;
	}
	//hashed value of the root
	public int getTreeHeight(int i) {
		return this.treeHeight[i];
	}
	public void setTreeHeight(String[] kmers, generateHash g, int base) {
		for(int i = 0; i < kmers.length; i++) {
			String root = this.getRoot(kmers[i], g, base);
			int rootHash = (int) g.hashFunction(base, root);
			int height = this.getHeight(kmers[i], g, base);
			if(height > this.treeHeight[rootHash]) {
				this.treeHeight[rootHash] = height;
			}
		}
	}
	public boolean mergeTrees(String v, String u, generateHash g, int base, int alpha) {
		String newRoot = "";
		String rootV = this.getRoot(v, g, base);
		int rootHashV = (int) g.hashFunction(base, v);
		int heightV = this.getHeight(v, g, base);
		int treeHeightV = this.getTreeHeight(rootHashV);
		
		String rootU = this.getRoot(u, g, base);
		int rootHashU = (int) g.hashFunction(base, u);
		int heightU = this.getHeight(u, g, base);
		int treeHeightU = this.getTreeHeight(rootHashU);
		
		int newHeight = (treeHeightU + heightU + treeHeightV + heightV + 1) / 2;
		//new root in u
		if(newHeight <= treeHeightU + heightU) {
			int reverseHeight = treeHeightU + heightU - newHeight;
			if(reverseHeight > heightU) {
				//use old root
				reverseHeight = heightU;
			}
			if(reverseHeight + treeHeightV + heightV + 1 > 2 * alpha) {
				System.out.println("Resulting in one tree too big");
				return false;
			}
			newRoot = this.traverseUp(rootU, g, base, reverseHeight);
			//remove old root;
			//add new root;
			//reverse edge between old root and new root;
			//add edge between u and v
		}
		return true;
	}
	public String traverseUp(String u, generateHash g, int base, int reverseHeight) {
		String root = u;
		for(int i = 0; i < reverseHeight; i++) {
			int hashed = (int) g.hashFunction(base, root);
			root = this.getParent(root, hashed);
		}
		return root;
	}
}