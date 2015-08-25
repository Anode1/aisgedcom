/*
 	Copyright (C) 2009 Vasili Gavrilov

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.is.ais.gedcom.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.Box;
import javax.swing.JPanel;

/**
 * Message box for LoginPanel showing a message to the user
 * (a message from the server). Plays the role of the message bar.
 */
public class MessageBox extends JPanel{

	private JLabel label;
	Color origColor;

	/**
	 * The constructor
	 */
	public MessageBox(){

		setLayout(new javax.swing.OverlayLayout(this));
		/*
      Dimension dim=new Dimension(400,25);
      setMinimumSize(dim);
      setPreferredSize(dim);
		 */
		label=new JLabel();

		label.setVisible(false);
		label.setFont(new Font("SanSerif", Font.BOLD, 14));
		origColor=label.getForeground();
		label.setForeground(Color.red);

		//add small Component to prevent changing of size:
		Component strut=Box.createVerticalStrut(30);
		add(strut);

		add(label);
	}

	/**
	 * Shows a message in one of 2 colors: red or default.
	 * If the argument passed is true - the message is shown in red color.
	 */
	public void showMessage(String message, boolean red){

		if(red)label.setForeground(Color.red);
		else label.setForeground(origColor);

		label.setText(message);
		label.setVisible(true);
	}

	/**
	 * Hides the message
	 */
	public void hideMessage(){

		label.setVisible(false);
	}

}
