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

package org.apertium.api.translate.internal;

import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.one2one.ChatHelper;
import it.uniba.di.cdg.xcore.one2one.ChatManager;
import it.uniba.di.cdg.xcore.one2one.ChatManager.IChatStatusListener;
import it.uniba.di.cdg.xcore.one2one.IChatService.ChatContext;
import it.uniba.di.cdg.xcore.ui.IUIHelper;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class TranslateChatHelper extends ChatHelper {

	public TranslateChatHelper(INetworkBackendHelper backend_Helper,
			IUIHelper ui_Helper) {
		super(backend_Helper, ui_Helper);

	}

	public void openChatWindow(ChatContext chatContext) {
		if (openChats.containsKey(chatContext.getBuddyId())) {
			// System.err.println( "You are already chatting with " +
			// chatContext.getBuddyId() );
			return;
		}

		TranslateChatManager chat = new TranslateChatManager();
		chat.setBackendHelper(backendHelper);
		chat.setUihelper(uihelper);

		chat.addChatStatusListener(new IChatStatusListener() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * it.uniba.di.cdg.xcore.one2one.ui.Chat.IChatStatusListener#closed
			 * (it.uniba.di.cdg.xcore.one2one.ui.Chat)
			 */
			public void chatClosed(ChatManager chat) {
				openChats.remove(chat.getBuddyId());
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * it.uniba.di.cdg.xcore.one2one.ui.Chat.IChatStatusListener#open(it
			 * .uniba.di.cdg.xcore.chat.ui.Chat)
			 */
			public void chatOpen(ChatManager chat) {
				Shell shell = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell();
				Point size = shell.getSize();
				if (size.x < 640 || size.y < 480)
					shell.setSize(640, 480);
			}
		});
		// Track the chat if it open ok
		if (chat.open(chatContext))
			openChats.put(chatContext.getBuddyId(), chat);
	}
}
