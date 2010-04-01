package org.apertium.api.translate;

import javax.swing.*;
import java.awt.*;

public class EntryRenderer extends JLabel implements ListCellRenderer {

	private static final long serialVersionUID = -8230366668996692971L;

	public EntryRenderer() {
		this.setOpaque(true);
	}

	public Component getListCellRendererComponent(JList listbox, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		String pair = (String) value;

		if (pair != null) {
			this.setText(pair);
		}

		if (isSelected) {
			this.setBackground(UIManager.getColor("ComboBox.selectionBackground"));
			this.setForeground(UIManager.getColor("ComboBox.selectionForeground"));
		} else {
			this.setBackground(UIManager.getColor("ComboBox.background"));
			this.setForeground(UIManager.getColor("ComboBox.foreground"));
		}

		return this;
	}

}
