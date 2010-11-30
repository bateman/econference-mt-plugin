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

package org.apertium.api.translate.actions;

import java.util.Set;
import java.util.TreeSet;

import org.apertium.api.translate.ISO639;
import org.apertium.api.translate.Language;
import org.apertium.api.translate.Services;
import org.apertium.api.translate.TranslatePlugin;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TranslateConfigurationDialog extends TitleAreaDialog {
	private Services services = null;
	private ISO639 iso = null;

	private Combo mtServiceProviderCombo;
	private Combo languagesCombo;
	private Text mtServiceUrl;
	private String mtServiceUrlString = "";
	int mtServiceSelectionIndex = -1;
	int languageSelectionIndex = -1;

	public TranslateConfigurationDialog() {
		super(null);
		System.out.println("TranslateConfigDialog()");
		services = new Services();
		iso = new ISO639();
	}

	@Override
	public void create() {
		super.create();
		// Set the title
		setTitle("Machine Translation Configuration");
		// Set the message
		setMessage("Choose the MT service to use and your own language",
				IMessageProvider.INFORMATION);

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		// layout.horizontalAlignment = GridData.FILL;
		parent.setLayout(layout);

		// The text fields will grow with the size of the dialog
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;

		Label mtServiceProviderLabel = new Label(parent, SWT.NONE);
		mtServiceProviderLabel.setText("MT Service Provider");
		mtServiceProviderCombo = new Combo(parent, SWT.READ_ONLY);
		mtServiceProviderCombo.setLayoutData(gridData);
		mtServiceProviderCombo.setItems(createServiceModel());
		mtServiceProviderCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				mtServiceSelectionIndex = mtServiceProviderCombo
						.getSelectionIndex();
				checkUrl();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// nothing
			}
		});
		Label mtServiceUrlLabel = new Label(parent, SWT.NONE);
		mtServiceUrlLabel.setText("Url");
		mtServiceUrl = new Text(parent, SWT.BORDER);
		mtServiceUrl.setLayoutData(gridData);
		mtServiceUrl.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				mtServiceUrlString = mtServiceUrl.getText();
			}

			@Override
			public void focusGained(FocusEvent e) {
				// Nothing
			}
		});

		Label languageLabel = new Label(parent, SWT.NONE);
		languageLabel.setText("Native language");
		languagesCombo = new Combo(parent, SWT.READ_ONLY);
		languagesCombo.setLayoutData(gridData);
		languagesCombo.setItems(createLanguageModel());
		languagesCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				languageSelectionIndex = languagesCombo.getSelectionIndex();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Nothing
			}
		});

		loadProperties();
		return parent;

	}

	private void saveProperties() {
		TranslateConfiguration data = getData();
		TranslateConfiguration configuration = TranslatePlugin.getDefault()
				.getConfiguration();

		System.out.println("TranslateConfigurationDialog.saveProperties(): "
				+ data.getService());

		configuration.setService(data.getService());
		configuration.setUserLanguage(data.getUserLanguage());
		configuration.setUrl(data.getUrl());

		configuration.storeProperties();

	}

	public void loadProperties() {
		System.out.println("TranslateConfigDialog.loadProperties()");
		// loads default
		TranslateConfiguration configuration = TranslatePlugin.getDefault()
				.getConfiguration();

		configuration.loadProperties();

		setData(configuration);
		checkUrl();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.CENTER;

		parent.setLayoutData(gridData);
		// Create Ok button
		Button okButton = createButton(parent, OK, "Save", true);
		// Add a SelectionListener
		okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (isValidInput()) {
					okPressed();
				}
			}
		});

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, "Cancel", false);
		// Add a SelectionListener
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	private boolean isValidInput() {
		return mtServiceUrl.equals("") ? false : true;
	}

	// We allow the user to resize this dialog
	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void okPressed() {
		saveProperties();
		super.okPressed();
	}

	private void checkUrl() {
		try {
			String selected = mtServiceProviderCombo
					.getItem(mtServiceProviderCombo.getSelectionIndex());
			Services.ServiceType ser = services.getServiceType(selected);

			if (ser == Services.ServiceType.APERTIUM) {
				mtServiceUrl.setEditable(true);
				mtServiceUrl.setEnabled(true);
			} else {
				mtServiceUrl.setEditable(false);
				mtServiceUrl.setEnabled(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String[] setToArray(Set<String> set) {
		String[] ret = new String[set.size()];
		set.toArray(ret);
		return ret;
	}

	private String[] createServiceModel() {
		Set<String> items = null;

		System.out.println("TranslateConfigurationForm.createServiceModel()");

		try {
			items = services.getServices();
		} catch (Exception e) {
			items = new TreeSet<String>();
		}

		String[] ret = setToArray(items);
		java.util.Arrays.sort(ret);
		return ret;
	}

	private String[] createLanguageModel() {
		Set<String> items;
		try {
			items = iso.getLanguages();
		} catch (Exception e) {
			items = new TreeSet<String>();
		}
		String[] ret = setToArray(items);
		java.util.Arrays.sort(ret);
		return ret;
	}

	private void setData(TranslateConfiguration data) {

		System.out.println("TranslateConfigurationForm.setData()");

		if (data.getUserLanguage() != null) {
			String[] model = languagesCombo.getItems();
			boolean ok = false;

			for (int i = 0; i < model.length && !ok; i++) {
				String item = (String) model[i];

				if (item.equals(data.getUserLanguage().getName())) {
					System.out.println("Received "
							+ data.getUserLanguage().getName());
					languagesCombo.select(i);
					languageSelectionIndex = i;
					System.out.println("Set "
							+ languagesCombo.getItem(languagesCombo
									.getSelectionIndex()));
					ok = true;
				}
			}

		}

		if (data.getService() != null) {
			String[] model = mtServiceProviderCombo.getItems();
			boolean ok = false;

			for (int i = 0; i < model.length && !ok; i++) {
				String item = (String) model[i];
				if (item.equals(services.getService(data.getService()))) {
					System.out.println("Received " + data.getService());
					mtServiceProviderCombo.select(i);
					mtServiceSelectionIndex = i;
					System.out.println("Set "
							+ mtServiceProviderCombo
									.getItem(mtServiceProviderCombo
											.getSelectionIndex()));
					ok = true;
				}
			}
		}

		if (data.getUrl() != null) {
			mtServiceUrl.setText(data.getUrl().trim());
			mtServiceUrlString = mtServiceUrl.getText();
			System.out.println("Set " + mtServiceUrlString);
		}
	}

	private TranslateConfiguration getData() {
		TranslateConfiguration ret = new TranslateConfiguration();
		System.out.println("TranslateConfigurationForm.getData()");

		if (mtServiceSelectionIndex != -1) {
			String[] servicesArray = (String[]) createServiceModel();
			String serviceSelectedItem = (String) servicesArray[mtServiceSelectionIndex];
			System.out.println("Selected " + serviceSelectedItem);
			ret.setService(services.getServiceType(serviceSelectedItem));
		}

		if (languageSelectionIndex != -1) {
			String[] langArray = createLanguageModel();
			String srcSelectedItem = langArray[languageSelectionIndex];
			System.out.println("Selected " + srcSelectedItem);
			ISO639 iso = new ISO639();
			// our language
			Language src = new Language(iso.getCode(srcSelectedItem));
			// the others' language, we don't know it upfront			
			ret.setUserLanguage(src);
		}

		if (mtServiceUrl != null) {
			ret.setUrl(mtServiceUrlString);
			System.out.println("Selected " + mtServiceUrlString);
		}

		return ret;
	}

	public static void main(String[] args) {
		TranslateConfigurationDialog c = new TranslateConfigurationDialog();
		c.open();
	}
}