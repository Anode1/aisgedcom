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

import java.io.File;
import java.io.FileOutputStream;

import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.ColorUIResource;

import org.is.ais.gedcom.Constants;

//import org.apache.log4j.Logger;

/**
 * Singleton for the GUI - analog of Config in the console version
 */
public class GUIManager{

	public static final String releaseVersionString="<ver "+Constants.releaseVersionString+">"; 
	public static StatusBar statusBar;

	//private static Logger log = Logger.getLogger(GUIManager.class);	
	
	/**
	 * Single instance of this class
	 */
	private static GUIManager instance;

	/**
	 * Ref to MainPanel panel
	 */
	private WrapperPanel mainPanel;

	/** 
	 * one-way flag needed only on-exiting (not to invoke finalization routine twice)
	 */
	private boolean finalizedAlready;

	/**
	 * Static initialization routine which must be called once at the very begining
	 * by user class
	 */
	public static void initialize(){

		if(instance!=null){
			return;  //ignore if already exists an instance in the VM
			//put global check here - if needed (opened file or socket as lock?)
		}
		instance=new GUIManager();
		instance.start();
	}

	/**
	 * Internal instance init method. It is called from the main the very first time
	 * so it is safe from thread-safety perspective (when GUI are built/shown in it)
	 */
	private void start(){

		//check java version first:
		String vers = System.getProperty("java.version");
		if(vers.compareTo("1.4") < 0){//lexicographical comparing of strings (2<3 etc.)
			System.err.println("VM found:"+vers+" but must be 1.4 or higher -- exiting");
			return;
			//System.exit(1);
		}

		try{
			//javax.swing.FocusManager.disableSwingFocusManager();

			Config config = new Config();

			setSystemLookAndFeel();

			//System.out.println("user.dir="+System.getProperty("user.dir"));
			//System.out.println("user.home="+System.getProperty("user.home"));        
			//System.out.println("java.io.tmpdir="+System.getProperty("java.io.tmpdir"));

//			Look and feel:

			String className=null;//UserProperties.getInstance().getProperty("look_and_feel_class");
			if(className!=null){
				setUserLaf(className);
			}
			else{ 
				setSystemLookAndFeel();
			}

			//set custom colors/fonts
			changeLafIfNeeded();


			//System.out.println("Parameters been loaded:\n"+Parameters.getInstance());

//			cache business screens for fast appearence:
			//  Cache.getInstance().update("aaa", new aaa());
			//Thread.sleep(200);            

			//   Cache.getInstance().update("bbb", new bbb());   
			//Thread.sleep(200);           

			//  Cache.getInstance().update("ccc", new ccc()); 
			//Thread.sleep(200);      

			//put some init stuff here

			//   Cache.getInstance().update("ddd", new ddd());   
		    
		    //User-defined properties:
		    UserParameters userProps=UserParameters.getInstance();
		    String pathToUserProps=config.getConfigDir() + File.separator + UserParameters.USER_DEFINED_PROPERTIES_FILENAME;
		    File file = new File(pathToUserProps);
		    if(file.exists() && file.canRead()){
		    	userProps.load(new java.io.BufferedInputStream(new java.io.FileInputStream(pathToUserProps)));
		    }

		    //UserParameters.getInstance().get("asdfasdfasdf");
		    
			//add shutdown hook:
			//(this will be executed even on CTRL-C and sure on each leagal exit. The 
			//only case - is KILL through ProcessManager wich is KILL -9 :-) - nothing to do
			Runtime.getRuntime().addShutdownHook(new Thread("ShutdownHookThread"){
				public void run(){
					onShutDownAsync();
				}
			});

			//Thread.sleep(200);

			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					MainFrame mmf=new MainFrame();
					mmf.show();
				}
			});        

			//   Cache.getInstance().update("UC", new UC());  

			//Utils.sample();        

		}
		catch(Exception e){
			System.err.println("initInternal:"+e);     
		}
	}

	/**
	 * This method is invoked on ShutDownHookup (when user Ctrl-C the app from 
	 * command line and any valid exiting from the application using gui controls:
	 * close buttons, 'Exit' buttons).
	 * Notice that in the case of normal 'Exit', additional operations can be done
	 * - that is why this method is called from onShutDownFromGUI() which has those
	 * additional finalization methods (saving of user preferences etc)
	 */
	private void onShutDownAsync(){ 

		if(finalizedAlready)return; //already
		finalizedAlready=true;           

		try{
			//do not remove the following message - it is assumed to be in stdout:
			//if(Main.consoleRun)
				//if(log.isDebugEnabled())log.debug("Shutting down ..."); 

		    UserParameters userProps=UserParameters.getInstance();
		    String pathToUserProps=Config.getConfigDir() + File.separator + UserParameters.USER_DEFINED_PROPERTIES_FILENAME;
		    
		    userProps.save(new FileOutputStream(pathToUserProps), "");
			
		    //if(Main.consoleRun)
		    //	if(log.isDebugEnabled())log.debug("finished"); 
		}
		catch(Exception e){
			System.err.println("Error during application shutdown:"+e);
			e.printStackTrace();
		}

	}

	/**
	 * Sets custom colors, fonts through UIManager.
	 * There are more than 500 of those - play with them!
	 */
	private void changeLafIfNeeded(){

		//boolean updateNeeded=false;

		java.awt.Color fg=null;
		String p=null;//UserProperties.getInstance().getProperty("bgcolor");
		if(p!=null){
			fg=null;//Utils.str2Color(p.trim());
			if(fg!=null){
				ColorUIResource oldPane = (ColorUIResource)UIManager.get("OptionPane.background");
				ColorUIResource oldPanel = (ColorUIResource)UIManager.get("Panel.background");
				ColorUIResource r=new ColorUIResource(fg);
				UIManager.put("OptionPane.background", r);
				UIManager.put("Panel.background", r);            

				/*
          UIManager.put("Button.font", sansserifPlain11);
          UIManager.put("Label.font", sansserifPlain11);
          UIManager.put("Table.font", sansserifPlain11);
          UIManager.put("TextField.font", sansserifPlain11);
          UIManager.put("ScrollPane.font", sansserifPlain11);
          UIManager.put("ComboBox.font", sansserifPlain11);
          UIManager.put("CheckBox.font", sansserifPlain11);
          UIManager.put("TitledBorder.font", sansserifPlain11);
          UIManager.put("RadioButton.font", sansserifPlain11);
          UIManager.put("ToolTip.font", sansserifPlain11);
          UIManager.put("TextPane.font", sansserifPlain11);
          UIManager.put("TextArea.font", sansserifPlain11);

          UIManager.put("Tree.font", sansserifBold12);
          UIManager.put("List.font", sansserifPlain11);

          UIManager.put("MenuBar.font", sansserifBold12);
          UIManager.put("Menu.font", sansserifBold11);
          UIManager.put("MenuItem.font", sansserifBold11);
          UIManager.put("TableHeader.font", sansserifBold11);
          UIManager.put("TabbedPane.font", sansserifBold12);

				 */
				//  SwingUtilities.updateComponentTreeUI(this);                  
			}
		}

	}


	/**
	 * Gets single instance of this class
	 */
	public static GUIManager getInstance(){

		return instance;
	}

	/**
	 * Sets MainMenuPanel
	 */
	public void setMainPanel(WrapperPanel mainPanel){

		this.mainPanel=mainPanel;
	}

	/**
	 * Internal utility (debugging) method for printing available look-and-feels 
	 */
	private void showLaF(){

		UIManager.LookAndFeelInfo[]  lfInfo = UIManager.getInstalledLookAndFeels();
		for (int i = 0; i < lfInfo.length; i++){
			try{
				String className = lfInfo[i].getClassName();
				Class cl = Class.forName(className);
				LookAndFeel lf = (LookAndFeel)cl.newInstance();
				if(!lf.isSupportedLookAndFeel()) {
					className = null;
				}

				if(className != null) {
					//lookAndFeel_combo.addItem(lfInfo[i].getName());
					//UIManager.setLookAndFeel(lfInfo[i].getClassName());
					System.out.println(lfInfo[i].getName()+"-->"+lfInfo[i].getClassName());
				}
			}catch (Throwable t) {
				System.err.println("Failed loading " + lfInfo[i].getClassName() + ":\n" + t);
			}
		}//for
	}

	/**
	 * Tries to setup a look and feel according to user's preferences
	 */
	private static void setSystemLookAndFeel(){

		// Force SwingApplet to come up in the System L&F vs default Metal
		String laf = UIManager.getSystemLookAndFeelClassName();
		try{
			UIManager.setLookAndFeel(laf);
			// If you want the Cross Platform L&F instead, comment out the above line and
			// uncomment the following:
			// UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}catch (UnsupportedLookAndFeelException exc){
			System.err.println("Unsupported LookAndFeel: " + laf);
		}catch (Exception exc) {
			System.err.println("Error loading " + laf + ": " + exc);
		}

	}

	/**
	 * Sets look and feel taken from ResourceBundles
	 */
	private void setUserLaf(String className){

		LookAndFeel oldLF = UIManager.getLookAndFeel();  //save if error in user-defined one

		try{
			Class cl = Class.forName(className);
			LookAndFeel lf = (LookAndFeel)cl.newInstance();
			if(!lf.isSupportedLookAndFeel()){
				return;
			}
		}catch(Throwable t){
			System.err.println("User defined LookAndFeel does not exist");
			return;
		}

		try{
			UIManager.setLookAndFeel(className);
		}catch(Throwable t) {
			// Ignore all exceptions in l&f leaving the original l&f
			try{
				UIManager.setLookAndFeel(oldLF);
				System.err.println("User defined l&f is problematic - old one is used instead: "+t);             
			}catch (Throwable e) {
				System.err.println("Error in L&F: "+e);           
			}
		}

	}//setUserLaf

	/**
	 * This routine just shows 572 values for L&F :-)
	 *//*
  public static void main(String[] args){

    new TopManager().showLaF();

    javax.swing.UIDefaults defaults = UIManager.getDefaults();
    System.out.println("Count Item = " + defaults.size());
    String[ ] colName = {"Key", "Value"};
    String[ ][ ] rowData = new String[ defaults.size() ][ 2 ];
    int i = 0;
    for(java.util.Enumeration e = defaults.keys(); e.hasMoreElements(); i++){
      Object key = e.nextElement();
      rowData[ i ][ 0 ] = key.toString();
      rowData[ i ][ 1 ] = ""+defaults.get(key);
    }
    javax.swing.JFrame f = new javax.swing.JFrame("UIDefaults Key-Value sheet");
    javax.swing.JTable t = new javax.swing.JTable(rowData, colName);
    f.setContentPane(new javax.swing.JScrollPane(t));
    f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    f.pack();
    f.setVisible(true);
  } */


}

