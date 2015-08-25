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

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


/**
 * Panel where tabs are placed (we may put panels dynamically here - not to create too many widgets - if more GUI will be necessary
 */
public class WrapperPanel extends JPanel{

	
	public WrapperPanel()throws Exception{
		
		super(new GridLayout(1, 1));
		this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		add(new TabbedPanel());
		
		//add(new MainPanel());
	}




}

