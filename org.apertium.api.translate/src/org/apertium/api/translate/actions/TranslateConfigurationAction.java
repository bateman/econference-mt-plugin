package org.apertium.api.translate.actions;

import org.apertium.api.translate.*;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
//import org.eclipse.ui.IEditorActionDelegate;
//import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
//import org.eclipse.ui.actions.ActionDelegate;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class TranslateConfigurationAction implements IWorkbenchWindowActionDelegate {
	
	private Services services = null;
	
	private static TranslateConfigurationAction instance = null;

	public static TranslateConfigurationAction getInstance() {
		System.out.println("ConfigTranslateAction.getInstance()");
		
		if (instance == null) {
			try {
				instance = new TranslateConfigurationAction();
			} catch (Exception e) {
				e.printStackTrace();
			}
			instance.readProperties();
		}
		return instance;
	}
	
	private void readProperties() {
		System.out.println("ConfigTranslateAction.readProperties()");
		
		String userDir = System.getProperty("user.dir");
		Properties props = new Properties();
		
		File file = new File(userDir + "/" + ".translate");
		if (file.exists()) {
			try {
				props.load(new FileInputStream(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		String tservice = (String)props.get("translate.service");
		
		TranslateConfiguration configuration = TranslatePlugin.getDefault().getConfiguration();
		
		if (tservice != null) {
			configuration.setService(services.getServiceType(tservice));
		}
		
		String tsrc = (String)props.get("translate.srcLang");
		String tdest = (String)props.get("translate.destLang");
		
		if (tsrc != null && tdest != null) {
			Language src = new Language(tsrc);
			Language dest = new Language(tdest);

			configuration.setLangPair(new LanguagePair(src, dest));
		}
		
		String turl = (String) props.get("translate.url");
		
		if (turl != null) {
			configuration.setUrl(turl);
		}
	}

	private void writeProperties() {
		System.out.println("TranslateConfigurationAction.writeProperties()");
		String userDir = System.getProperty("user.dir");

		Properties props = new Properties();
		
		TranslateConfiguration configuration = TranslatePlugin.getDefault().getConfiguration();
		
		if (configuration.getService() != null) {
			props.put("translate.service", services.getService(configuration.getService()));
		}
		
		if (configuration.getLangPair() != null) {
			props.put("translate.srcLang", configuration.getLangPair().getSrcLang().getCode());
			props.put("translate.destLang", configuration.getLangPair().getDestLang().getCode());
		}
		
		if (configuration.getUrl() != null) {
			props.put("translate.url", configuration.getUrl());
		}
		
		File file = new File(userDir + "/" + ".translate");
		
		try {
			
			props.store(new FileOutputStream(file), "translate properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TranslateConfigurationAction() throws Exception {
		services = new Services();
	}

	public void run(IAction action) {
		System.out.println("ConfigTranslateAction.run()");
		
		readProperties();
		
		final TranslateConfigurationDialog dialog = new TranslateConfigurationDialog(null);

		dialog.loadProperties();
		
		dialog.pack();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		dialog.addWindowListener(new WindowAdapter() {

			public void windowClosed(WindowEvent e) {
				if (dialog.isAnswer()) {
					writeProperties();
				}
			}
		});

		dialog.setVisible(true);
	}

	@Override
	public void dispose() {
		System.out.println("ConfigTranslateAction.dispose()");
	}

	@Override
	public void init(IWorkbenchWindow window) {
		System.out.println("ConfigTranslateAction.init()");	
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		System.out.println("ConfigTranslateAction.selectionChanged()");
	}

}
