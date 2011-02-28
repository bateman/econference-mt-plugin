package org.apertium.api.translate.actions;

import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.econference.ui.dialogs.InviteWizard;
import it.uniba.di.cdg.xcore.econference.util.MailFactory;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;

import java.io.FileNotFoundException;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.apertium.api.translate.model.MTContextWriter;
import org.eclipse.ui.INewWizard;

import com.google.gdata.GoogleDocManager;

public class WizardMT extends InviteWizard implements INewWizard {

	public WizardMT() {
		super();

	}

	public boolean performFinish() {
		lastOnePage.saveData();
		this.context = ((EConferenceContext) lastOnePage.getContext());
		String filepath = genInfoPage.getFilePath();
		String backendID = NetworkPlugin.getDefault().getRegistry()
		.getDefaultBackendId();
//		if (filepath.equals(DEFAULT_FILE_PATH.concat("econference.ecx"))) {
//			filepath = DEFAULT_FILE_PATH
//					.concat(genInfoPage.getConferenceName()).concat(".ecx");
//		}
		
		//FIXME: in xmpp the FullName of Moderator(so its nick) is empty
		if (context.getModerator().getFullName().equals("")) {
			context.getModerator().setFullName(context.getModerator().getId());
		}
		//FIXME: In Skype the filepath doesn't like "RoomName$ID_of_Moderator.ecx" but finish with "RoomName.ecx"
		// When a context is loaded the system try finding "RoomName$ID_of_Moderator.ecx" 
		//So We must choose if: 1. Remove the if-then above and change the loading of .ecx file OR
		//                      2. Let the if-then above as it'is now 
		
		if (backendID.equals("it.uniba.di.cdg.skype.skypeBackend")) {
			if (filepath.endsWith(".ecx")) {
				String fixpath = filepath.substring(0, filepath.length() - 4);
				fixpath = fixpath.concat("$").concat(context.getModerator().getId());
				fixpath = fixpath.concat(".ecx");
				filepath = fixpath;
			}
		}
		try {

			MTContextWriter writer = new MTContextWriter(filepath,
					(EConferenceContext) context);

			writer.serialize();
			// if we save the ecx file not in the default location
			// we store a copy there
			if (!filepath.startsWith(DEFAULT_FILE_PATH)) {
				String filename = genInfoPage.getConferenceName();
				filename += ".ecx";
				writer = new MTContextWriter(DEFAULT_FILE_PATH + filename,
						(EConferenceContext) context);
				writer.serialize();
			}

			

			// the following code work only with Jabber/Xmpp protocol
			if (canSendInvitation()
					&& backendID.equals("it.uniba.di.cdg.jabber.jabberBackend")) {

				String user = NetworkPlugin.getDefault().getRegistry()
						.getDefaultBackend().getUserId();
				String passwd = NetworkPlugin.getDefault().getRegistry()
						.getDefaultBackend().getUserAccount().getPassword();
				String title = genInfoPage.getConferenceName() + ".ecx";
				String subject = context.getRoom().split("@")[0];
				Vector<String> toRecipients = lastOnePage.recipients();
				String mailBody;

				if (!toRecipients.isEmpty()) {
					try {
					GoogleDocManager manager = new GoogleDocManager(user,
							passwd);
					String googleDocLink = manager.uploadFile(filepath, title,
							toRecipients);
					mailBody = MailFactory.createMailBody(context,
							googleDocLink);

					manager.sendMail(subject, toRecipients, mailBody, filepath);
					} catch (Exception e) {
						System.out.println("WizardMT.PerformFinisch(): Impossible send a GoogleMail");
					}
				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return true;

	}

}
