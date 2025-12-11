package org.mql.java.ui;


import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class LabelUi extends JPanel {
	private static final long serialVersionUID = 1L;

	private JLabel label;
	
	public LabelUi() {
		this("");
	}
	
	public LabelUi(String title) {
		setOpaque(false);
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		label = new JLabel(title);
		add(label);
	}
	

	public void setUnderline() {
		Font font = label.getFont();
		Font underlineFont = new Font(font.getName(), Font.PLAIN , font.getSize());
		label.setFont(underlineFont);

	}
	public void addText(String text) {
		label.setText(label.getText() + text);
	}
	
	public String getText() {
		return label.getText();
	}
	
	public void setFont(Font font) {
		if (label != null) {
			label.setFont(font);
		}
	}
	
	public Font getFont() {
		return label != null ? label.getFont() : null;
	}
}