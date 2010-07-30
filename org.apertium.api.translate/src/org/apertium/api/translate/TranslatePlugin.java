package org.apertium.api.translate;

import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.events.BackendStatusChangeEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.ui.views.ITalkView.ISendMessagelListener;

import java.util.LinkedList;
import java.util.List;

import org.apertium.api.translate.actions.TranslateConfiguration;
import org.apertium.api.translate.listeners.TranslateListener;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class TranslatePlugin extends AbstractUIPlugin implements IStartup,
		IBackendEventListener {

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

		translateListeners = new LinkedList<IBackendEventListener>();
		translateListeners.add(new TranslateListener());

		sendListeners = new LinkedList<ISendMessagelListener>();
		sendListeners.add(new TranslateListener());
	}

	public void addListener(IBackendEventListener listener) {
		translateListeners.add(listener);
		NetworkPlugin
				.getDefault()
				.getHelper()
				.registerBackendListener(
						NetworkPlugin.getDefault().getRegistry()
								.getDefaultBackendId(), listener);
	}

	public void removeListener(IBackendEventListener listener) {
		translateListeners.remove(listener);
		NetworkPlugin
				.getDefault()
				.getHelper()
				.unregisterBackendListener(
						NetworkPlugin.getDefault().getRegistry()
								.getDefaultBackendId(), listener);
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
			NetworkPlugin
					.getDefault()
					.getHelper()
					.registerBackendListener(
							NetworkPlugin.getDefault().getRegistry()
									.getDefaultBackendId(), listener);
		}

		NetworkPlugin
				.getDefault()
				.getHelper()
				.registerBackendListener(
						NetworkPlugin.getDefault().getRegistry()
								.getDefaultBackendId(), this);

	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("TranslatePlugin.stop()");

		for (IBackendEventListener listener : translateListeners) {
			NetworkPlugin
					.getDefault()
					.getHelper()
					.unregisterBackendListener(
							NetworkPlugin.getDefault().getRegistry()
									.getDefaultBackendId(), listener);
		}

		NetworkPlugin
				.getDefault()
				.getHelper()
				.unregisterBackendListener(
						NetworkPlugin.getDefault().getRegistry()
								.getDefaultBackendId(), this);

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

	@Override
	// TODO do we really need to implement IStartup to make sure the plugin is auto started?
	public void earlyStartup() {
		System.out.println("TranslatePlugin.earlyStartup()");
	}

	@Override
	public void onBackendEvent(IBackendEvent event) {
		if (event instanceof BackendStatusChangeEvent) {
			BackendStatusChangeEvent bsce = (BackendStatusChangeEvent) event;
			if (bsce.isOnline())
				TranslatePlugin.getDefault().getConfiguration()
						.loadProperties();
		}

	}
}
