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

package org.apertium.api.translate.infopopup;

import org.apertium.api.translate.entity.ITranslateMessage;
import org.apertium.api.translate.entity.ITranslateQuestion;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;

/*
 * Internal class used to implement the secondary popup.
 */
public class InfoPopUp extends PopupDialog {

	private final class MyMouseTrackListener implements MouseTrackListener {
		@Override
		public void mouseHover(MouseEvent e) {
			cambiaTipo();
		}

		@Override
		public void mouseExit(MouseEvent e) {

		}

		@Override
		public void mouseEnter(MouseEvent e) {

		}
	}

	private static final String EMPTY = "";
	private static final String MESSAGEINFOCLOSE = "Click on Top-Right Button to close";
	private static final String MESSAGETITLE = "Original Phrase";
	private static final int HEIGHT = 120;
	private static final int WIDTH = 250;
	/*
	 * The text control that displays the text.
	 */
	private Text text;

	/*
	 * The String shown in the popup.
	 */
	private String contents = EMPTY;
	private Rectangle bounds;
	private boolean RESIZE = false;
	private Composite Parent;
	private GridData gd;
	private MouseTrackListener MouseListener;
	private Control infoTextArea;
	private Control titleControl;
	/*
	 * Construct an info-popup with the specified parent.
	 */
	private Point MinDimension;
	private Point MaxDimension;

	
	public InfoPopUp(Shell parent, Point point, String testo) {
		// super(parent,PopupDialog.HOVER_SHELLSTYLE,false, false,true, false,
		// MESSAGETITLE, MESSAGGEINFORESIZE);
		super(parent, SWT.RESIZE, false, false, true, true, false,
				MESSAGETITLE, MESSAGEINFOCLOSE);

		// super(parent, SWT.RESIZE, false, false, true, false, MESSAGETITLE,
		// MESSAGEINFOCLOSE);
		// RESIZE=false;
		RESIZE = true;
		setContents(testo);
		bounds = new Rectangle(point.x, point.y, WIDTH, HEIGHT);
		MinDimension = new Point(WIDTH, HEIGHT);
		MaxDimension = new Point(750, 360);

	}

	public InfoPopUp(Shell parent, Point point, ITranslateMessage message) {
		// super(parent,PopupDialog.HOVER_SHELLSTYLE,false, false,true, false,
		// MESSAGETITLE, MESSAGGEINFORESIZE);
		super(parent, SWT.RESIZE, false, false, true, true, false, message
				.getFrom(), MESSAGEINFOCLOSE);
		// RESIZE=false;
		RESIZE = true;
		setContents(message.getOriginalText());
		bounds = new Rectangle(point.x, point.y, WIDTH, HEIGHT);
		MinDimension = new Point(WIDTH, HEIGHT);
		MaxDimension = new Point(750, 360);

	}

	public InfoPopUp(Shell parent, Point point, String nick, String message) {

		super(parent, SWT.RESIZE, false, false, true, true, false, nick,
				MESSAGEINFOCLOSE);
		// RESIZE=false;
		RESIZE = true;
		setContents(message);
		bounds = new Rectangle(point.x, point.y, WIDTH, HEIGHT);
		MinDimension = new Point(WIDTH, HEIGHT);
		MaxDimension = new Point(750, 360);
	}

	public InfoPopUp(Shell parent, Point point, Point Min_Dimension,
			Point Max_Dimension, ITranslateMessage message) {

		// super(parent,PopupDialog.HOVER_SHELLSTYLE,false, false,true, false,
		// MESSAGETITLE, MESSAGGEINFORESIZE);
		super(parent, SWT.RESIZE, false, false, true, true, false, message
				.getFrom(), MESSAGEINFOCLOSE);
		// RESIZE=false;
		RESIZE = true;
		setContents(message.getOriginalText());
		bounds = new Rectangle(point.x, point.y, WIDTH, HEIGHT);
		MinDimension = Min_Dimension;
		MaxDimension = Max_Dimension;

	}

	public InfoPopUp(Shell parent, Point point, ITranslateQuestion question) {

		// super(parent,PopupDialog.HOVER_SHELLSTYLE,false, false,true, false,
		// MESSAGETITLE, MESSAGGEINFORESIZE);
		super(parent, SWT.RESIZE, false, false, true, true, false, question
				.getFrom(), MESSAGEINFOCLOSE);
		// RESIZE=false;
		RESIZE = true;
		setContents(question.getOriginalText());
		bounds = new Rectangle(point.x, point.y, WIDTH, HEIGHT);
		MinDimension = new Point(WIDTH, HEIGHT);
		MaxDimension = new Point(750, 360);

	}

	protected Control createTitleMenuArea(Composite arg0) {
		Control ctrl = super.createTitleMenuArea(arg0);
		Composite composite = (Composite) ctrl;
		Control[] ctrls = composite.getChildren();

		ToolBar toolBar = (ToolBar) ctrls[1];
		// ToolItem[] toolItems = toolBar.getItems();
		// toolItems[0].setImage(JFaceResources.getImage(Dialog.));
		toolBar.setToolTipText("Close");
		return ctrl;
	}

	protected void fillDialogMenu(IMenuManager dialogMenu) {

		dialogMenu.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager arg0) {
				close();
				handleShellCloseEvent();
			}
		});

	}

	protected Control createTitleControl(Composite parent) {
		titleControl = super.createTitleControl(parent);

		return titleControl;
	}

	protected Control createInfoTextArea(Composite parent) {
		infoTextArea = super.createInfoTextArea(parent);

		return infoTextArea;

	}

	/*
	 * Create a text control for showing the info about a proposal.
	 */
	protected Control createDialogArea(Composite parent) {

		Parent = parent;
		if (RESIZE) {
			text = new Text(parent, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL
					| SWT.WRAP | SWT.NO_FOCUS);
		} else {
			text = new Text(parent, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP
					| SWT.NO_FOCUS);
		}

		// text.setBounds(bounds.x + 100, bounds.y + 100, bounds.width + 100,
		// bounds.height + 100);
		// Use the compact margins employed by PopupDialog.

		gd = new GridData(GridData.BEGINNING | GridData.FILL_BOTH);
		gd.horizontalIndent = PopupDialog.POPUP_HORIZONTALSPACING;
		gd.verticalIndent = PopupDialog.POPUP_VERTICALSPACING;
		text.setLayoutData(gd);
		text.setText(contents);

		if (!RESIZE) {
			MouseListener = new MyMouseTrackListener();
			text.addMouseTrackListener(MouseListener);

		}

		return text;
	}

	protected void adjustBounds() {

		Point pt;

		if (RESIZE) {
			pt = new Point(bounds.x, bounds.y);
		} else {
			pt = getShell().getDisplay().getCursorLocation();
		}

		getShell().setBounds(pt.x, pt.y - MinDimension.y, MinDimension.x,
				MinDimension.y);
		// getShell().setBounds(pt.x,pt.y -
		// bounds.height,bounds.width,bounds.height);
		growUp(pt);
	}

	private void growUp(Point pt) {
		// int height = bounds.height;
		// int width = bounds.width;

		int height = MinDimension.y;
		int width = MinDimension.x;

		int maxHeight = MaxDimension.y;
		int maxWidth = MaxDimension.x;

		while (height < maxHeight
				&& width < maxWidth
				&& text.getVerticalBar().getMaximum() != text.getVerticalBar()
						.getMinimum()) {
			height = height + 10;
			width = width + 10;
			getShell().setBounds(pt.x, pt.y - height, width, height);
		}

	}

	private void cambiaTipo() {
		RESIZE = true;
		setShellStyle(SWT.RESIZE);

		setInfoText(MESSAGEINFOCLOSE);

		text = new Text(Parent, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL
				| SWT.WRAP | SWT.NO_FOCUS);
		text.setLayoutData(gd);
		setContents(this.contents);

		close();
		open();

	}

	/*
	 * Set the text contents of the popup.
	 */
	void setContents(String newContents) {
		if (newContents == null) {
			newContents = EMPTY;
		}
		this.contents = newContents;
		if (text != null && !text.isDisposed()) {
			text.setText(contents);
		}
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public boolean getResize() {
		return RESIZE;
	}

	public boolean hasFocus() {
		return text.isFocusControl();
	}

}
