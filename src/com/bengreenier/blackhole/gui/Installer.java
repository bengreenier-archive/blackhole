package com.bengreenier.blackhole.gui;

import java.awt.EventQueue;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.bengreenier.blackhole.util.StaticStrings;

public class Installer {

	private JFrame frmInstaller;
	private JLabel label;
	private JProgressBar progressBar;
	private JFileChooser fc;
	private int counter;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Installer();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public Installer() {
		counter = 0;
		initialize();
		if (new File(StaticStrings.getString("archive-location")).exists()) {
			installFromFile(StaticStrings.getString("archive-location"));
			complete();
		} else {
			JOptionPane.showMessageDialog(frmInstaller, "Update archive file not found. You will be asked to select it.");
			fc.showOpenDialog(frmInstaller);
			if (fc.getSelectedFile() != null) {
				installFromFile(fc.getSelectedFile().getPath());
				complete();
			} else {
				JOptionPane.showMessageDialog(frmInstaller, "Update archive not selected. Exiting installer.");
				delete();
			}
		}
			
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmInstaller = new JFrame();
		frmInstaller.setAlwaysOnTop(true);
		frmInstaller.setResizable(false);
		frmInstaller.setTitle("Installer");
		frmInstaller.setBounds(100, 100, 390, 160);
		frmInstaller.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmInstaller.getContentPane().setLayout(null);
		frmInstaller.setVisible(true);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(125, 70, 146, 14);
		frmInstaller.getContentPane().add(progressBar);
		
		label = new JLabel("");
		label.setBounds(10, 45, 364, 14);
		frmInstaller.getContentPane().add(label);
		
		fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
		fc.setFileFilter(new FileNameExtensionFilter("update.zip","update.zip"));
		fc.setDialogTitle("Select Update.zip");
		
	}
	
	private void installFromFile(String filename) {
		try {
			extractFolder(".",filename);
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//from http://stackoverflow.com/questions/981578/how-to-unzip-files-recursively-in-java
	private void extractFolder(String newPath, String zipFile) throws ZipException, IOException 
	{
	    //System.out.println(zipFile);
	    int BUFFER = 2048;
	    File file = new File(zipFile);

	    ZipFile zip = new ZipFile(file);
	    //String newPath = zipFile.substring(0, zipFile.length() - 4);

	    new File(newPath).mkdir();
	    Enumeration<? extends ZipEntry> zipFileEntries = zip.entries();

	    // Process each entry
	    while (zipFileEntries.hasMoreElements())
	    {
	        // grab a zip file entry
	        ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
	        String currentEntry = entry.getName();
	        File destFile = new File(newPath, currentEntry);
	        
	        File destinationParent = destFile.getParentFile();

	        // create the parent directory structure if needed
	        destinationParent.mkdirs();

	        if (!entry.isDirectory())
	        {
	            BufferedInputStream is = new BufferedInputStream(zip
	            .getInputStream(entry));
	            int currentByte;
	            // establish buffer for writing file
	            byte data[] = new byte[BUFFER];

	            // write the current file to disk, erasing it if its already there.
	            if (destFile.exists())
	            	destFile.delete();
	            
	            //create an empty file
	            destFile.createNewFile();
	            
	            //do the actual writing
	            FileOutputStream fos = new FileOutputStream(destFile);
	            BufferedOutputStream dest = new BufferedOutputStream(fos,
	            BUFFER);

	            // read and write until last byte is encountered
	            while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
	                dest.write(data, 0, currentByte);
	            }
	            dest.flush();
	            dest.close();
	            
	           
	            
	            is.close();
	        }
	        
	        counter++;
            progressBar.setValue((int)(((float)counter/(float)zip.size())*100));
            label.setText(destFile.getPath());
            
	        if (currentEntry.endsWith(".zip"))
	        {
	            // found a zip file, try to open
	            extractFolder(newPath+currentEntry.substring(0, currentEntry.length()-4),destFile.getAbsolutePath());
	        }
	    }
	    zip.close();
	}
	
	private void complete() {
		JOptionPane.showMessageDialog(frmInstaller, "Update Complete.");
		delete();
	}
	
	private void delete() {
		frmInstaller.dispose();
	}
}
