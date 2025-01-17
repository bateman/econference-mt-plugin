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

import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.econference.internal.EConferenceHelper;
import it.uniba.di.cdg.xcore.m2m.IMultiChatManager.IMultiChatListener;
import it.uniba.di.cdg.xcore.m2m.events.InvitationEvent;
import it.uniba.di.cdg.xcore.m2m.service.Invitee;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.ui.IUIHelper;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import org.apertium.api.translate.actions.WizardMT;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class TranslateM2MHelper extends EConferenceHelper implements
		ITranslateM2MHelper {

	private ITranslateM2MManager managerMT;
	public TranslateM2MHelper(IUIHelper uihelper,
			INetworkBackendHelper backendHelper) {
		super(uihelper, backendHelper);

	}
	public TranslateM2MHelper() {
		
	}
	public ITranslateM2MManager open(EConferenceContext context,
			boolean autojoin) {
		ITranslateM2MManager manager = null;

		try {
			final IWorkbenchWindow window = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
			manager = new TranslateM2MManager();
			
			manager.setBackendHelper(NetworkPlugin.getDefault().getHelper());
			manager.setUihelper(UiPlugin.getUIHelper());
			manager.setWorkbenchWindow(window);
			manager.addListener(new IMultiChatListener() {
				private Point previousSize;

				public void open() {
					System.out.println("Resizing window!");
					Shell shell = window.getShell();
					Point size = shell.getSize();
					if (size.x < 800 || size.y < 600)
						shell.setSize(800, 600);
					previousSize = size;
				}

				public void closed() {
					Shell shell = window.getShell();
					shell.setSize(previousSize);
				}
			});
			//manager.open(context, true);
			manager.open(context, autojoin);
		} catch (Exception e) {
			e.printStackTrace();
			uihelper.showErrorMessage("Could not start eConference: "
					+ e.getMessage());

			// Close this perspective since it is unuseful ...
			uihelper.closeCurrentPerspective();
		}
		managerMT = manager;
		return manager;
	}

	public void dispose() {
		super.dispose();
		if (managerMT != null && managerMT.getService() != null) {
			NetworkPlugin.getDefault().getHelper().unregisterBackendListener(managerMT.getService());
		}
	}
	@Override
    public void openInviteWizard() {
        Display display = Display.getDefault();
        Shell shell = new Shell( display );
        WizardMT wizard = new WizardMT();
        // Instantiates the wizard container with the wizard and opens it
        WizardDialog dialog = new WizardDialog( shell, wizard );
        dialog.create();
        dialog.open();
        if (wizard.canSendInvitation()) {
            boolean autojoin = false;
            ITranslateM2MManager manager = open( wizard.getContext(), autojoin );
            for (Invitee i : wizard.getContext().getInvitees())
                manager.inviteNewParticipant( i.getId() );
            manager.close();
            dispose();
        }
    }
	public EConferenceContext askUserAcceptInvitation(InvitationEvent invitation) {
		// Skip invitations which do not interest us ...
		if (!MT_REASON.equals(invitation.getReason()))
			return null;

		IBackend backend = backendHelper.getRegistry().getBackend(
				invitation.getBackendId());

		String message = String
				.format("User %s has invited you to join to an eConference."
						+ "\nIf you want to accept, choose your display name and press Yes, otherwise press Cancel.",
						invitation.getInviter());
		String chosenNickNamer = uihelper.askFreeQuestion(
				"Invitation received", message, backend.getUserAccount()
						.getId());
		if (chosenNickNamer != null) {
			EConferenceContext context = new EConferenceContext(
					chosenNickNamer, "", invitation);
			return context;
		} else
			invitation.decline("No reason");

		return null;
	}
}
