package com.bengreenier.blackhole.core;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JLabel;

import com.bengreenier.blackhole.util.FileIO;
import com.bengreenier.blackhole.util.MD5;
import com.bengreenier.blackhole.util.StaticStrings;

public class Syncer {

	private JFrame frmBlackholeSyncer;
	
	//TODO mark transfer process with this
	private JProgressBar progressBar;
	
	private JFileChooser fc;
	private Timer timer;
	private static int INTERVAL = 5000;//refresh rate

	//the combined efforts of these two, provide both file change/add notifs, and file deletion notifs.
	private ConcurrentHashMap<String,String> map;
	private ArrayList<File> directoryFiles;
	
	private void index(File file) {
		MD5 md5 = new MD5(FileIO.getByteArray(file.getPath()));
		if (map.containsKey(file.getPath())) {
			if (!map.get(file.getPath()).equals(md5.getStringDigest())) {
				map.put(file.getPath(), md5.getStringDigest());
				Logger.getLogger("com.bengreenier.blackhole").log(Level.INFO,file.getPath()+" updated.");
			}
		}else{
			map.put(file.getPath(), md5.getStringDigest());
			Logger.getLogger("com.bengreenier.blackhole").log(Level.INFO,file.getPath()+" indexed.");
		}

	}

	private void iterate(File[] files) {
		for (File file : files) {
			if (file.isDirectory()) {
				iterate(file.listFiles()); // Calls same method again.
			} else {
				directoryFiles.add(file);
				index(file);
			}
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Syncer window = new Syncer();
					window.frmBlackholeSyncer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Syncer() {
		map = new ConcurrentHashMap<String,String>();
		directoryFiles = new ArrayList<File>();
		initialize();

		JOptionPane.showMessageDialog(frmBlackholeSyncer, "Select directory to sync...");
		fc.showOpenDialog(frmBlackholeSyncer);
		if (fc.getSelectedFile() != null) {
			final File directory = fc.getSelectedFile();
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask(){

				@Override
				public void run() {
					directoryFiles.clear();
					iterate(directory.listFiles());
					for (String s : map.keySet()) {
						boolean del = true;
						for (File f : directoryFiles)
							if (f.getPath().equals(s))
								del = false;
						if (del) {
							Logger.getLogger("com.bengreenier.blackhole").log(Level.INFO,s+" rm-ed.");
							map.remove(s);
						}
					}
						
				}}, 10, INTERVAL);
		} else {
			JOptionPane.showMessageDialog(frmBlackholeSyncer, "No directory selected. Exiting.");
			askWindowExit();
		}


	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBlackholeSyncer = new JFrame();
		frmBlackholeSyncer.setTitle("Blackhole Syncer");
		frmBlackholeSyncer.setIconImage(new ImageIcon(StaticStrings.getString("syncer")).getImage());
		frmBlackholeSyncer.setResizable(false);
		frmBlackholeSyncer.setUndecorated(true);
		frmBlackholeSyncer.setBackground(new Color(0,0,0,0));
		frmBlackholeSyncer.setBounds(100, 100, 71, 71);
		frmBlackholeSyncer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBlackholeSyncer.getContentPane().setLayout(null);


		progressBar = new JProgressBar();
		progressBar.setBounds(10, 29, 51, 14);
		frmBlackholeSyncer.getContentPane().add(progressBar);

		//this is inefficent @2x ImageIcon
		JLabel syncIcon = new JLabel(new ImageIcon(new ImageIcon(StaticStrings.getString("syncer")).getImage().getScaledInstance(71,71, Image.SCALE_SMOOTH)));
		syncIcon.setBounds(0, 0, 71,71);
		frmBlackholeSyncer.getContentPane().add(syncIcon);

		//directory chooser
		fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setDialogTitle("Select Syncer Directory");
		
		final JPopupMenu rightClickMenu = new JPopupMenu();
		JMenuItem rightClickExit = new JMenuItem("Exit");
		rightClickExit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				askWindowExit();
			}});
		rightClickMenu.add(rightClickExit);
		
		frmBlackholeSyncer.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {}

			@Override
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				if (arg0.getButton() == MouseEvent.BUTTON3)
					rightClickMenu.show(frmBlackholeSyncer, arg0.getX(),arg0.getY());
					
			}});
	}

	private void askWindowExit() {
		//this results in the window calling exit()
		if (timer != null)
			timer.cancel();
		frmBlackholeSyncer.dispatchEvent(new WindowEvent(frmBlackholeSyncer, WindowEvent.WINDOW_CLOSING));
	}
}
