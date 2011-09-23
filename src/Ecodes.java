package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Ecodes {
	final static int pieces = 4;
	static String path = "src/scripts/output/o";

	public static void main(String[] args) {

		byte fileContent[][] = null;
		for (int i = 0 ; i < pieces ; i++)
		{
			File file = new File(path+i);
			try
			{
				//create FileInputStream object
				FileInputStream fin = new FileInputStream(file);
				if(i==0)
				{
					fileContent = new byte[pieces][(int)file.length()];
				}
				//				byte fileContent[i][] = new byte[][(int)file.length()];
				fin.read(fileContent[i]);
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
		for (int j = 0 ; j < pieces ; j++)
		{
			//create string from byte array
			String strFileContent = new String(fileContent[j]);

			System.out.println("File content : ");
			System.out.println(strFileContent);

		}
	}
}
