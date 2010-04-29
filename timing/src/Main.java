import gnu.getopt.Getopt;

import java.net.URL;
import java.util.*;

import net.sf.okapi.common.LocaleId;
import net.sf.okapi.connectors.microsoft.MicrosoftMTConnector;
import net.sf.okapi.lib.translation.IQuery;

import org.apertium.api.ApertiumXMLRPCClient;
import org.apertium.api.MosesXMLRPCClient;

import com.google.api.translate.Language;
import com.google.api.translate.Translate;

public class Main {

	private static String _translate(String text, String src, String dest, Object c) throws Exception {
		String ret = text;
		
		if (c != null) {
			if (c instanceof IQuery) {
				IQuery q = (IQuery) c;
				if (c instanceof MicrosoftMTConnector) {
					net.sf.okapi.connectors.microsoft.Parameters p = new net.sf.okapi.connectors.microsoft.Parameters();
					p.setAppId("28AEB40E8307D187104623046F6C31B0A4DF907E");
					q.setParameters(p);
				}

				q.setLanguages(LocaleId.fromString(src), LocaleId
						.fromString(dest));

				/* int hits = */ 
				q.query(text);

				// System.out.println("Hits: " + hits);

				if (q.hasNext()) {
					ret = q.next().target.toString();
				}
			} else if (c instanceof ApertiumXMLRPCClient) {
				ApertiumXMLRPCClient a = (ApertiumXMLRPCClient) c;
				ret = a.translate(text, src, dest).get("translation");
			} else if (c instanceof MosesXMLRPCClient) {
				MosesXMLRPCClient m = (MosesXMLRPCClient) c;
				ret = m.translate(text).get("translation");
			}
		} else { 
			ret = Translate.execute(text, Language.fromString(src), Language.fromString(dest));
		}
		
		return ret;
	}
	
	public static long bench(String text, Object c, int cycles) throws Exception {
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < cycles; ++i) {
			_translate(text, "es", "en", c);
		}
		
		long stopTime = System.currentTimeMillis();
		return stopTime - startTime;
	}

    public static List<String> campiona(List<String> l) {
        List<String> ret = new LinkedList<String>();
        long u = new Long(l.get(l.size() - 1).length()) / 20;
        long count = 1;
        for (String s : l) {
                long t = u * count;
                if (s.length() >= t) {
                        ret.add(s);
                        count += 1;
                }
        }
        return ret;
    }
    
    public static String showArray(String mark, List<Long> l) {
    	String ret = mark + " = [";
    	boolean first = true;
    	for (Long v : l) {
    		if (!first) {
    			ret += ", ";
    		}
    		ret += v;
    		first = false;
    	}
    	ret += "];";
    	return ret;
    }

	public enum ServiceType {
		APERTIUM, GOOGLE, MOSES
	}

	private static String usage(String prog) {
		String ret = "Usage: " + prog + "[-s <Service Address>] [-t <Service Type>] [-i <Input File>]\n"
			+ "\t<Service Type> can be either \"google\", \"apertium-service\" or \"moses-service\".";
		return ret;
	}
	
	public static void main(String[] args) throws Exception {

		String prog = "timing";
		Getopt g = new Getopt(prog, args, "s:t:i:");

		String service = null;
		ServiceType type = ServiceType.GOOGLE;
		
		String input = null;

		int c;
		while ((c = g.getopt()) != -1) {
			switch (c) {
			case 's':
				service = g.getOptarg();
				System.out.println("Service set to " + ((service != null) ? service : "null"));
				break;
			case 't':
				String t = g.getOptarg().toLowerCase().trim();
				if (t.equals("google")) {
					type = ServiceType.GOOGLE;
				} else if (t.equals("apertium-service")) {
					type = ServiceType.APERTIUM;
				} else if (t.equals("moses-service")) {
					type = ServiceType.MOSES;
				}
				break;
			case 'i':
				input = g.getOptarg();
				System.out.println("Input set to " + ((input != null) ? input : "null"));
			default:
				System.out.println("getopt() returned " + c);
			}
		}

		Object instance = null;
		
		switch (type) {
		case APERTIUM:
			instance = new ApertiumXMLRPCClient(new URL(service));
			break;
		case MOSES:
			instance = new MosesXMLRPCClient(new URL(service));
		case GOOGLE:
			instance = null;
			Translate.setHttpReferrer("http://www.neuralnoise.com");
			break;
		default:
			System.out.println("getopt() returned " + c);
		}

		//List<String> strings = Utils.getAllSortedStrings();
		List<String> strings = Utils.readSortedLines(input);
		
		int train = 32;
		int cycles = 256;
		
		List<String> camp = campiona(strings);
		
		List<Long> len = new LinkedList<Long>();
		List<Long> ms = new LinkedList<Long>();
		
		for (String s : camp) {
			len.add(new Long(s.length()));
			
			bench(s, instance, train);
			ms.add(bench(s, instance, cycles));
		}
		
		System.out.println(showArray("len", len));
		System.out.println(showArray("ms", ms));

		String bigger = strings.get(strings.size() - 1);
		
		List<Long> threads = new LinkedList<Long>();
		List<Long> mst = new LinkedList<Long>();
		
		for (int i = 1; i <= 8; ++i) {
			Timing.bench(instance, bigger, train, i);
			long t = Timing.bench(instance, bigger, cycles, i) / i;
			
			threads.add(new Long(i));
			mst.add(t);
		}
		
		System.out.println(showArray("threads", threads));
		System.out.println(showArray("mst", mst));
	}
}
