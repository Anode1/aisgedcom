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
public class MainPanel extends JPanel{

	private JButton processButton;
	private JPanel centralPanel;
	private JTextField inputTextField = new JTextField();
	private JTextField idField = new JTextField();
	private JButton resetButton;
	private JButton inputFileBrowseButton;
	private JComboBox langsComboBox = new JComboBox();
	private JTextField id2Field=new JTextField();
	public boolean findRelations;

	private static Logger log = Logger.getLogger(MainPanel.class);
	
	
	public MainPanel()throws Exception{

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
		
		JLabel l2=new JLabel("GEDCOM file:");
		c.gridx=1; c.gridy=1; c.anchor=GridBagConstraints.EAST;
		gridbag.setConstraints(l2, c);
		panel.add(l2, c);           

		inputTextField.setMinimumSize(textFieldsDimension);
		inputTextField.setPreferredSize(textFieldsDimension);
		final String inputFile = UserParameters.getInputFile();
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
				chooser.setDialogTitle("Select GEDCOM File");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setMultiSelectionEnabled(false);
				
				chooser.addChoosableFileFilter(new FileFilter(){
					    public boolean accept(File f) {
					        if (f.isDirectory()) {
					            return true;
					        }
						    if(f.getName().toLowerCase().endsWith(".ged"))
					        	return true;
			                return false;
					    }
					    public String getDescription() {
					        return "GED pedigree files";
					    }
				});
				
				File file = new File(inputFile);
				//chooser.setCurrentDirectory(dir);
				chooser.setSelectedFile(file);
				
				int status = chooser.showOpenDialog(null);
				if(status == JFileChooser.APPROVE_OPTION){

					File f = chooser.getSelectedFile();
					inputTextField.setText(f.getAbsolutePath());
				}
			}
		});

		//////////////////////////////////////////////
		
		c.gridx=2; c.gridy=2; //c.anchor=GridBagConstraints.EAST;
		JCheckBox cb1 = new JCheckBox("Do not show first letter", false);
		cb1.addItemListener(
		    new ItemListener() {
		        public void itemStateChanged(ItemEvent e) {
		        	boolean val= (e.getStateChange() == ItemEvent.SELECTED);
		        	if(val)
		        		Parameters.addProperty(Constants.ATREE_NO_PREFIX_KEY, "true");
		        	else
		        		Parameters.addProperty(Constants.ATREE_NO_PREFIX_KEY, "false");
		        }
		    }
		);
		panel.add(cb1,c);
		
		
		c.gridx=2; c.gridy=3; //c.anchor=GridBagConstraints.EAST;
		final JCheckBox cb = new JCheckBox("Generate "+ Constants.DEFAULT_ATREE_FILE, true);
		cb.addItemListener(
		    new ItemListener() {
		        public void itemStateChanged(ItemEvent e) {
		        	boolean val= (e.getStateChange() == ItemEvent.SELECTED);
		        	if(val)
		        		Parameters.addProperty(Constants.GENERATE_ATREE, "true");
		        	else
		        		Parameters.addProperty(Constants.GENERATE_ATREE, "false");
		        }
		    }
		);
		panel.add(cb,c);		

		
		c.gridx=2; c.gridy=4; //c.anchor=GridBagConstraints.EAST;
		final JCheckBox cb2 = new JCheckBox("In " + Constants.DEFAULT_DESC_FILE+" show only living and last " + Constants.NUM_GENERATIONS_LIKELY_NOT_LIVING+" generations", false);
		cb2.addItemListener(
		    new ItemListener() {
		        public void itemStateChanged(ItemEvent e) {
		        	boolean val= (e.getStateChange() == ItemEvent.SELECTED);
		        	if(val)
		        		Parameters.addProperty(Constants.LIVING_KEY, "true");
		        	else
		        		Parameters.addProperty(Constants.LIVING_KEY, "false");
		        }
		    }
		);
		panel.add(cb2,c);

		
		//////////////////////////////////////////////////////
		
		JPanel statPanel=new JPanel();
		statPanel.setBorder(BorderFactory.createEtchedBorder());
		statPanel.setLayout(new BoxLayout(statPanel, BoxLayout.X_AXIS));
		
		
		final JCheckBox cb5 = new JCheckBox("generate "+Constants.STAT_FILE, false);
		final JCheckBox cb14 = new JCheckBox("Show centuries statistics", false);
		final JCheckBox cb33 = new JCheckBox("generate "+Constants.ANOMALIES_FILE);
		cb14.setEnabled(false);
		cb33.setEnabled(false);		
		
		cb5.addItemListener(
		    new ItemListener() {
		        public void itemStateChanged(ItemEvent e) {
		        	boolean val= (e.getStateChange() == ItemEvent.SELECTED);
		        	if(val){
		        		Parameters.addProperty(Constants.GENERATE_STATISTICS, "true");
		        		cb14.setEnabled(true);
		        		cb33.setEnabled(true);
		        	}
		        	else{
		        		Parameters.addProperty(Constants.GENERATE_STATISTICS, "false");
		        		cb14.setEnabled(false);
		        		cb33.setEnabled(false);
		        	}
		        }
		    }
		);
		statPanel.add(cb5);
		
		cb14.addItemListener(
		    new ItemListener() {
		        public void itemStateChanged(ItemEvent e) {
		        	boolean val= (e.getStateChange() == ItemEvent.SELECTED);
		        	if(val)
		        		Parameters.addProperty(Constants.SHOW_CENTURIES, "true");
		        	else
		        		Parameters.addProperty(Constants.SHOW_CENTURIES, "false");
		        }
		    }
		);
		statPanel.add(cb14);
		
		cb33.addItemListener(
		    new ItemListener() {
		        public void itemStateChanged(ItemEvent e) {
		        	boolean val= (e.getStateChange() == ItemEvent.SELECTED);
		        	if(val)
		        		Parameters.addProperty(Constants.SHOW_ANOMALIES, "true");
		        	else
		        		Parameters.addProperty(Constants.SHOW_ANOMALIES, "false");
		        }
		    }
		);
		statPanel.add(cb33);		
		
		
		c.gridx=2; c.gridy=5; 
		panel.add(statPanel,c);
		
		
		//////////////////////////////////////////////
		
		JLabel l4=new JLabel("GEDCOM ID:");
		c.gridx=1; c.gridy=7; c.anchor=GridBagConstraints.EAST;
		panel.add(l4, c);
		
		idField.setColumns(16);
		idField.setMinimumSize(textFieldsDimension);
		idField.setPreferredSize(textFieldsDimension);	
		c.gridx=2;   c.anchor=GridBagConstraints.WEST;
		panel.add(idField,c);
		
		idField.setText(UserParameters.getId());
		
		//////////////////////////////////////////////
		
		
		JPanel relationsPanel=new JPanel();
		relationsPanel.setBorder(BorderFactory.createEtchedBorder());
		relationsPanel.setLayout(new FlowLayout());
		
		JCheckBox cb11 = new JCheckBox("Print relation between 2 IDs", false);

		relationsPanel.add(cb11);	
		
		final JLabel id2Label=new JLabel("ID for the 2nd person");
		relationsPanel.add(id2Label);
		id2Field.setColumns(16);
		relationsPanel.add(id2Field);
		id2Field.setText("");
		id2Field.addKeyListener(new KeyAdapter() {
	        public void keyTyped(KeyEvent e) {
	        	Parameters.addProperty(Constants.SECOND_ID_KEY, id2Field.getText());
	        }
	    });		
		
		final JLabel l1=new JLabel("Language (relation names only):");
		relationsPanel.add(l1);
		relationsPanel.add(langsComboBox);
		
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
		
		id2Label.setEnabled(false);
		id2Field.setEnabled(false);
		l1.setEnabled(false);
		langsComboBox.setEnabled(false);
		
		cb11.addItemListener(
			    new ItemListener() {
			        public void itemStateChanged(ItemEvent e) {
			        	boolean val= (e.getStateChange() == ItemEvent.SELECTED);
			        	findRelations=val;
		        		id2Label.setEnabled(val);
		        		id2Field.setEnabled(val);
		        		l1.setEnabled(val);
		        		langsComboBox.setEnabled(val);
		        		
		        		//disable/enable reports all components
		        		cb.setEnabled(!val);
		        		cb2.setEnabled(!val);
		        		cb5.setEnabled(!val);
	        			if(cb5.isSelected()){
		        			cb14.setEnabled(!val);
		        			cb33.setEnabled(!val);
		        		} //otherwise do not touch
	        			
	        			if(val){
	        				id2Label.setEnabled(true);
	        				id2Field.setEnabled(true);
	        				l1.setEnabled(true);
	        				langsComboBox.setEnabled(true);
	        			}
	        			else{
	        				findRelations=false;
	        			}
		        		
			        }
			    }
			);
		
		c.gridx=1; c.gridy=6; c.gridwidth=3;
		panel.add(relationsPanel,c);
	
	
		//////////////////////////////////////////////
		
		c.gridx=3; c.gridy=10; c.gridwidth=1;
		
		processButton = new JButton("Process");
		gridbag.setConstraints(processButton, c);
		panel.add(processButton,c);
		
		processButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				//valueTextField.setText(""); //clean 
				GUIManager.statusBar.showStatus("Processing...");
				
				Parameters.getInstance().put(Constants.INPUT_FILE_KEY, inputTextField.getText().trim());
				Parameters.getInstance().put(Constants.START_ID_KEY, idField.getText().trim());
				if(findRelations)
					Parameters.getInstance().put(Constants.SECOND_ID_KEY, id2Field.getText().trim());
				
				
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
		
		c.gridx=1; c.gridy=11; 

		resetButton = new JButton("Reset to Defaults");
		//browseButton.setEnabled(false);
		gridbag.setConstraints(resetButton, c);
		panel.add(resetButton,c);
		
		resetButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				inputTextField.setText(Config.getDefaultInputFile());
				//UserParameters.saveInputFile(inputTextField.getText());
				idField.setText(Config.getDefaultId());
				//UserParameters.saveId("");
				
				UserParameters.getInstance().clear();
			}
		});	
		
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
			UserParameters.saveInputFile(Parameters.getString(Constants.INPUT_FILE_KEY));
			UserParameters.saveId(Parameters.getString(Constants.START_ID_KEY));
			
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
	
	
	void resetLangToDefault(){
		String defaultLang=Strings.getDefault();
		Parameters.addProperty(Constants.LANG_KEY, defaultLang);
		langsComboBox.setSelectedItem(defaultLang);
	}	

}

