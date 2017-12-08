# main program to run
main = dynamicDBG

# name of the package (Note that we need the "/")



# default command line arguments: [input fastq file, k value]
args="data/test.fastq" 51

jc javac c compile: 
	test -d bin || mkdir bin
	javac -d bin src/debruijnGraph/node.java
	javac -d bin src/debruijnGraph/minimalPerfectHash.java 
	javac -d bin -cp src src/debruijnGraph/generateHash.java 
	javac -d bin -cp src src/debruijnGraph/forest.java 
	javac -d bin src/test/fastqReader.java 
	javac -d bin -cp src src/debruijnGraph/deBruijnGraph.java 
	javac -d bin -cp src src/test/dynamicDBG.java 

j java r run: 
	java -classpath bin test/${main} ${args}

cr: compile run

clean:
	-rm -f bin/debruijnGraph/*.class *~
	-rm -f bin/test/*.class *~
	-rm -f bin/reference/*.class *~
		




