package org.apertium.api.translate.actions;

import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.econference.ui.dialogs.InviteWizard;
import it.uniba.di.cdg.xcore.econference.util.MailFactory;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.ui.wizards.IConfigurationConstant;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.apertium.api.translate.model.MTContextWriter;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.ui.INewWizard;
import org.osgi.service.prefs.Preferences;

import com.google.gdata.GoogleDocManager;

public class WizardMT extends InviteWizard implements INewWizard {

	public WizardMT() {
		super();

	}

	public boolean performFinish() {
		lastOnePage.saveData();
		this.context = ((EConferenceContext) lastOnePage.getContext());

		Preferences preferences = new ConfigurationScope()
				.getNode(IConfigurationConstant.CONFIGURATION_NODE_QUALIFIER);
		Preferences pathPref = preferences.node(IConfigurationConstant.PATH);
		String preferredFilePath = pathPref.get(IConfigurationConstant.DIR, "");

		String filepath = genInfoPage.getFilePath();
		new File(filepath.substring(0,
				filepath.lastIndexOf(System.getProperty("file.separator"))))
				.mkdirs();

		// FIXME: in xmpp the FullName of Moderator(so its nick) is empty
		if (context.getModerator().getFullName().equals("")) {
			context.getModerator().setFullName(context.getModerator().getId());
		}
		// FIXME: In Skype the filepath doesn't like
		// "RoomName$ID_of_Moderator.ecx" but finish with "RoomName.ecx"
		// When a context is loaded the system try finding
		// "RoomName$ID_of_Moderator.ecx"
		// So We must choose if: 1. Remove the if-then above and change the
		// loading of .ecx file OR
		// 2. Let the if-then above as it'is now

		String backendID = NetworkPlugin.getDefault().getRegistry()
				.getDefaultBackendId();

		if (backendID.equals("it.uniba.di.cdg.skype.skypeBackend")) {
			if (filepath.endsWith(".ecx")) {
				String fixpath = filepath.substring(0, filepath.length() - 4);
				fixpath = fixpath.concat("$").concat(
						context.getModerator().getId());
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
			if (!filepath.startsWith(preferredFilePath)) {
				String filecopy = genInfoPage.computeFilePath();
				writer = new MTContextWriter(filecopy,
						(EConferenceContext) context);
				writer.serialize();
			}

			// the following code work only with Gmail accounts
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
					GoogleDocManager manager = new GoogleDocManager(user,
							passwd);
					String googleDocLink = manager.uploadFile(filepath, title,
							toRecipients);
					mailBody = MailFactory.createMailBody(context,
							googleDocLink);
					manager.sendMail(subject, toRecipients, mailBody, filepath);
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
