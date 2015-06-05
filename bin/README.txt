Installation:

1. Copy "perl" to harddrive. [PERL_PATH] refers to the path of this directory (i.e. "./perl")

2. Make sure Lingua::Stem (a Perl module) is installed. If not, run 
		cpan Lingua::Stem
		
		
Run:
There are two modes: file and batch. The file mode extracts one file at a time. The batch modes takes a directory as input, and extract algorithms in all the pdf files.

1. File mode
	 java -jar algo_extractor.jar [PERL_PATH] f [INPUT_FILE] [OUTPUT_DIRECTORY]
	 Ex.  java -jar algo_extractor.jar ./perl f 10.1.1.111.8323.pdf .
	 
2. Batch mode:
	java -jar algo_extractor.jar [PERL_PATH] d [INPUT_DIR] [OUTPUT_DIRECTORY]
	 Ex.  java -jar algo_extractor.jar ./perl d ./sample ./output