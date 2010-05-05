import java.io.*;
import java.util.Collections;
import java.util.HashMap;
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

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

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
	
	public static List<Result> readResultsCSV(String path, String engine) throws IOException, ParserConfigurationException, SAXException {
		HashMap<Integer, List<Utterance>> collections = new HashMap<Integer, List<Utterance>>();
		HashMap<Integer, List<Utterance>> collections_trad = new HashMap<Integer, List<Utterance>>();
		
		HashMap<Integer, Integer> entries = new HashMap<Integer, Integer>();
		
		for (Integer i = 1; i <= 5; ++i) {
			List<Utterance> u = readUtterances("./testset/testset_log_" + i + ".xml");
			List<Utterance> ut = readUtterances("./testset/testset_log_" + i + ".trans." + engine + ".it.xml");
		
			System.out.println("coll id: " + i + " u: " + u.size() + " ut: " + ut.size());
			
			collections.put(i, u);
			collections_trad.put(i, ut);
			
			entries.put(i, 0);
		}
		
		InputStreamReader isr = new InputStreamReader(new FileInputStream(path));
        CsvReader csvReader = new CsvReader(isr, ';');
        
        List<Result> results = new LinkedList<Result>();
        
        Integer entry = 0;
        
        while (csvReader.readRecord()) {
        	if (entry > 0 && csvReader.get(0).length() > 0) {
        		Integer collId = Integer.parseInt(String.valueOf(csvReader.get(0).charAt(2)));
        		
        		Integer r1 = Integer.parseInt((csvReader.get(1).equals("") ? "0" : csvReader.get(1)));
        		Integer r2 = Integer.parseInt((csvReader.get(2).equals("") ? "0" : csvReader.get(2)));
        		Integer r3 = Integer.parseInt((csvReader.get(3).equals("") ? "0" : csvReader.get(3)));
        		Integer r4 = Integer.parseInt((csvReader.get(4).equals("") ? "0" : csvReader.get(4)));

        		if (r1 == 0 || r2 == 0 || r3 == 0 || r4 == 0) {
        			System.err.println("MISSING VALUE IN CL" + collId + ": entry num. " + entries.get(collId));
        		}
        		
    			Utterance u = collections.get(collId).get(entries.get(collId));
    			Utterance tu = collections_trad.get(collId).get(entries.get(collId));
    			
    			entries.put(collId, entries.get(collId) + 1);
    			
    			Result r = new Result(collId, u, tu, r1, r2, r3, r4);
    			results.add(r);
        	}
        	entry++;
        }
        
        csvReader.close();
        
        return results;
	}
	
	public static void writeResultsToCSV(List<Result> rs, CsvWriter w) throws IOException {
		for (Result r : rs) {
			w.writeRecord(new String[] { "CL" + r.collId, r.utterance.getUtterance(), r.translatedUtterance.getUtterance(),
					r.rater1.toString(), r.rater2.toString(), r.rater3.toString(), r.rater4.toString() });
		}
	}
	
	public static void main(String[] args) throws Exception {
		List<Result> ra = readResultsCSV("./results/AP.csv", "apertium");
		List<Result> rg = readResultsCSV("./results/GT.csv", "google");
		
        PrintWriter pwa = new PrintWriter(new FileWriter("./comparison/AP.csv"));        
        CsvWriter wa = new CsvWriter(pwa, ';');
        
        PrintWriter pwg = new PrintWriter(new FileWriter("./comparison/GT.csv"));        
        CsvWriter wg = new CsvWriter(pwg, ';');
        
        writeResultsToCSV(ra, wa);
        writeResultsToCSV(rg, wg);
        
        wa.close();
        wg.close();
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
