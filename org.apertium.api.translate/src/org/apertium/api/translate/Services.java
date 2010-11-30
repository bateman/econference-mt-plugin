/**
 * This file is part of the eConference project and it is distributed under the 

 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2010 Collaborative Development Group - Dipartimento di Informatica, 
 *                    University of Bari, http://cdg.di.uniba.it
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this 
 * software and associated documentation files (the "Software"), to deal in the Software 
 * without restriction, including without limitation the rights to use, copy, modify, 
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies 
 * or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.apertium.api.translate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Services {
	public enum ServiceType {
		NONE, APERTIUM, GOOGLE, MICROSOFT
	};

	private Map<String, ServiceType> serviceMap = null;
	private Map<ServiceType, String> revServiceMap = null;

	public Services() {
		serviceMap = new HashMap<String, ServiceType>();

		serviceMap.put("Apertium XML-RPC", ServiceType.APERTIUM);
		serviceMap.put("Google", ServiceType.GOOGLE);
		serviceMap.put("Microsoft", ServiceType.MICROSOFT);
		serviceMap.put("None", ServiceType.NONE);

		revServiceMap = new HashMap<ServiceType, String>();
		for (String l : serviceMap.keySet()) {
			revServiceMap.put(serviceMap.get(l), l);
		}
	}

	public Set<String> getServices() {
		return serviceMap.keySet();
	}

	public Set<ServiceType> getServiceTypes() {
		return revServiceMap.keySet();
	}

	public String getService(ServiceType t) {
		String ret = null;
		if (t != null) {
			ret = revServiceMap.get(t);
		}		
		return ret;
	}

	public ServiceType getServiceType(String s) {
		ServiceType ret = null;
		if (s != null) {
			ret = serviceMap.get(s);
		}
		return ret;
	}

	public ServiceType getServiceType(int index) {
		ServiceType ret = null;
		if (index >= 0 && index < ServiceType.values().length)
			ret = ServiceType.values()[index];
		return ret;
	}

}
