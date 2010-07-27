package org.apertium.api.translate.views;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import org.apertium.api.exceptions.ApertiumXMLRPCClientException;
import org.apertium.api.translate.TranslatePlugin;
import org.apertium.api.translate.Translator;
import org.apertium.api.translate.actions.TranslateConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.ui.part.ViewPart;

import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.action.IChatServiceActions;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import  it.uniba.di.cdg.xcore.network.events.chat.*;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatMessageEvent;
import it.uniba.di.cdg.xcore.network.messages.MessageType;

public class TranslateView extends ViewPart implements ITranslateView, IBackendEventListener {
	
	private Composite top = null;
	private SashForm sashForm = null;
	
	protected StyledText translations = null;
	protected int position = 0;
	
	public static final String ID = TranslatePlugin.ID + ".views.translateView";
    private static final String SEPARATOR = System.getProperty("line.separator");
    
    private Vector<NotTraslatedMessage> messagesToTranslate;
    private HashMap<String, String> buddiesLenguages;
    
	private final static String LANGUAGE_REQUEST = "languageRequest";
	private final static String LANGUAGE_RESPONSE = "languageResponse";
	private final static String LANGUAGE = "language";
	
    public TranslateView() {
    	super();
    	messagesToTranslate = new Vector<NotTraslatedMessage>();
		buddiesLenguages = new HashMap<String, String>();
    }
    
	@Override
	public void createPartControl(Composite parent) {
		System.out.println("TranslateView.createPartControl()");
		
        top = new Composite(parent, SWT.NONE);
        top.setLayout(new FillLayout());
		
        createSashForm();        
        
        TranslatePlugin.getDefault().addListener(this);
	}

    @Override
    public void dispose() {
    	System.out.println("TranslateView.dispose()");
    	TranslatePlugin.getDefault().removeListener(this);
    }

	
	@Override
	public void setFocus() {
		System.out.println("TranslateView.setFocus()");
		
		translations.setFocus();
	}
	
	private String getLanguageFromRosterBuddy(String name){
		String lan = null;
		
		if(buddiesLenguages.containsKey(name))
			lan = buddiesLenguages.get(name);
		else{
			IBackend b = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();
			IChatServiceActions chat = b.getChatServiceAction();
			HashMap<String, String> param = new HashMap<String, String>();
			chat.SendExtensionProtocolMessage(name, LANGUAGE_REQUEST, param);
		}
		
		return lan;
	}
	
    private void createSashForm() {
        sashForm = new SashForm(top, SWT.NONE);
        sashForm.setOrientation(org.eclipse.swt.SWT.VERTICAL);
        
		translations = new StyledText(sashForm, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		translations.setEditable(false);
    }

    private static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    
	private static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}
    
    public void newMessage(String original, String who, boolean toTranslate) {
    	Color blu = new Color(Display.getCurrent(), 25, 25, 112);
    	Color red = new Color(Display.getCurrent(), 142, 35, 35);
    	
		Translator tran = TranslatePlugin.getDefault().getTranslator();
		String translated = original;
		
		try {
			translated = tran.translate(original, who, getLanguageFromRosterBuddy(who));
			
			String n = now();
			
			appendMessage(String.format("[%s - %s] %s", who, n, original), blu);
	        appendMessage(String.format("[%s - %s] %s", who, n, translated), red);
	        
		} catch (ApertiumXMLRPCClientException e){
			messagesToTranslate.add(new NotTraslatedMessage(original, who));
		} catch (Exception e) {
			e.printStackTrace();
		}

    }
    
    public void updateNotTranslatedMessages(String newBuddyLauguage){
    	
    	Vector<NotTraslatedMessage> messagesToDelete = new Vector<NotTraslatedMessage>();
    	
    	for(NotTraslatedMessage mess: messagesToTranslate){
    		if (newBuddyLauguage.equals(mess.getSender())){
    			newMessage(mess.message, mess.sender, true);
    			messagesToDelete.add(mess);
    		}
    	}
    	
    	for(NotTraslatedMessage mess: messagesToDelete){
    		messagesToTranslate.removeElement(mess);
    	}
    	
    	messagesToDelete.clear();
    	
    }
    
    public void appendMessage(final String message, final Color color) {
    	System.out.println("TranslateView.appendMessage()");
    	
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				String textToAppend = message + SEPARATOR;
				translations.append(textToAppend);
				
				StyleRange styleRange = new StyleRange();
				styleRange.start = position;
				styleRange.length = textToAppend.length();
				styleRange.fontStyle = SWT.BOLD;
				styleRange.foreground = color;
				translations.setStyleRange(styleRange);
				
				position += textToAppend.length();
				scrollToEnd();
			}
		});
	}

    protected void scrollToEnd() {
    	System.out.println("TranslateView.scrollToEnd()");
    	
        ScrollBar scrollBar = translations.getVerticalBar();
        scrollBar.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				System.out.println("TranslateView.widgetDefaultSelected()");
			}
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("TranslateView.widgetSelected()");
			}
        });
        
        int n = translations.getCharCount();
        translations.setSelection(n, n);
        translations.showSelection();
    }

	@Override
	public void onBackendEvent(IBackendEvent event) {
		System.out.println("TranslateView.onBackendEvent() - event is " + event.getClass().toString());
		
		if (event instanceof ChatMessageReceivedEvent) {
			ChatMessageReceivedEvent cmrEvent = (ChatMessageReceivedEvent)event;
			
			newMessage(cmrEvent.getMessage().trim(), cmrEvent.getBuddy().getId(), true);
		}/*else if(event instanceof MultiChatMessageEvent){
			MultiChatMessageEvent mcme = (MultiChatMessageEvent)event;
			
			newMessage(mcme.getMessage().trim(), mcme.getFrom(), true);
			
		}*/
		if(event instanceof ChatExtensionProtocolEvent){
			
			IBackend b = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();
			IChatServiceActions chat = b.getChatServiceAction();
			ChatExtensionProtocolEvent cepe = (ChatExtensionProtocolEvent)event;
			
			if(cepe.getExtensionName().equals(LANGUAGE_REQUEST)){
				HashMap<String, String> param = new HashMap<String, String>();
				TranslateConfiguration c = TranslatePlugin.getDefault().getConfiguration();
				param.put(LANGUAGE, c.getLangPair().getDestLang().getCode());
				chat.SendExtensionProtocolMessage(cepe.getFrom(), LANGUAGE_RESPONSE, param);
			}
			
			else if(cepe.getExtensionName().equals(LANGUAGE_RESPONSE)){
				buddiesLenguages.put(cepe.getFrom(), (String)cepe.getExtensionParameter(LANGUAGE));
				updateNotTranslatedMessages(cepe.getFrom());
			}
		}
		
	}
	
	private class NotTraslatedMessage{
		
		private String message;
		private String sender;
		
		public String getMessage() {
			return message;
		}
		
		public String getSender() {
			return sender;
		}

		public NotTraslatedMessage(String message, String sender) {
			super();
			this.message = message;
			this.sender = sender;
		}
		
	}
    
}