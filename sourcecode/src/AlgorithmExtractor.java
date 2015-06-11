import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 * Main class for extracting algorithms from a pdf file
 * @author Suppawong Tuarob
 *
 */


public class AlgorithmExtractor {
	
	public final String csxdbhost = "localhost";
	public final String csxdbname = "citeseerx";
	public final String csxdbusername = "csx-read";
	public final String csxdbpassword = "csx-read";
	public final String TEMPPATH = "./temp";
	public final String PERLDIR = "./perl";
	public final int MAX_PAPER_PAGES = 50;
	
	
	public void extractAlgorithmsFromPDF(String inputFilename, String docID, String outputFilename)
	{
		extractAlgorithmsFromPDF( inputFilename,  docID,  outputFilename, null);
	}
	/**
	 * 
	 * @param inputFilename : path to input PDF file
	 * @param outputFilename : path to output XML file
	 */
	public void extractAlgorithmsFromPDF(String inputFilename, String docID, String outputFilename, String _perlpath)
	{	System.out.println("Processing .. "+inputFilename);
		//ConfigReader config = new ConfigReader();
		String tempDirPath = TEMPPATH+"_"+docID;
		String perlPath = PERLDIR;
		if(_perlpath != null) perlPath = _perlpath;
		
		String cmd = "";
		
		//check file size (# of pages)
		PDDocument doc;
		try {
			doc = PDDocument.load(new File(inputFilename));
			int numPages = doc.getNumberOfPages();
			
			if(numPages > MAX_PAPER_PAGES)
			{
				System.out.println("@@@ Skipping "+inputFilename+" (too large).");
				return;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		//make sure temp dirs are cleared
		clearTempFiles(tempDirPath);
		
		//extract text file
		//text filename docID.txt
		String textFilename = tempDirPath+"/text_files/"+docID+".txt";
		try{	TextExtractor.pdfToTextWithPageNumbers(inputFilename, textFilename);
		}catch(Exception e)
		{	e.printStackTrace();
			return;
		}
		//get captions, ref. sentences, and sentencen file
		//sentence filename = sent_id.txt
		cmd = "perl "+perlPath+"/getcaprefsent.pl "+tempDirPath;
		System.out.println(cmd);
		CommandExecutor.exec(cmd, false);
		
		//get features and caption-map file
		//Note* A caption-map file map captions and their corresponding ref. sentences
		cmd = "perl "+perlPath+"/getfeatures.pl "+tempDirPath+" n "+perlPath;
		System.out.println(cmd);
		CommandExecutor.exec(cmd, false);
		
		//get synopses
		cmd = "perl "+perlPath+"/getSynopses.pl "+tempDirPath;
		System.out.println(cmd);
		CommandExecutor.exec(cmd, false);
		
		//get the rest of information and generate xml file
		AlgorithmXMLGenerator.genXMLToFile(inputFilename, tempDirPath+"/CaptionMaps/map.txt", tempDirPath+"/Synopses", outputFilename);
		
		
		//clean up the temp files
		//clearTempFiles(tempDirPath);
		try {
			FileUtils.deleteDirectory(new File(tempDirPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static private void clearTempFiles(String tempDirPath)
	{	
		
		String[] tempDirs = {	"Captions", 
								"ReferenceSentences", 
								"text_files",
								"TextFiles",
								"Features",
								"CaptionMaps",
								"Synopses"};
		
		for(String dir:tempDirs)
		{	String directoryName = tempDirPath+"/"+dir;
			File directory = new File(directoryName);
			// Get all files in directory
			if(directory.exists())
			{
				try {
					FileUtils.deleteDirectory(directory);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
				try {
					FileUtils.forceMkdir(directory);
				} catch (IOException e) {
	
					e.printStackTrace();
				}
			
			/*
			File[] files = directory.listFiles();
			for (File file : files)
			{
			   // Delete each file
				if(file.getName().startsWith(".")) continue; //skip svn and system files
				System.out.println("Deleting "+file.getName());
			   if (!file.delete())
			   {	//Failed to delete file
			       System.out.println("Failed to delete "+file);
			   }
			}
			*/
		}
		
	}
	
	/**
	 * If resumable is on, then original pdf is *deleted* after being extracted.
	 * @param inPdfDirname
	 * @param outDirname
	 * @param resumable
	 */
	public static void batchExtract(String inPdfDirname, String outDirname, boolean resumable)
	{
		List<File> pdfFiles = (List<File>) FileUtils.listFiles(new File(inPdfDirname), new String[]{"pdf"}, true);
		
		
		for(File pdfFile: pdfFiles)
		{	String filename = pdfFile.getName();
			String filePathname = pdfFile.getAbsolutePath();
			String fileID = FilenameUtils.removeExtension(filename).replace(' ', '_');
			String outFilename = outDirname+"/"+fileID+".xml";
			
			boolean needToExtract = true;
			if(resumable)	//no need to re-extract if already extracted
			{	System.out.println("@@@ Skipping"+outFilename);
				if((new File(outFilename)).exists()) needToExtract = false;
			}
			
			
			if(needToExtract)
			{
				AlgorithmExtractor ae = new AlgorithmExtractor();
				ae.extractAlgorithmsFromPDF(filePathname, fileID, outFilename);
			}
			
			if(resumable) FileUtils.deleteQuietly(pdfFile);
		}
	}
	
	public static void main(String[] args)
	{	
		//args[0] = perl script dir
		//args[1] = mode (f)ile or (d)irectory
		//args[2] = input file if mode f, directory if mode d
		//args[2] = output directory
		/*
		if(args.length < 2)
		{
			System.out.println("USAGE: input_pdf_filename output_xml_filename paper_id");
		}
		
		AlgorithmExtractor ae = new AlgorithmExtractor();
		
		
		String inputPDFFilename = args[0].trim();
		String outputXMLFilename = args[1].trim();
		String paperid = "";
		if(args.length >= 3) paperid = args[2];
		else
		{
			paperid = "10.1.1.1.temp";
		}
		
		ae.extractAlgorithmsFromPDF(inputPDFFilename, paperid, outputXMLFilename);
		*/
		
		//Example java -jar AlgorithmExtractor ./perl f ./samples/10.1.1.111.8323.pdf ./data/output
		//java -jar AlgorithmExtractor ./perl ./data/input ./data/output
		
		//Default configuration
		String perlPath = "./perl";
		String mode = "d";
		String input = "./data/input";
		String outputDir = "./data/output";
		
		
		if(args.length != 4)
		{
			System.out.println("Invalid Usage [perl_dir mode(f or d) input(file or directory) ouput): Using Default configuration.\n");
		}
		else
		{
			perlPath = args[0].trim();
			mode = args[1].trim();
			input = args[2].trim();
			outputDir = args[3].trim();
		}
		
		if(mode.equalsIgnoreCase("f"))
		{
			AlgorithmExtractor ae = new AlgorithmExtractor();
			
			
			String paperid = FilenameUtils.removeExtension(FilenameUtils.getName(input)).replace(' ', '_');
			String outputXMLFilename = outputDir+"/"+paperid+".xml";
			
			ae.extractAlgorithmsFromPDF(input, paperid, outputXMLFilename, perlPath);
		}
		else if(mode.equalsIgnoreCase("d"))
		{
			batchExtract(input, outputDir, false);
		}
		else
		{
			System.out.println("Error: Invalid Mode\n");
		}
		
		
	}
}

