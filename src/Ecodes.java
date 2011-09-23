package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Ecodes {
	void init()
	{
		File file = new File("C://FileIO//ReadString.txt");
		 
	    try
	    {
	      //create FileInputStream object
	      FileInputStream fin = new FileInputStream(file);
	 
	      /*
	       * Create byte array large enough to hold the content of the file.
	       * Use File.length to determine size of the file in bytes.
	       */
	 
	 
	       byte fileContent[] = new byte[(int)file.length()];
	 
	       /*
	        * To read content of the file in byte array, use
	        * int read(byte[] byteArray) method of java FileInputStream class.
	        *
	        */
	       fin.read(fileContent);
	 
	       //create string from byte array
	       String strFileContent = new String(fileContent);
	 
	       System.out.println("File content : ");
	       System.out.println(strFileContent);
	 
	    }
	    catch(FileNotFoundException e)
	    {
	      System.out.println("File not found" + e);
	    }
	    catch(IOException ioe)
	    {
	      System.out.println("Exception while reading the file " + ioe); 
	    }
		
	}
}
