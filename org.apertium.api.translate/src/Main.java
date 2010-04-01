import java.net.MalformedURLException;
import java.net.URL;

import org.apertium.api.ApertiumXMLRPCClient;
import org.apertium.api.exceptions.ApertiumXMLRPCClientException;
import org.apertium.api.translate.Translator;

public class Main {

	public static void main(String[] args) throws MalformedURLException, InterruptedException, ApertiumXMLRPCClientException {
		ApertiumXMLRPCClient a = new ApertiumXMLRPCClient(new URL("http://localhost:6173/RPC2"));
		System.out.println(Translator._translate("hello world", "en", "es", a));
	}

}
