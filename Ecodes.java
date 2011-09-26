
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class Ecodes {
	final static int pieces = 4;
	static String path = "scripts/output/o";
	static int file_length;
	static int last_piece;
	static int numberOfNodes = 5;

	static boolean ByteArrayCompare(byte[] a1, byte[] a2) 
	{
		if(a1.length!=a2.length)
			return false;

		for(int i=0; i<a1.length; i++)
			if(a1[i]!=a2[i])
				return false;

		return true;
	}

	static void print(byte[] a)
	{
		String strFileContent = new String(a);

		//	System.out.println(" the content : ");
		System.out.println(strFileContent);

	}
	static byte[] XOR(byte[] a1, byte[] a2)
	{
		byte[] result = new byte[a1.length];
		for(int i = 0 ; i < a1.length ; i++)
		{
			result[i] = (byte) (a1[i]^a2[i]);
		}
		return result;
	}

	static byte[] XORVector(byte[] a1, byte[] a2)
	{
		byte[] result = new byte[a1.length];
		for(int i = 0 ; i < a1.length ; i++)
		{
			result[i] = (byte) (a1[i]^a2[i]);
			if(result[i]==1)
			{
				result[i]=49;
			}
			else if(result[i]==0)
			{
				result[i]=48;
			}
		}
		return result;
	}

	static void fixNode(int numberOfNode, int firstNode, int secondNode, int thirdNode, int failNode)
	{
		if(numberOfNode == 2)
		{
			byte[][] theByteArray = new byte[numberOfNode*2][4];
			byte[][] theContentArray = new byte[numberOfNode*2][file_length];
			byte[][] theResultArray = new byte[2][file_length];
			byte[][] theFailArray = new byte[2][4];
			File config_file = new File("config");
			try {
				String thisLine;  
				BufferedReader br = new BufferedReader(new FileReader(config_file));
				int node = 0;
				int index = 0;
				int failIndex = 0;
				while ((thisLine = br.readLine()) != null) 
				{ // while loop begins here
					if(node==firstNode||node==secondNode||node==thirdNode)
					{
						String[] strarray=thisLine.split(","); 
						//skip the first line so i = 1
						for (int i = 1; i < strarray.length; i++) 
						{
							//			System.out.println(strarray[i]); 
							String tmp = new String(strarray[i].toCharArray());
							theByteArray[index] = tmp.getBytes();
							index++;
						}
					}
					if(node==failNode)
					{
						String[] strarray=thisLine.split(","); 
						//skip the first line so i = 1
						for (int i = 1; i < strarray.length; i++) 
						{
							//			System.out.println(strarray[i]); 
							String tmp = new String(strarray[i].toCharArray());
							theFailArray[failIndex] = tmp.getBytes();
							failIndex++;
						}
					}
					node++;
				} 
				br.close();
				int array_index = 0;
				for(int i = 0 ; i < numberOfNodes ; i++)
				{
					if(i==firstNode||i==secondNode||i==thirdNode)
					{
						for(int j = 0 ; j < 2 ; j++)
						{
							File file = new File("nodes/node"+i+"/"+"object"+j);
							FileInputStream fis = new FileInputStream(file);
							fis.read(theContentArray[array_index*2+j]);
							fis.close();
						}
						array_index++;
					}
				}
				/*
				String strFileContent = new String(theContentArray[1]);

				System.out.println(" content : ");
				System.out.println(strFileContent);
				 */



				byte standardByteArray[][] = {{49,48,48,48},{48,49,48,48},{48,48,49,48},{48,48,48,49}};

				//		System.out.println(ByteArrayCompare(XORVector(theByteArray[1],XORVector(theByteArray[1],theByteArray[2])),standardByteArray[1]));



				//		print(XOR(theByteArray[1],theByteArray[2]));




				byte[] tmp = new byte[4];
				for(int i = 0 ; i < numberOfNode*2; i++)
				{
					tmp = theByteArray[i];
					for(int k = 0 ; k < 2 ; k++)
					{
						//	System.out.println(ByteArrayCompare(tmp,standardByteArray[k]));
						if(ByteArrayCompare(tmp,theFailArray[k]))
						{
							//			System.out.println("in here");
							theResultArray[k]=theContentArray[i];
						}
					}
					for(int j = i+1 ; j < numberOfNode*2 ; j++)
					{
						tmp = XORVector(theByteArray[i],theByteArray[j]);
						for(int k = 0 ; k < 2 ; k++)
						{
							if(ByteArrayCompare(tmp,theFailArray[k]))
							{
								theResultArray[k]=XOR(theContentArray[i],theContentArray[j]);
							}
						}
						for(int p = j+1 ; p < numberOfNode*2; p++)
						{
							tmp = XORVector(theByteArray[p],XORVector(theByteArray[i],theByteArray[j]));
							for(int k = 0 ; k < 2 ; k++)
							{
								if(ByteArrayCompare(tmp,theFailArray[k]))
								{
									theResultArray[k]=XOR(theContentArray[p],XOR(theContentArray[i],theContentArray[j]));
								}
							}
							for(int q = p+1 ; q < numberOfNode*2; q++)
							{
								tmp = XORVector(theByteArray[q],XORVector(theByteArray[p],XORVector(theByteArray[i],theByteArray[j])));								
								for(int k = 0 ; k < 2 ; k++)
								{
									if(ByteArrayCompare(tmp,theFailArray[k]))
									{
										theResultArray[k]=XOR(theContentArray[q],XOR(theContentArray[p],XOR(theContentArray[i],theContentArray[j])));
									}
								}

							}
						}
					}
				}
				for(int i = 0 ; i<2 ; i++)
				{
					File file = new File("nodes/newNode"+failNode);
					file.mkdir();

					FileOutputStream fos = new FileOutputStream("nodes/newNode"+failNode+"/object"+i);
					fos.write(theResultArray[i]);
					fos.close();
					print(theResultArray[i]);
				}
				System.out.println("finish repairing");
			}
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (numberOfNode == 3)
		{
			byte[][] theByteArray = new byte[numberOfNode*2][4];
			byte[][] theContentArray = new byte[3][file_length];
			byte[][] theResultArray = new byte[2][file_length];
			byte[][] theFailArray = new byte[2][4];
			File config_file = new File("config");
			try {
				String thisLine;  
				BufferedReader br = new BufferedReader(new FileReader(config_file));
				int node = 0;
				int index = 0;
				int failIndex = 0;
				while ((thisLine = br.readLine()) != null) 
				{ // while loop begins here
					if(node==firstNode||node==secondNode||node==thirdNode)
					{
						String[] strarray=thisLine.split(","); 
						//skip the first line so i = 1
						for (int i = 1; i < strarray.length; i++) 
						{
							//			System.out.println(strarray[i]); 
							String tmp = new String(strarray[i].toCharArray());
							theByteArray[index] = tmp.getBytes();
							index++;
						}
					}
					if(node==failNode)
					{
						System.out.println(failNode);
						String[] strarray=thisLine.split(","); 
						//skip the first line so i = 1
						for (int i = 1; i < strarray.length; i++) 
						{
							//			System.out.println(strarray[i]); 
							String tmp = new String(strarray[i].toCharArray());
							theFailArray[failIndex] = tmp.getBytes();
							failIndex++;
						}
					}
					node++;
				} 
				br.close();

				/*
				int array_index = 0;
				for(int i = 0 ; i < numberOfNodes ; i++)
				{
					if(i==firstNode||i==secondNode||i==thirdNode)
					{
						for(int j = 0 ; j < 2 ; j++)
						{
							File file = new File("nodes/node"+i+"/"+"object"+j);
							FileInputStream fis = new FileInputStream(file);
							fis.read(theContentArray[array_index*2+j]);
							fis.close();
						}
						array_index++;
					}
				}

				 */
				/*
				String strFileContent = new String(theContentArray[1]);

				System.out.println(" content : ");
				System.out.println(strFileContent);
				 */

				//		System.out.println(ByteArrayCompare(XORVector(theByteArray[1],XORVector(theByteArray[1],theByteArray[2])),standardByteArray[1]));



				//		print(XOR(theByteArray[1],theByteArray[2]));
				byte[][] theTestArray = new byte[3][4];

				byte[] tmp = new byte[4];
				boolean result1 = false;
				boolean result2 = false;
				int q=0,m=0,n=0;
				Outer:
					for( q = 0 ; q < numberOfNode*2; q++)
						for( m = q+1 ; m < numberOfNode*2; m++)
							for( n = m+1 ; n < numberOfNode*2; n++)
							{
								theTestArray[0]=theByteArray[q];
								theTestArray[1]=theByteArray[m];
								theTestArray[2]=theByteArray[n];
								result1=false;
								result2=false;
								for(int i = 0 ; i < 3; i++)
								{
									tmp = theTestArray[i];
									for(int k = 0 ; k < 2 ; k++)
									{
										//	System.out.println(ByteArrayCompare(tmp,standardByteArray[k]));
										if(ByteArrayCompare(tmp,theFailArray[k]))
										{
											if(k==0)
												result1=true;
											else if(k==1)
												result2=true;
											if(result1==true&&result2==true)
												break Outer;
										}
									}
									for(int j = i+1 ; j < 3 ; j++)
									{
										tmp = XORVector(theTestArray[i],theTestArray[j]);
										for(int k = 0 ; k < 2 ; k++)
										{
											if(ByteArrayCompare(tmp,theFailArray[k]))
											{
												if(k==0)
													result1=true;
												else if(k==1)
													result2=true;
												if(result1==true&&result2==true)
													break Outer;
											}
										}
										for(int p = j+1 ; p < 3; p++)
										{
											tmp = XORVector(theTestArray[p],XORVector(theTestArray[i],theTestArray[j]));
											for(int k = 0 ; k < 2 ; k++)
											{
												if(ByteArrayCompare(tmp,theFailArray[k]))
												{
													if(k==0)
														result1=true;
													else if(k==1)
														result2=true;
													if(result1==true&&result2==true)
														break Outer;
												}
											}
										}
									}
								}
							}
				System.out.println("q="+q+" m="+m+" n="+n);
				int array_index = 0;
				int content_index = 0;
				for(int i = 0 ; i < numberOfNodes ; i++)
				{
					if(i==firstNode||i==secondNode||i==thirdNode)
					{
						for(int j = 0 ; j < 2 ; j++)
						{
							if(array_index==q||array_index==m||array_index==n)
							{
								File file = new File("nodes/node"+i+"/"+"object"+j);
								FileInputStream fis = new FileInputStream(file);
								fis.read(theContentArray[content_index]);
								fis.close();
								content_index++;
							}
							array_index++;
						}
					}
				}

				//				
				//				
				//				File file = new File("nodes/node"+(q/2)+"/"+"object"+(q%2));
				//				FileInputStream fis = new FileInputStream(file);
				//				fis.read(theContentArray[0]);
				//				fis.close();
				//
				//				file = new File("nodes/node"+(m/2)+"/"+"object"+(m%2));
				//				fis = new FileInputStream(file);
				//				fis.read(theContentArray[1]);
				//				fis.close();
				//
				//				file = new File("nodes/node"+(n/2)+"/"+"object"+(n%2));
				//				fis = new FileInputStream(file);
				//				fis.read(theContentArray[2]);
				//				fis.close();

				//				print(theContentArray[0]);
				//				print(theContentArray[1]);
				//				print(theContentArray[2]);
				for(int i = 0 ; i < 3; i++)
				{
					tmp = theTestArray[i];
					for(int k = 0 ; k < 2 ; k++)
					{
						//	System.out.println(ByteArrayCompare(tmp,standardByteArray[k]));
						if(ByteArrayCompare(tmp,theFailArray[k]))
						{
							if(ByteArrayCompare(tmp,theFailArray[k]))
							{
								theResultArray[k]=theContentArray[i];
							}
						}
					}
					for(int j = i+1 ; j < 3 ; j++)
					{
						tmp = XORVector(theTestArray[i],theTestArray[j]);
						for(int k = 0 ; k < 2 ; k++)
						{
							if(ByteArrayCompare(tmp,theFailArray[k]))
							{
								theResultArray[k]=XOR(theContentArray[i],theContentArray[j]);
							}
						}
						for(int p = j+1 ; p < 3; p++)
						{
							tmp = XORVector(theTestArray[p],XORVector(theTestArray[i],theTestArray[j]));
							for(int k = 0 ; k < 2 ; k++)
							{
								if(ByteArrayCompare(tmp,theFailArray[k]))
								{
									theResultArray[k]=XOR(theContentArray[p],XOR(theContentArray[i],theContentArray[j]));
								}
							}
						}
					}
				}

				for(int a = 0 ; a<2 ; a++)
				{
					File fileA = new File("nodes/newNode"+failNode);
					fileA.mkdir();

					FileOutputStream fos = new FileOutputStream("nodes/newNode"+failNode+"/object"+a);
					fos.write(theResultArray[a]);
					fos.close();
					//			print(theResultArray[a]);
				}
				System.out.println("finish repairing");
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

	static void fileRecovery(int numberOfNode, int firstNode, int secondNode, int thirdNode)
	{


		byte[][] theByteArray = new byte[numberOfNode*2][4];
		byte[][] theContentArray = new byte[numberOfNode*2][file_length];
		byte[][] theResultArray = new byte[4][file_length];
		File config_file = new File("config");
		try {
			String thisLine;  
			BufferedReader br = new BufferedReader(new FileReader(config_file));
			int node = 0;
			int index = 0;
			while ((thisLine = br.readLine()) != null) 
			{ // while loop begins here
				if(node==firstNode||node==secondNode||node==thirdNode)
				{
					String[] strarray=thisLine.split(","); 
					//skip the first line so i = 1
					for (int i = 1; i < strarray.length; i++) 
					{
						//			System.out.println(strarray[i]); 
						String tmp = new String(strarray[i].toCharArray());
						theByteArray[index] = tmp.getBytes();
						index++;
					}
				}
				node++;
			} 
			br.close();
			int array_index = 0;
			for(int i = 0 ; i < numberOfNodes ; i++)
			{
				if(i==firstNode||i==secondNode||i==thirdNode)
				{
					for(int j = 0 ; j < 2 ; j++)
					{
						File file = new File("nodes/node"+i+"/"+"object"+j);
						FileInputStream fis = new FileInputStream(file);
						fis.read(theContentArray[array_index*2+j]);
						fis.close();
					}
					array_index++;
				}
			}
			/*
				String strFileContent = new String(theContentArray[1]);

				System.out.println(" content : ");
				System.out.println(strFileContent);
			 */



			byte standardByteArray[][] = {{49,48,48,48},{48,49,48,48},{48,48,49,48},{48,48,48,49}};

			//		System.out.println(ByteArrayCompare(XORVector(theByteArray[1],XORVector(theByteArray[1],theByteArray[2])),standardByteArray[1]));



			//		print(XOR(theByteArray[1],theByteArray[2]));




			byte[] tmp = new byte[4];
			for(int i = 0 ; i < numberOfNode*2; i++)
			{
				tmp = theByteArray[i];
				for(int k = 0 ; k < 4 ; k++)
				{
					//	System.out.println(ByteArrayCompare(tmp,standardByteArray[k]));
					if(ByteArrayCompare(tmp,standardByteArray[k]))
					{
						//			System.out.println("in here");
						theResultArray[k]=theContentArray[i];
					}
				}
				for(int j = i+1 ; j < numberOfNode*2 ; j++)
				{
					tmp = XORVector(theByteArray[i],theByteArray[j]);
					for(int k = 0 ; k < 4 ; k++)
					{
						if(ByteArrayCompare(tmp,standardByteArray[k]))
						{
							theResultArray[k]=XOR(theContentArray[i],theContentArray[j]);
						}
					}
					for(int p = j+1 ; p < numberOfNode*2; p++)
					{
						tmp = XORVector(theByteArray[p],XORVector(theByteArray[i],theByteArray[j]));
						for(int k = 0 ; k < 4 ; k++)
						{
							if(ByteArrayCompare(tmp,standardByteArray[k]))
							{
								theResultArray[k]=XOR(theContentArray[p],XOR(theContentArray[i],theContentArray[j]));
							}
						}
						for(int q = p+1 ; q < numberOfNode*2; q++)
						{
							tmp = XORVector(theByteArray[q],XORVector(theByteArray[p],XORVector(theByteArray[i],theByteArray[j])));								
							for(int k = 0 ; k < 4 ; k++)
							{
								if(ByteArrayCompare(tmp,standardByteArray[k]))
								{
									theResultArray[k]=XOR(theContentArray[q],XOR(theContentArray[p],XOR(theContentArray[i],theContentArray[j])));
								}
							}
							for(int m = q+1 ; m < numberOfNode*2; m++)
							{
								tmp = XORVector(theByteArray[m],XORVector(theByteArray[q],XORVector(theByteArray[p],XORVector(theByteArray[i],theByteArray[j]))));								
								for(int k = 0 ; k < 4 ; k++)
								{
									if(ByteArrayCompare(tmp,standardByteArray[k]))
									{
										theResultArray[k]=XOR(theContentArray[m],XOR(theContentArray[q],XOR(theContentArray[p],XOR(theContentArray[i],theContentArray[j]))));
									}
								}
								for(int n = m+1 ; n < numberOfNode*2; n++)
								{
									tmp = XORVector(theByteArray[n],XORVector(theByteArray[m],XORVector(theByteArray[q],XORVector(theByteArray[p],XORVector(theByteArray[i],theByteArray[j])))));								
									for(int k = 0 ; k < 4 ; k++)
									{
										if(ByteArrayCompare(tmp,standardByteArray[k]))
										{
											theResultArray[k]=XOR(theContentArray[n],XOR(theContentArray[m],XOR(theContentArray[q],XOR(theContentArray[p],XOR(theContentArray[i],theContentArray[j])))));
										}
									}

								}
							}

						}
					}
				}
			}
			for(int i = 0 ; i<4 ; i++)
			{
				File file = new File("target/");
				file.mkdir();

				FileOutputStream fos = new FileOutputStream("target/"+"object"+i);
				fos.write(theResultArray[i]);
				fos.close();
				print(theResultArray[i]);
			}
			System.out.println("finish");
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static void init()
	{
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
				if(i==pieces-1)
				{
					last_piece = (int)file.length();
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
			// print(fileContent[j]);

		}
		////// initialization of the nodes
		File config_file = new File("config");
		int index;
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
					//	System.out.println(strarray[i]); 
					char[] theCharArray = strarray[i].toCharArray();

					byte result[] = new byte[file_length];
					boolean result_initial = true;
					for(int j = 0; j < theCharArray.length ; j++)
					{
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
								} 
							}
						}
					}
					File file = new File("nodes/node"+node+"/");
					file.mkdir();
					index = i - 1;
					FileOutputStream fos = new FileOutputStream("nodes/node"+node+"/"+"object"+index);
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


	public static void main(String[] args) {

		init();
		String opType = args[0];
		int numberOfNodes = Integer.parseInt(args[1]);
		int node1 = Integer.parseInt(args[2]);
		int node2 = Integer.parseInt(args[3]);
		int node3 = Integer.parseInt(args[4]);
		if(opType.equalsIgnoreCase("fix"))
		{
			int failedNode = Integer.parseInt(args[5]);
			if(numberOfNodes==2)
			{
				node3=-1;
			}
			fixNode(numberOfNodes,node1,node2,node3,failedNode);
		}
		else if(opType.equalsIgnoreCase("recover"))
		{
			if(numberOfNodes==2)
			{
				node3=-1;
			}
			fileRecovery(numberOfNodes,node1,node2,node3);
		}
	}



}
