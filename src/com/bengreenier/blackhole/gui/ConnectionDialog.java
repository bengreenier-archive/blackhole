package com.bengreenier.blackhole.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingConstants;
import javax.swing.JButton;

public class ConnectionDialog {

	private JFrame frmBlackholeConnection;
	private JTextField remoteServer;
	private JTextField remotePort;
	private JTextField remoteFolder;
	private JLabel fileLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConnectionDialog window = new ConnectionDialog();
					window.frmBlackholeConnection.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ConnectionDialog() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBlackholeConnection = new JFrame();
		frmBlackholeConnection.setResizable(false);
		frmBlackholeConnection.setTitle("Blackhole - New Connection");
		frmBlackholeConnection.setBounds(100, 100, 450, 300);
		frmBlackholeConnection.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBlackholeConnection.getContentPane().setLayout(null);
		
		JLabel lblRemoteAddress = new JLabel("Remote Address:");
		lblRemoteAddress.setBounds(25, 85, 128, 14);
		frmBlackholeConnection.getContentPane().add(lblRemoteAddress);
		
		remoteServer = new JTextField();
		remoteServer.setBounds(163, 82, 147, 20);
		frmBlackholeConnection.getContentPane().add(remoteServer);
		remoteServer.setColumns(10);
		
		JLabel lblRemotePort = new JLabel("Remote Port:");
		lblRemotePort.setBounds(25, 123, 74, 14);
		frmBlackholeConnection.getContentPane().add(lblRemotePort);
		
		remotePort = new JTextField();
		remotePort.setBounds(163, 120, 147, 20);
		frmBlackholeConnection.getContentPane().add(remotePort);
		remotePort.setColumns(10);
		
		fileLabel = new JLabel("");
		fileLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fileLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		fileLabel.setBounds(10, 11, 414, 60);
		frmBlackholeConnection.getContentPane().add(fileLabel);
		
		JLabel lblRemoteFolder = new JLabel("Remote Folder:");
		lblRemoteFolder.setBounds(25, 165, 89, 14);
		frmBlackholeConnection.getContentPane().add(lblRemoteFolder);
		
		remoteFolder = new JTextField();
		remoteFolder.setBounds(163, 162, 147, 20);
		frmBlackholeConnection.getContentPane().add(remoteFolder);
		remoteFolder.setColumns(10);
		
		JButton btnEstablishSync = new JButton("Establish Sync");
		btnEstablishSync.setBounds(25, 211, 133, 23);
		btnEstablishSync.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				askWindowExit();	
			}
		});
		frmBlackholeConnection.getContentPane().add(btnEstablishSync);
		
		frmBlackholeConnection.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
	}
	
	private void askWindowExit() {
		frmBlackholeConnection.dispatchEvent(new WindowEvent(frmBlackholeConnection, WindowEvent.WINDOW_CLOSING));
	}
	
	private void exit() {
		//register the connection in the connections list or something like that.
	}
}
