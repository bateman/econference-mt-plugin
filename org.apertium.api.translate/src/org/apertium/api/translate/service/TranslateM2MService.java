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

package org.apertium.api.translate.service;

import it.uniba.di.cdg.aspects.SwtAsyncExec;
import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.econference.model.hr.IQuestion;
import it.uniba.di.cdg.xcore.econference.model.hr.IQuestion.QuestionStatus;
import it.uniba.di.cdg.xcore.econference.model.hr.Question;
import it.uniba.di.cdg.xcore.econference.service.EConferenceService;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.action.IMultiChatServiceActions;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatExtensionProtocolEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatMessageEvent;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import java.util.ArrayList;
import java.util.HashMap;

import org.apertium.api.exceptions.ApertiumXMLRPCClientException;
import org.apertium.api.translate.Language;
import org.apertium.api.translate.LanguagePair;
import org.apertium.api.translate.TranslatePlugin;
import org.apertium.api.translate.Translator;
import org.apertium.api.translate.actions.TranslateConfiguration;
import org.apertium.api.translate.entity.ITranslateMessage;
import org.apertium.api.translate.entity.ITranslateQuestion;
import org.apertium.api.translate.entity.TranslateMultiChatMessage;
import org.apertium.api.translate.entity.TranslateQuestion;
import org.apertium.api.translate.internal.ITranslateM2MService;
import org.apertium.api.translate.listeners.IMTMessagegeReceivedListener;
import org.apertium.api.translate.views.TranslateM2MHandRaiseView;
import org.apertium.api.translate.views.TranslateM2Mview;
import org.apertium.api.translate.views.TranslateWhiteBoardView;

public class TranslateM2MService extends EConferenceService implements
		ITranslateM2MService {

	private ArrayList<IMTMessagegeReceivedListener> messageMTReceivedListeners = null;
	private ArrayList<ITranslateMessage> messagesToTranslate = null;
	private ITranslateMessage whiteBoardTextToTranslate = null;
	private HashMap<String, LanguagePair> buddiesLenguages = null;

	private TranslateM2Mview translateM2Mview;
	private TranslateWhiteBoardView translateWhiteBoardView;
	private TranslateM2MHandRaiseView translateM2MHandRaiseView;
	private ArrayList<ITranslateQuestion> questionToTranslate;

	private final static String LANGUAGE_REQUEST = "languageRequest";
	private final static String LANGUAGE_RESPONSE = "languageResponse";
	private final static String LANGUAGE = "language";
	private static final String TRANSLATIONSERVICEERROR = "The resource you chose is not available. The translation is failed";
	private static final String LANGUAGE_USER = "languageuser";
	private static final String LANGUAGE_UPDATE = "languageUpdate";
	private Translator translator;
	
	public TranslateM2MService(EConferenceContext context, IBackend backend) {
		super(context, backend);
		messagesToTranslate = new ArrayList<ITranslateMessage>();
		buddiesLenguages = new HashMap<String, LanguagePair>();
		messageMTReceivedListeners = new ArrayList<IMTMessagegeReceivedListener>();
		questionToTranslate = new ArrayList<ITranslateQuestion>();
		translator = TranslatePlugin.getDefault().getTranslator();
	}

	private String getLanguageFromRosterBuddy(String name) {
		String lan = null;
		if (buddiesLenguages.containsKey(name)) {
			LanguagePair lp = buddiesLenguages.get(name);
			lan = lp.getDestLang().getCode();
		}
		return lan;
	}

	public void updateNotTranslatedMessages(String newBuddyLauguage) {

		ArrayList<ITranslateMessage> messagesToDelete = new ArrayList<ITranslateMessage>();

		for (ITranslateMessage mess : messagesToTranslate) {

			if (newBuddyLauguage.equals(mess.getFrom())) {
				String who = removeRoomAddress(mess.getFrom());
				String translated = translateMessage(mess);
				ITranslateMessage translatedMessage;

				if (translated == null) {
					if (mess.isPrivateMessage()) {
						translatedMessage = new TranslateMultiChatMessage(who,
								mess.getText(), mess.getText(), true, false, true);
						translateM2Mview
								.privateMessageReceived(translatedMessage);
					} else {
						translatedMessage = new TranslateMultiChatMessage(who,
								mess.getText(), mess.getText(), false, false, true);
						translateM2Mview.appendMessage(translatedMessage);
					}
				} else {
					if (mess.isPrivateMessage()) {
						translatedMessage = new TranslateMultiChatMessage(who,
								translated, mess.getText(), true, false, true);
						translateM2Mview
								.privateMessageReceived(translatedMessage);
					} else {
						translatedMessage = new TranslateMultiChatMessage(who,
								translated, mess.getText(), false, false, true);
						translateM2Mview.appendMessage(translatedMessage);
					}
				}
				messagesToDelete.add(mess);
			}
		}
		for (ITranslateMessage mess : messagesToDelete) {
			messagesToTranslate.remove(mess);
		}
		messagesToDelete.clear();
	}

	public void updateNotTranslatedWhiteBoardText(String newBuddyLauguage) {
		if (whiteBoardTextToTranslate != null
				&& newBuddyLauguage.equals(whiteBoardTextToTranslate.getFrom())) {
			String translatedString = translateWhiteBoardText(whiteBoardTextToTranslate);
			ITranslateMessage message = null;
			if (translatedString == null) {
				getModel().setWhiteBoardText(
						whiteBoardTextToTranslate.getText());
				message = new TranslateMultiChatMessage(
						whiteBoardTextToTranslate.getFrom(),
						whiteBoardTextToTranslate.getOriginalText(),
						whiteBoardTextToTranslate.getOriginalText(), false,
						false, false);
			} else {
				getModel().setWhiteBoardText(translatedString);
				message = new TranslateMultiChatMessage(
						whiteBoardTextToTranslate.getFrom(), translatedString,
						whiteBoardTextToTranslate.getOriginalText(), false,
						false, true);
			}
			translateWhiteBoardView.setOriginalWhiteBoardText(message);
			whiteBoardTextToTranslate = null;
		}

	}

	private void updateNotTranslatedQuestion(String newBuddyLauguage) {

		ArrayList<ITranslateQuestion> messagesToDelete = new ArrayList<ITranslateQuestion>();

		for (ITranslateQuestion mess : questionToTranslate) {

			if (newBuddyLauguage.equals(mess.getFrom())) {
				String translated = translateQuestion(mess);
				mess.setTranslatedText(translated);
				messagesToDelete.add(mess);
				getHandRaisingModel().getQuestion(mess.getIndex()).setText(
						translated);
			}

		}
		for (ITranslateQuestion mess : messagesToDelete) {
			questionToTranslate.remove(mess);
		}
		messagesToDelete.clear();
	}

	public String translateMessage(ITranslateMessage incoming) {
		//Translator tran = TranslatePlugin.getDefault().getTranslator();
		String translated = null;
		try {
			translated = translator.translate(incoming.getText(), incoming.getFrom(),
					getLanguageFromRosterBuddy(incoming.getFrom()));
		} catch (ApertiumXMLRPCClientException e) {
			messagesToTranslate.add(incoming);
			IBackend b = NetworkPlugin.getDefault().getRegistry()
					.getDefaultBackend();
			IMultiChatServiceActions chat = b.getMultiChatServiceAction();
			HashMap<String, String> param = new HashMap<String, String>();
			chat.SendExtensionProtocolMessage(LANGUAGE_REQUEST, param);
		} catch (Exception e) {
			UiPlugin.getUIHelper().showMessage(TRANSLATIONSERVICEERROR);
			//translateM2Mview.setTranslationOFF();
			translated = null;
		}
		return translated;
	}

	private String translateQuestion(ITranslateQuestion translatedQuestion) {
		//Translator tran = TranslatePlugin.getDefault().getTranslator();
		String translated = null;
		try {
			translated = translator.translate(translatedQuestion.getOriginalText(),
					translatedQuestion.getFrom(),
					getLanguageFromRosterBuddy(translatedQuestion.getFrom()));
		} catch (ApertiumXMLRPCClientException e) {
			questionToTranslate.add(translatedQuestion);
			IBackend b = NetworkPlugin.getDefault().getRegistry()
					.getDefaultBackend();
			IMultiChatServiceActions chat = b.getMultiChatServiceAction();
			HashMap<String, String> param = new HashMap<String, String>();
			chat.SendExtensionProtocolMessage(LANGUAGE_REQUEST, param);
		} catch (Exception e) {
			UiPlugin.getUIHelper().showMessage(TRANSLATIONSERVICEERROR);
			//translateM2MHandRaiseView.setTranslationOFF();
			translated = null;
		}

		return translated;
	}

	public String translateWhiteBoardText(ITranslateMessage message) {
		String who = message.getFrom();
		String text = message.getText();
		//Translator tran = TranslatePlugin.getDefault().getTranslator();
		String translated = null;

		try {
			translated = translator.translate(text, who,
					getLanguageFromRosterBuddy(who));

		} catch (ApertiumXMLRPCClientException e) {
			whiteBoardTextToTranslate = message;
			IBackend b = NetworkPlugin.getDefault().getRegistry()
					.getDefaultBackend();
			IMultiChatServiceActions chat = b.getMultiChatServiceAction();
			HashMap<String, String> param = new HashMap<String, String>();
			chat.SendExtensionProtocolMessage(LANGUAGE_REQUEST, param);
		} catch (Exception e) {
			UiPlugin.getUIHelper().showMessage(TRANSLATIONSERVICEERROR);
			//translateWhiteBoardView.setTranslationOFF();
			translated = null;
		}

		return translated;
	}

	public void onBackendEvent(IBackendEvent event) {
		super.onBackendEvent(event);

		if (event instanceof MultiChatExtensionProtocolEvent) {
			MultiChatExtensionProtocolEvent mcepe = (MultiChatExtensionProtocolEvent) event;

			if (manageLanguageRequest(mcepe))
				return;

			if (manageLanguageResponse(mcepe))
				return;

			if (manageLanguageUpdate(mcepe))
				return;

		}

	}

	protected boolean manageLanguageResponse(
			MultiChatExtensionProtocolEvent mcepe) {
		if (mcepe.getExtensionName().equals(LANGUAGE_RESPONSE)) {
			TranslateConfiguration c = TranslatePlugin.getDefault()
					.getConfiguration();
			String destCode = (String) mcepe.getExtensionParameter(LANGUAGE);
			String user = (String) mcepe.getExtensionParameter(LANGUAGE_USER);

			LanguagePair lp = new LanguagePair(c.getUserLanguage(),
					new Language(destCode));

			buddiesLenguages.put(mcepe.getFrom(), lp);
			updateNotTranslatedMessages(mcepe.getFrom());
			updateNotTranslatedWhiteBoardText(mcepe.getFrom());
			updateNotTranslatedQuestion(mcepe.getFrom());

			// The private message has a different "sender"
			buddiesLenguages.put(user, lp);
			updateNotTranslatedMessages(user);
			updateNotTranslatedWhiteBoardText(user);
			updateNotTranslatedQuestion(user);
			return true;
		}
		return false;
	}

	protected boolean manageLanguageRequest(
			MultiChatExtensionProtocolEvent mcepe) {

		if (mcepe.getExtensionName().equals(LANGUAGE_REQUEST)) {
			IBackend b = NetworkPlugin.getDefault().getRegistry()
					.getDefaultBackend();
			IMultiChatServiceActions chat = b.getMultiChatServiceAction();
			HashMap<String, String> param = new HashMap<String, String>();
			TranslateConfiguration c = TranslatePlugin.getDefault()
					.getConfiguration();
			param.put(LANGUAGE, c.getUserLanguage().getCode());
			param.put(LANGUAGE_USER, getLocalUserId());
			chat.SendExtensionProtocolMessage(LANGUAGE_RESPONSE, param);
			return true;
		}
		return false;
	}

	protected boolean manageWhiteBoardChanged(
			MultiChatExtensionProtocolEvent mcepe) {

		if (mcepe.getExtensionName().equals(WHITE_BOARD_CHANGED)) {
			String text = mcepe.getExtensionParameter(WHITE_BOARD_TEXT);
			String who = mcepe.getExtensionParameter(FROM);
			// Try to use a nickname (friendlier to see), if available
			// IParticipant p = getLocalUserOrParticipant( who );
			// if (p != null) {
			// who = p.getId();
			// }
			
			if (translateWhiteBoardView.isTranslationOn()) {
				boolean translated = !getLocalUserId().equals(who);
				ITranslateMessage incoming = new TranslateMultiChatMessage(who,
						text, text, false, false);
				String translatedString = translateWhiteBoardText(incoming);
				if (translatedString != null) {
					getModel().setWhiteBoardText(translatedString);
					translateWhiteBoardView
							.setOriginalWhiteBoardText(new TranslateMultiChatMessage(
									removeRoomAddress(who), translatedString,
									text, false, false, translated));
				}
			} else {
				getModel().setWhiteBoardText(text == null ? "" : text);
				translateWhiteBoardView.setOriginalWhiteBoardText(null);
			}
			return true;
		}

		return false;

	}

	protected boolean manageQuestionUpdate(MultiChatExtensionProtocolEvent mcepe) {

		if (mcepe.getExtensionName().equals(QUESTION_UPDATE)) {

			String from = (String) mcepe.getExtensionParameter(FROM);
			String text = (String) mcepe.getExtensionParameter(QUESTION);
			Integer id = new Integer(
					(String) mcepe.getExtensionParameter(QUESTION_ID));
			QuestionStatus status = QuestionStatus.valueOf((String) mcepe
					.getExtensionParameter(QUESTION_STATUS));

			final IParticipant p = getLocalUserOrParticipant(from);

			if (p == null)
				return true;

			IQuestion existing = getHandRaisingModel().getQuestion(id);
			// Unknown question? Just add it to the model. A question with
			// the same id as an
			// old one? Replace the previous one too!
			if (existing == null
					|| (existing != null && QuestionStatus.PENDING
							.equals(status))) {
				// This user is the moderator someone has asked to raise
				// hand
				final IQuestion q = new Question(getHandRaisingModel(), id,
						text, from, status);
				getHandRaisingModel().addQuestion(q);
				boolean isTranslated = !from.equals(getLocalUserId());
				ITranslateQuestion translatedQuestion = new TranslateQuestion(
						from, text, text, id.intValue(),isTranslated);
				if (translateM2MHandRaiseView.isTranslationON()) {
					String translated = translateQuestion(translatedQuestion);
					
					if (translatedQuestion != null) {
						translatedQuestion.setTranslatedText(translated);
						q.setText(translated);
					}
				}
				translateM2MHandRaiseView
						.addTranslatedQuestion(translatedQuestion);

			}

			// Otherwise rejected or approved questions are simply removed
			// from model:
			// the controller is responsible for performing additional
			// functions, like
			// freezing or unfreezing.
			else if (QuestionStatus.REJECTED.equals(status)) {
				notifyLocalSystemMessage(String
						.format("Moderator has rejected the following question from %s:\n\"%s\"",
								p.getNickName(), existing.getText()));
				getHandRaisingModel().removeQuestion(existing);

				translateM2MHandRaiseView
						.removeTranslateQuestion(id.intValue());

			} else if (QuestionStatus.APPROVED.equals(status)) {
				notifyLocalSystemMessage(String
						.format("Moderator has approved the following question from %s:\n\"%s\"",
								p.getNickName(), existing.getText()));
				getHandRaisingModel().removeQuestion(existing);
				translateM2MHandRaiseView
						.removeTranslateQuestion(id.intValue());
			}

			return true;
		}
		return false;

	}

	protected boolean manageMultiChatMessage(IBackendEvent event) {
		if (event instanceof MultiChatMessageEvent) {

			MultiChatMessageEvent mcme = (MultiChatMessageEvent) event;
			ITranslateMessage incoming = new TranslateMultiChatMessage(
					mcme.getFrom(), mcme.getMessage(), mcme.getMessage(),
					false, false);
			TranslatePlugin.getDefault().getHelper();
			if (translateM2Mview.isTranslationON()
					&& !isMyMessage(mcme.getFrom())) {
				manageTranslation(incoming, mcme);
//				String translatedString = translateMessage(incoming);
//				if (translatedString != null) {
//					ITranslateMessage translatedIncoming = new TranslateMultiChatMessage(
//							removeRoomAddress(mcme.getFrom()),
//							translatedString, mcme.getMessage(), false, false, true);
//					translateM2Mview.appendMessage(translatedIncoming);
//				}
			} else {
				translateM2Mview.appendMessage(new TranslateMultiChatMessage(
						removeRoomAddress(incoming.getFrom()), incoming
								.getText(), incoming.getText(), false, false, false));
			}
			return true;
		}
		return false;
	}
	@SwtAsyncExec
	private void manageTranslation(ITranslateMessage incoming, MultiChatMessageEvent mcme) {
		String translatedString = translateMessage(incoming);
		if (translatedString != null) {
			ITranslateMessage translatedIncoming = new TranslateMultiChatMessage(
					removeRoomAddress(mcme.getFrom()),
					translatedString, mcme.getMessage(), false, false, true);
			translateM2Mview.appendMessage(translatedIncoming);
		}
	}
	// Update the language
	protected boolean manageLanguageUpdate(MultiChatExtensionProtocolEvent mcepe) {
		if (mcepe.getExtensionName().equals(LANGUAGE_UPDATE)) {
			TranslateConfiguration c = TranslatePlugin.getDefault()
					.getConfiguration();
			String destCode = (String) mcepe.getExtensionParameter(LANGUAGE);
			String user = (String) mcepe.getExtensionParameter(LANGUAGE_USER);

			LanguagePair lp = new LanguagePair(c.getUserLanguage(),
					new Language(destCode));

			buddiesLenguages.put(mcepe.getFrom(), lp);

			// The private message has a different "sender"
			buddiesLenguages.put(user, lp);
			return true;
		}
		return false;

	}

	protected void notifyLocalSystemMessage(String message) {
		translateM2Mview.appendMessage(new TranslateMultiChatMessage(null,
				message.replace("\n", " "), message.replace("\n", " "), false,
				true));
	}

	@Override
	public void setTranslateM2MView(TranslateM2Mview view) {
		translateM2Mview = view;
	}

	@Override
	public void setTranslateWhiteBoardView(TranslateWhiteBoardView view) {
		translateWhiteBoardView = view;
	}

	@Override
	public void setTranslateHandRaiseView(TranslateM2MHandRaiseView view) {
		translateM2MHandRaiseView = view;
	}

	public void notifyWhiteBoardChanged(String text) {
		System.out.println(String.format("notifyWhiteBoardChange( %s )", text));
		HashMap<String, String> param = new HashMap<String, String>();
		param.put(WHITE_BOARD_TEXT, text);
		// param.put(FROM,
		// getContext().getRoom().concat("/").concat(getContext().getNickName()));
		param.put(FROM, getLocalUserId());
		getMultiChatServiceActions().SendExtensionProtocolMessage(
				WHITE_BOARD_CHANGED, param);
	}

	public void sendPrivateMessage(IParticipant p, String message) {
		String userId = p.getId();
		HashMap<String, String> param = new HashMap<String, String>();
		param.put(MESSAGE, message);
		param.put(TO, userId);
		// param.put(FROM,
		// getContext().getRoom().concat("/").concat(getContext().getNickName()));
		param.put(FROM, getLocalUserId());
		multiChatServiceActions.SendExtensionProtocolMessage(PRIVATE_MESSAGE,
				param);
		notifyLocalSystemMessage(String.format("[PM sent to %s] %s",
				p.getNickName(), message));
	}

	private boolean isMyMessage(String sender) {
		String from = sender;
		if (sender.startsWith(getContext().getRoom())) {
			from = removeRoomAddress(sender);
		}
		IParticipant p = getModel().getParticipant(from);
		if (p == null) {
			p = getModel().getParticipantByNickName(from);
		}
		IParticipant localPartecipant = getModel().getLocalUser();
		if (localPartecipant.getId().equals(p.getId())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void addMessageMTReceivedListener(
			IMTMessagegeReceivedListener listener) {
		messageMTReceivedListeners.add(listener);
	}

	@Override
	public void removeMessageMTReceivedListener(
			IMTMessagegeReceivedListener listener) {
		messageMTReceivedListeners.remove(listener);
	}

	@Override
	public void notifyLanguageUpdate() {
		IBackend b = NetworkPlugin.getDefault().getRegistry()
				.getDefaultBackend();
		IMultiChatServiceActions chat = b.getMultiChatServiceAction();
		HashMap<String, String> param = new HashMap<String, String>();
		TranslateConfiguration c = TranslatePlugin.getDefault()
				.getConfiguration();
		param.put(LANGUAGE, c.getUserLanguage().getCode());
		param.put(LANGUAGE_USER, getLocalUserId());
		chat.SendExtensionProtocolMessage(LANGUAGE_UPDATE, param);

	}

	protected boolean managePrivateMessage(MultiChatExtensionProtocolEvent mcepe) {
		if (mcepe.getExtensionName().equals(PRIVATE_MESSAGE)) {
			if (mcepe.getExtensionParameter(TO).equals(getLocalUserId())) {
				final String from = (String) mcepe.getExtensionParameter(FROM);
				final String text = (String) mcepe
						.getExtensionParameter(MESSAGE);
				final IParticipant p = getLocalUserOrParticipant(from);

				String nick;
				String who = from;
				if (p != null) {
					who = p.getId();
					nick = p.getNickName();
				} else {
					nick = removeRoomAddress(who);
				}

				ITranslateMessage incoming = new TranslateMultiChatMessage(who,
						text, text, true, false);

				if (translateM2Mview.isTranslationON()) {

					String translatedString = translateMessage(incoming);
					if (translatedString != null) {
						ITranslateMessage translatedIncoming = new TranslateMultiChatMessage(
								nick, translatedString, text, true, false, true);

						for (IMTMessagegeReceivedListener l : messageMTReceivedListeners) {
							l.privateMessageReceived(translatedIncoming);
						}
					}
				} else {

					ITranslateMessage translatedIncoming = new TranslateMultiChatMessage(
							nick, text, text, true, false, false);
					for (IMTMessagegeReceivedListener l : messageMTReceivedListeners) {
						l.privateMessageReceived(translatedIncoming);
					}
				}
			}
			return true;
		}
		return false;
	}

	protected boolean manageCurrentAgendaItem(
			MultiChatExtensionProtocolEvent mcepe) {
		translateM2Mview.printOriginalThread();
		return super.manageCurrentAgendaItem(mcepe);
	}
}
