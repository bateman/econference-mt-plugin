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

import it.uniba.di.cdg.xcore.econference.EConferencePlugin;
import it.uniba.di.cdg.xcore.econference.ui.views.HandRaisingView;
import it.uniba.di.cdg.xcore.m2m.MultiChatPlugin;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import java.util.ArrayList;

import org.apertium.api.translate.TranslatePlugin;
import org.apertium.api.translate.entity.ITranslateQuestion;
import org.apertium.api.translate.infopopup.InfoPopUp;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class TranslateM2MHandRaiseView extends HandRaisingView implements
		ITranslateM2MHandRaiseView {
	public static String ID = "org.apertium.api.translate.views.TranslateM2MHandRaiseView";
	private ArrayList<ITranslateQuestion> translatedQuestions;
	private boolean translatingOn;
	private Action switchOnOffTranslation;
	private InfoPopUp infoPopUp;

	public TranslateM2MHandRaiseView() {
		translatedQuestions = new ArrayList<ITranslateQuestion>();
	}
	
	public void createPartControl(Composite parent) {
		makeTranslateAction();
		super.createPartControl(parent);
		questionViewer.getTable().addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {

			}

			@Override
			public void mouseDown(MouseEvent e) {
				closePopUp();
				openPopUp(questionViewer.getTable().getShell());

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {

			}
		});

		questionViewer.getTable().addMouseMoveListener(new MouseMoveListener() {

			@Override
			public void mouseMove(MouseEvent e) {
				Point point = questionViewer.getTable().getDisplay()
						.getCursorLocation();
				Point viewLocation = questionViewer.getTable().toDisplay(
						questionViewer.getTable().getLocation());
				Point viewPoint = new Point(Math.abs(point.x - viewLocation.x),
						Math.abs(point.y - viewLocation.y));
				TableItem item = questionViewer.getTable().getItem(viewPoint);
				
				if (item != null && !translatedQuestions.get(questionViewer.getTable().indexOf(item)).isNoTranslation()) {
					Cursor helpCursor = new Cursor(questionViewer.getTable()
							.getDisplay(), SWT.CURSOR_HELP);
					questionViewer.getTable().setCursor(helpCursor);
				} else {
					questionViewer.getTable().setCursor(
							new Cursor(questionViewer.getTable().getDisplay(),
									SWT.CURSOR_ARROW));
				}
			}
		});
		
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
			switchOnOffTranslation.setText("Switch Off Translation");
			switchOnOffTranslation
					.setToolTipText("Switch Off the real time Translation");
		} else {
			switchOnOffTranslation.setText("Switch On Translation");
			switchOnOffTranslation
					.setToolTipText("Switch On the real time Translation");
		}
	}

	@Override
	public void addTranslatedQuestion(ITranslateQuestion message) {
	
		translatedQuestions.add(message);
	}

	@Override
	public ITranslateQuestion getTranslatedQuestion(int index) {
		return translatedQuestions.get(index);
	}

	@Override
	public void removeTranslateQuestion(int index) {
		for (int i = 0; i < translatedQuestions.size(); i++) {
			if (translatedQuestions.get(i).getIndex() == index) {
				translatedQuestions.remove(i);
				return;
			}
		}
		
	}

	@Override
	public boolean isTranslationON() {
		return translatingOn;
	}

	public void setTranslationOFF() {
		translatingOn = false;
	}

	private void closePopUp() {

		if (infoPopUp != null) {
			infoPopUp.close();
		}
	}

	private void openPopUp(final Shell shell) {

		closePopUp();
		Point point = questionViewer.getTable().getDisplay()
				.getCursorLocation();
		Point viewLocation = questionViewer.getTable().toDisplay(
				questionViewer.getTable().getLocation());
		Point viewPoint = new Point(Math.abs(point.x - viewLocation.x),
				Math.abs(point.y - viewLocation.y));

		TableItem item = questionViewer.getTable().getItem(viewPoint);
		int index = questionViewer.getTable().indexOf(item);
		ITranslateQuestion question = getTranslatedQuestion(index);

		if (question != null && !question.isNoTranslation()) {
			infoPopUp = new InfoPopUp(shell, shell.getDisplay()
					.getCursorLocation(), question);
			infoPopUp.open();
		}
	}
	protected void makeActions() {
        enableDisableHRAction = new Action() {
            /* (non-Javadoc)
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
                if (getManager() == null)
                    return;
                
                setReadOnly( !isReadOnly() );
                getManager().notifyViewReadOnly( ID, isReadOnly() );
                updateActionStatusAccordingToStatus();
            }
        };
        raiseHandAction = new Action() {
            /* (non-Javadoc)
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
                IParticipant moderator = findModerator();
                
                if (moderator == null) {
                   UiPlugin.getUIHelper().showErrorMessage( "Sorry, no moderator seems to be online at the moment." );
                   return;
                }
                
                String question = UiPlugin.getUIHelper().askFreeQuestion(
                        "Please, type your question in the text box below and press OK.", 
                        "" );
                if (question != null)
                    getManager().notifyRaiseHand( moderator, question );
            }
        };
        raiseHandAction.setText( "Raise hand" );
        raiseHandAction
                .setToolTipText( "Click to raise hand and ask something to a moderator" );
        raiseHandAction.setImageDescriptor( MultiChatPlugin.imageDescriptorFromPlugin(
                EConferencePlugin.ID, "icons/action_raise_hand.png" ) );

        updateActionStatusAccordingToStatus();
    }
}
