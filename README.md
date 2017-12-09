# Project-for-BigDataInBIO

Project name: Fully dynamic de Bruijn Graph Implementation


- [User Instruction](#user-instruction)
- [File Structure](#file-structure)
- [Project Coding Timeline](#project-coding-timeline)



## User Instruction

For mac or linux: Download the code, enter the root directory of the code in the terminal.

Type into the terminal: 
```bash
make cr
```

The code will automatically compile and run. The result will be displayed in the terminal.

If you want to clean up, type:

```bash
make clean
```

Defult k value is 51. If you want to change the k value, open the makefile with any text editor. 
You can change the value accordingly. Recommand k value is from 31 to 51.

```
# default command line arguments: [input fastq file, k value]
args="data/test.fastq" 51
```
Current code hasn't implemented the succinct representation of k-mers. So it can not handle the large 
fastq files.

## File Structure

### bin

*.class files.

### data

Test input data: Including fastq file of Illumina sequence reads for part of E.coli.

### src

```
/ src
  / debruijnGraph                      # data structures: forest, DBG; hash functions: karp-rabin, minimum perfect hash
  / reference                          # open source libraries that I use: karp-rabin, minimum perfect hash
  / test                               # main: entrance of the program
```

## Project Coding Timeline:

1. Build de Bruijn graph from input string (finished)

2. Build de Bruijn graph from kmers (finished)

3. Rabin Karp Hash (finished)

4. Minimal Perfect Hash (finished)

5. Forest construction (finished)

6. Membership query (finished)

7. Adding/Deleting edges (finished)

8. Adding/Deleting vertices (can be achieved by rehashing every time)
