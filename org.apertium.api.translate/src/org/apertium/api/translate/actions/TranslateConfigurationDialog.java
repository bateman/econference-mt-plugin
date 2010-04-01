package org.apertium.api.translate.actions;

import javax.swing.*;

import org.apertium.api.translate.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TranslateConfigurationDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 3929003692312556323L;

	private TranslateConfigurationForm form = null;

	private JButton okButton = null;
	private JButton cancelButton = null;
	private JButton applyButton = null;

	private boolean answer = false;

	public TranslateConfigurationDialog(Frame owner) {
		super(owner, true);

		System.out.println("TranslateConfigDialog()");
		
		form = new TranslateConfigurationForm();
		
		okButton = new JButton("Ok");
		cancelButton = new JButton("Cancel");
		applyButton = new JButton("Apply");
		
		//setPreferredSize(new Dimension(400, 200));
		//setSize(300, 200);

		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		applyButton.addActionListener(this);

		Container contentPane = getContentPane();

		contentPane.add(form.getRootComponent(), "Center");

		JPanel panel = new JPanel();

		panel.add(okButton);
		panel.add(cancelButton);
		panel.add(applyButton);

		contentPane.add(panel, "South");
		
		System.out.println("TranslateConfigDialog() 3");
	}

	protected JRootPane createRootPane() {
		JRootPane rootPane = new JRootPane();
		KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
		Action actionListener = new AbstractAction() {
			private static final long serialVersionUID = -6213167658429616784L;

			public void actionPerformed(ActionEvent actionEvent) {
				dispose();
			}
		};
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(stroke, "ESCAPE");
		rootPane.getActionMap().put("ESCAPE", actionListener);

		return rootPane;
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == okButton) {
			saveProperties();
			answer = true;
			dispose();
		} else if (source == cancelButton) {
			answer = false;
			dispose();
		} else if (source == applyButton) {
			answer = true;
			saveProperties();
		}
	}

	private void saveProperties() {
		TranslateConfiguration data = form.getData();
		TranslateConfiguration configuration = TranslatePlugin.getDefault().getConfiguration();
		
		System.out.println("TranslateConfigurationDialog.saveProperties(): " + data.getService());
		
		configuration.setService(data.getService());
		configuration.setLangPair(data.getLangPair());
		configuration.setUrl(data.getUrl());
	}

	public void loadProperties() {
		System.out.println("TranslateConfigDialog.loadProperties()");
		TranslateConfiguration configuration = TranslatePlugin.getDefault().getConfiguration();
		form.setData(configuration);
	}

	public boolean isAnswer() {
		return answer;
	}

}