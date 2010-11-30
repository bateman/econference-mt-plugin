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

import it.uniba.di.cdg.xcore.ui.views.TalkView;

import java.util.ArrayList;

import org.apertium.api.translate.TranslatePlugin;
import org.apertium.api.translate.infopopup.InfoPopUp;
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

public class TranslateOne2OneView extends TalkView {
	public static final String ID = "org.apertium.api.translate.TranslateOne2OneView";
	protected IAction switchOnOffTranslation;
	protected boolean translatingOn;
	private InfoPopUp infoPopUp;
	private ArrayList<String> unTranslatedString = new ArrayList<String>();

	public TranslateOne2OneView() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {

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

	private boolean isCursorCloseToWord() {

		Point point = messageBoardText.getDisplay().getCursorLocation();
		Point viewLocation = messageBoardText.toDisplay(messageBoardText
				.getLocation());
		Point viewPoint = new Point(Math.abs(point.x - viewLocation.x),
				Math.abs(point.y - viewLocation.y));

		if (messageBoardText.getText() != ""
				&& !getUntranslatedString(viewPoint).equals("")) {
			return true;
		} else {
			return false;
		}

	}

	private void openPopUp(final Shell shell) {
		closePopUp();
		String nick = getTitle();
		if (nick.lastIndexOf("@") > -1) {
			nick = nick.substring(0, nick.lastIndexOf("@"));
		}

		Point point = messageBoardText.getDisplay().getCursorLocation();
		Point viewLocation = messageBoardText.toDisplay(messageBoardText
				.getLocation());
		Point viewPoint = new Point(Math.abs(point.x - viewLocation.x),
				Math.abs(point.y - viewLocation.y));

		String message = getUntranslatedString(viewPoint);

		if (message != "") {
			infoPopUp = new InfoPopUp(shell, point, nick, message);
			infoPopUp.open();
		}

	}

	private void closePopUp() {
		if (infoPopUp != null) {
			infoPopUp.close();
		}
	}

	private String getUntranslatedString(Point point) {
		String originalString = "";

		int index = messageBoardText.getLineIndex(point.y);

		if ((index < unTranslatedString.size() && index >= 0)
				&& ((index < messageBoardText.getLineCount() - 1) || ((index == messageBoardText
						.getLineCount() - 1) && point.y <= messageBoardText
						.getLineHeight()))) {

			originalString = unTranslatedString.get(index);
		}
		return originalString;
	}

	public void appendMessage(String whoName, String originalMessage,
			String translatedMessage) {
		String currentMessage = whoName + " > " + translatedMessage;
		unTranslatedString.add(originalMessage);
		super.appendMessage(currentMessage);
	}

	public void appendMessage(String text) {
		unTranslatedString.add("");
		super.appendMessage(text);
	}

	private void setMessageBoard() {
		messageBoardText.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
				openPopUp(messageBoardText.getShell());
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		// Add a MouseMoveListener to manage the closing
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

	public void setTranslationOFF() {
		translatingOn = false;
		switchAction();
	}

	public boolean isTranslationON() {
		return translatingOn;
	}
}
