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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Main application menu shown on MainFrame
 */
public class MainFrameMenuBar extends JMenuBar{

	final private JFrame frame;

	public MainFrameMenuBar(JFrame f) throws Exception{

		super();

		this.frame=f;

		JMenu menu=add(new JMenu("File"));


		JMenuItem menuItem;
		
		/* menuItem = menu.add(new JMenuItem("Preferences"));
		menuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//(new PreferencesDialog()).setVisible(true);
				javax.swing.JOptionPane.showMessageDialog(null, "Preferences not implemented yet");
			}
		});
		*/
		
		menu.addSeparator();

		menuItem = menu.add("Exit");
		menuItem.addActionListener(new Actions.ExitAction(null));

		menu.addSeparator();

		menu = add(new JMenu("Help"));

		menuItem = menu.add(new JMenuItem("Help Topics"));
		menuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				javax.swing.JOptionPane.showMessageDialog(null, "Help is not implemented yet");
			}
		});

		menu.addSeparator();

		menuItem = menu.add(new JMenuItem("About"));
		menuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String text = "<html><body><center>Data Transformation Tools<br>2008,2011<br>SourcePortal Inc.<br></center></body></html>";
				javax.swing.JOptionPane.showMessageDialog(null, text);
				/*
				JOptionPane.showMessageDialog(null, text, 
						"About", JOptionPane.PLAIN_MESSAGE,
						Images.getIcon("splash.gif"));
				//(new org.sp.gui.dialogs.About(frame)).show();
				 */
			}
		});

	}




}
