JFLAGS = -g

LIBS = .:./bin/:./lib/pdfbox-app-1.8.5.jar:./lib/commons-io-2.4.jar:./lib/mysql-connector-java-5.1.18-bin.jar:./lib/commons-lang3-3.3.2.jar
JC = javac -cp $(LIBS)

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

extract:
	java -cp $(LIBS) AlgorithmExtractor
	

clean:
	$(RM) -r *.class algo_count.txt \
	temp/Captions/* \
	temp/ReferenceSentences/* \
	temp/text_files/* \
	temp/TextFiles/* \
	temp/Features/* \
	temp/CaptionMaps/* \
	temp/Synopses/*
