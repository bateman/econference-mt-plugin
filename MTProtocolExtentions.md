#summary Brief explanation of Machine Translator Protocol Extensions
#labels Featured,Phase-Implementation


<wiki:toc max_depth="3" />

= Introduction =

In this tutorial are shown and explained  the most relevant packets of the machine translator protocol extensions, trasmissed during a typical usage of the system.


= Chat Use Cases =

The following packets are the main protocol extensions concerning all the actions taken during a chat session.

== Typing Message ==
This kind of packet is always obtained when us or someone else starts typing something, even if the message isn't sent at last.

<code language="xml">
<message id="h1Ykw-13" 
 to="romeo@gmail.com"
  from="7f1nre20kfl13yf@public.talk.google.com/eConf.C8121727"
   type="chat">
     <thread>2CTmo1</thread> 
       <composing xmlns="http://jabber.org/protocol/chatstates"> 
            <who>juliet@gmail.com</who> 
	      </composing> 
	        <x xmlns="google:nosave"/> 
		  <record xmlns="http://jabber.org/protocol/archive"/> 
		  </message>
		  </code>

		  == Language Request ==
		  This kind of packet is received/sent when an istance of EconferenceMT system wants to know language of another one.

		  <code language="xml">
		  <message id="h1Ykw-18" 
		    to="romeo@gmail.com" 
		      from="7f1nre20kfl13yf@public.talk.google.com/eConf.C8121727"  
		        type="chat"> 
			  <thread>32nRk1</thread> 
			    <x xmlns="google:nosave"/> 
			      <record xmlns="http://jabber.org/protocol/archive"/> 
			        <properties xmlns="http://www.jivesoftware.com/xmlns/xmpp/properties"> 
				    <property> 
				           <name>ExtensionName</name> 
					          <value type="string">languageRequest</value> 
						      </property> 
						        </properties> 
							</message>
							</code>

							== Language Response ==
							This kind of packet is received/sent in response to a language request packet, indicating the current language.

							<code language="xml">
							<message id="h1Ykw-18" 
							  to="7f1nre20kfl13yf@public.talk.google.com/eConf.C8121727"
							    from="romeo@gmail.com" 
							      type="chat"> 
							        <thread>2CTmo2</thread> 
								  <properties xmlns="http://www.jivesoftware.com/xmlns/xmpp/properties"> 
								    <property> 
								         <name>ExtensionName</name> 
									      <value type="string">languageResponse</value> 
									        </property> 
										  <property> 
										       <name>language</name> 
										            <value type="string">it</value> 
											      </property> 
											        </properties> 
												</message>

												  
												  </code>


												  == Inactivity State ==
												  This kind of packet is received/sent after a certain time of inactivity.

												  <code language="xml">
												  <message id="A653CC9D"
												    to="nicombk@gmail.com" 
												      from="7f1nre20kfl13yf@public.talk.google.com" 
												        type="chat"> 
													  <thread>2CTmo1</thread> 
													    <inactive xmlns="http://jabber.org/protocol/chatstates"/> 
													    </message>
													    </code>

													    = Conference Use Cases =
													    The following packets are the main protocol extensions concerning all the actions taken during a conference session.

													    == Item List ==
													    This packet contains the item list relative to the current conference.

													    <code language="xml">
													    <message id="bLtTp-9" to="econference@conference.codingteam.net" type="groupchat"> 
													      <properties xmlns="http://www.jivesoftware.com/xmlns/xmpp/properties"> 
													          <property> 
														        <name>Items</name> 
															      <value type="string">item1//item2//item3//</value> 
															          </property> 
																      <property> 
																            <name>ExtensionName</name> 
																	          <value type="string">ItemList</value> 
																		      </property> 
																		        </properties> 
																			</message>
																			</code>

																			== Conference Status Change ==
																			This kind of packet is referred to the start/stop of the current conference.

																			<code language="xml">
																			<message id="bLtTp-11" to="econference@conference.codingteam.net" type="groupchat"> 
																			 <properties xmlns="http://www.jivesoftware.com/xmlns/xmpp/properties"> 
																			     <property> 
																			           <name>Status</name> 
																				         <value type="string">STARTED</value> 
																					     </property> 
																					         <property> 
																						       <name>ExtensionName</name> 
																						             <value type="string">StatusChange</value> 
																							         </property> 
																								  </properties> 
																								  </message>
																								  </code>


																								  == Current Agenda Item ==
																								  This kind of packet is received/sent whenever an agenda item is selected by moderator;"ItemId" assumes integer values depending on item position in the list. When the conference is stopped, the property "ItemId" switches value to "__FREE_TALK__" just like the packet below.

																								  <code language="xml">
																								  <message id="bLtTp-44" to="econference@conference.codingteam.net" type="groupchat"> 
																								    <properties xmlns="http://www.jivesoftware.com/xmlns/xmpp/properties"> 
																								        <property> 
																									      <name>ItemId</name> 
																									            <value type="string">__FREE_TALK__</value> 
																										        </property> 
																											    <property> 
																											          <name>ExtensionName</name> 
																												        <value type="string">CurrentAgendaItem</value> 
																													    </property> 
																													      </properties> 
																													      </message>
																													      </code>

																													      == Raise Hande Request ==
																													      This packet is received/sent whenever someone "raise hand".

																													      <code language="xml">
																													      <message id="bLtTp-13" to="econference@conference.codingteam.net" type="groupchat"> 
																													       <properties xmlns="http://www.jivesoftware.com/xmlns/xmpp/properties"> 
																													           <property> 
																														         <name>ExtensionName</name> 
																															       <value type="string">RiseHand</value> 
																															           </property> 
																																       <property> 
																																             <name>Question</name> 
																																	           <value type="string">let me talk about my work</value> 
																																		       </property> 
																																		           <property> 
																																			         <name>From</name> 
																																				       <value type="string">romeo@gmail.com</value> 
																																				           </property> 
																																					     </properties> 
																																					     </message>
																																					     </code>

																																					     == Raise Hand Status ==
																																					     This kind of packet is received/sent in case of "raise hand", assuming various states(Pending,Approved,Rejected). The packet below is relative to "APPROVED" action.

																																					     <code language="xml">
																																					     <message id="bLtTp-20" to="econference@conference.codingteam.net" type="groupchat"> 
																																					       <properties xmlns="http://www.jivesoftware.com/xmlns/xmpp/properties"> 
																																					           <property> 
																																						         <name>QuestionStatus</name> 
																																							       <value type="string">APPROVED</value> 
																																							          </property> 
																																								     <property> 
																																								           <name>ExtensionName</name> 
																																									         <value type="string">QuestionUpdate</value> 
																																										    </property> 
																																										       <property> 
																																										             <name>Question</name> 
																																											           <value type="string">vorrei parlare del mio lavoro</value> 
																																												      </property> 
																																												         <property> 
																																													       <name>QuestionId</name> 
																																													             <value type="string">2</value> 
																																														        </property> 
																																															   <property> 
																																															         <name>From</name> 
																																																       <value type="string">juliet@gmail.com</value> 
																																																          </property> 
																																																	    </properties> 
																																																	    </message>
																																																	    </code>
																																																	          
																																																		  == Changed Special Privileges ==
																																																		  These packets are received/sent whenever the moderator grant or revoke scribe privileges. The packet below is relative to "GRANT" action.

																																																		  <code language="xml">
																																																		  <message id="bLtTp-21" to="econference@conference.codingteam.net" type="groupchat"> 
																																																		    <properties xmlns="http://www.jivesoftware.com/xmlns/xmpp/properties"> 
																																																		        <property> 
																																																			     <name>SpecialRole</name> 
																																																			          <value type="string">SCRIBE</value> 
																																																				      </property> 
																																																				          <property> 
																																																					       <name>ExtensionName</name> 
																																																					            <value type="string">ChangedSpecialPrivilege</value> 
																																																						        </property> 
																																																							    <property> 
																																																							         <name>UserId</name> 
																																																								      <value type="string">juliet@gmail.com</value> 
																																																								          </property> 
																																																									      <property> 
																																																									           <name>RoleAction</name> 
																																																										        <value type="string">GRANT</value> 
																																																											    </property> 
																																																											      </properties> 
																																																											      </message>
																																																											      </code>
																																																											            
																																																												    == Decision Whiteboard Changed ==
																																																												    This packet is received/sent whenever the scribe writes something in the decision place.

																																																												    <code language="xml">
																																																												    <message id="EbLVJ-20" 
																																																												      to="romeo.com/eConf.DC80C0FD"   
																																																												        from="econference@conference.codingteam.net/juliet@gmail.com" 
																																																													  type="groupchat">
																																																													    <properties xmlns="http://www.jivesoftware.com/xmlns/xmpp/properties"> 
																																																													         <property> 
																																																														         <name>ExtensionName</name> 
																																																															         <value type="string">WhiteBoardChanged</value> 
																																																																      </property> 
																																																																           <property> 
																																																																	           <name>WhiteBoardText</name> 
																																																																		           <value type="string">modify the wiki page</value> 
																																																																			        </property> 
																																																																				     <property> 
																																																																				             <name>From</name> 
																																																																					             <value type="string">juliet@gmail.com</value> 
																																																																						          </property> 
																																																																							    </properties> 
																																																																							    </message>

																																																																							    </code>

																																																																							    == Stop/Allow back in conversation ==
																																																																							    These packets are received/sent whenever the moderator stops/allows back someone in the current conversation changing the role to visitor/partecipant.

																																																																							    <code language="xml">
																																																																							    <iq id="bLtTp-23" to="econference@conference.codingteam.net" type="set"> 
																																																																							      <query xmlns="http://jabber.org/protocol/muc#admin"> 
																																																																							          <item nick="juliet@gmail.com" role="visitor"/> 
																																																																								    </query> 
																																																																								    </iq>
																																																																								    </code>
