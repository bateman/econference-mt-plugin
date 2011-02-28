/**
 * This file is part of the eConference project and it is distributed under the 

 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2005 Collaborative Development Group - Dipartimento di Informatica, 
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
package org.apertium.api.translate.model;

import it.uniba.di.cdg.xcore.econference.model.ConferenceContextLoader;
import it.uniba.di.cdg.xcore.econference.model.InvalidContextException;
import it.uniba.di.cdg.xcore.econference.model.definition.IServiceContextLoader;

import java.io.InputStream;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

public class MTContextLoader extends ConferenceContextLoader implements
		IServiceContextLoader {
	protected static final String MT_SERVICE = "econference-mt";

	public MTContextLoader() {
		super();
	}

	public void load(InputStream is) throws InvalidContextException {
		try {
			super.load(is);

			XPathFactory factory = XPathFactory.newInstance();
			XPath xPath = factory.newXPath();

			// Get the service
			String service = xPath.evaluate("/meeting/service", doc);
			if (!MT_SERVICE.equals(service))
				throw new InvalidContextException("Wrong MT service.\n"
						+ "Expected: " + MT_SERVICE + "\n" + "Found: "
						+ service);

		} catch (Exception e) {
			throw new InvalidContextException(e);
		}

	}

}
