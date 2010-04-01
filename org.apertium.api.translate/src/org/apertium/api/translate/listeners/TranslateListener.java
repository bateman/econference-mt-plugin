package org.apertium.api.translate.listeners;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.events.chat.ChatMessageReceivedEvent;
import it.uniba.di.cdg.xcore.ui.views.ITalkView.ISendMessagelListener;


public class TranslateListener implements IBackendEventListener, ISendMessagelListener {
	@Override
	public void onBackendEvent(IBackendEvent event) {
		System.out.println("TranslateListener.onBackendEvent() - event is " + event.getClass().toString());
	
		if (event instanceof ChatMessageReceivedEvent) {
			ChatMessageReceivedEvent cmrEvent = (ChatMessageReceivedEvent)event;
			System.out.println("TranslateListener.onBackendEvent(): \"" + cmrEvent.getMessage() + "\"");
		}
	
	}

	@Override
	public void notifySendMessage(String message) {
		System.out.println("TranslateListener.notifySendMessage(): " + message);
	}

}
