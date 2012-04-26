/**
 * This file is part of the eConference project and it is distributed under the 

 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2012 Collaborative Development Group - Dipartimento di Informatica, 
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

package org.apertium.api.translate.model;

import java.util.HashMap;

import org.apertium.api.translate.TranslatePlugin;
import org.apertium.api.translate.ui.IImageResourcesMT;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.IWorkbenchAdapter;

import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Status;
import it.uniba.di.cdg.xcore.m2m.model.ParticipantAdapterFactory;
import it.uniba.di.cdg.xcore.m2m.model.ParticipantSpecialPrivileges;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

public class MTParticipantAdapterFactory extends ParticipantAdapterFactory {

	public MTParticipantAdapterFactory() {
		/**
		 * Participants adapter.
		 */
		participantAdapter = new IWorkbenchAdapter() {
			
			public Object[] getChildren(Object o) {
				return new Object[0]; // No child
			}

			public ImageDescriptor getImageDescriptor(Object object) {
				IParticipant participant = (IParticipant) object;
				Image im1 = null, im2 = null;

				if (Role.MODERATOR.equals(participant.getRole())) {
					if (participant
							.hasSpecialPrivilege(ParticipantSpecialPrivileges.SCRIBE)
							&& participant
									.hasSpecialPrivilege(ParticipantSpecialPrivileges.VOTER)) {
						im1 = UiPlugin
								.getDefault()
								.getImageDescriptor(
										IImageResourcesMT.ICON_MODERATOR_SCRIBE_VOTER)
								.createImage();
						im2 = getFlag(participant);
						return concatImage(im1, im2);
					} else if (participant
							.hasSpecialPrivilege(ParticipantSpecialPrivileges.SCRIBE)) {
						im1 = UiPlugin
								.getDefault()
								.getImageDescriptor(
										IImageResourcesMT.ICON_MODERATOR_SCRIBE)
								.createImage();
						im2 = getFlag(participant);
						return concatImage(im1, im2);
					} else if (participant
							.hasSpecialPrivilege(ParticipantSpecialPrivileges.VOTER)) {
						im1 = UiPlugin
								.getDefault()
								.getImageDescriptor(
										IImageResourcesMT.ICON_MODERATOR_VOTER)
								.createImage();
						im2 = getFlag(participant);
						return concatImage(im1, im2);
					}
					im1 = UiPlugin
							.getDefault()
							.getImageDescriptor(
									IImageResourcesMT.ICON_USER_MODERATOR)
							.createImage();
					im2 = getFlag(participant);
					return concatImage(im1, im2);
				} else if (Status.JOINED.equals(participant.getStatus())) {
					if (participant
							.hasSpecialPrivilege(ParticipantSpecialPrivileges.SCRIBE)
							&& participant
									.hasSpecialPrivilege(ParticipantSpecialPrivileges.VOTER)) {
						im1 = UiPlugin
								.getDefault()
								.getImageDescriptor(
										IImageResourcesMT.ICON_SCRIBE_VOTER)
								.createImage();
						im2 = getFlag(participant);
						return concatImage(im1, im2);
					} else if (participant
							.hasSpecialPrivilege(ParticipantSpecialPrivileges.SCRIBE)) {
						im1 = UiPlugin
								.getDefault()
								.getImageDescriptor(
										IImageResourcesMT.ICON_SCRIBE)
								.createImage();
						im2 = getFlag(participant);
						return concatImage(im1, im2);
					} else if (participant
							.hasSpecialPrivilege(ParticipantSpecialPrivileges.VOTER)) {
						im1 = UiPlugin
								.getDefault()
								.getImageDescriptor(
										IImageResourcesMT.ICON_VOTER)
								.createImage();
						im2 = getFlag(participant);
						return concatImage(im1, im2);
					}
					im1 = UiPlugin
							.getDefault()
							.getImageDescriptor(
									IImageResourcesMT.ICON_USER_ACTIVE)
							.createImage();
					im2 = getFlag(participant);
					return concatImage(im1, im2);
				} else if (Status.FROZEN.equals(participant.getStatus()))
					return UiPlugin.getDefault().getImageDescriptor(
							IImageResourcesMT.ICON_USER_FROZEN);
				else
					// NOT_JOINED otherwise
					return UiPlugin.getDefault().getImageDescriptor(
							IImageResourcesMT.ICON_USER_OFFLINE);
			}

			public Image getFlag(IParticipant participant) {
				UserLanguages lang = UserLanguages.getInstance();
				HashMap<String, String> languages = lang.get_languages();
				Image im2 = null;

				if ((languages.containsKey(participant.getId()))
						&& (languages.get(participant.getId()) != null)) {
					im2 = new Image(null, 22, 10);
					String value = languages.get(participant.getId());
					if (value.equalsIgnoreCase("ar")) {
						im2 = TranslatePlugin.getDefault()
								.getImageDescriptor(IImageResourcesMT.FLAG_AR)
								.createImage();
					} else if (value.equalsIgnoreCase("bn")) {
						im2 = TranslatePlugin.getDefault()
								.getImageDescriptor(IImageResourcesMT.FLAG_BN)
								.createImage();
					} else if (value.equalsIgnoreCase("en")) {
						im2 = TranslatePlugin.getDefault()
								.getImageDescriptor(IImageResourcesMT.FLAG_EN)
								.createImage();
					} else if (value.equalsIgnoreCase("es")) {
						im2 = TranslatePlugin.getDefault()
								.getImageDescriptor(IImageResourcesMT.FLAG_ES)
								.createImage();
					} else if (value.equalsIgnoreCase("fr")) {
						im2 = TranslatePlugin.getDefault()
								.getImageDescriptor(IImageResourcesMT.FLAG_FR)
								.createImage();
					} else if (value.equalsIgnoreCase("de")) {
						im2 = TranslatePlugin.getDefault()
								.getImageDescriptor(IImageResourcesMT.FLAG_GE)
								.createImage();
					} else if (value.equalsIgnoreCase("hi")) {
						im2 = TranslatePlugin.getDefault()
								.getImageDescriptor(IImageResourcesMT.FLAG_HI)
								.createImage();
					} else if (value.equalsIgnoreCase("it")) {
						im2 = TranslatePlugin.getDefault()
								.getImageDescriptor(IImageResourcesMT.FLAG_IT)
								.createImage();
					} else if (value.equalsIgnoreCase("ja")) {
						im2 = TranslatePlugin.getDefault()
								.getImageDescriptor(IImageResourcesMT.FLAG_JA)
								.createImage();
					} else if (value.equalsIgnoreCase("pl")) {
						im2 = TranslatePlugin.getDefault()
								.getImageDescriptor(IImageResourcesMT.FLAG_PL)
								.createImage();
					} else if (value.equalsIgnoreCase("pt")) {
						im2 = TranslatePlugin.getDefault()
								.getImageDescriptor(IImageResourcesMT.FLAG_PT)
								.createImage();
					} else if (value.equalsIgnoreCase("ru")) {
						im2 = TranslatePlugin.getDefault()
								.getImageDescriptor(IImageResourcesMT.FLAG_RU)
								.createImage();
					} else if (value.equalsIgnoreCase("th")) {
						im2 = TranslatePlugin.getDefault()
								.getImageDescriptor(IImageResourcesMT.FLAG_TH)
								.createImage();
					} else if (value.equalsIgnoreCase("tr")) {
						im2 = TranslatePlugin.getDefault()
								.getImageDescriptor(IImageResourcesMT.FLAG_TR)
								.createImage();
					} else if (value.equalsIgnoreCase("zh")) {
						im2 = TranslatePlugin.getDefault()
								.getImageDescriptor(IImageResourcesMT.FLAG_ZH)
								.createImage();
					} else if (value.equalsIgnoreCase("ko")) {
						im2 = TranslatePlugin.getDefault()
								.getImageDescriptor(IImageResourcesMT.FLAG_KO)
								.createImage();
					} else {
						im2 = TranslatePlugin.getDefault()
								.getImageDescriptor(IImageResourcesMT.FLAG_DEF)
								.createImage();
					}
				}
				return im2;

			}

			public ImageDescriptor concatImage(Image im1, Image im2) {
				if (im2 == null) {
					return ImageDescriptor.createFromImage(im1);
				} else {
					Image bigImage = new Image(null, im1.getImageData().width
							+ im2.getImageData().width,
							im1.getImageData().height);
					GC gc = new GC(bigImage);
					gc.drawImage(im1, 0, 0);
					gc.drawImage(im2, im1.getImageData().width, 0);
					gc.dispose();
					return ImageDescriptor.createFromImage(bigImage);
				}
			}

			public String getLabel(Object o) {
				IParticipant p = (IParticipant) o;
				if (p.getPersonalStatus() != null
						&& !p.getPersonalStatus().equals(""))
					return String.format("%s (%s)", p.getNickName(),
							p.getPersonalStatus());
				else
					return p.getNickName();
			}

			public Object getParent(Object o) {
				return null;
			}
		};

	}
}
