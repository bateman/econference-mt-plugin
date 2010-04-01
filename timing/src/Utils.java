import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.okapi.common.LocaleId;
import net.sf.okapi.connectors.google.GoogleMTConnector;
import net.sf.okapi.connectors.microsoft.MicrosoftMTConnector;
import net.sf.okapi.lib.translation.IQuery;

import org.apertium.api.ApertiumXMLRPCClient;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.csvreader.CsvReader;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;

public class Utils {

	public static String makeXML(List<Utterance> uts) throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		DOMImplementation impl = builder.getDOMImplementation();

		Document doc = impl.createDocument(null, null, null);
		Element root = doc.createElement("meeting");
		doc.appendChild(root);

		for (Utterance u : uts) {

			Element un = doc.createElement("utterance");
			root.appendChild(un);

			un.setAttribute("turn", u.getTurn().toString());
			un.setAttribute("who", u.getWho());
			
			if (u.getRole() != null)
				un.setAttribute("role", u.getRole().toString());
			//un.setAttribute("utterance", u.getUtterance());
			
			if (u.getCategory() != null)
				un.setAttribute("category", u.getCategory());
			
			un.appendChild(doc.createTextNode(u.getUtterance()));
		}
		
        DOMSource domSource = new DOMSource(doc);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();

        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        
        java.io.StringWriter sw = new java.io.StringWriter();
        StreamResult sr = new StreamResult(sw);
        
        transformer.transform(domSource, sr);
        
        String xml = sw.toString();
        
        return xml;
	}
	
	public static List<Utterance> readUtterances(String path)
			throws ParserConfigurationException, SAXException, IOException {
		
		List<Utterance> ret = new LinkedList<Utterance>();

		File file = new File(path);

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		
		doc.getDocumentElement().normalize();

		NodeList utteranceNodes = doc.getElementsByTagName("utterance");
		
		for (int i = 0; i < utteranceNodes.getLength(); ++i) {
			Node utteranceNode = utteranceNodes.item(i);
			
			NamedNodeMap attributes = utteranceNode.getAttributes();

			Integer turn = Integer.decode(attributes.getNamedItem("turn")
					.getNodeValue());

			String who = attributes.getNamedItem("who").getNodeValue();

			Utterance.Role role = null;

			if (attributes.getNamedItem("role") != null) {
				if (attributes.getNamedItem("role").equals("Client"))
					role = Utterance.Role.CLIENT;
				else if (attributes.getNamedItem("role").equals("Developer"))
					role = Utterance.Role.DEVELOPER;
				else
					role = Utterance.Role.OTHER;
			}

			String utterance = utteranceNode.getTextContent();

			String category = null;
			if (attributes.getNamedItem("category") != null) {
				category = attributes.getNamedItem("category").getNodeValue();
			}
			Utterance u = new Utterance(turn, who, role, utterance, category);

			ret.add(u);
		}
		
		return ret;
	}
	
	public static List<Utterance> readCSVnew(String path) throws IOException {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(path));
        CsvReader csvReader = new CsvReader(isr, ';');
        
        List<Utterance> uts = new LinkedList<Utterance>();
        
        while (csvReader.readRecord()) {
        	
            Integer turn = 0;
            try {
            	turn = Integer.parseInt(csvReader.get(0));
            } catch (Exception e) {
            	//
            }
            
            String who = csvReader.get(1);

            
            String utterance = csvReader.get(2);
            
            if (turn > 0) {
            	Utterance u = new Utterance(turn, who, null, utterance, null);
            	uts.add(u);
            }
        }
        
        csvReader.close();
        
        return uts;
	}
	
	public static List<Utterance> readCSV(String path) throws IOException {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(path));
        CsvReader csvReader = new CsvReader(isr, ';');
        
        List<Utterance> uts = new LinkedList<Utterance>();
        
        while (csvReader.readRecord()) {
        	
            Integer turn = 0;
            try {
            	turn = Integer.parseInt(csvReader.get(0));
            } catch (Exception e) {
            	//
            }
            
            String who = csvReader.get(1);
            Utterance.Role role = Utterance.Role.OTHER;
            if (csvReader.get(2).equals("client")) {
            	 role = Utterance.Role.CLIENT;
            } else if (csvReader.get(2).equals("dev")) {
            	 role = Utterance.Role.DEVELOPER;
            }
            
            String utterance = csvReader.get(3);
            String category = csvReader.get(4);
            
            if (turn > 0) {
            	Utterance u = new Utterance(turn, who, role, utterance, category);
            	uts.add(u);
            }
        }
        
        csvReader.close();
        
        return uts;
	}
	
	
	public static void main(String[] args) throws Exception {
		ApertiumXMLRPCClient a = new ApertiumXMLRPCClient(new URL("http://localhost:6173/RPC2"));

		IQuery qm = new MicrosoftMTConnector();
		IQuery qg = new GoogleMTConnector();
		
		qm.setLanguages(LocaleId.fromString("en"), LocaleId.fromString("it"));
		qg.setLanguages(LocaleId.fromString("en"), LocaleId.fromString("it"));

		qm.open();
		qg.open();
		
		for (int i = 1; i <= 5; ++i) {
			List<Utterance> orig = readCSVnew("testset/testset_log_" + i + ".csv");

			BufferedWriter out = new BufferedWriter(new FileWriter("testset/testset_log " + i + ".xml"));
			out.write(makeXML(orig));
			out.close();

			List<Utterance> utterances = readUtterances("testset/testset_log " + i	+ ".xml");

			List<Utterance> newutterancesa = new LinkedList<Utterance>();
			List<Utterance> newutterancesm = new LinkedList<Utterance>();
			List<Utterance> newutterancesg = new LinkedList<Utterance>();
			
			for (Utterance u : utterances) {
				Utterance nua = u.clona();
				Utterance num = u.clona();
				Utterance nug = u.clona();
				
				System.out.println(u.getUtterance());
				
				nua.setUtterance(a.translate(u.getUtterance(), "en", "it").get("translation"));
				//num.setUtterance(_translate(u.getUtterance(), qm));
				//nug.setUtterance(_translate(u.getUtterance(), qg));
				
				newutterancesa.add(nua);
				newutterancesm.add(num);
				newutterancesg.add(nug);
			}

			out = new BufferedWriter(new FileWriter("testset/testset_log " + i	+ ".trans.apertium.xml"));
			out.write(makeXML(newutterancesa));
			out.close();
			
			out = new BufferedWriter(new FileWriter("testset/testset_log " + i	+ ".trans.microsoft.xml"));
			out.write(makeXML(newutterancesm));
			out.close();
			
			out = new BufferedWriter(new FileWriter("testset/testset_log " + i	+ ".trans.google.xml"));
			out.write(makeXML(newutterancesg));
			out.close();
		}
	}
	
	public static void _main(String[] args) throws Exception {
		
		List<Utterance> orig = readCSV("log.csv");
		
		BufferedWriter out = new BufferedWriter(new FileWriter("log.xml"));
		out.write(makeXML(orig));
		out.close();
		
		List<Utterance> utterances = readUtterances("log.xml");
		
		ApertiumXMLRPCClient a = new ApertiumXMLRPCClient(new URL("http://www.neuralnoise.com:6173/RPC2"));
		
		//IQuery qes = new GoogleMTConnector();
		//IQuery qit = new GoogleMTConnector();
		
		//qes.setLanguages(LocaleId.fromString("en"), LocaleId.fromString("es"));
		//qit.setLanguages(LocaleId.fromString("en"), LocaleId.fromString("it"));

		//qes.open();
		//qit.open();
		
		Translate.setHttpReferrer("http://www.neuralnoise.com");
		
		List<Utterance> utterancesApertiumEs = new LinkedList<Utterance>();
		List<Utterance> utterancesApertiumIt = new LinkedList<Utterance>();
		
		List<Utterance> utterancesGoogleEs = new LinkedList<Utterance>();
		List<Utterance> utterancesGoogleIt = new LinkedList<Utterance>();
		
		for (Utterance u : utterances) {
			
			System.out.println("Translating: " + u.getUtterance());
			
			String tradApertiumEs = a.translate(u.getUtterance(), "en", "es").get("translation");
			System.out.println("Apertium EN -> ES: " + tradApertiumEs);
			String tradApertiumIt = a.translate(u.getUtterance(), "en", "it").get("translation");
			System.out.println("Apertium EN -> IT: " + tradApertiumIt);
			
			String tradGoogleEs = Translate.execute(u.getUtterance(), Language.ENGLISH, Language.SPANISH);
			System.out.println("Google EN -> ES: " + tradGoogleEs);
			String tradGoogleIt = Translate.execute(u.getUtterance(), Language.ENGLISH, Language.ITALIAN);
			System.out.println("Google EN -> IT: " + tradGoogleIt);
			
			//String tradGoogleEs = invokeGoogle(u.getUtterance(), qes);
			//String tradGoogleIt = invokeGoogle(u.getUtterance(), qit);
			
			Utterance uaes = u.clona();
			Utterance uait = u.clona();
			
			uaes.setUtterance(tradApertiumEs);
			uait.setUtterance(tradApertiumIt);
			
			Utterance uges = u.clona();
			Utterance ugit = u.clona();
			
			uges.setUtterance(tradGoogleEs);
			ugit.setUtterance(tradGoogleIt);
			
			utterancesApertiumEs.add(uaes);
			utterancesApertiumIt.add(uait);
			
			utterancesGoogleEs.add(uges);
			utterancesGoogleIt.add(ugit);
		}
		
		out = new BufferedWriter(new FileWriter("log-apertium-es.xml"));
		out.write(makeXML(utterancesApertiumEs));
		out.close();
		
		out = new BufferedWriter(new FileWriter("log-apertium-it.xml"));
		out.write(makeXML(utterancesApertiumIt));
		out.close();
		
		out = new BufferedWriter(new FileWriter("log-google-es.xml"));
		out.write(makeXML(utterancesGoogleEs));
		out.close();
		
		out = new BufferedWriter(new FileWriter("log-google-it.xml"));
		out.write(makeXML(utterancesGoogleIt));
		out.close();
	}
	
	public static List<String> getAllSortedStrings() throws ParserConfigurationException, SAXException, IOException {	
		List<Utterance> utterancestr = Utils.readUtterances("trainingset/log.xml");
		
		List<Utterance> utterancests1 = Utils.readUtterances("testset/testset_log_1.xml");
		List<Utterance> utterancests2 = Utils.readUtterances("testset/testset_log_2.xml");
		List<Utterance> utterancests3 = Utils.readUtterances("testset/testset_log_3.xml");
		List<Utterance> utterancests4 = Utils.readUtterances("testset/testset_log_4.xml");
		List<Utterance> utterancests5 = Utils.readUtterances("testset/testset_log_5.xml");
		
		List<String> strings = new LinkedList<String>();
		
		for (Utterance u : utterancestr) {
			strings.add(u.getUtterance());
		}
		
		for (Utterance u : utterancests1) {
			strings.add(u.getUtterance());
		}
		
		for (Utterance u : utterancests2) {
			strings.add(u.getUtterance());
		}
		
		for (Utterance u : utterancests3) {
			strings.add(u.getUtterance());
		}
		
		for (Utterance u : utterancests4) {
			strings.add(u.getUtterance());
		}
		
		for (Utterance u : utterancests5) {
			strings.add(u.getUtterance());
		}
		
		Collections.sort(strings, new StringLengthComparator());
		
		return strings;
	}
	
	public static List<String> readSortedLines(String fileName) {
		List<String> ret = readLines(fileName);
		Collections.sort(ret, new StringLengthComparator());
		return ret;
	}
	
	public static List<String> readLines(String fileName) {
		List<String> ret = new LinkedList<String>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line = null;
			while ((line = br.readLine()) != null) {
				ret.add(line);
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

}
