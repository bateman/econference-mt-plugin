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

package org.apertium.api.translate;

import it.uniba.di.cdg.xcore.econference.EConferencePlugin;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.events.BackendStatusChangeEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.one2one.ChatPlugin;
import it.uniba.di.cdg.xcore.ui.UiPlugin;
import it.uniba.di.cdg.xcore.ui.views.ITalkView.ISendMessagelListener;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apertium.api.translate.actions.TranslateConfiguration;
import org.apertium.api.translate.internal.TranslateChatHelper;
import org.apertium.api.translate.internal.TranslateM2MHelper;
import org.apertium.api.translate.listeners.TranslateListener;
import org.apertium.api.translate.ui.IImageResourcesMT;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class TranslatePlugin extends AbstractUIPlugin implements IStartup,
		IBackendEventListener {

	public static final String ID = "org.apertium.api.translate";

	private List<IBackendEventListener> translateListeners = null;
	private List<ISendMessagelListener> sendListeners = null;

	private Translator translator = null;
	private TranslateConfiguration configuration = null;

	private TranslateM2MHelper helper;

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

		// Setting the chatManager for OnetoOneChat
		// ChatPlugin.getDefault().setChatManager(new TranslateChatManager());
		ChatPlugin.getDefault().setHelper(
				new TranslateChatHelper(NetworkPlugin.getDefault().getHelper(),
						UiPlugin.getUIHelper()));
		// //Setting the multiChatManager for M2MChat
		// MultiChatPlugin.getDefault().getHelper().setMultiChatManager(new
		// TranslateMultiChatManager());
		//
		// //Setting the multiChatManager for M2MChat whit wizard
		// EConferenceHelper eConferenceHelper = (EConferenceHelper)
		// EConferencePlugin.getDefault().getHelper();
		// eConferenceHelper.setEconferenceManager(new
		// TranslateM2MEConfereceManager());
		//
		helper = new TranslateM2MHelper(UiPlugin.getUIHelper(), NetworkPlugin
				.getDefault().getHelper());
		EConferencePlugin defaultPlugin = EConferencePlugin.getDefault();
		defaultPlugin.setHelper(helper);

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

		//setupLogging();
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

	public ImageDescriptor getImageDescriptor(String path) {
		//return imageDescriptorFromPlugin(ID, path);
	    ImageDescriptor descr = getImageRegistry().getDescriptor( path );
        if (descr == null) {
            descr = loadAndRegisterIconImage( path );
        }
        return descr;
	}

    /**
     * Actually loads the image.
     * FIXME And what happens if the image file is not found?
     * 
     * @param symbolicName
     * @return the image descriptor
     */
    private ImageDescriptor loadAndRegisterIconImage( String symbolicName ) {
        Bundle bundle = Platform.getBundle( ID );

        URL url = bundle.getResource( IImageResourcesMT.ICON_PATH + symbolicName + IImageResourcesMT.ICON_FILES_EXT );
        ImageDescriptor descr = ImageDescriptor.createFromURL( url );
        getImageRegistry().put( symbolicName, descr );

        return descr;
    }
    
	@Override
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

	public void setHelper(TranslateM2MHelper translateM2MHelper) {
		if (this.helper != null)
			this.helper.dispose();
		this.helper = translateM2MHelper;
		this.helper.init();

	}

	public TranslateM2MHelper getHelper() {
		return helper;
	}

}
