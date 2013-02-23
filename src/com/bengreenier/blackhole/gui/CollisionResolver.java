package com.bengreenier.blackhole.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class CollisionResolver {

	private JFrame frmBlackholeCollision;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CollisionResolver window = new CollisionResolver();
					window.frmBlackholeCollision.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CollisionResolver() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBlackholeCollision = new JFrame();
		frmBlackholeCollision.setTitle("Blackhole - Collision Resolver");
		frmBlackholeCollision.setResizable(false);
		frmBlackholeCollision.setBounds(100, 100, 450, 300);
		frmBlackholeCollision.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBlackholeCollision.getContentPane().setLayout(null);
		
		JList<String> ourList = new JList<String>();
		ourList.setBounds(0, 202, 152, -163);
		frmBlackholeCollision.getContentPane().add(ourList);
		
		JList<String> theirList = new JList<String>();
		theirList.setBounds(292, 202, 152, -163);
		frmBlackholeCollision.getContentPane().add(theirList);
		
		JLabel lblOursVsTheirs = new JLabel("Ours vs Theirs");
		lblOursVsTheirs.setBounds(184, 114, 75, 14);
		frmBlackholeCollision.getContentPane().add(lblOursVsTheirs);
		
		JButton btnResolve = new JButton("Resolve");
		btnResolve.setBounds(184, 225, 75, 23);
		frmBlackholeCollision.getContentPane().add(btnResolve);
		
		JLabel fileLabel = new JLabel("");
		fileLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fileLabel.setBounds(0, 0, 444, 28);
		frmBlackholeCollision.getContentPane().add(fileLabel);
	}
}
