package org.apertium.api;

import java.net.URL;
import java.util.*;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcSunHttpTransportFactory;

import org.apertium.api.exceptions.ApertiumXMLRPCClientException;

public class ApertiumXMLRPCClient {
	
	private URL serverUrl;
	private int connectionTimeout;
	private int replyTimeout;
	
	public ApertiumXMLRPCClient(URL serverUrl) {
		this(serverUrl, 60 * 1000, 60 * 1000);
	}
	
	public ApertiumXMLRPCClient(URL serverUrl, int connectionTimeout, int replyTimeout) {
		this.serverUrl = serverUrl;
		this.connectionTimeout = connectionTimeout;
		this.replyTimeout = replyTimeout;
	}
	
	public URL getServerUrl() {
		return this.serverUrl;
	}
	
	public void setServerUrl(URL serverUrl) {
		this.serverUrl = serverUrl;
	}
	
	public int getConnectionTimeout() {
		return this.connectionTimeout;
	}
	
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	
	public int getReplyTimeout() {
		return this.replyTimeout;
	}
	
	public void setReplyTimeout(int replyTimeout) {
		this.replyTimeout = replyTimeout;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> translate(String text, String srcLang, String destLang) throws ApertiumXMLRPCClientException {
		Object[] params = new Object[] { text, srcLang, destLang };
		Object r = invokeMethod("translate", params);
		return (Map<String, String>) r;
	}
	
	public String detect(String text) throws ApertiumXMLRPCClientException {
		Object[] params = new Object[] { text };
		Object r = invokeMethod("detect", params);
		return (String) r;
	}
	
	public String synthesise(List<String> translations, String srcLang, String destLang) throws ApertiumXMLRPCClientException {
		Object[] params = new Object[] { translations.toArray(), srcLang, destLang };
		Object r = invokeMethod("synthesise", params);
		return (String) r;
	}
	
	public Object[] languagePairs() throws ApertiumXMLRPCClientException {
		Object[] params = new Object[] { };
		Object r = invokeMethod("languagePairs", params);
		return (Object[]) r;
	}
	
	private Object invokeMethod(String methodName, Object[] params) throws ApertiumXMLRPCClientException {
		Object ret = null;
		
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		
		config.setServerURL(this.serverUrl);
		
        config.setEnabledForExtensions(true);
        config.setEnabledForExceptions(true);
        
        config.setConnectionTimeout(this.connectionTimeout);
        config.setReplyTimeout(this.replyTimeout);
        
        config.setBasicEncoding("UTF-8");
		
		XmlRpcClient client = new XmlRpcClient();
		
		client.setTransportFactory(new XmlRpcSunHttpTransportFactory(client));
		client.setConfig(config);
	
		try {
			ret = client.execute(methodName, params);
		} catch (XmlRpcException e) {
			throw new ApertiumXMLRPCClientException(e.getMessage());
		}
		
		return ret;
	}
	
}
