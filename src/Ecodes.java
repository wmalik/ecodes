

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class Ecodes {
	final static int pieces = 4;
	static String path = "script/output/test";
	static int file_length;

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
					file_length = (int)file.length();
					fileContent = new byte[pieces][file_length];
				}
				//				byte fileContent[i][] = new byte[][(int)file.length()];
				fin.read(fileContent[i]);
				fin.close();
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
		////// initialization of the nodes
		File config_file = new File("config");
		try {
			String thisLine;  
			BufferedReader br = new BufferedReader(new FileReader(config_file));
			int node = 0;
			while ((thisLine = br.readLine()) != null) 
			{ // while loop begins here
				String[] strarray=thisLine.split(","); 
				//skip the first line so i = 1
				for (int i = 1; i < strarray.length; i++) 
				{
					System.out.println(strarray[i]); 
					char[] theCharArray = strarray[i].toCharArray();

					byte result[] = new byte[file_length];
					boolean result_initial = true;
					for(int j = 0; j < theCharArray.length ; j++)
					{
						if(node==0)
							System.out.println("length = "+theCharArray.length);
						if(theCharArray[j]=='1')
						{
							if(result_initial==true)
							{
								for(int k = 0 ; k < file_length ; k++)
								{
									result[k] = fileContent[j][k];
									result_initial=false;
								}
							}
							else
							{
								for(int k = 0 ; k < file_length ; k++)
								{


									result[k] =(byte) (result[k] ^ fileContent[j][k]);
					//				result[k] =(byte) (( result[k]) ^ fileContent[j][k]);
									if(node==0)
										System.out.println("result: "+result[k]);
								} 
							}
						}
					}
					File file = new File("nodes/node"+node+"/");
					file.mkdir();

					FileOutputStream fos = new FileOutputStream("nodes/node"+node+"/"+"object"+i);
					fos.write(result);
					fos.close();
				}
				node++;
			} 
			br.close();
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
