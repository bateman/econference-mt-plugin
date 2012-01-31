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

import it.uniba.di.cdg.xcore.econference.ui.views.WhiteBoardView;

import org.apertium.api.translate.TranslatePlugin;
import org.apertium.api.translate.entity.ITranslateMessage;
import org.apertium.api.translate.infopopup.InfoPopUp;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class TranslateWhiteBoardView extends WhiteBoardView {

	public static final String ID = "org.apertium.api.translate.views.TranslateWhiteBoardView";
	private Action switchOnOffTranslation;
	private boolean translatingOn;
	private ITranslateMessage originalWhiteBoardMessage;
	private InfoPopUp infoPopUp;

	public TranslateWhiteBoardView() {
		super();
		originalWhiteBoardMessage = null;
	}

	protected void makeActions() {
		makeTranslateAction();
		setWhiteBoard();
		super.makeActions();

	}

	private void makeTranslateAction() {

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

	}

	private void switchAction() {
		if (translatingOn) {
		    switchOnOffTranslation.setId( "switch" );
			switchOnOffTranslation.setText("Switch Off Translation");
			switchOnOffTranslation
					.setToolTipText("Switch Off the real time Translation");
		} else {
			switchOnOffTranslation.setText("Switch On Translation");
			switchOnOffTranslation
					.setToolTipText("Switch On the real time Translation");

		}
	}

	public boolean isTranslationOn() {

		return translatingOn;
	}

	public void setOriginalWhiteBoardText(ITranslateMessage message) {
		originalWhiteBoardMessage = message;
	}

	private boolean isCursorCloseToWord() {

		Point point = whiteBoardText.getDisplay().getCursorLocation();
		Point viewLocation = whiteBoardText.toDisplay(whiteBoardText
				.getLocation());

		Point viewPoint = new Point(Math.abs(point.x - viewLocation.x),
				Math.abs(point.y - viewLocation.y));

		int offset = -1;
		try {
			offset = whiteBoardText.getOffsetAtLocation(viewPoint);
		} catch (Exception e) {
			offset = -1;
		}

		if (originalWhiteBoardMessage != null
				&& !originalWhiteBoardMessage.isNoTranslation() && offset > -1) {
			return true;
		} else {
			return false;
		}

	}

	private void openPopUp(final Shell shell) {

		closePopUp();
		if (isCursorCloseToWord()) {
			infoPopUp = new InfoPopUp(shell, shell.getDisplay()
					.getCursorLocation(), new Point(300, 250), new Point(600,
					500), originalWhiteBoardMessage);
			infoPopUp.open();
		}

	}

	private void closePopUp() {

		if (infoPopUp != null) {
			infoPopUp.close();
		}
	}

	private void setWhiteBoard() {
		whiteBoardText.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {

			}

			@Override
			public void mouseDown(MouseEvent e) {

				openPopUp(whiteBoardText.getShell());
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {

			}
		});

		whiteBoardText.addMouseMoveListener(new MouseMoveListener() {

			@Override
			public void mouseMove(MouseEvent e) {

				if (isCursorCloseToWord()) {
					Cursor helpCursor = new Cursor(whiteBoardText.getDisplay(),
							SWT.CURSOR_HELP);
					whiteBoardText.setCursor(helpCursor);
				} else {
					whiteBoardText.setCursor(new Cursor(whiteBoardText
							.getDisplay(), SWT.CURSOR_ARROW));
				}
			}
		});
	}

	public void setTranslationOFF() {

		translatingOn = false;
		switchAction();
	}

	// @Override
	// public boolean isSaveOnCloseNeeded() {
	// return false;
	// }
}
