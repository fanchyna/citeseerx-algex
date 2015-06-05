import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;



//Main purpose is to extract text from a PDF file
//Current version uses PDFBox
public class TextExtractor {
	
	
	/**
	 * Extracts text from the pdf file and include page numbers
	 * @param fileName: path to input pdf filename
	 * @param outfilepath: path to out put text file
	 */
	static void pdfToTextWithPageNumbers(String fileName, String outfilepath) throws IOException
	{
		//using PDFTextStripper
		Writer output = null;
        PDDocument document = null;
        PDFTextStripper stripper = null;
        
        try{
        	document = PDDocument.load(fileName, true);
        }catch(IOException e)
        {	throw e;
        	//e.printStackTrace();
        	//System.exit(-1);
        }
        
        try{
	        stripper = new PDFTextStripper("UTF-8");
	        stripper.setForceParsing( true );
			//get number of pages 
	        int numPages = document.getNumberOfPages();
	        
	        //parse page by page
	       // BufferedWriter writer = new BufferedWriter(new FileWriter(outfilepath));
	        output = new OutputStreamWriter(new FileOutputStream( outfilepath ) );
	        for(int i = 1; i <= numPages; i++)
	        {
	        	stripper.setStartPage( i );
	            stripper.setEndPage( i );
	            stripper.writeText(document, output);
	            //writer.write(output.toString());
	            output.write("\n<PAGE>"+i+"\n");
	            
	            try{
	            	output.flush();
	            }catch(Exception e)
	            {
	            	
	            }
	        }
	        
	        //writer.close();
        }catch(IOException e)
        {	throw e;
        	//e.printStackTrace();
        }finally
        {
        	try{
		        if( output != null )
		        {
		            output.close();
		        }
		        if( document != null )
		        {
		            document.close();
		        }
        	}catch(Exception fe)
        	{
        		
        	}
        }
	}
	
	static boolean PDFBox_pdfToText(String fileName, String outfilepath) {
		PDFParser parser;
		String parsedText = null;;
		PDFTextStripper pdfStripper = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		File file = new File(fileName);
		if (!file.isFile()) {
			System.err.println("File " + fileName + " does not exist.");
			return false;
		}
		try {
			parser = new PDFParser(new FileInputStream(file));
		} catch (IOException e) {
			System.err.println("Unable to open PDF Parser. " + e.getMessage());
			return false;
		}
		try {
			parser.parse();
			cosDoc = parser.getDocument();
			pdfStripper = new PDFTextStripper();
			pdDoc = new PDDocument(cosDoc);
			pdfStripper.setStartPage(1);
			pdfStripper.setEndPage(pdfStripper.getEndPage());
			
			parsedText = pdfStripper.getText(pdDoc);
		} catch (Exception e) {
			System.err
					.println("An exception occured in parsing the PDF Document."
							+ e.getMessage());
		} finally {
			try {
				if (cosDoc != null)
					cosDoc.close();
				if (pdDoc != null)
					pdDoc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//writing out output file
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(outfilepath));
			out.write(parsedText);
			
			out.close();
		}catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void main(String args[]){
		try {
			TextExtractor.pdfToTextWithPageNumbers("10.1.1.1.5415.pdf", "10.1.1.1.5415.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
}


