package org.apertium.api.translate.actions;

import org.apertium.api.translate.*;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;

public class TranslateConfigurationForm {
	private Box rootComponent = null;
	
	private Services services = null;
	private JComboBox serviceComboBox = null;

	private JComboBox srcComboBox = null;
	private JComboBox destComboBox = null;

	private JTextField urlTextField = null;

	public TranslateConfigurationForm() {
		System.out.println("TranslateConfigurationForm()");
		
		services = new Services();
		rootComponent = Box.createHorizontalBox();
		
		Box mainComponentSx = Box.createVerticalBox();
		Box mainComponentDx = Box.createVerticalBox();
		
		serviceComboBox = new JComboBox();
		serviceComboBox.setMaximumSize(new Dimension(250, 20));
		
		mainComponentSx.add(new JLabel("Service type:"));
		mainComponentDx.add(serviceComboBox);
		
		mainComponentSx.add(Box.createHorizontalStrut(5));
		mainComponentDx.add(Box.createHorizontalStrut(5));
		
		urlTextField = new JTextField();
		urlTextField.setMaximumSize(new Dimension(250, 20));
		
		mainComponentSx.add(new JLabel("Service URL:"));
		mainComponentDx.add(urlTextField);
		
		mainComponentSx.add(Box.createHorizontalStrut(5));
		mainComponentDx.add(Box.createHorizontalStrut(5));
		
		srcComboBox = new JComboBox();
		srcComboBox.setMaximumSize(new Dimension(250, 20));
		
		mainComponentSx.add(new JLabel("Source language:"));
		mainComponentDx.add(srcComboBox);
		
		mainComponentSx.add(Box.createHorizontalStrut(5));
		mainComponentDx.add(Box.createHorizontalStrut(5));
		
		destComboBox = new JComboBox();
		destComboBox.setMaximumSize(new Dimension(250, 20));
		
		mainComponentSx.add(new JLabel("Destination language:"));
		mainComponentDx.add(destComboBox);
		
		rootComponent.add(mainComponentSx);
		rootComponent.add(Box.createHorizontalStrut(10));
		rootComponent.add(mainComponentDx);
		
		ISO639 iso = new ISO639();
		
		serviceComboBox.removeAllItems();
		serviceComboBox.setModel(createServiceModel(services));
		serviceComboBox.setRenderer(new EntryRenderer());
		
		serviceComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				checkUrl();
			}
		});
		
		srcComboBox.removeAllItems();
		srcComboBox.setModel(createLanguageModel(iso));
		srcComboBox.setRenderer(new EntryRenderer());

		if (srcComboBox.getModel().getSize() > 0) {
			srcComboBox.setSelectedIndex(0);
		}
		
		destComboBox.removeAllItems();
		destComboBox.setModel(createLanguageModel(iso));
		destComboBox.setRenderer(new EntryRenderer());

		if (destComboBox.getModel().getSize() > 0) {
			destComboBox.setSelectedIndex(0);
		}
		
		checkUrl();
	}

	public void checkUrl() {
		String selected = (String)serviceComboBox.getSelectedItem();
		Services.ServiceType ser = services.getServiceType(selected);
		
		if (ser == Services.ServiceType.APERTIUM) {
			urlTextField.setEditable(true);
		} else {
			urlTextField.setEditable(false);
		}
	}
	
	public JComboBox getSrcComboBox() {
		return srcComboBox;
	}

	public JComboBox getDestComboBox() {
		return destComboBox;
	}

	private String[] setToArray(Set<String> set) {
		String[] ret = new String[set.size()];
		int count = 0;
		for (String s : set) {
			ret[count++] = s;
		}
		return ret;
	}
	
	private ComboBoxModel createServiceModel(Services services) {
		Set<String> items = null;
		
		System.out.println("TranslateConfigurationForm.createServiceModel()");
		
		try {
			items = services.getServices();
		} catch (Exception e) {
			items = new TreeSet<String>();
		}
		
		String[] ret = setToArray(items);	
		java.util.Arrays.sort(ret);
		return new DefaultComboBoxModel(ret);
	}
	
	private ComboBoxModel createLanguageModel(ISO639 iso) {
		Set<String> items;
		try {
			items = iso.getLanguages();
		} catch (Exception e) {
			items = new TreeSet<String>();
		}
		String[] ret = setToArray(items);
		java.util.Arrays.sort(ret);
		return new DefaultComboBoxModel(ret);
	}

	public JComponent getRootComponent() {
		return rootComponent;
	}

	public void setData(TranslateConfiguration data) {

		System.out.println("TranslateConfigurationForm.setData()");

		if (data.getLangPair() != null) {
			ComboBoxModel model = srcComboBox.getModel();
			boolean ok = false;

			for (int i = 0; i < model.getSize() && !ok; i++) {
				String item = (String) model.getElementAt(i);

				if (item.equals(data.getLangPair().getSrcLang().getName())) {
					srcComboBox.setSelectedItem(item);
					ok = true;
				}
			}

			model = destComboBox.getModel();
			ok = false;
			for (int i = 0; i < model.getSize() && !ok; i++) {
				String item = (String) model.getElementAt(i);
				if (item.equals(data.getLangPair().getDestLang().getName())) {
					destComboBox.setSelectedItem(item);
					ok = true;
				}
			}
		}

		if (data.getService() != null) {
			ComboBoxModel model = serviceComboBox.getModel();
			boolean ok = false;

			for (int i = 0; i < model.getSize() && !ok; i++) {
				String item = (String) model.getElementAt(i);
				if (item.equals(services.getService(data.getService()))) {
					serviceComboBox.setSelectedItem(item);
					ok = true;
				}
			}
		}

		if (data.getUrl() != null) {
			urlTextField.setText(data.getUrl());
		}
	}

	public TranslateConfiguration getData() {
		TranslateConfiguration ret = new TranslateConfiguration();
		
		String serviceSelectedItem = (String)serviceComboBox.getSelectedItem();
		ret.setService(services.getServiceType(serviceSelectedItem));
		
		String srcSelectedItem = (String)srcComboBox.getSelectedItem();
		String destSelectedItem = (String)destComboBox.getSelectedItem();
		
		ISO639 iso = new ISO639();
		
		Language src = new Language(iso.getCode(srcSelectedItem));
		Language dest = new Language(iso.getCode(destSelectedItem));
		
		LanguagePair pair = new LanguagePair(src, dest);
		
		ret.setLangPair(pair);
		
		if (urlTextField != null) {
			ret.setUrl(urlTextField.getText().trim());
		}
		
		return ret;
	}

	public boolean isModified(TranslateConfiguration data) {
		TranslateConfiguration tc = getData();
		boolean ret = tc.equals(data);
		return ret;
	}

}
