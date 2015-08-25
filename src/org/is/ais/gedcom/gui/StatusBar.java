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
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Status bar for Main Frame
 */
public class StatusBar extends JPanel {

	protected String string;
	protected JLabel label;
	protected int height=18;

	public StatusBar() {

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		add(Box.createRigidArea(new Dimension(5,0)));

		
		label=new JLabel();
		label.setForeground(Color.black);
		label.setMinimumSize(new Dimension(20,height));
		label.setPreferredSize(new Dimension(500,height));
		label.setMaximumSize(new Dimension(1000,height));
		label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		add(label);
		
		setMinimumSize(new Dimension(30,30));
		setBorder(BorderFactory.createLoweredBevelBorder());
		//label.setText(" For Help, press F1");
	}


	public StatusBar(String newString){

		this();
		showStatus(newString);
	}

	
	public void showStatus(String newString){

		string=new String(newString);
		label.setText(string);
		repaint();
	}


}