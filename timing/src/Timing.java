import org.apertium.api.exceptions.ApertiumXMLRPCClientException;

class Timing extends Thread {
	
	private Object connector = null;
	private String text = null;
	private int cycles = 0;
	
	public Timing(Object c, String t, int cy) {
		connector = c;
		text = t;
		cycles = cy;
	}
	
	public static long bench(Object connector, String text, int cycles, int threads) throws InterruptedException {
		Timing[] t = new Timing[threads];
		
		for (int i = 0; i < threads; ++i)
			t[i] = new Timing(connector, text, cycles);
		
		long startTime = System.currentTimeMillis();
		
		for (int i = 0; i < threads; ++i)
			t[i].start();
		
		for (int i = 0; i < threads; ++i)
			t[i].join();
		
		long stopTime = System.currentTimeMillis();
		
		return stopTime - startTime;
	}
	
	public void run() {
		try {
			Main.bench(text, connector, cycles);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}