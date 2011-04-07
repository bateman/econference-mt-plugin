package org.apertium.api.translate.actions;

import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.econference.ui.dialogs.InviteWizard;
import it.uniba.di.cdg.xcore.econference.util.MailFactory;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.ui.wizards.IConfigurationConstant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.ParserConfigurationException;

import org.apertium.api.translate.model.MTContextWriter;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
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

			 Preferences gmailPref = preferences.node(IConfigurationConstant.GMAIL);
	    		Preferences smtpPref = preferences.node(IConfigurationConstant.SMTP);
	            
	            if (canSendInvitation()) {
	            	
	            	String gUser = gmailPref.get(IConfigurationConstant.USERNAME, "");
	                String gPasswd = gmailPref.get(IConfigurationConstant.PASSWORD, "");
	            	String server = smtpPref.get(IConfigurationConstant.SERVER, "");
	            	int port = smtpPref.getInt(IConfigurationConstant.PORT, -1);
	                final String user = smtpPref.get(IConfigurationConstant.USERNAME, "");
	                final String passwd = smtpPref.get(IConfigurationConstant.PASSWORD, "");
	                String title = genInfoPage.getConferenceName() + ".ecx";
	                String subject = context.getRoom().split( "@" )[0];
	                Vector<String> toRecipients = lastOnePage.recipients();
	                String mailBody;
	                String googleDocLink = "";

	                if (!toRecipients.isEmpty()) {
	                	if (getCanCreateGoogleDoc()){
	                		try{
		                		GoogleDocManager manager = new GoogleDocManager( gUser, gPasswd );
		                    	googleDocLink = manager.uploadFile( filepath, title, toRecipients );
	                		}catch (Exception e){
	                			MessageBox mb = new MessageBox(getShell(), SWT.ERROR | SWT.OK);
	                            mb.setText("Google Docs Error");
	                            mb.setMessage("An error has occurred in creating Google document\n"
	                            		+ "Please review the GMail settings from the menu Options > Configuration Wizard");
	                            mb.open();
	                		}
	                	}
	                	
	                    mailBody = MailFactory.createMailBody( context, googleDocLink );

	                    Properties props = System.getProperties();
	                    props.put("mail.smtp.host", server);
	                    props.put("mail.smtp.user", user);
	                    props.put("mail.smtp.password", passwd);
	                    props.put("mail.smtp.port", port);
	                    props.put("mail.smtp.auth", "true");
	                    if(smtpPref.get(IConfigurationConstant.SECURE, "").compareTo("SSL/TLS") == 0){
	                        props.put("mail.smtp.ssl.enable", "true");
	                    }
	                    if(smtpPref.get(IConfigurationConstant.SECURE, "").compareTo("STARTTLS") == 0){
	                        props.put("mail.smtp.starttls.enable", "true");
	                    }

	                    Session session = Session.getDefaultInstance(props, null);
	                    
	                    MimeMessage message = new MimeMessage(session);
	                    message.setFrom(new InternetAddress(user));

	                    for( int i=0; i < toRecipients.size(); i++ ){
	                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toRecipients.get(i)));
	                    }
	                    message.setSubject(subject);
	                    
	                    MimeBodyPart body = new MimeBodyPart();
	                    body.setText(mailBody);

	                    MimeBodyPart attachment = new MimeBodyPart();
	                    attachment.attachFile(filepath);
	                    attachment.setFileName(new File(filepath).getName().replace(' ','_'));

	                    Multipart multipart = new MimeMultipart();
	                    multipart.addBodyPart(body);
	                    multipart.addBodyPart(attachment);
	                    message.setContent(multipart);
	                    
	                    Transport transport = session.getTransport("smtp");
	                    transport.connect(server, port, user, passwd);
	                    transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
	                    transport.close();
	                }
	            }

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (NoSuchProviderException e) {
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR | SWT.OK);
            mb.setText("Emails not sent");
            mb.setMessage("Emails were not sent correctly to recipients.\n"
            		+ "Please review the SMTP settings from the menu Options > Configuration Wizard");
            mb.open();
		} catch (MessagingException e) {
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR | SWT.OK);
            mb.setText("Emails not sent");
            mb.setMessage("Emails were not sent correctly to recipients.\n"
            		+ "Please review the SMTP settings from the menu Options > Configuration Wizard");
            mb.open();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;

	}

}
