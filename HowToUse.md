#summary How to use the eConference MT plugin.
#labels Howto,Phase-Support,Featured
<wiki:toc max_depth="3" />
= Introduction =
This tutorial will show you how to use the eConferenceMT, according to the role you will play during the conferencing session.

== Download ==
You can get the latest build of eConferenceMT from the [https://code.google.com/p/econference-mt-plugin/downloads/list Download] page.

== The eConferenceMT UI ==
This perspective contains following views (as shown in Figure 1a):

<p align="center"><img src="http://econference-mt-plugin.googlecode.com/svn/wiki/img/gui mt.png" width="700px" /></p>
<p align="center">*Figure 1a. The MT Plug-in*  </p>

  * The *Agenda* view shows the item to be discussed. It is managed by the moderator.
    * The *Messageboard* view shows the messages exchanged.
      * The *Who's on*  view lists the parcitipants currently online.
        * The *Decision place* view shows the meeting minute per item. It is managed by the scribe.
	  * The *Hand raising* view enables the social protocol for turn taking.

	  <font size="3" color="red"><b>Note that all the content of the views that show this icon <img src="http://econference-mt-plugin.googlecode.com/svn/wiki/img/logo.png" width="16px" /> is automatically machine-translated.</b></font>

	  These views (i.e. Messageboard, Decisions place, and Hand raising), automatically show translated messages. However, each participant can still inspect the original (i.e. non translated) message by simply moving the mouse cursor on a sentence and clicking on it (see the pictures below). A popup will show up with the original sentence. To close the popup, click again.

	  <p align="center"><img style="border:1px" src="http://econference-mt-plugin.googlecode.com/svn/wiki/img/mt1.png" /><img style="border:1px" src="http://econference-mt-plugin.googlecode.com/svn/wiki/img/mt2.png" /><img style="border:1px" src="http://econference-mt-plugin.googlecode.com/svn/wiki/img/mt3.png" /></p>
	  <p align="center">*Figure 1b. The MT Plug-in in action*  </p>

	  The rest of this how to will further detail how each view works.

	  == Running the tool ==
	  After downloading the tool, extract it and double click on the .exe in the folder. If you already know what network to use (e.g. Skype), then launch the batch file named _skype-startup.bat_. 


	  == Connecting ==
	  Click on the Plug icon in the top-left corner of the GUI. 

	  *Option 1.* If you are using the *Skype* network, *remember to have the Skype client installed and running before launching eConference.*
	  The first time you run eConference you will have to allow the tool to connect to attach to the Skype network through the Skype client. You will se the Skype icon glowing and a notification like the one in Figure 2a appear in the top of the client window. Press allow to let eConference connect.


	  <p align="center"><img src="http://econference-mt-plugin.googlecode.com/svn/wiki/img/skypeaccess.png" width="680px" /></p>  
	  <p align="center">*Figure 2a. Allow eConference to connect to Skype (first run only)* </p>  

	  *Option 2.* When using *XMPP/GMail* accounts, instead, a Login Dialog is displayed (see Figure  2b) fill in only the email and password fields, as server settings are automatically entered for you. Check the checkbox and your profile will be stored and available when you reconnect.


	  <p align="center">http://econference-planning-poker-plugin.googlecode.com/svn/wiki/img/Connessione.PNG</p>  
	  <p align="center">*Figure 2b. The login Dialog* </p>  


	  To check whether you are connected, see if the light bulb in the bottom-right corner is enabled and move the mouse over it: the tooltip should display all the connection details.

	  If you don't see anyone in the roster, then it means you have no buddies. ATM, you can add contacts only when using XMPP/GMail network. This feature does not work with Skype yet (see [http://code.google.com/p/econference4/issues/detail?id=21 issue 21]).

	  == Configuring the MT plugin ==
	  Once you have successfully connected press the MT icon in the toolbar, as shown in the picture below. This will open a popup where you can pick what MT service to use and your native language.

	  As for the service to use, the default is Google. Leave it unchanged. As for the language, we will ask you to interact either using you native languange (i.e. Portuguese, Italian) or using English instead. You'll be instructed what to do before the experiment starts.

	  <p align="center"><img src="http://econference-mt-plugin.googlecode.com/svn/wiki/img/mt config.png" /></p>  
	  <p align="center">*Figure 2c. The MT plugin configuation popup.* </p>  

	  == Organizing an eConferenceMT event ==
	  This section will show you how to create a machine-translated eConference meeting only if you are the *moderator* elected. 

	  To organize the event, start the wizard as shown in Figure 3.

	  <p align="center">http://econference-mt-plugin.googlecode.com/svn/wiki/img/startwizard.png</p>  
	  <p align="center">*Figure  3. Start the organization of a MT event*</p>  

	  === When using Skype ===
	  In the first page of the wizard (see Figure 4) you will have to enter the moderator id (Conference chair, in this case _cohn_), which  99.9% of the time will be your account id.

	  Then enter the conference name. Please note that when using Skype, _the conference name must be in this format: *conference-name$moderator-id*._ In the example, the full conference name is _my3ptest$cohn_. Feel free to specify the topic or leave this field blank.

	  Afterwards, choose the schedule and where to store file. Choose the location (the default is _Folder-Profile\.econference\_. You can choose the location, but you *cannot change the filename, nor the extension* (e.g. in the example, the file name must stay _my3ptest$cohn.ecx_).

	  <p align="center"><img src="http://econference-planning-poker-plugin.googlecode.com/svn/wiki/img/startwizard-geninfo.png" width="660px" /></p>  
	  <p align="center">*Figure  4. Wizard step 1: general info*</p>  

	  In the second step, you can select participants from your contact list. Both online and offile buddies are shown (see Figure 5).

	  <p align="center"><img src="http://econference-planning-poker-plugin.googlecode.com/svn/wiki/img/startwizard-invite1.png" width="660px" /></p>  
	  <p align="center">*Figure  5. Wizard step 2: select participant from contacts*</p>  

	  Only when using skype you can invite to the meeting people whose contact id is known, even though they are not in your buddy list, in this case the contact _Fabio_ (see Figure 6).

	  Unfortunately, when using Skype, you can't use eConference to send invitation emails. You will have to manually send such emails to participants, attaching the ecx file that will be stored once you click finish. However, you are still able to invite them by sending an IM.

	  <p align="center"><img src="http://econference-planning-poker-plugin.googlecode.com/svn/wiki/img/startwizard-invite2.png" width="660px" /></p>  
	  <p align="center">*Figure  6. Wizard step 3: add participant not in your contact list*</p> 

	  === When using XMPP/GMail ===
	  TBD

	  == Joining an eConferenceMT event ==
	  This section will show you how to join a MT eConference when you are either the moderator or a participant. 

	  === The Moderator point of view ===
	  You are going to role-play the moderator during a MT conferencing session. You have a couple of options to create and join the session. 

	  *Option 1.* If you created the meeting using the Wizard, then you should see the event displayed in the Stored Events view of the tool. In the example below (see Figure 7), you see highlighted in event _test_. Double click on it, and a dialogue will appear. 

	  *Option 2.* If you are the moderator, but you didn't create the event thru the wizard yourself, the you should have received the config file .ecx somehow. Therefore, click on the "Open configuration file" icon in the toolbar (see Figure 7), then browse and select the file. Alternatively, go to the menu "File > eConference > Load configuration". Click OK and a dialogue will appear.

	  <p align="center"><img src="http://econference-mt-plugin.googlecode.com/svn/wiki/img/load join mt event.png" /></p>  
	  <p align="center">*Figure  7. Joining the MT session as a moderator.*</p> 

	  In the dialogue, choose the name that will be displayed to other participants (e.g. _Mike Cohn_). The checkbox tells the tool to send invitations to all the participants registered in the configuration. Be sure to leave it checked, otherwise you will have to invite each of them manually. Press ok and you will see the GUI changing and showing the new perspective for the structured conversation.

	  *Please, keep in mind that the room gets created when the moderator enters it for the first time. So, be sure to be online _before_ any other participant joins*.

	  <wiki:comment>
	  <p align="center">http://econference-planning-poker-plugin.googlecode.com/svn/wiki/img/LoadConf.PNG</p>  
	  <p align="center">*Figure  3. Loading the .ecx file*</p>  


	  <p align="center">http://econference-planning-poker-plugin.googlecode.com/svn/wiki/img/SetRole.PNG</p>
	  <p align="center">*Figure  4. Moderator is about to create and join the event*</p> 
	  </wiki:comment>


	  === The participant point of view ===
	  If you are a participant, you have a couple of options to join the event.

	  *Option 1*. Just be sure to connect and see if you have received an online invitation as an IM: it will show up as an item in the Events pane (see Figure 8). This invitation will be displayed in this pane, and stored until you delete it. When you select an invitation, the details about the event will be show in the lower part of the Events pane. Typically, if you are using GTalk, it is not a problem if the IM invitation was sent to you while you were offline. The server will store it for you and send it as soon as you get connected. Double click on the invitation received and a dialogue will appear. Instead, it is a problem if you are using Skype, so be sure to be online when the moderator invites you, otherwise you will have to join using the next method.

	  <p align="center"><img src="http://econference-mt-plugin.googlecode.com/svn/wiki/img/onlineinvitation.png" width="660px" /> </p>
	  <p align="center">*Figure  8. The participant enters the MT eConfernece using the online invitation*</p>

	  *Option 2.* Other than using online invitations, you can enter an eConference session loading the configuration file, provided that the moderator has sent the .ecx file to you. Download the attachment and store it somewhere. Then, press on the "Open configuration file" icon in the toolbar (see Figure 8), then browse and select the file. Alternatively, go to the menu "File > eConference > Load configuration". Click OK and a dialogue will appear.


	  Now to join an eConference event, select your name and press Yes as pictured below (Figure 9), and you will see the GUI change, showing the new perspective for the conferencing session. 


	  <p align="center">http://econference-mt-plugin.googlecode.com/svn/wiki/img/invited_part.png 
	  <p align="center">*Figure  9. The participant select username and enters the MT eConference.*</p>


	  == How to participate in a Machine-Translated eConference Event ==

	  This section will briefly show what the views are meant for, and what action each participant can do on them.

	  Once you have entered the event, you will see the online participants in the Who's on view (see Figure 10 below). The blue icon indicates the Moderator. All the other participants are shown in green. 

	  <p align="center">http://econference4.googlecode.com/svn/wiki/img/whoson.png</p>
	  <p align="center">*Figure  10a. Who's on list, showing online participants and scribe privileges*</p>   

	  Before the conversation about item starts, the Moderator usually grants him-/herself or someone else the Scribe right. To accomplish this by right-clicking the selected stakeholder and select the 'Grant scribe right' item from the popup menu (see figures below). Note that the scribe can be changed on the fly and that there can be multiple scribes at the same time. When a user receives the scribe privilege, a pencil will appear on the his/her icon.


	  <p align="center">
	  http://econference4.googlecode.com/svn/wiki/img/select_scribe.png 
	  <br />*Figure  10b. Selecting the scribe*  
	  </p>

	  <br />

	  <p align="center">
	  http://econference4.googlecode.com/svn/wiki/img/scribe.png 
	  <br />*Figure  10c. Tester 2 is the scribe* 
	  </p>


	  It would be good to select the scribe before the conversation takes place. So the moderator should ask the stakeholders who wants to be the scribe. Also note that the scribe can be changed on the fly and that there can be multiple scribe at the same time.

	  Also note that the moderator can even revoke the rights to contribute messages <wiki:comment>and to vote</wiki:comment>. As the moderator, be _*extremely careful*_ when using these features.


	  ==== What does being a Scribe mean? ====
	  If you have been selected as a Scribe, then you will see the refresh icon in the Decisions Place view enabled now (Figure 11). When the discussion takes place, you will have to sum-up all the decisions that have been taken about the task at hand, as it is being discussed. Thus, you will edit an ongoing minute of the event. Be sure to press the refresh icon at every significant update, so that to show what you have written to the other stakeholders.


	  <p align="center">http://econference.googlecode.com/svn/wiki/img/decisions_place.png  </p>

	  <p align="center">*Figure  11. Decision places view being edited* </p>

	  === The Structured Conversation ===
	  As soon as you enter the event the eConference is still stopped, but you can freely chat with the participants who are currently online while you wait for the others to join. When everyone is online, the moderator decides that it is time to start to work and presses the start button in the Agenda.

	  This view basically shows the list of the items that the stakeholders have to discuss about (see Figure  12). This view is read only for all of the participants. Instead the moderator uses it to:

	  *Append new item to discuss about

	  ->Clicking on the icon in the right-top corner, a popup is displayed so that the moderator can enter the new item (see Figure  13). All the changes are propagate to all the online participants

	  *Start / stop the conference

	  ->After pressing the start button, the moderator can select the current item from the list. This means that the discussion must be about the current item only.


	  <p align="center">
	  http://econference4.googlecode.com/svn/wiki/img/agenda.png 
	  <br />*Figure  12. The agenda view*  
	  </p>
	  <br />
	  <p align="center">
	  http://econference4.googlecode.com/svn/wiki/img/append_q.png 
	  <br />*Figure  13. Appending a new question*   
	  </p>
	    
	    As soon as the moderator selects the first item in the Agenda, say Item 1, you will see the talk view tab renamed from 'Free talk' into 'Item 1'. Also, all the previous messages will be hidden, just to show only the newly-entered statements related to the current item (see Figure  14). 




	    <p align="center">
	    http://econference4.googlecode.com/svn/wiki/img/threaded1.png 
	    <br />*Figure  14. Conversation about "Item 1"*  
	    </p>

	    Suppose you have reached a consensus about the current item, and the Scribe has summed up the decisions taken: It is time to move the another item, say Item 2. The Moderator selects it in the agenda. All the statements about Item 1 are hidden, and the talk view is again clean, ready to list only the statements related to Item 2 (see below).



	    <p align="center">
	    http://econference4.googlecode.com/svn/wiki/img/threaded2.png 
	    <br />*Figure  15. Conversation about "Item 2"*   
	    </p>


	    Yeah, but suppose that someone reads what the scribe has written is in the Decisions Place and realizes he/she is not agree? Can we move back??? Sure, you can. 
	    Furthermore, as soon as you move back to an item that has been previously discussed, all the messages already sent will appear in the talk view again. The dashes indicates the beginning of a new session in the conversation about the Item 1 (see Figure  16).  


	    <p align="center">
	    http://econference4.googlecode.com/svn/wiki/img/threaded1_1.png 
	    <br />*Figure  16.  Sessions in the conversation about "Item 1"*  
	    </p>

	    The discussion goes on for each single item, until all have been discussed and consensus on them reached. Then the Moderator presses the Stop button and you are back in the free talk view to say _ciao_ to the stakeholders.

	    === Hand raising: a social protocol ===
	    The hand raising feature is not fundamental, but it can be useful if you see that too many stakeholders are talking simultaneously and things are getting a bit messy.

	    Thus the moderator can press this icon   in the Hand Raising View, and tells the stakeholders to raise hand to start a turn-taking conversation. Thus, as in real life, you raise your hand to ask someone a question or simply asking the right to speak. This is a social protocol however, as you are always allowed to jump in whenever you want. Interrupt people, but do it at your own risk.

	    Participants request the right to speak by pressing the following icon   in the view. Note that it is disabled until the moderator decides to enable the use of this protocol.

	    Fill the popup with a brief description of what you want to ask for instance, and press Ok.

	    <p align="center">
	    http://econference4.googlecode.com/svn/wiki/img/ask_q.png 
	    <br />*Figure  17. Asking a question (Participant role)*  
	    </p>

	    The questions will appear in the view and the moderator has the power to approve question or reject them right-clicking on each of them (see below). Question approved will show in the Talk view.

	    The moderator should avoid to reject questions as much as he/she can, or at least should provide a motivation and avoid to frustrate the other stakeholders.


	    <p align="center">
	    http://econference4.googlecode.com/svn/wiki/img/approve_reject_q.png 
	    <br />*Figure  18.  Moderator can approve/reject questions* 
	    </p>


	    === Save your logs ===
	    The event is over. As soon as you try to disconnect, close the perspective or even shut down the application you will be prompted to save both the conversation logs and the Decisions place content (see Figure  19). Each log will be store in a flat text file. Be sure to say yes, as you will need them to complete your task later.

	    <p align="center">
	    http://econference4.googlecode.com/svn/wiki/img/savelog.png 
	    <br />*Figure  19. Save your logs*
	    </p>

	    == Troubleshooting ==
	    === Accidental disconnections ===
	    If you get accidentally disconnected and you are the moderator, reconnect and join again the  event. If you see someone disappear from the who's on list, be sure to invite him/her again pressing the icon highlighted in the picture below. Then select the id of the missing stakeholders from the drop-down list.

	    <p align="center">
	    http://econference4.googlecode.com/svn/wiki/img/manual_invitation.png 
	    <br />*Figure  20. Manual invitation* 
	    </p>

	    === Tool begins to behave erratically? Save, clean and restore the tool state ===
	    In this case, the moderator should:
	      # Tell the participants to leave & quit the tool.
	        # Do not forget to save the logs.
		  # Restart the tool yourself and join the event again.
		    # Wait for participants to relaunch the tool and join the event again.
		      # Import the content of the decision place from the file that you created in the 2nd step

		      Now that you have reset the tool to the previous, but clean, state you are good to go on with the meeting.


