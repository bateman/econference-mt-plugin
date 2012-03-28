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

package org.apertium.api.translate.views;

import it.uniba.di.cdg.aspects.SwtAsyncExec;
import it.uniba.di.cdg.xcore.m2m.model.UserLanguages;
import it.uniba.di.cdg.xcore.m2m.ui.views.MultiChatTalkView;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.model.tv.Entry;
import it.uniba.di.cdg.xcore.network.model.tv.ITalkModel;
import it.uniba.di.cdg.xcore.network.model.tv.ITalkModelListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apertium.api.translate.TranslatePlugin;
import org.apertium.api.translate.entity.ITranslateMessage;
import org.apertium.api.translate.entity.TranslateMultiChatMessage;
import org.apertium.api.translate.infopopup.InfoPopUp;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.service.prefs.Preferences;

public class TranslateM2Mview extends MultiChatTalkView implements
		ITranslateM2Mview {

	public static final String ID = "org.apertium.api.translate.views.TranslateM2Mview";
	private InfoPopUp infoPopUp = null;
	protected IAction switchOnOffTranslation;
	protected boolean translatingOn;
	private HashMap<String, ArrayList<ITranslateMessage>> unTranslatedCachedTalks;
	private HashMap<String, ArrayList<ITranslateMessage>> queueMessage;
	private static final String THREADSEPARATOR = "---------------------";
	private static final String CONFIGURATION_NODE_QUALIFIER = "it.uniba.di.cdg.xcore.econference.mt";
	private static final String MT_CONFIG_PATH_NODE = "mt_";	
	private static final String MT_USER_LANGUAGE = "mt_user_language";
	private static final String MT_PREFERENCES_HASH = "mt_preference_hash";

	protected ITalkModelListener modelListenerMT = new ITalkModelListener() {
		public void entryAdded(String threadId, Entry entry) {
			// appendMessage( formatEntry( entry ) );
		}

		public void currentThreadChanged(String oldThread, String newThread) {
			// Add a separator to the old threading (when this event is fired
			// the current
			// thread in the talk model has not been changed yet, so it safe to
			// call appendMessage() here and have it attached the message to
			// "oldThread"
			// putSeparator( oldThread );
			
			
			
	//		emptyQueue(newThread);
			insertSeparator(oldThread);
			synchronizeCachedText();
			showThread(newThread);
		}

		private void emptyQueue(String newThread) {
			ArrayList<ITranslateMessage> queue = queueMessage.get(newThread);
			if (queue == null) {
				return;
			}
			
//			while (queue.size() > 0 ) {
//				ITranslateMessage message = queue.get(queue.size() - 1);
//				queue.remove(queue.size() - 1);
//				appendMessage(message);
//			}
			
			for (int i = 0; i < queue.size(); i++) {
				ITranslateMessage message = queue.get(i);
				appendMessage(message,newThread);
			}
			queue.clear();
		}

	};

	public TranslateM2Mview() {
		unTranslatedCachedTalks = new HashMap<String, ArrayList<ITranslateMessage>>();
		queueMessage = new HashMap<String, ArrayList<ITranslateMessage>>();
		translatingOn = true;
		//put my language in hashmap
		UserLanguages u = UserLanguages.getInstance();
		HashMap<String, String> lang = u.get_languages();
		lang.put(NetworkPlugin.getDefault().getRegistry().getDefaultBackend().getUserId(), getMyLanguage());

	}

	private void insertSeparator(String oldThread) {

		appendSecure(new TranslateMultiChatMessage(null, THREADSEPARATOR,
				THREADSEPARATOR, false, true), oldThread);

	}

	@SwtAsyncExec
	public void appendMessage(ITranslateMessage message, String oldThread) {
		addOriginalPhrase(message, oldThread);
		
		
		
		// In the product eConference the incoming private message aren't added
		// in the model
		// so with this if-then i preserve the orginal behavior.
		Entry entry = makeEntry(message);
		if (!message.isPrivateMessage()) {
			model.addEntry(oldThread, entry);
		}

		appendMessage( entry );
		scrollToEnd();
	}

	@SwtAsyncExec
	protected void scrollToEnd() {
		int n = messageBoardText.getCharCount();
        messageBoardText.setSelection( n, n );
        messageBoardText.showSelection();
	}

	public void appendSecure(ITranslateMessage message, String threadId) {
		addOriginalPhrase(message, threadId);
		Entry entry = makeEntry(message);

		if (!message.isPrivateMessage()) {
			model.addEntry(threadId, entry);
		}

		if (threadId.equals(model.getCurrentThread())) {
			appendSec(entry);
		}
	}
	@SwtAsyncExec
	private void appendSec(Entry entry) {
		appendMessage( entry );
		scrollToEnd();
	}

	public void appendMessageinThread(ITranslateMessage message, String threadId) {
		
		
		if (!threadId.equals(model.getCurrentThread())) {
			ArrayList<ITranslateMessage> queue = queueMessage.get(threadId);
			if (queue == null) {
				queue = new ArrayList<ITranslateMessage>();
			}
			queue.add(message);
			queueMessage.put(threadId, queue);
		} else {
			appendMessage(message, threadId);
		}
		
	}
	private void addOriginalPhrase(ITranslateMessage message, String oldThread) {

		cacheUnTranslatedMessage(oldThread, message);
	}

	private boolean isCursorCloseToWord() {
		Point point = messageBoardText.getDisplay().getCursorLocation();
		Point viewLocation = messageBoardText.toDisplay(messageBoardText
				.getLocation());
		Point viewPoint = new Point(Math.abs(point.x - viewLocation.x),
				Math.abs(point.y - viewLocation.y));
		ITranslateMessage message = getUntranslatedString(viewPoint);

		if (messageBoardText.getText() != "" && message != null
				&& !message.isNoTranslation()) {
			return true;
		} else {
			return false;
		}
	}

	public void setModel(ITalkModel model) {
		if (this.model != null)
			this.model.removeListener(modelListenerMT);
		this.model = model;
		model.addListener(modelListenerMT);
	}

	private void openPopUp(final Shell shell) {

		closePopUp();
		Point point = messageBoardText.getCaret().getLocation();
		ITranslateMessage message = getUntranslatedString(point);

		if (message != null && !message.isNoTranslation()) {
			infoPopUp = new InfoPopUp(shell, shell.getDisplay()
					.getCursorLocation(), message);
			infoPopUp.open();
		}
	}

	private void synchronizeCachedText() {
	    cachedTalks.clear();

		Iterator<String> i = unTranslatedCachedTalks.keySet().iterator();
		while (i.hasNext()) {
			String k = i.next();
			ArrayList<ITranslateMessage> lista = unTranslatedCachedTalks.get(k);
			
			List<Entry> cachedEntryList = getOrCreateCachedEntries( k );
			for (int j = 0; j < lista.size(); j++) {
			    Entry entry = makeEntry( lista.get(j) );
			    cachedEntryList.add( entry );
			}
		}
	}

	private void closePopUp() {

		if (infoPopUp != null) {
			infoPopUp.close();
		}
	}

	private ITranslateMessage getUntranslatedString(Point location) {
		ArrayList<ITranslateMessage> unTranslatedString = unTranslatedCachedTalks
				.get(model.getCurrentThread());
		if (unTranslatedString == null) {
			return null;
		}
		ITranslateMessage message = null;
		Point point = location;
		int index = messageBoardText.getLineIndex(point.y);

		if (index < unTranslatedString.size()
				&& index >= 0
				&& ((index < messageBoardText.getLineCount() - 1) || ((index == messageBoardText
						.getLineCount() - 1) && point.y <= messageBoardText
						.getLineHeight()))) {
			message = unTranslatedString.get(index);
		}
		return message;
	}

	public void createPartControl(Composite parent) {
		setAction();
		super.createPartControl(parent);
		setMessageBoard();
	}

	private void switchAction() {
		if (translatingOn) {
			switchOnOffTranslation.setText("Switch Off Translation");
			switchOnOffTranslation
					.setToolTipText("Switch Off the real time Translation");
		} else {
			switchOnOffTranslation.setText("Switch On Translation");
			switchOnOffTranslation
					.setToolTipText("Switch On the real time Translation");
		}
	}

	private void setAction() {

		switchOnOffTranslation = new Action() {
			@Override
			public void run() {

				translatingOn = !translatingOn;
				switchOnOffTranslation.setChecked(translatingOn);
				switchAction();
			}
		};
		translatingOn = true;
		switchOnOffTranslation.setChecked(translatingOn);
		switchAction();
		switchOnOffTranslation.setImageDescriptor(AbstractUIPlugin
				.imageDescriptorFromPlugin(TranslatePlugin.ID,
						"icons/translate_16.png"));

		getViewSite().getActionBars().getToolBarManager()
				.add(switchOnOffTranslation);
		getViewSite().getActionBars().getMenuManager()
				.add(switchOnOffTranslation);
	}

	private void setMessageBoard() {
		messageBoardText.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {

			}

			@Override
			public void mouseDown(MouseEvent e) {

				closePopUp();
				openPopUp(messageBoardText.getShell());
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {

			}
		});

		messageBoardText.addMouseMoveListener(new MouseMoveListener() {

			@Override
			public void mouseMove(MouseEvent e) {

				if (isCursorCloseToWord()) {
					Cursor helpCursor = new Cursor(messageBoardText
							.getDisplay(), SWT.CURSOR_HELP);
					messageBoardText.setCursor(helpCursor);
				} else {
					messageBoardText.setCursor(new Cursor(messageBoardText
							.getDisplay(), SWT.CURSOR_ARROW));
				}
			}
		});
	}

	public boolean isTranslationON() {

		return translatingOn;
	}

	public void addOriginalPhrase(ITranslateMessage message) {

		cacheUnTranslatedMessage(model.getCurrentThread(), message);
	}

	private void cacheUnTranslatedMessage(String threadId,
			ITranslateMessage message) {
		ArrayList<ITranslateMessage> sb = getOrCreateUnTranslatedCachedText(threadId);
		sb.add(message);
	}

	private ArrayList<ITranslateMessage> getOrCreateUnTranslatedCachedText(
			String threadId) {
		ArrayList<ITranslateMessage> sb = unTranslatedCachedTalks.get(threadId);
		if (sb == null) {
			sb = new ArrayList<ITranslateMessage>();
			unTranslatedCachedTalks.put(threadId, sb);
		}
		return sb;
	}

	public void setTranslationOFF() {

		translatingOn = false;
		switchAction();
	}

	// @Override
	// public boolean isSaveOnCloseNeeded() {
	// return false;
	// }
	@SwtAsyncExec
	public void appendMessage(ITranslateMessage message) {
		addOriginalPhrase(message);
		Entry entry = makeEntry(message);

		// In the product eConference the incoming private message aren't added
		// in the model
		// so with this if-then i preserve the orginal behavior.
		if (!message.isPrivateMessage()) {
			model.addEntry(entry);
		}
		
		appendMessage( entry );
		scrollToEnd();
	}

	protected Entry makeEntry(ITranslateMessage message) {
		final Date now = Calendar.getInstance().getTime();

		Entry entry = new Entry();
		entry.setTimestamp(now);

		entry.setText(message.getTranslatedText());
		
		if (message.isSystemMessage()) {
		    entry.setType( Entry.EntryType.SYSTEM_MSG );
		} else if (message.isPrivateMessage()) {
		    entry.setType( Entry.EntryType.PRIVATE_MSG );
		} else {
		    entry.setType( Entry.EntryType.CHAT_MSG );
		}

		if (message.isSystemMessage() || message.isPrivateMessage()) {
			entry.setWho("***");
		} else {
			entry.setWho(message.getFrom());
		}
		return entry;
	}

	private String buildMessageToPrint(ITranslateMessage message) {
		String messageToPrint = new String();
		if (message.isPrivateMessage()) {
			messageToPrint = String.format("*** > [PM from %s] %s",
					message.getFrom(), message.getTranslatedText());
			return messageToPrint;
		}
		if (message.isSystemMessage()) {
			if (message.getTranslatedText().equals(THREADSEPARATOR)) {
				messageToPrint = message.getTranslatedText();
			} else {
				messageToPrint = messageToPrint.concat("*** > ").concat(
						message.getTranslatedText());
			}
			return messageToPrint;
		}

		messageToPrint = String.format("%s > %s", message.getFrom(),
				message.getTranslatedText());
		messageToPrint = messageToPrint.replace("\n", " ");
		return messageToPrint;
	}

	@Override
	public void privateMessageReceived(ITranslateMessage message) {

		appendMessage(message);
	}

	public void printOriginalThread() {
		Iterator<String> itera = unTranslatedCachedTalks.keySet().iterator();
		while (itera.hasNext()) {
			String threadName = itera.next();
			ArrayList<ITranslateMessage> lista = unTranslatedCachedTalks
					.get(threadName);

			for (int i = 0; i < lista.size(); i++) {
				System.out.print(lista.get(i).getOriginalText() + "   ");
				System.out.println(lista.get(i).getTranslatedText());
			}

		}

	}			

  private String getMyLanguage(){
		Preferences preferences = new ConfigurationScope().getNode(CONFIGURATION_NODE_QUALIFIER);
		String id = NetworkPlugin.getDefault().getRegistry().getDefaultBackend().getUserId();
		Preferences connections = preferences.node(MT_CONFIG_PATH_NODE + id);
		Preferences node = connections.node(MT_PREFERENCES_HASH);
		String mtUserlLanguageCode = node.get(MT_USER_LANGUAGE, null);
		return(mtUserlLanguageCode);
	}
}
