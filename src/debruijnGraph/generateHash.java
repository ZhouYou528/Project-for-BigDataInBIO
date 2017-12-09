package debruijnGraph;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import reference.minimalPerfectHash;
import reference.minimalPerfectHash.LongHash;
import reference.minimalPerfectHash.UniversalHash;

import java.util.HashSet;

public class generateHash {
	
	private int length;	// number of tuples
	private int k; 		// k-mer
	private int R;		
	private int P;		// prime
	private int base;	//base
	private String[] inputString;
	private Map<String, Long> hashed; 	//Rabin Karp Hash Table
	private Set<Long> krHash; //Rabin Karp Hash value
	private UniversalHash<Long> u; //used for minimum perfect hash
	private byte[] desc; //used for minimum perfect hash
	private minimalPerfectHash<Long> mph; //used for minimum perfect hash

    public generateHash(String[] inputString) {
    	this.length = inputString.length;
    	this.k = inputString[0].length();
    	this.R = Math.max(4, k * length * length);
    	this.P = firstLargerPrime(R);
    	this.inputString = inputString;
    	this.u = new LongHash();
    	
    }
    //generate hash
    public int generate() {	
    	boolean isInjective = false;
    	
    	while(!isInjective) {
    	
    		boolean hasDup = false;
    		this.hashed = new HashMap<String, Long>();
    		this.krHash = new HashSet<Long>();
        	Set<Long> duplicate = new HashSet<Long>(); 
        	
    		int random = (int)(Math.random() * (P - 1));
        	this.base = random;
        	for(int i = 0; i < length; i++) {
        		if(hasDup) break;
        		long value = hash(this.base, inputString[i]);
        		if(duplicate.contains(value)) {
        			hasDup = true;
        			continue;
        		} else {
        			duplicate.add(value);
        			hashed.put(inputString[i], value);
        			krHash.add(value);
        			if (i == length - 1) isInjective = true;
        		}
        	}
    	}
    	generateMFH();
    	return this.base;
    }
    
    //generate hash with base information
    public void generateWithBase(int base) {
    	this.base = base;
    	this.hashed = new HashMap<String, Long>();
    	for(int i = 0; i < length; i++) {
    		long value = hash(this.base, inputString[i]);
    		hashed.put(inputString[i], value);	
    	}
    	generateMFH();
    }
    
    //check whether a number is a prime
    private boolean isPrime (long n) {
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
    private long hash(long base, String key) { 
    	int m = key.length();
    	long h = 0; 
        for (int j = 0; j < m; j++) 
            h = (base * h + key.charAt(j)) % P;
        return h;
    }

    //generate Minimum Perfect Hash 
    public void generateMFH() {
    	this.desc = minimalPerfectHash.generate(krHash, this.u);
    	this.mph = new minimalPerfectHash<Long>(desc, u);
    }
    
    //get the hashed value of a string with fixed base
    public long hashFunction(long base, String key) {
    	long hashed = this.hash(base, key);
    	return this.mph.get(hashed);
    }
    
    public void showMap() {
    	String temp = "";
    	for(String i : hashed.keySet()) {
    		long h = hashed.get(i);
    		temp = i + " -> " + h + " -> " + this.mph.get(h);
    		System.out.println(temp);
    	}
    }
    public long getPrime() {
    	return this.P;
    }
}