package reference;

import java.util.HashSet;
import java.util.Set;

import reference.minimalPerfectHash.UniversalHash;
import reference.minimalPerfectHash.LongHash;

public class testMPH {
	public static void main(String[] args) {
		Set<Long> data = new HashSet<Long>();
        for (int i = 0; i < 100; i++) {
            data.add((long) (i * 2)); 
        }
        UniversalHash<Long> u = new LongHash();
        byte[] desc = minimalPerfectHash.generate(data, u);
        minimalPerfectHash mpg = new minimalPerfectHash(desc, u);
        for(int i = 0; i < 100; i++) {
        	System.out.println(2 * i + " -> " +mpg.get((long)(2 * i)));
        }
        
        System.out.println(190 + " -> " +mpg.get((long)(9990)));
    }
}