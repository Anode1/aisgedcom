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
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.is.ais.gedcom.Constants;
import org.is.ais.gedcom.Parameters;


/**
 * Not used right now 
 */
public class TabbedPanel extends JPanel{
	
	
	public TabbedPanel()throws Exception{
	
		super(new GridLayout(1, 1));
		JTabbedPane tabbedPane = new JTabbedPane();
		this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	
	    tabbedPane.addTab("Main", new MainPanel());
	    //tabbedPane.setMnemonicAt(0, KeyEvent.VK_h);
	    
	    final Atree2GedcomPanel atree2GedcomPanel= new Atree2GedcomPanel();
	    tabbedPane.addTab("atree2GEDCOM", atree2GedcomPanel);
	    //tabbedPane.setMnemonicAt(1, KeyEvent.VK_r);
	    
	    final TranscodePanel transcodePanel=new TranscodePanel();
	    tabbedPane.addTab("Transcode", transcodePanel);	    
	
		add(tabbedPane);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		
		tabbedPane.addChangeListener(new ChangeListener(){
	        public void stateChanged(ChangeEvent changeEvent) {
	          JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
	          int index = sourceTabbedPane.getSelectedIndex();
	          switch(index){
				case 0:
					Parameters.getInstance().put(Constants.ATREE2GEDCOM, "false"); //default panel, so we load it always
					break;
				case 1:
					Parameters.getInstance().put(Constants.ATREE2GEDCOM, "true");
					atree2GedcomPanel.init();
					break;
				case 2:
					Parameters.getInstance().put(Constants.TRANSCODE, "true");
					transcodePanel.init();
					break;
				default:
					throw new NullPointerException("Not expected to be here");
			}
	      }
	    });	
		
	}
	

}

