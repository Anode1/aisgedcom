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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;
import org.is.ais.gedcom.Constants;
import org.is.ais.gedcom.Parameters;
import org.is.ais.gedcom.Processor;
import org.is.ais.gedcom.Result;
import org.is.ais.gedcom.Strings;
import org.is.ais.gedcom.tworelatives.Encodings;

/**
 * Panel showing the name of the user, current logging time
 * and last login time in the MainMenuPanel.
 */
public class Atree2GedcomPanel extends JPanel{

	private boolean initialized;	
	
	private JButton processButton;
	private JPanel centralPanel;
	private JTextField inputTextField = new JTextField();
	//private JButton resetButton;
	private JButton inputFileBrowseButton;
	//private JComboBox langsComboBox = new JComboBox();

	
	private static Logger log = Logger.getLogger(Atree2GedcomPanel.class);
	
		
	public void init(){
		if(initialized)
			return;
		initialized=true;
		
		this.setBorder(BorderFactory.createEmptyBorder(0,5,5,5));
		setLayout(new BorderLayout());
		centralPanel=this;

		//northPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		//
		//central panel - input controls
		//
		final JPanel panel = new JPanel();

		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();            
		c.insets=new Insets(5,5,5,5);

		//panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,20,20,30));
		
		Dimension textFieldsDimension=new Dimension(350,inputTextField.getPreferredSize().height);		
		
		JLabel l2=new JLabel("ATree file:");
		c.gridx=1; c.gridy=1; c.anchor=GridBagConstraints.EAST;
		gridbag.setConstraints(l2, c);
		panel.add(l2, c);           

		inputTextField.setMinimumSize(textFieldsDimension);
		inputTextField.setPreferredSize(textFieldsDimension);
		
		final String inputFile = Config.getApplicationRoot() + File.separator + Constants.DEFAULT_ATREE_FILE;
		inputTextField.setText(inputFile);
		
		c.gridx=2; c.anchor=GridBagConstraints.WEST;
		gridbag.setConstraints(inputTextField, c);
		panel.add(inputTextField, c);

		inputFileBrowseButton = new JButton("Browse");
		//browseButton.setEnabled(false);
		c.gridx=3;
		gridbag.setConstraints(inputFileBrowseButton, c);
		panel.add(inputFileBrowseButton,c);
		
		inputFileBrowseButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				GUIManager.statusBar.showStatus("");
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Select atree File");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setMultiSelectionEnabled(false);
				
				chooser.addChoosableFileFilter(new FileFilter(){
					    public boolean accept(File f) {
					        if (f.isDirectory()) {
					            return true;
					        }
						    if(f.getName().toLowerCase().endsWith(".txt"))
					        	return true;
			                return false;
					    }
					    public String getDescription() {
					        return "atree pedigree files";
					    }
				});
				
				File file = new File(inputFile);
				//chooser.setCurrentDirectory(Config.getApplicationRoot());
				chooser.setSelectedFile(file);
				
				int status = chooser.showOpenDialog(null);
				if(status == JFileChooser.APPROVE_OPTION){

					File f = chooser.getSelectedFile();
					inputTextField.setText(f.getAbsolutePath());
				}
			}
		});
		
		/*
		final JLabel l1=new JLabel("Language:");
		c.gridx=1; c.gridy=6; c.anchor=GridBagConstraints.EAST;
		panel.add(l1, c);

		c.gridx=2; c.anchor=GridBagConstraints.WEST;
		panel.add(langsComboBox, c);
		
		
		String[] encodings = Encodings.loadSupportedEncoding();
		for(int i=0; i<encodings.length; i++){
			langsComboBox.addItem(encodings[i]);
		}
		
		String defaultEncoding = UserParameters.getString(Constants.LANG_KEY);
		 
		if(defaultEncoding!=null && !defaultEncoding.equals("")){
			langsComboBox.setSelectedItem(defaultEncoding);
			Parameters.addProperty(Constants.LANG_KEY, defaultEncoding);
		}
		else{
			resetLangToDefault();
		}
		
		langsComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				String selected = (String)langsComboBox.getSelectedItem();
				//UserParameters.addProperty(Constants.LANG_KEY, selected);
				Parameters.addProperty(Constants.LANG_KEY, selected);
			}
		});
		
	   */

		
		c.gridx=2; c.gridy=10; c.gridwidth=1; c.anchor=GridBagConstraints.EAST;
		
		processButton = new JButton("Process");
		gridbag.setConstraints(processButton, c);
		panel.add(processButton,c);
		
		processButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				//valueTextField.setText(""); //clean 
				GUIManager.statusBar.showStatus("Processing...");
				
				Parameters.getInstance().put(Constants.INPUT_FILE_KEY, inputTextField.getText().trim());

				processButton.setEnabled(false);
				inputFileBrowseButton.setEnabled(false);
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				
				new SwingWorker(){
				    public Object construct() {
				    	
				    	final Result result = process();
						
						SwingUtilities.invokeLater(new Runnable(){
							public void run(){
								if(result.criticalMessage!=null){
									Toolkit.getDefaultToolkit().beep();
									JOptionPane.showMessageDialog(centralPanel, result.criticalMessage, "ERROR", JOptionPane.ERROR_MESSAGE);
									GUIManager.statusBar.showStatus("Finished: " + result.criticalMessage);
								}
								else if(result.infoMessage!=null){
									JOptionPane.showMessageDialog(centralPanel, result.infoMessage);
									GUIManager.statusBar.showStatus("Finished: Success");
								}
								else{
									GUIManager.statusBar.showStatus("Finished: Success");	
								}
								
								processButton.setEnabled(true);								
								inputFileBrowseButton.setEnabled(true);
								setCursor(null);
							}
						});  
						return new Object();
				    }

				}.start();  //Start the background thread				

			}
		});	 		
		
		//////////////////////////////////////////////
/*		
		c.gridx=1; c.gridy=11; 

		resetButton = new JButton("Reset to Defaults");
		//browseButton.setEnabled(false);
		gridbag.setConstraints(resetButton, c);
		panel.add(resetButton,c);
		
		resetButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){


			}
		});	
	*/	
		/*
		JLabel resetLabel=new JLabel("<html>You can reset directories and<br> output filename to the default values</html>");
		c.gridx=2; c.anchor=GridBagConstraints.WEST;
		gridbag.setConstraints(resetLabel, c);
		panel.add(resetLabel, c);           
		*/
		
		add(panel, BorderLayout.CENTER);
		
		//
		//south panel
		//
		/*
		JPanel southPanel = new JPanel();
		add(southPanel, BorderLayout.SOUTH);  
		southPanel.setLayout(new java.awt.FlowLayout());

		southPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		*/
	} 


	private Result process(){
		
		Result result=new Result();
		try{
	
			Processor.process(result);
			
			//save directories - only if processing was successful
			//UserParameters.saveInputFile(Parameters.getString(Constants.INPUT_FILE_KEY));
			//UserParameters.saveId(Parameters.getString(Constants.START_ID_KEY));
			
		}
		catch(Exception e){
			log.error("", e);
			if(e.getMessage()==null){
				result.criticalMessage="Internal error"; //for GUI
			}
			else{
				result.criticalMessage=e.getMessage(); //for GUI
			}
		}
	
		return result;
	}
	
/*
	void resetLangToDefault(){
		String defaultLang=Strings.getDefault();
		Parameters.addProperty(Constants.LANG_KEY, defaultLang);
		langsComboBox.setSelectedItem(defaultLang);
	}	
*/
}

