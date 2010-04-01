package org.apertium.api.translate;

import java.util.*;

public class Services {
	public enum ServiceType { NONE, APERTIUM, GOOGLE, MICROSOFT };
	
	
	private Map<String, ServiceType> serviceMap = null;
	private Map<ServiceType, String> revServiceMap = null;
	
	public Services() {
		serviceMap = new HashMap<String, ServiceType>();
		
		serviceMap.put("Apertium XML-RPC", ServiceType.APERTIUM);
		serviceMap.put("Google", ServiceType.GOOGLE);
		serviceMap.put("Microsoft", ServiceType.MICROSOFT);
		serviceMap.put("Nessuno", ServiceType.NONE);
		
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
		System.out.println("Service.getService(): asked for " + t + ", returning " + ret);
		return ret;
	}
	
	public ServiceType getServiceType(String s) {
		ServiceType ret = null;
		if (s != null) {
			ret =  serviceMap.get(s);
		}
		return ret;
	}
}
