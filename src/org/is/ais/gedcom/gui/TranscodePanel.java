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
import java.io.File;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
public class TranscodePanel extends JPanel{

	private boolean initialized;
	
	private JButton processButton;
	private JPanel centralPanel;
	private JTextField inputTextField = new JTextField();
	//private JButton resetButton;
	private JButton inputFileBrowseButton;
	private JComboBox fromComboBox = new JComboBox();
	private JComboBox toComboBox = new JComboBox();
	
	
	private static Logger log = Logger.getLogger(TranscodePanel.class);
	
	
	public void init(){
		if(initialized)
			return;
		initialized=true;
		
		this.setBorder(BorderFactory.createEmptyBorder(0,5,5,5));
		setLayout(new BorderLayout());
		centralPanel=this;

		//
		//central panel - input controls
		//
		final JPanel panel = new JPanel();

		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();            
		c.insets=new Insets(5,5,5,5);

		Dimension textFieldsDimension=new Dimension(350,inputTextField.getPreferredSize().height);		
		
		JLabel l2=new JLabel("Input file:");
		c.gridx=1; c.gridy=1; c.anchor=GridBagConstraints.EAST;
		gridbag.setConstraints(l2, c);
		panel.add(l2, c);           

		inputTextField.setMinimumSize(textFieldsDimension);
		inputTextField.setPreferredSize(textFieldsDimension);
		
		final String inputFile = Config.getApplicationRoot();
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
				        	return true;
					    }
					    public String getDescription() {
					        return "Input file";
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
		
		JPanel encPanel=new JPanel();
		encPanel.setBorder(BorderFactory.createEtchedBorder());
		encPanel.setLayout(new FlowLayout());

		final JLabel labelFrom=new JLabel("From:");
		
		encPanel.add(labelFrom);
		
		encPanel.add(fromComboBox);
		
		final JLabel labelTo=new JLabel("To:");
		encPanel.add(labelTo);

		encPanel.add(toComboBox);
		
		loadEncodings(); //populate, preselect comboboxes (dropdowns)
		
		JCheckBox cb1 = new JCheckBox("phonetic ('translit')", false);
		cb1.addItemListener(
		    new ItemListener() {
		        public void itemStateChanged(ItemEvent e) {
		        	boolean val= (e.getStateChange() == ItemEvent.SELECTED);
		        	if(val)
		        		Parameters.addProperty(Constants.TO_TRANSLIT, "true");
		        	else
		        		Parameters.addProperty(Constants.TO_TRANSLIT, "false");
		        }
		    }
		);
		encPanel.add(cb1);

		c.gridx=1; c.gridy=2; c.gridwidth=6; 
		gridbag.setConstraints(encPanel, c);
		panel.add(encPanel, c);
		
		//
		//Process button
		//
		
		c.gridx=3; c.gridy=4; c.gridwidth=1;
		
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
	
	
	private void loadEncodings(){
	
		try{
			String[] encodings = Encodings.loadSupportedEncoding();
			
			Map map = Charset.availableCharsets();
			Iterator itr = map.keySet().iterator();
			while(itr.hasNext()){
				String id=(String)itr.next();
				fromComboBox.addItem(id);
				toComboBox.addItem(id);
			}

			//preselect
			String defaultEncoding = UserParameters.getString(Constants.E1);
			if(defaultEncoding!=null && !defaultEncoding.equals("")){
				fromComboBox.setSelectedItem(defaultEncoding);
				Parameters.addProperty(Constants.E1, defaultEncoding);
			}
			else{
				String defaultEnc="windows-1251";
				Parameters.addProperty(Constants.E1, defaultEnc);
				fromComboBox.setSelectedItem(defaultEnc);
			}
			
			String defaultEncoding2 = UserParameters.getString(Constants.E2);
			if(defaultEncoding2!=null && !defaultEncoding2.equals("")){
				toComboBox.setSelectedItem(defaultEncoding);
				Parameters.addProperty(Constants.E2, defaultEncoding2);
			}
			else{
				String defaultEnc="UTF-8";
				Parameters.addProperty(Constants.E2, defaultEnc);
				toComboBox.setSelectedItem(defaultEnc);
			}
			
			fromComboBox.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					String selected = (String)fromComboBox.getSelectedItem();
					//UserParameters.addProperty(Constants.LANG_KEY, selected);
					Parameters.addProperty(Constants.E1, selected);
				}
			});
			
			toComboBox.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					String selected = (String)toComboBox.getSelectedItem();
					//UserParameters.addProperty(Constants.LANG_KEY, selected);
					Parameters.addProperty(Constants.E2, selected);
				}
			});
						
		}
		catch(Exception e){
			log.error("", e);
		}
	
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
	
}

