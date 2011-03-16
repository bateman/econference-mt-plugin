package org.apertium.api.translate.internal.test;

import static org.junit.Assert.*;

import org.apertium.api.translate.Services;
import org.junit.Test;

public class ServicesTest {

	@Test
	public void testServices() {
		Services s = new Services();
		assertTrue(s instanceof Services);
	}

	@Test
	public void testGetServices() {
		Services s = new Services();
		assertTrue(s.getServices().size()==4);
	}

	@Test
	public void testGetServiceTypes() {
		Services s = new Services();
		assertTrue(s.getServiceTypes().size()==4);
		
	}

	@Test
	public void testGetService() {
		Services s = new Services();
		String service = null;
		service = s.getService(Services.ServiceType.APERTIUM);
		assertTrue(service.equals("Apertium XML-RPC"));
		service = s.getService(Services.ServiceType.GOOGLE);
		assertTrue(service.equals("Google"));
		service = s.getService(Services.ServiceType.MICROSOFT);
		assertTrue(service.equals("Microsoft"));
		service = s.getService(Services.ServiceType.NONE);
		assertTrue(service.equals("None"));
		
	}

	@Test
	public void testGetServiceTypeString() {
		Services s = new Services();
		Services.ServiceType service = null;
		
		service = s.getServiceType("Apertium XML-RPC");
		assertTrue(service == Services.ServiceType.APERTIUM);
		
		service = s.getServiceType("Google");
		assertTrue(service == Services.ServiceType.GOOGLE);
		
		service = s.getServiceType("Microsoft");
		assertTrue(service == Services.ServiceType.MICROSOFT);
		
		
		service = s.getServiceType("None");
		assertTrue(service == Services.ServiceType.NONE);
	}

	@Test
	public void testGetServiceTypeInt() {
		Services s = new Services();
		int i = -1;
		
		assertTrue(s.getServiceType(i) == null);
		i = 1;
		assertTrue(s.getServiceType(i) == Services.ServiceType.APERTIUM);
		i = 2;
		assertTrue(s.getServiceType(i) == Services.ServiceType.GOOGLE);
		i = 3;
		assertTrue(s.getServiceType(i) == Services.ServiceType.MICROSOFT);
		i = 0;
		assertTrue(s.getServiceType(i) == Services.ServiceType.NONE);
		i = 4;
		assertTrue(s.getServiceType(i) == null);
	}

}
