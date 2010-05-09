package com.neuralnoise.atd;

import java.io.*;
import java.net.*;

public class ATDClient {

	private static final String baseUrl = "http://www.neuralnoise.com:1049";
	private static final String baseQuery = "/checkDocument?data=%s";
	
	public static void main(String[] args) throws IOException {
		String text = "hello world";
		
		URL url = new URL(baseUrl + String.format(baseQuery, URLEncoder.encode(text, "UTF-8")));
		URLConnection conn = url.openConnection();

		conn.setRequestProperty("User-Agent", "ATDClient");
		conn.setRequestProperty("Referer", baseUrl);
	}

}
