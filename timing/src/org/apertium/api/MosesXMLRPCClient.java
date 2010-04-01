/**
 * @file
 * @author  Pasquale Minervini <p.minervini@gmail.com>
 * @version 1.0
 *
 * @section LICENSE
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @section DESCRIPTION
 *
 * Apertium XML-RPC client Java module.
 */

package org.apertium.api;

import java.net.URL;
import java.util.*;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcSunHttpTransportFactory;

import org.apertium.api.exceptions.MosesXMLRPCClientException;

public class MosesXMLRPCClient {
	private URL serverUrl;
	private int connectionTimeout;
	private int replyTimeout;
	
	public MosesXMLRPCClient(URL serverUrl) {
		this(serverUrl, 60 * 1000, 60 * 1000);
	}
	
	public MosesXMLRPCClient(URL serverUrl, int connectionTimeout, int replyTimeout) {
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
	public Map<String, String> translate(String text) throws MosesXMLRPCClientException {
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("text", text);
		
		Object[] params = new Object[] { map };
		
		Object r = invokeMethod("translate", params);
		return (Map<String, String>) r;
	}
	
	private Object invokeMethod(String methodName, Object[] params) throws MosesXMLRPCClientException {
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
			throw new MosesXMLRPCClientException(e.getMessage());
		}
		
		return ret;
	}
	
}
