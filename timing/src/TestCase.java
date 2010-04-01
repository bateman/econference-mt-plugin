import java.io.*;
import java.net.*;
import java.util.*;

import org.apertium.api.*;
import org.apertium.api.exceptions.ApertiumXMLRPCClientException;

public class TestCase {
	
	private static String readFileAsString(String filePath)	throws IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}
    
	public static List<String> toCombine() throws IOException {
		List<String> ret = new LinkedList<String>();
		
		ret.add(readFileAsString("./t1.txt"));
		ret.add(readFileAsString("./t2.txt"));
		ret.add(readFileAsString("./t3.txt"));
		ret.add(readFileAsString("./t4.txt"));
		
		return ret;
	}

	public static void main(String[] args) throws ApertiumXMLRPCClientException, IOException {
		ApertiumXMLRPCClient a = new ApertiumXMLRPCClient(new URL("http://www.neuralnoise.com:6173/RPC2"));
		
		System.out.println(a.translate("Tiger tiger burning bright in the forest of the night", "en", "it"));
	}
}
