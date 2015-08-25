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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * Main frame (window) class
 */
public class MainFrame extends JFrame{

	protected MainFrameMenuBar menuBar;
	private StatusBar statusBar;

	
	MainFrame(){
		try{
			setTitle("aisgedcom"); 

			Container contentPane = getContentPane();
			contentPane.setLayout(new BorderLayout());

			contentPane.add(new WrapperPanel(), BorderLayout.CENTER);

			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); //we have our own handler - do not close

			final MainFrame instance=this;
			addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent evt){
					Actions.fireAction(instance, new Actions.ExitAction(null)); 
				}
				//we want to refresh statistics each time this window has been reactivated
				public void windowActivated(WindowEvent evt){
					//mainAgentPanel.populateStatAsync();
				}               
				/*
		         public void windowDeiconified(WindowEvent e){
		            //System.out.println("deiconified");
		         }
				 */
			});      

			//get Dimensions of our frame from properties
			int sizeX=800;
			int sizeY=600;
			this.setResizable(false);
			/*
		      String value=org.sp.util.Parameters.getString("MainMenuFrame.size.x");
		      if(value!=null){
		        try{
		          sizeX=Integer.parseInt(value);
		        }
		        catch(NumberFormatException nfe){
		          System.err.println("MainMenuFrame size ignored:"+nfe);
		        }
		      }
		      value=org.sp.util.Parameters.getString("MainMenuFrame.size.y");
		      if(value!=null){
		        try{
		          sizeY=Integer.parseInt(value);
		        }
		        catch(NumberFormatException nfe){
		          System.err.println("MainMenuFrame size ignored:"+nfe);
		        }
		      }
			 */
			this.setSize(new Dimension(sizeX,sizeY));

			menuBar = new MainFrameMenuBar(this);
			setJMenuBar(menuBar);      

			JPanel toolBarsWrappingPanel=new JPanel();
			toolBarsWrappingPanel.setLayout(new BoxLayout(toolBarsWrappingPanel, BoxLayout.Y_AXIS));
			contentPane.add(toolBarsWrappingPanel, BorderLayout.NORTH);

			JPanel wp = new JPanel();
			wp.setLayout(new BoxLayout(wp, BoxLayout.Y_AXIS));
			
			/*
		      ToolBar toolBar1 = new ToolBar();
		      wp.add(toolBar1);
		      contentPane.add(wp, BorderLayout.NORTH);
		    */

			statusBar=new StatusBar();
			contentPane.add(statusBar, BorderLayout.SOUTH);
			GUIManager.statusBar = statusBar;
			
			GUIUtils.setCentalizedLocation(this);

			//set small frame icon (all child dialogs will inherit it - if this frame is truly passed to them)

			//setIconImage(((ImageIcon)Images.getIcon("icon.gif")).getImage());
		}
		catch(Exception e){
			//log.error("", e);
			e.printStackTrace();
		}
	}
	

	/**
	 * Override super's method - to add additional (debug currently) title suffix
	 */
	public void setTitle(String title){

		super.setTitle(title+" "+GUIManager.releaseVersionString);
	}  

	/**
	 * Entry point of the application if without Login
	 */
	public static void main(String args[]) throws Exception{
		
		try{
			GUIManager.initialize();
			
		}catch (Throwable t) {
			//log.error("", t);
			t.printStackTrace();
		}
	}

}
