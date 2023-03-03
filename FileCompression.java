import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Scanner;


/**
 * @author Tyler Schultz
 * 11/15/22
 *
 */

public class FileCompression {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		FileCompression fc = new FileCompression();
		File file = new File("SimpleText.txt");
		int [] frequency = new int [26];										//this array will hold frequencies for every word
		
		char[] alphabet = new char[26];											//this array will just contain the alphabet
		for(int i = 0; i < 26 ; i++)
		{
		    alphabet[i] = (char)('A' + i);										//this for loop will add each letter to the array one by one
		}
		
		try(Scanner s = new Scanner(file))										//now we will start scanning the file
		{
			while(s.hasNext())
			{
				String line = "";												//this string will contain whatever is on the current line
				line = s.nextLine();
				for(int i = 0; i < line.length(); i++)							//this for loop will determine whether each character on the line is either a letter or not
				{
					char c = (char) line.charAt(i);
					if (Character.isLetter(c))									//if there is a letter, we capitalize it and then we add to the frequency based on the character
					{
						c = Character.toUpperCase(c);				
						frequency[c-65]++;
					}
				}
			}
		}
		
		/*Step 1 - Generate a Frequency Table*/
		System.out.println("Letter  	Frequency");
		for (int i = 0; i < 26; i++)
		{
			if (frequency[i] > 0)												//if the letter showed up in the file
			{
			   System.out.println(alphabet[i] + "     		" + frequency[i]);	//print it and add it to map
			}
		}
		
		fc.setUpQueue(frequency, alphabet);
	}
	
	void setUpQueue(int[] frequency, char[] alphabet) throws IOException, ClassNotFoundException
	{
		FileCompression fc = new FileCompression();
		PQueue pq = new PQueue();
		
		/*Step 2 - Create a new node out of the char and frequency and then put it in a priority queue*/
		for (int i = 0; i < frequency.length; i++) 
		{
			if (frequency[i] > 0)												//only make nodes for characters that actually showed up
			{
				TreeNode newNode = new TreeNode(frequency[i], alphabet[i]);
				pq.push(newNode);
			}
	    }
		
		TreeNode root = createTree(pq);
		Integer[] code = new Integer[100];														//this array will hold the code for each character
		HashMap<Character, String> codeMap = new HashMap<Character,String>();				//make a hashmap for our characters and code
		printCode(root, code, 0, codeMap);
		fc.compressFile(codeMap);																	//send the completed code map to the file compressor
	}
	
	/*Step 3 - Create the Huffman Tree*/
	static TreeNode createTree(PQueue pq)
	{
		while(pq.size != 1)
		{
			TreeNode l = pq.peek();														//left and right node will be the two least frequent nodes in the queue
			pq.poll();																	//we pull each of them through the queue
		
			TreeNode r = pq.peek();
			pq.poll();
		
			TreeNode sumNode = new TreeNode(l.frequency + r.frequency,'?');				//create a new node, whose frequency is the sum of left and right. the character we assign won't matter
			sumNode.left = l;															//assign the new node its children
			sumNode.right = r;
			pq.push(sumNode);															//push sum node into the priority queue
		}
		return pq.peek();																//the element we peek should be the root of our code tree
	}
	
	/*Step 4 - Create a code map*/
	static void printCode(TreeNode root, Integer[] code, int top, HashMap<Character, String> codeMap)
	{
		String codeString = "";
		if (root.left != null)															//if it has a left child, we add a 0 to the code
		{
			code[top] = 0;
			printCode(root.left, code, top+1, codeMap);									//use recursion and modify code until we reach a leaf node, increase top by 1
		}
		
		if (root.right != null)															//if it has a right child, we add a 1 to the code
		{
			code[top] = 1;
			printCode(root.right, code, top+1, codeMap);
		}
		
		if (root.left == null && root.right == null)									//leaf node, our root should be a character
		{
			System.out.print("\n" + root.c + ": ");									//print the character and loop through the code array to give it its code
			for(int i = 0; i < top; i++)
			{
				codeString += code[i];													//isolate each characters code in its own string
			}
			System.out.println(codeString);
			codeMap.put(root.c, codeString);													//put the character as well as its generated code into a map
		}
	}
	
	
	void compressFile(HashMap<Character, String> codeMap) throws IOException, ClassNotFoundException
	{
		/*Step 5 (beginning) - Compress the file*/
		System.out.println();
		File file = new File("SimpleText.txt");							//we'll call back our text file and translate it to bits
		try(Scanner s = new Scanner(file))
		{
			while(s.hasNext())
			{
				String line = "";
				line = s.next();
				for(int i = 0; i < line.length(); i++)
				{
					char c = line.charAt(i);
					if(Character.isLetter(c))							//if the character is a letter, we print out the letter through bits
					{
						c = Character.toUpperCase(c);
						System.out.print(codeMap.get(c));
					}
				}
			}
		}
		
		FileOutputStream fileStream = new FileOutputStream("CompressedFile.txt"); 
		//make a file and object output stream
		 
		try
		 
		{
		ObjectOutputStream objStream = new ObjectOutputStream(fileStream);
		 
		objStream.writeObject
		(codeMap); 
		//write codeMap into the file
		 
		objStream.close();
		//close our streams
		 
		}
		 
		finally{fileStream.close();}
	}
}
class TreeNode{																			//tree class so we can establish the different nodes and data
	
	TreeNode next;
	int frequency;
	char c;
	
	TreeNode left,
			right;
	TreeNode(int frequency, char c)
	{
		this.frequency = frequency;
		this.c = c;
		left = right = null;															//when creating a new node we should establish it without children
	}
}

