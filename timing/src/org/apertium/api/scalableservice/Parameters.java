/*===========================================================================
  Copyright (C) 2008-2009 by the Okapi Framework contributors
-----------------------------------------------------------------------------
  This library is free software; you can redistribute it and/or modify it 
  under the terms of the GNU Lesser General Public License as published by 
  the Free Software Foundation; either version 2.1 of the License, or (at 
  your option) any later version.

  This library is distributed in the hope that it will be useful, but 
  WITHOUT ANY WARRANTY; without even the implied warranty of 
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
  General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License 
  along with this library; if not, write to the Free Software Foundation, 
  Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

  See also the full LGPL text here: http://www.gnu.org/copyleft/lesser.html
===========================================================================*/

package org.apertium.api.scalableservice;

import net.sf.okapi.common.*;
import net.sf.okapi.common.uidescription.*;

public class Parameters extends BaseParameters implements IEditorDescriptionProvider {

	static final String SERVER = "server";
	
	private String server;
	
	public Parameters () {
		reset();
		toString();
	}
	
	public Parameters (String initialData) {
		fromString(initialData);
	}
	
	public String getServer () {
		return server;
	}

	public void setServer (String server) {
		this.server = server;
	}

	public void fromString (String data) {
		reset();
		buffer.fromString(data);
		server = buffer.getString(SERVER, server);
	}

	public void reset () {
		server = "http://localhost:8080/ApertiumServerRouter/resources/translate";
	}

	@Override
	public String toString () {
		buffer.reset();
		buffer.setString(SERVER, server);
		return buffer.toString();
	}

	@Override
	public ParametersDescription getParametersDescription () {
		ParametersDescription desc = new ParametersDescription(this);
		desc.add(SERVER, "Server URL:", "Full URL of the server");
		return desc;
	}

	public EditorDescription createEditorDescription(ParametersDescription paramsDesc) {
		EditorDescription desc = new EditorDescription("Apertium-service MT Connector Settings");
		desc.addTextInputPart(paramsDesc.get(SERVER));
		return desc;
	}

}
