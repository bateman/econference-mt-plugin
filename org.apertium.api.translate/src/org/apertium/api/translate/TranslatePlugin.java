package org.apertium.api.translate;

import java.util.LinkedList;
import java.util.List;

import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.ui.views.ITalkView.ISendMessagelListener;

import org.apertium.api.translate.actions.TranslateConfiguration;
import org.apertium.api.translate.actions.TranslateConfigurationAction;
import org.apertium.api.translate.listeners.TranslateListener;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class TranslatePlugin extends AbstractUIPlugin {

	public static final String ID = "org.apertium.api.translate";
	
	private List<IBackendEventListener> translateListeners = null;
	private List<ISendMessagelListener> sendListeners = null;
	
	private Translator translator = null;
	private TranslateConfiguration configuration = null;
	
	private static TranslatePlugin plugin = null;
	
	public TranslatePlugin() {
		System.out.println("TranslatePlugin()");
		plugin = this;
		
		translator = new Translator();
		configuration = new TranslateConfiguration();
		
		TranslateConfigurationAction.getInstance();
		
		translateListeners = new LinkedList<IBackendEventListener>();
		translateListeners.add(new TranslateListener());
		
		sendListeners = new LinkedList<ISendMessagelListener>();
		sendListeners.add(new TranslateListener());
	}
	
	public void addListener(IBackendEventListener listener) {
		translateListeners.add(listener);
		NetworkPlugin.getDefault().getHelper().registerBackendListener(NetworkPlugin.getDefault().getRegistry().getDefaultBackendId(), listener);
	}
	
	public void removeListener(IBackendEventListener listener) {
		translateListeners.remove(listener);
		NetworkPlugin.getDefault().getHelper().unregisterBackendListener(NetworkPlugin.getDefault().getRegistry().getDefaultBackendId(), listener);
	}
	
	public void addSendListener(ISendMessagelListener listener) {
		sendListeners.add(listener);
	}

	public void removeSendListener(ISendMessagelListener listener) {
		sendListeners.remove(listener);
	}
	
	public void start(BundleContext context) throws Exception {
		super.start(context);
		System.out.println("TranslatePlugin.start()");
		
		for (IBackendEventListener listener : translateListeners) {
			NetworkPlugin.getDefault().getHelper().registerBackendListener(NetworkPlugin.getDefault().getRegistry().getDefaultBackendId(), listener);
		}
		
		for (ISendMessagelListener listener : sendListeners) {
			
		}
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("TranslatePlugin.stop()");
		
		for (IBackendEventListener listener : translateListeners) {
			NetworkPlugin.getDefault().getHelper().unregisterBackendListener(NetworkPlugin.getDefault().getRegistry().getDefaultBackendId(), listener);
		}
		
		for (ISendMessagelListener listener : sendListeners) {
			
		}
		
		plugin = null;
		super.stop(context);
	}

	public static TranslatePlugin getDefault() {
		System.out.println("TranslatePlugin.getDefault()");
		
		return plugin;
	}

	public Translator getTranslator() {
		System.out.println("TranslatePlugin.getTranslator()");
		return translator;
	}
	
	public TranslateConfiguration getConfiguration() {
		System.out.println("TranslatePlugin.getTConfiguration()");
		return configuration;
	}
	
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(ID, path);
	}
}
