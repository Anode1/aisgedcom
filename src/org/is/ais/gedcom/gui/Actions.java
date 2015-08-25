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
import java.awt.Component;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.Action;

/**
 * Collection of business logic actions. 
 */
public class Actions{

	/**
	 * Exit from the main menu amainAgentPanelction
	 */
	public static class ExitAction extends AbstractAction{

		static final String NAME="exit";
		private Component p;

		public ExitAction(Component parent){

			super(NAME);
			p=parent;
		}

		public void actionPerformed(ActionEvent e) {

			int response=JOptionPane.showConfirmDialog(p, "Do you really want to exit?");
			switch(response){

			case JOptionPane.YES_OPTION:
				//thread is not to block GUI.
				//we are thread safe here - not running in GUI thread, because anyway we are exiting
				System.exit(0);
			case JOptionPane.NO_OPTION:

			case JOptionPane.CANCEL_OPTION:

			case JOptionPane.CLOSED_OPTION:
			}
		}
	}//ExitAction

	/**
	 * Reserved action used as a template and for debugging
	 */
	public static class ReservedAction extends AbstractAction{

		public ReservedAction(){
		}

		public void actionPerformed(ActionEvent e) {
		}

	}//class ReservedAction

	/**
	 * Method simulating JButton/JMenu behaviour (firing of Action) to share
	 * the same Actions delivery mechanism with buttons/menus
	 */  
	public static void fireAction(Object source, Action action){

		action.actionPerformed(new ActionEvent(source, ActionEvent.ACTION_PERFORMED, (String)action.getValue(Action.NAME)));
	}     

}
