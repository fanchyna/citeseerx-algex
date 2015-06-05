import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.lang3.SystemUtils;


public class CommandExecutor {
	public static void exec(String cmd, boolean verbose)
	{
		//String cmd = "ls -al";
		
		if(SystemUtils.IS_OS_WINDOWS)
		{
			cmd = cmd.replace('/', '\\');
		}
		
		Runtime run = Runtime.getRuntime();
		try{
			Process pr = run.exec(cmd);
			pr.waitFor();
			
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			BufferedReader errbuf = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
			String line = "";
			while ((line=buf.readLine())!=null) {
				if(verbose) System.out.println(line);
			}
			while ((line=errbuf.readLine())!=null) {
				if(verbose) System.out.println(line);
			}
			
			buf.close();
			errbuf.close();
			
		}catch(Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
}
