/**
 * This file is part of the eConference project and it is distributed under the 

 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2006 - 2012 Collaborative Development Group - Dipartimento di Informatica, 
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

package org.apertium.api.translate.extensionpoint.contribution.statusbar;

import it.uniba.di.cdg.aspects.SwtAsyncExec;
import it.uniba.di.cdg.collaborativeworkbench.ui.extensionpoint.definition.statusbar.IStatusBarExtensionPoint;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;

import org.apertium.api.translate.Flags;
import org.apertium.api.translate.TranslatePlugin;
import org.apertium.api.translate.actions.TranslateConfiguration;
import org.apertium.api.translate.actions.TranslateConfigurationAction;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;

@SuppressWarnings("restriction")
public class MTStatusBarExtensionPoint extends ContributionItem implements
		IStatusBarExtensionPoint, MouseListener
//		,IChangeNativeLanguageListener 
		{
	public static String ID = "org.apertium.api.translate.extensionpoint.contribution.statusbar.MTStatusBarExtensionPoint";

	@Override
	public String getId() {		
		return ID;
	}

	@Override
	public void fill(Composite parent) {

		super.fill(parent);
		if (NetworkPlugin.getDefault().getHelper().getOnlineBackends().size() > 0) {
			// online

			TranslateConfiguration configuration = TranslatePlugin.getDefault()
					.getConfiguration();

			System.out.println("User Language: "
					+ configuration.getUserLanguage());

			Image imFlag = Flags.getFlag(configuration
					.getUserLanguage());
			String txtFlag = configuration.getUserLanguage().getName();

			CLabel text = new CLabel(parent, SWT.NONE);
			CLabel bulb = new CLabel(parent, SWT.NONE);
			bulb.setToolTipText("Native language: " + txtFlag);
			text.setToolTipText(bulb.getToolTipText());
			bulb.setImage(imFlag);
			text.setText(txtFlag);

			text.addMouseListener(this);
			bulb.addMouseListener(this);
		}

	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		

	}

	@Override
	public void mouseDown(MouseEvent e) {
		TranslateConfigurationAction tc = new TranslateConfigurationAction();
		tc.run(null);

	}

	@Override
	public void mouseUp(MouseEvent e) {
		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IContributionItem#update()
	 */
	@Override
	@SwtAsyncExec
	public void update() {
		if (!isVisible()) {
			setVisible(true);
		}
		IContributionManager contributionManager = getParent();

		if (contributionManager != null)
			contributionManager.update(true);

		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();

		try {
			if (workbenchWindow instanceof WorkbenchWindow) {
				((WorkbenchWindow) workbenchWindow).getStatusLineManager()
						.update(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

//	@SwtAsyncExec
//	private void updateStatusBar() {
//		update(); 
//	}
//
//	@Override
//	public void onChangeNativeLanguageListener(ChangeNativeLanguageEvent event) {
//		System.out.println("OnChangeNativeLanguageListener");
//		this.updateStatusBar();
//
//	}

}
