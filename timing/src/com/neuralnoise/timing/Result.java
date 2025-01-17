package com.neuralnoise.timing;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import com.csvreader.CsvWriter;

public class Result {
	public Integer collId = 0;

	public Utterance utterance = null;
	public Utterance translatedUtterance = null;
	
	public Integer rater1 = 0;
	public Integer rater2 = 0;
	public Integer rater3 = 0;
	public Integer rater4 = 0;
	
	public List<com.neuralnoise.atd.ATDError> errors = null;
	
	public Result(Integer c, Utterance u, Utterance tu, Integer r1, Integer r2, Integer r3, Integer r4) {
		collId = c;

		utterance = u;
		translatedUtterance = tu;
		
		rater1 = r1;
		rater2 = r2;
		rater3 = r3;
		rater4 = r4;
		
		errors = new LinkedList<com.neuralnoise.atd.ATDError>();
	}
	
	public String toCSV() throws IOException {
		StringWriter sw = new StringWriter();
		PrintWriter pwg = new PrintWriter(sw);
		CsvWriter w = new CsvWriter(pwg, ';');
		
		w.writeRecord(new String[] { "CL" + collId, utterance.getTurn().toString(), utterance.getUtterance(), translatedUtterance.getUtterance(),
				rater1.toString(), rater2.toString(), rater3.toString(), rater4.toString(), Boolean.valueOf(errors.size() > 0).toString() });
		
		return sw.toString();
	}
	
	public String getErrors() {
		StringBuilder ret = new StringBuilder();
		boolean first = true;
		for (com.neuralnoise.atd.ATDError e : errors) {
			if (!first) {
				ret.append("+");
			}
			ret.append(e.getCompact());
			first = false;
		}
		return ret.toString();
	}
}
