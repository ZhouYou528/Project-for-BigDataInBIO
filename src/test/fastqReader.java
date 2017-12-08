package test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class fastqReader {
	private String file;
	private List<String> kmer;
	public fastqReader(String file) {
	     this.file = file;
	     this.kmer = new ArrayList<String>();
	}
	public String[] generateKmer(int k) throws Exception {
		final int DEFAULT_BUFFER_SIZE=5096;

		BufferedReader r;
		
		if(file.endsWith(".gz")) {
			r = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file),DEFAULT_BUFFER_SIZE)),DEFAULT_BUFFER_SIZE);
		} else {
			r = new BufferedReader(new FileReader(file),DEFAULT_BUFFER_SIZE);
		}
		
		String line;
		long count = 0;
		while((line=r.readLine())!=null) {
			count++;
			if(count % 4 == 2) {
				//System.out.println(line);
				StringBuilder s = new StringBuilder();
				for(int j = 0; j < line.length(); j++) {
					if(line.charAt(j) != 'N') s.append(line.charAt(j));
					else {
						if(j % 4 == 0) s.append('A');
						if(j % 4 == 1) s.append('G');
						if(j % 4 == 2) s.append('C');
						if(j % 4 == 3) s.append('T');
					}
				}
				for(int i = 0; i < line.length() - k; i++){
					
					kmer.add(s.toString().substring(0 + i, k + 1 + i));
				}
			}
			
        }
    r.close();
    return kmer.toArray(new String[0]);
    }
}