import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.ResultSet;


import java.io.FileInputStream;
import java.security.MessageDigest;

/* Sample generated XML content:

<doc>
	<algorithm id="10.1.1.1.temp_Algo_1">
		<caption>Algorithm 1 Residual resampling 1: Let ฯ�ฬ� jt = ฯ�</caption>
		<reftext>However, they offer flexibility and, in the case of residual resampling, may be computationally beneficial. 5.1 Residual resampling Residual sampling is a mostly-deterministic approach that enforces the number of copies of a particle retained during resampling to be (approximately) propor- tional to the weight of the sample. (Note that this is the expected result of random resampling.) The technique is shown in Algorithm 1. j t/โ�‘ N i=1 ฯ� i t 2: Retain k j = bNฯ�ฬ� j tc copies of ฯ� j t 3: Let Nr = N โ�’ k1 โ�’ . . .โ�’ kN 4: Obtain Nr i.i.d. draws (with replacement) from {ฯ�1t , . . . , ฯ� N t } w.p. proportional to Nฯ�ฬ� jt โ�’ k j 5: โ�€j, ฯ� jt = 1/N Since the number of deterministically selected copies of all particles, โ�‘ N j=1 k j, may be less than N, random resampling is performed according to the residu- als Nฯ�ฬ� jt โ�’ k j in Step 4 to prevent bias.</reftext>
		<synopsis>To our knowledge... i = 1 . . .</synopsis>
		<paperid>10.1.1.1.temp</paperid>
		<pagenum>12</pagenum>
		<year></year>
		<ncites></ncites>
		<checksum>7af4c2fc1a7de21d156f9b3d520b0820f771dd56</checksum>
	</algorithm>

	<algorithm id="10.1.1.1.temp_Algo_2">
		<caption>Algorithm 2 Generalized resampling 1: for j = 1 . . . N do</caption>
		<reftext>The intuition behind this approach, which is depicted in Algorithm 2, is that {ait} can be used to the weights of particles, balancing focus (giving more presence to particles with high weights) with particle diversity. 2: Draw k from {1, . . . , N} according to ait, i = 1 . . .</reftext>
		<synopsis>However, they offer flexibility and, ... described in Section 5.</synopsis>
		<paperid>10.1.1.1.temp</paperid>
		<pagenum>12</pagenum>
		<year></year>
		<ncites></ncites>
		<checksum>7af4c2fc1a7de21d156f9b3d520b0820f771dd56</checksum>
	</algorithm>

</doc>
*/


public class AlgorithmXMLGenerator
{	
	public 	AlgorithmXMLGenerator()
	{	
	}
	
	public static String getChecksum(String inputFilename, String hashAlgorithm)
	{
		String datafile = inputFilename;
		if(hashAlgorithm == null) hashAlgorithm = "SHA1"; 
		String result = "";
		try{
		    MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
		    FileInputStream fis = new FileInputStream(datafile);
		    byte[] dataBytes = new byte[1024];
		 
		    int nread = 0; 
		 
		    while ((nread = fis.read(dataBytes)) != -1) {
		      md.update(dataBytes, 0, nread);
		    };
		 
		    byte[] mdbytes = md.digest();
		 
		    //convert the byte to hex format
		    StringBuffer sb = new StringBuffer("");
		    for (int i = 0; i < mdbytes.length; i++) {
		    	sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		    }
		 
		    result = sb.toString();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static void genXMLToFile(String inputPDFFilename, String capmapFilename, String synDir, String outputFilename)
	{
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename));
			writer.write(getXML(inputPDFFilename, capmapFilename, synDir));
			writer.close();

		}catch(Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println("Done. "+outputFilename+" has been generated.");
	}
	
	public static String getXML(String inputPDFFile, String capmapFilename, String synDir)
	{
		String result = "";
		String checksum = getChecksum(inputPDFFile, "SHA1");
		//suppwong: remove if no DB
		//CsxDBConnector csxdb = new CsxDBConnector();
		result += "<doc>\n";
		try{
			//BufferedWriter xmlwriter = new BufferedWriter(new FileWriter(xmlFilename)); 
			BufferedReader mapReader = new BufferedReader(new FileReader(capmapFilename));
			 String mapLine = "";
			 String paperid = "";
			 String page = "";
			 String id = "";
			 String caption = "";
			 String refsent = "";
			 String year = "";
			 String ncites = "";
			 String syn = "";
			 
			 boolean gettingInfo = false;
			 while((mapLine = mapReader.readLine()) != null)
			 {
				 mapLine = mapLine.trim();
				 if(mapLine.isEmpty())continue;
				 
				 if(mapLine.startsWith("<:algorithm:>"))
				 {	
					 
					 paperid = "";
					 page = "";
					 id = "";
					 caption = "";
					 refsent = "";
					 year = "";
					 ncites = "";
					 syn = "";
					 gettingInfo = true;
					 
				 }
				 else if(mapLine.startsWith("<:info:>"))
				 {
					 if(gettingInfo)
					 {
						 mapLine = mapLine.replace("<:info:>", "");
						 String[] tokens = mapLine.split("<###>");
						 assert(tokens.length == 4);
						 paperid = tokens[0].trim();
						 page = tokens[1].trim();
						 id = tokens[2].trim();
						 caption = tokens[3].trim();
						 
					 }
				 }
				 else if(mapLine.startsWith("<:/algorithm:>"))
				 {	
					 
					 //get synopsys
					 String synfile = synDir+"/"+id+".txt";
					 
					 try{
						 BufferedReader synreader = new BufferedReader(new FileReader(synfile));
						 String synline = "";
						 while((synline = synreader.readLine()) != null)
						 {
							 synline = synline.trim();
							 if(synline.isEmpty()) continue;
							 syn += synline+" ";
						 }
						 synreader.close();
					 }catch(FileNotFoundException fnf)
					 {	//no synopses for this algorithm
						 //fe.printStackTrace();
						 //Util.log(fe.toString()+" "+synfile+"\n");
					 }
					 catch(Exception fe)
					 {
						 fe.printStackTrace();
						 System.exit(-1);
						 //Util.errlog(fe.toString());
					 }
					 
					 //get year and ncited
					 String query = "SELECT year, ncites FROM papers WHERE id = '"+paperid+"' LIMIT 1;";
					 //comment out for local testing
					 
					 //suppwong: remove if no DB
					/* ResultSet r = csxdb.executeQuery(query);
					 while(r.next())
					 {
						 year = r.getString("year");
						 ncites = r.getString("ncites");
					 }
					 */
					 
					 result += "\t<algorithm id=\""+id+"\">\n";
					 result += "\t\t<caption>"+caption+"</caption>\n";
					 result += "\t\t<reftext>"+refsent.trim()+"</reftext>\n";
					 result += "\t\t<synopsis>"+syn.trim()+"</synopsis>\n";
					 result += "\t\t<paperid>"+paperid+"</paperid>\n";
					 result += "\t\t<pagenum>"+page+"</pagenum>\n";
					 result += "\t\t<year>"+year+"</year>\n";
					 result += "\t\t<ncites>"+ncites+"</ncites>\n";
					 result += "\t\t<checksum>"+checksum+"</checksum>\n";
					 result += "\t</algorithm>\n\n";
					 
					 gettingInfo = false;
					 
				 }
				 else //ref sentences
				 {	if(gettingInfo)
				 	{
					 refsent += mapLine +" ";
				 	}
				 }
			 }
			 
			
			 mapReader.close();
		}catch(Exception e)
		{	e.printStackTrace();
			
			System.exit(-1);
		}
		result += "</doc>\n";
		return result;
	}

	public void genXML(String docID, String outfile)
	//generate xml for particular docID
	{	
	}
	
	
	public static void main(String[] args)
	{
	}

}
