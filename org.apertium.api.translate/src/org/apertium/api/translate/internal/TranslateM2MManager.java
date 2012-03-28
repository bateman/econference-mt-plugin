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

import it.uniba.di.cdg.xcore.econference.internal.EConferenceManager;
import it.uniba.di.cdg.xcore.m2m.model.UserLanguages;
import it.uniba.di.cdg.xcore.econference.model.ConferenceModelListenerAdapter;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel.ConferenceStatus;
import it.uniba.di.cdg.xcore.econference.model.IItemList;
import it.uniba.di.cdg.xcore.econference.ui.views.IHandRaisingView;
import it.uniba.di.cdg.xcore.econference.ui.views.IWhiteBoard;
import it.uniba.di.cdg.xcore.m2m.model.ChatRoomModelAdapter;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Status;
import it.uniba.di.cdg.xcore.m2m.model.Privileged;
import it.uniba.di.cdg.xcore.m2m.service.IInvitationRejectedListener;
import it.uniba.di.cdg.xcore.m2m.service.UserStatusAdapter;
import it.uniba.di.cdg.xcore.m2m.ui.views.ChatRoomView;
import it.uniba.di.cdg.xcore.m2m.ui.views.IChatRoomView;
import it.uniba.di.cdg.xcore.m2m.ui.views.IMultiChatTalkView;
import it.uniba.di.cdg.xcore.network.BackendException;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.IBackendDescriptor;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.action.IChatServiceActions;
import it.uniba.di.cdg.xcore.network.events.BackendStatusChangeEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.events.chat.ChatExtensionProtocolEvent;
import it.uniba.di.cdg.xcore.network.model.tv.ITalkModel;
import it.uniba.di.cdg.xcore.ui.UiPlugin;
import it.uniba.di.cdg.xcore.ui.views.ITalkView.ISendMessagelListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apertium.api.translate.MTPerspective;
import org.apertium.api.translate.TranslatePlugin;
import org.apertium.api.translate.entity.TranslateMultiChatMessage;
import org.apertium.api.translate.service.TranslateM2MService;
import org.apertium.api.translate.views.TranslateM2MHandRaiseView;
import org.apertium.api.translate.views.TranslateM2Mview;
import org.apertium.api.translate.views.TranslateWhiteBoardView;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener3;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.WorkbenchException;
import org.osgi.service.prefs.Preferences;

@SuppressWarnings("restriction")
public class TranslateM2MManager extends EConferenceManager implements
		ITranslateM2MManager {
	private static final String CONFIGURATION_NODE_QUALIFIER = "it.uniba.di.cdg.xcore.econference.mt";
	private static final String MT_CONFIG_PATH_NODE = "mt_";	
	private static final String MT_USER_LANGUAGE = "mt_user_language";
	private static final String MT_PREFERENCES_HASH = "mt_preference_hash";
	protected static final String ALLOWCONVERSATION = "Moderator has allowed you back in conversation!";
	protected static final String STOPCONVERSATION = "Moderator has stopped you from contributing to conversation!";
	public static final String GETLANGUAGE="GET_LANGUAGE";
	public static final String USERLANGUAGE="USER_LANGUAGE";
	public static final String UPDATE="UPDATE";
	

	private ConferenceModelListenerAdapter conferenceModelListenerMT = new ConferenceModelListenerAdapter() {
		@Override
		public void statusChanged() {
			final IConferenceModel model = getService().getModel();
			boolean conferenceStopped = ConferenceStatus.STOPPED.equals(model
					.getStatus());

			String threadId = ITalkModel.FREE_TALK_THREAD_ID;
			if (conferenceStopped) {
				getTranslateM2Mview().appendSecure(new TranslateMultiChatMessage(null,
						conferenceStoppedMessage,
						conferenceStoppedMessage, false, true), ITalkModel.FREE_TALK_THREAD_ID);
//				getTranslateM2Mview().appendMessage(
//						new TranslateMultiChatMessage(null,
//								conferenceStoppedMessage,
//								conferenceStoppedMessage, false, true));
				getTranslateM2Mview().setTitleText(FREE_TALK_NOW_MESSAGE);
				
				// This case tipically occurs when the conference is started and stopped reamining in the free talk
				// In this case no change of thread is happend so event is gone to be generated.
				int currItem = getService().getModel().getItemList()
				.getCurrentItemIndex();
				if (currItem == IItemList.NO_ITEM_SELECTED) {
					return;
				}
				
				
			} else {
				getTranslateM2Mview().appendSecure(
						new TranslateMultiChatMessage(null,
								conferenceStartedMessage,
								conferenceStartedMessage, false, true), ITalkModel.FREE_TALK_THREAD_ID);
				int currItem = getService().getModel().getItemList()
						.getCurrentItemIndex();
				// This case tipically occurs when the conference is started for
				// the first time: no
				// item is still selected so we remain on the previous
				// "free talk" thread ...
				if (currItem == IItemList.NO_ITEM_SELECTED) {
					return;
				}
				threadId = Integer.toString(currItem);
			}
			// Notify to remote clients if we are moderators ...
			if (Role.MODERATOR.equals(getService().getModel().getLocalUser()
					.getRole())) {
				notifyCurrentAgendaItemChanged(threadId);
			}
		}
	};
	private IPerspectiveListener3 perspectiveListenerMT = new IPerspectiveListener3() {

		public void perspectiveOpened(IWorkbenchPage page,
				IPerspectiveDescriptor perspective) {
			System.out.println(String.format("perspectiveOpened( %s )",
					perspective.getId()));
		}

		public void perspectiveClosed(IWorkbenchPage page,
				IPerspectiveDescriptor perspective) {
			// The user has requested the perspective to be closed ... so
			// let's clean-up all
			if (page == getWorkbenchWindow().getActivePage()
					&& MTPerspective.ID.equals(perspective.getId())) {
				// Ask the user to save views even when the perspective is
				// closed (by default the
				// eclipse framework asks only when closing the whole app,
				// see BR #43).

				// page.saveAllEditors( true );

				// close perspective means leave the room
				// service.leave(); It's called in close()
				close();
				System.out.println(String.format("perspectiveClosed( %s )",
						perspective.getId()));
			}
		}

		public void perspectiveDeactivated(IWorkbenchPage page,
				IPerspectiveDescriptor perspective) {
			// System.out.println( String.format(
			// "perspectiveDeactivated( %s )", perspective.getId() ) );
		}

		public void perspectiveSavedAs(IWorkbenchPage page,
				IPerspectiveDescriptor oldPerspective,
				IPerspectiveDescriptor newPerspective) {
			// System.out.println( String.format( "perspectiveSaveAs()") );
		}

		public void perspectiveChanged(IWorkbenchPage page,
				IPerspectiveDescriptor perspective,
				IWorkbenchPartReference partRef, String changeId) {
			// System.out.println( String.format(
			// "perspectiveChanged( %s )", perspective.getId() ) );
		}

		public void perspectiveActivated(IWorkbenchPage page,
				IPerspectiveDescriptor perspective) {
			// System.out.println( String.format(
			// "perspectiveActivated( %s )", perspective.getId() ) );
		}

		public void perspectiveChanged(IWorkbenchPage page,
				IPerspectiveDescriptor perspective, String changeId) {
			// System.out.println( String.format(
			// "perspectiveChanged( %s )", perspective.getId() ) );
		}
	};
	private class eventListenerLanguage implements IBackendEventListener{
		public void start(){
			for (IBackendDescriptor d : NetworkPlugin.getDefault()
					.getRegistry().getDescriptors())
				NetworkPlugin.getDefault().getHelper().registerBackendListener(
						d.getId(), this);
		}
		public void onBackendEvent(IBackendEvent event){
			if(event instanceof ChatExtensionProtocolEvent){
				IBackend b = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();
				IChatServiceActions chat = b.getChatServiceAction();
				ChatExtensionProtocolEvent cepe = (ChatExtensionProtocolEvent)event;
				if(cepe.getExtensionName().equals(GETLANGUAGE)){
					HashMap<String, String> param = new HashMap<String, String>();
					param.put("LANGUAGE", getMyLanguage());					
					chat.OpenChat(cepe.getFrom());
					chat.SendExtensionProtocolMessage(cepe.getFrom(),USERLANGUAGE, param);
					chat.CloseChat(cepe.getFrom());
					chat.OpenChat(cepe.getFrom());
					chat.SendExtensionProtocolMessage(cepe.getFrom(),UPDATE, new HashMap<String,String>());
					chat.CloseChat(cepe.getFrom());
				}
				if(cepe.getExtensionName().equals(USERLANGUAGE)){ 
					String language = (String) cepe.getExtensionParameter("LANGUAGE");
					UserLanguages u = UserLanguages.getInstance();
					HashMap<String, String> lang = u.get_languages();
					lang.put(cepe.getFrom(),language);
					u.set_languages(lang);
					chat.CloseChat(cepe.getFrom());
				}
			}
		}
	}
	protected IBackendEventListener backendListenerMT = new IBackendEventListener() {

		public void onBackendEvent(IBackendEvent event) {
			if (event instanceof BackendStatusChangeEvent) {
				BackendStatusChangeEvent changeEvent = (BackendStatusChangeEvent) event;
				if (!changeEvent.isOnline())
					UiPlugin.getUIHelper().closePerspective(MTPerspective.ID);
			}	

		}
	};

	public TranslateM2MManager() {
		super();
		eventListenerLanguage listner = new eventListenerLanguage();
		listner.start();

	}

	// public void open(MultiChatContext context, boolean autojoin)
	// throws Exception {
	// this.context = context;
	// service = setupChatService();
	// service.join();
	// if (autojoin) {
	//
	// setupUI();
	// setupListeners();
	// // Notify chat listeners that the chat is
	// for (IMultiChatListener l : chatlisteners) {
	// l.open();
	// }
	// }
	//
	// }

	protected void setupUI() throws WorkbenchException {
		super.setupUI();

		TranslateM2MService m2mService = (TranslateM2MService) service;

		m2mService.setTranslateM2MView((TranslateM2Mview) talkView);

		m2mService
				.setTranslateWhiteBoardView((TranslateWhiteBoardView) whiteBoardView);

		m2mService
				.setTranslateHandRaiseView((TranslateM2MHandRaiseView) handRaisingView);
		
	}

	protected void setupUISwithPerspective() {
		// Switch to the multichat perspective too ...
		getUihelper().switchPerspective(MTPerspective.ID);
	}

	protected IViewPart setupUITalkView() throws WorkbenchException {
		IViewPart talkViewPart = workbenchWindow.getActivePage().showView(
				TranslateM2Mview.ID);

		// Setup the talk view: it must be able to display incoming messages
		// (both local and from network service)
		// and from
		talkView = (IMultiChatTalkView) talkViewPart;
		talkView.setTitleText(context.getRoom());
		talkView.setModel(getService().getTalkModel());

		return talkViewPart;
	}

	protected IViewPart setupUIRoomView() {
		IViewPart chatRoomViewPart = (IViewPart) workbenchWindow.getActivePage().findView(ChatRoomView.ID);
		System.out.println("class: "+workbenchWindow.getActivePage().findView(ChatRoomView.ID).toString());
		roomView = (IChatRoomView) chatRoomViewPart;
		roomView.setManager(this);				
		return chatRoomViewPart;

	}

	protected IViewPart setupUIWhiteBoard() throws WorkbenchException {

		IViewPart viewPart = getWorkbenchWindow().getActivePage().showView(
				TranslateWhiteBoardView.ID);
		whiteBoardView = (IWhiteBoard) viewPart;
		whiteBoardView.setManager(this);
		// By default the whiteboard cannot be modified: when the user is given
		// the SCRIBE
		// special role than it will be set read-write
		whiteBoardView.setReadOnly(true);

		return viewPart;

	}

	protected IViewPart setupUIHandRaise() throws WorkbenchException {
		// Hand raising panel is for all: context menu actions will be disabled
		// using the
		// actions' enablements provided by the plugin.xml and the adapter
		// factory

		IViewPart viewPart = getWorkbenchWindow().getActivePage().showView(
				TranslateM2MHandRaiseView.ID);
		handRaisingView = (IHandRaisingView) viewPart;
		handRaisingView.setManager(this);

		return viewPart;
	}

	protected ITranslateM2MService setupChatService() throws BackendException {

		IBackend backend = getBackendHelper().getRegistry().getBackend(
				getContext().getBackendId());
		ITranslateM2MService m2mService = new TranslateM2MService(getContext(),
				backend);

		return m2mService;
	}

	protected void setupListeners() {
		// workbenchWindow.addPerspectiveListener(perspectiveListener);
		workbenchWindow.addPerspectiveListener(perspectiveListenerMT);
		// Send messages typed by the user to the remote peers
		// talkView.addListener(new ISendMessagelListener() {
		// public void notifySendMessage(String message) {
		// service.sendMessage(message);
		// }
		// });
		getTranslateM2Mview().addListener(new ISendMessagelListener() {
			public void notifySendMessage(String message) {
				service.sendMessage(message);
			}
		});
		// ... and receives them too!
		getService().addMessageMTReceivedListener(getTranslateM2Mview());

		// Typing notification to remote client ...
		talkView.addTypingListener(service);
		service.addTypingEventListener(talkView);

		service.addUserStatusListener(new UserStatusAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * it.uniba.di.cdg.xcore.m2m.service.UserStatusAdapter#voiceGranted
			 * ()
			 */
			@Override
			public void voiceGranted() {
				getTranslateM2Mview().appendMessage(
						new TranslateMultiChatMessage(null, ALLOWCONVERSATION,
								ALLOWCONVERSATION, false, true));
				talkView.setReadOnly(false);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * it.uniba.di.cdg.xcore.m2m.service.UserStatusAdapter#voiceRevoked
			 * ()
			 */
			@Override
			public void voiceRevoked() {
				getTranslateM2Mview().appendMessage(
						new TranslateMultiChatMessage(null, STOPCONVERSATION,
								STOPCONVERSATION, false, true));
				talkView.setReadOnly(true);
			}
		});

		service.addInvitationRejectedListener(new IInvitationRejectedListener() {
			public void declined(String invitee, String reason) {
				uihelper.showMessage(String.format(
						"User %s rejected invitation with motivation\n \"%s\"",
						invitee, reason));
			}
		});

		// Make the view to react to model events
		talkView.setManager(this);
		getService().addManagerEventListener(managerEventListener);
		getBackendHelper().registerBackendListener(backendListenerMT);
		

		final IConferenceModel model = (IConferenceModel) getService()
				.getModel();
		model.addListener(conferenceModelListenerMT);
		// TODO see if it's ok to remove this 
		// model.getItemList().addListener(itemListListener);

		// The multichat has already registered a listener for participant
		// status changes: so
		// we simply register a listener and notify the item list to users that
		// joins:
		model.addListener(new ChatRoomModelAdapter() {
			@Override
			public void added(IParticipant participant) {
				getService().notifyItemListToRemote();
				IBackend b = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();
				IChatServiceActions chat = b.getChatServiceAction();
				chat.OpenChat(participant.getId());
				chat.SendExtensionProtocolMessage(participant.getId(),GETLANGUAGE, new HashMap<String, String>());
				chat.CloseChat(participant.getId());
				chat.OpenChat(participant.getId());
				HashMap<String, String> param = new HashMap<String,String>();
				param.put("LANGUAGE",getMyLanguage());
				chat.SendExtensionProtocolMessage(participant.getId(),USERLANGUAGE, param);
			}
		});

		TranslatePlugin.getDefault().getConfiguration()
				.registerLanguageUpdateListener(this);
	}

	private TranslateM2Mview getTranslateM2Mview() {
		return (TranslateM2Mview) getTalkView();
	}

	public ITranslateM2MService getService() {
		return (ITranslateM2MService) service;
	}

	public void close() {

		service.leave();
		service = null;

		workbenchWindow.removePerspectiveListener(perspectiveListenerMT);
		getBackendHelper().unregisterBackendListener(backendListenerMT);
		TranslatePlugin.getDefault().getConfiguration()
				.unregisterLanguageUpdateListener(this);

		// Notify chat listeners that the chat is open
		for (IMultiChatListener l : chatlisteners)
			l.closed();
		chatlisteners.clear();
		chatlisteners = null;

	}

	@Override
	public void notifyLanguageUpdate() {
		getService().notifyLanguageUpdate();
	}

	@Override
	public void inviteNewParticipant(String participantId) {
		getService().invite(participantId, ITranslateM2MHelper.MT_REASON);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.IMultiChat#toggleFreezeUnfreeze(it.uniba.di
	 * .cdg.xcore.m2m.model.IParticipant[])
	 */
	@Privileged(atleast = Role.MODERATOR)
	public void toggleFreezeUnfreeze(List<IParticipant> participants) {
		final List<String> frozen = new ArrayList<String>();
		final List<String> unfrozen = new ArrayList<String>();

		// Discriminate among who must be frozen and who must be unfrozen: do
		// not touch
		// the model: we rely on server for notification about participant's
		// status change
		for (IParticipant p : participants) {
			if (Status.NOT_JOINED.equals(p.getStatus()))
				continue;
			if (Status.FROZEN.equals(p.getStatus()))
				//unfrozen.add(p.getNickName());
				unfrozen.add(p.getId());
			else if (Status.JOINED.equals(p.getStatus()))
				//frozen.add(p.getNickName());
				frozen.add(p.getId());
		}

		if (!frozen.isEmpty())
			service.revokeVoice(frozen);
		if (!unfrozen.isEmpty())
			service.grantVoice(unfrozen);
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
