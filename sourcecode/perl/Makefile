JFLAGS = -g
JC = javac -cp .:./libs/pdfbox-app-1.5.0.jar
 
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS)  $*.java

CLASSES = \
	AlgorithmExtractor.java \
	AlgorithmXMLGenerator.java \
	CommandExecutor.java \
	ConfigReader.java \
	CsxDBConnector.java \
	TextExtractor.java
	 
	
	         
default: classes

classes: $(CLASSES:.java=.class)

testextract:
	java -cp .:./libs/pdfbox-app-1.5.0.jar:./libs/mysql-connector-java-5.1.15-bin.jar AlgorithmExtractor samples/10.1.1.111.8323.pdf samples/10.1.1.111.8323.xml 10.1.1.111.8323 

pdftotextwithpagenumbers:
	java -cp .:./libs/pdfbox-app-1.5.0.jar TextExtractor samples/10.1.1.111.8323.pdf samples/10.1.1.111.8323.txt

clean:
	$(RM) -r *.class algo_count.txt \
	temp/Captions/* \
	temp/ReferenceSentences/* \
	temp/text_files/* \
	temp/TextFiles/* \
	temp/Features/* \
	temp/CaptionMaps/* \
	temp/Synopses/*
