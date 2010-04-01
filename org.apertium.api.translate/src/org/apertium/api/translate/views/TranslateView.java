package org.apertium.api.translate.views;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apertium.api.translate.TranslatePlugin;
import org.apertium.api.translate.Translator;
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

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import  it.uniba.di.cdg.xcore.network.events.chat.*;

public class TranslateView extends ViewPart implements ITranslateView, IBackendEventListener {
	
	private Composite top = null;
	private SashForm sashForm = null;
	
	protected StyledText translations = null;
	protected int position = 0;
	
	public static final String ID = TranslatePlugin.ID + ".views.translateView";
    private static final String SEPARATOR = System.getProperty("line.separator");
	
    public TranslateView() {
    	super();
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
    	Color orange = new Color(Display.getCurrent(), 255, 127, 0);
    	Color lime = new Color(Display.getCurrent(), 127, 255, 127);
    	
		Translator tran = TranslatePlugin.getDefault().getTranslator();
		String translated = original;
		
		try {
			translated = tran.translate(original);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String n = now();
		
		appendMessage(String.format("[%s - %s] %s", who, n, original), orange);
        appendMessage(String.format("[%s - %s] %s", who, n, translated), lime);
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
			
			newMessage(cmrEvent.getMessage().trim(), cmrEvent.getBuddy().getName(), true);
		}
		
	}
    
}