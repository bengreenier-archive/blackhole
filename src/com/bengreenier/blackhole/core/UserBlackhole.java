package com.bengreenier.blackhole.core;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.bengreenier.blackhole.util.Lock;

/**
 * The default state
 * that all users get
 * where both a server (if exits exist)
 * and a client (to send files) run.
 * 
 * TODO This code will support os specific
 * integrations, like right click -> blackhole
 * and other usercentric stylings.
 * 
 * @author B3N
 *
 */
public class UserBlackhole {

	public static void main(String[] args) {
		new UserBlackhole(args).start();
	}

	private String[] args;
	private Properties prop;
	private Lock lock;
	private JFrame frame;

	public UserBlackhole(String[] args) {
		this.args = args;
		this.prop = new Properties();
		this.frame = new JFrame();
		this.lock = new Lock("lock.lock");

		//try to load properties from ./blackhole.config	
		try {
			FileInputStream is = new FileInputStream("blackhole.config");
			prop.loadFromXML(is);
			is.close();
		} catch (FileNotFoundException e) {
			writePropDefaults();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (lock.isLocked())
			exit();
	}


	public UserBlackhole start() {
		frame.setIconImage(new ImageIcon("res/blackhole.png").getImage());
		frame.setTitle("Blackhole");
		frame.setUndecorated(true);
		frame.setBackground(new Color(0,0,0,0));
		frame.setBounds(Integer.parseInt(prop.getProperty("location-x")), Integer.parseInt(prop.getProperty("location-y")), 183, 179);
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				exit();

			}
		});
		frame.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent arg0) {

				if (arg0.getButton() == 0) {
					//TODO make this not "jump" to top left to mouse
					int oX = (int)(frame.getLocation().getX()+arg0.getX());
					int oY = (int)(frame.getLocation().getY()+arg0.getY());


					frame.setLocation(oX, oY);
				}
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}});

		JLabel label = new JLabel(new ImageIcon("res/blackhole.png"));
		label.setBounds(0, 0,183,179);

		JLabel xout = new JLabel("x");
		xout.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				frame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				frame.setCursor(Cursor.getDefaultCursor());

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				askWindowExit();

			}});
		xout.setBounds(173, 0, 10, 10);


		final JPopupMenu rightClickMenu = new JPopupMenu();
		JMenuItem rightClickExit = new JMenuItem("Exit");
		rightClickExit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				askWindowExit();

			}});
		rightClickMenu.add(rightClickExit);




		frame.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				if (arg0.getButton() == MouseEvent.BUTTON3)
					rightClickMenu.show(frame, arg0.getX(),arg0.getY());

			}});


		frame.add(label);
		frame.add(xout);

		frame.repaint();
		return this;
	}

	public UserBlackhole exit() {
		writePropExit();

		//try to save properties to ./blackhole.config
		try{
			FileOutputStream os = new FileOutputStream("blackhole.config");
			prop.storeToXML(os, null);

			os.close();
		}catch (Exception e) {
			e.printStackTrace();
		}

		//i don't really like this line...
		frame.dispose();
		
		//release the lock
		lock.release();
		
		return this;
	}

	private void askWindowExit() {
		//this results in the window calling exit()
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	private void writePropDefaults() {
		prop.setProperty("save-location", "true");
		prop.setProperty("location-x", "100");
		prop.setProperty("location-y", "100");
	}

	private void writePropExit() {
		if (frame != null) {
			prop.setProperty("location-x", ""+(int)frame.getLocation().getX());
			prop.setProperty("location-y", ""+(int)frame.getLocation().getY());
		}
	}


}
