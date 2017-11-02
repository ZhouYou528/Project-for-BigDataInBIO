import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class generateHash {
	
	private int length;	// number of tuples
	private int k; 		// k-mer
	private int R;		
	private int P;		// prime
	private int base;	//base
	private Map<String, Long> hashed; 	//Rabin Karp Hash Table
	private Map<Long,Long> MFH; 		//Minimal Perfect Hash Table
	//private Map<Character, Integer> agct;
    public generateHash(String[] inputString) {
    	this.length = inputString.length;
    	this.k = inputString[0].length();
    	this.R = Math.max(4, k * length * length);
    	this.P = firstLargerPrime(R);
    	boolean isInjective = false;
    	
    	
    	while(!isInjective) {
    		
    		boolean hasDup = false;
    		this.hashed = new HashMap<String, Long>();
        	Set<Long> duplicate = new HashSet<Long>(); 
        	
    		int random = (int)(Math.random() * (P - 1));
        	this.base = random;
        	for(int i = 0; i < length; i++) {
        		if(hasDup) break;
        		long value = hash(inputString[i]);
        		if(duplicate.contains(value)) {
        			hasDup = true;
        			continue;
        		} else {
        			duplicate.add(value);
        			hashed.put(inputString[i], value);
        			if (i == length - 1) isInjective = true;
        		}
        	}
    	}
    	generateMFH();
    }
    //check whether a number is a prime
    private boolean isPrime (int n) {
    	//check if n is a multiple of 2
    	if (n % 2 == 0) return false;
    	//if not, then just check the odds
    	for(int i = 3; i * i <= n; i += 2) {
    	    if(n % i == 0) return false;
    	}
    	return true;
    }
    //get the smallest prime greater than num
    private int firstLargerPrime(int num) {
    	int i = num + 1;
    	while(!isPrime(i)) {
    		i += 1;
    	}
    	return i;
    }
    //Rabin Karp hash function
    private long hash(String key) { 
    	int m = key.length();
    	long h = 0; 
        for (int j = 0; j < m; j++) 
            h = (base * h + key.charAt(j)) % P;
        return h;
    }
    //isInjective()
//    private boolean isInjective() {
//    	
//    }
    //generate Minimum Perfect Hash Table
    public void generateMFH() {
    	MFH = new HashMap<Long, Long>();
    	long count = 0;
    	for(String i : hashed.keySet()) {
    		long key = hashed.get(i);
    		MFH.put(key, count++);
    	}
    }
    public void showMap() {
    	String temp = "";
    	for(String i : hashed.keySet()) {
    		long h = hashed.get(i);
    		temp = i + " -> " + h + " -> " + MFH.get(h);
    		System.out.println(temp);
    	}
    }
}