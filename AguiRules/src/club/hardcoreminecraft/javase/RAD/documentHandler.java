package club.hardcoreminecraft.javase.RAD;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class documentHandler {

	//I love static! Sorry you cannot convince me otherwise
	public static void createNewRulesFile(File file) {
		
		//If the file exists, this function should never be called, but because we are using threads I want to stay safe.
		  if(!file.exists())
			try {
				file.createNewFile();
				
				BufferedWriter br = new BufferedWriter(new FileWriter(file));
				
				//This is what we are populating the default rules.txt with
				br.write("&1You should change this default rules file in rules.txt\n");
				br.write("&5[1] &2act only in accordance with that maxim through which you can at the same time "+ 
				"will that it become a universal law\n");
				br.write("&5[2] &3Act in such a way that you treat humanity, whether in your own person or in the person"+
				" of another, always at the same time as an end and never simply as a means.\n");
				br.write("&5[3] &4By default there is NO formatting. The rules.txt file will be displayed " + 
				"EXACTLY as you write it. So if you want 'type &9/rules 2&4' to see the next page, you need to include "+
						"that line in the file. This is a feature!\n");
				br.write("&5[4] &5When you are done, type &9/rules accept\n");
				//Make sure to release the lock on the resource when we are done to avoid a deadlock.
				br.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		  
		  
	}
	
}
