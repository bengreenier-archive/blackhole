package com.bengreenier.blackhole.core;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.bengreenier.blackhole.server.TCPFileProcessor;
import com.bengreenier.blackhole.util.ByteArray;
import com.bengreenier.blackhole.util.FileIO;
import com.bengreenier.blackhole.util.Marker;
import com.bengreenier.blackhole.util.Port;
import com.bengreenier.blackhole.util.StaticStrings;
import com.matthewmichaud.blackhole.graphics.Animation;

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
	private JFrame frame;

	int mouse_click_X = 0;
	int mouse_click_Y = 0;
	
	private String ipAddress = "127.0.0.1";
	private TCPFileProcessor tcp;

	public UserBlackhole(String[] args) {
		this.args = args;
		this.prop = new Properties();
		this.frame = new JFrame();

		//just cause
		Thread.currentThread().setName("UserBlackhole");
		
		//see if the .config file exists, if not set the default value.
		//there may be some redundancy here, addressing the below load. address in the future
		File file = new File(StaticStrings.getString("config"));
		if(!file.exists()) {
			// make the file then populate it
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			writePropDefaults();
		} 
		
		//try to load properties from ./blackhole.config	
		try {
			FileInputStream is = new FileInputStream(file);
			prop.loadFromXML(is);
			is.close();
		} catch (FileNotFoundException e) {
			writePropDefaults();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		LoadTray();
	}
	private void LoadTray() {
		// Add the icon to the system tray
		final SystemTray tray = SystemTray.getSystemTray();
		final TrayIcon trayIcon = new TrayIcon(createImage("res/drawable/icon.png", "Tray Icon"));
		PopupMenu popup = new PopupMenu();
		
		MenuItem stahpItem = new MenuItem("Stahp");
		MenuItem ipItem = new MenuItem("Set IP");
		
		popup.add(ipItem);
		popup.add(stahpItem);
		
		trayIcon.setPopupMenu(popup);
		
		stahpItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                askWindowExit();
            }
        });
		ipItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				askSetIP();
			}
		});
		
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added");
		}
	}
	//Obtain the image
    protected static Image createImage(String path, String description) {
    	Image image = null;
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(image == null) {
			System.out.println("Image was null");
		}
    	return image;
    }

	public UserBlackhole start() {
		ipAddress = prop.getProperty("ip-address");
		
		tcp = new TCPFileProcessor();
		tcp.start();
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

		tcp.cleanCloseServer();
		
		//i don't really like this line...
		frame.dispose();

		//release the lock
		//lock.release();
		System.exit(0);
		return this;
	}

	private void askWindowExit() {
		//this results in the window calling exit()
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
	
	private void askSetIP() {
		//this results is the window popping up a new input dialog to change the IP address
		ipAddress = (String)JOptionPane.showInputDialog(
		                    frame,
		                    "Enter in new IP address",
		                    "Set IP",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    null,
		                    ipAddress);
	}

	private void writePropDefaults() {
		prop.setProperty("save-location", "true");
		prop.setProperty("location-x", "100");
		prop.setProperty("location-y", "100");
		prop.setProperty("ip-address", "127.0.0.1");
		try {
			FileOutputStream os = new FileOutputStream(StaticStrings.getString("config"));
			prop.storeToXML(os, "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writePropExit() {
		if (frame != null) {
			prop.setProperty("location-x", ""+(int)frame.getLocation().getX());
			prop.setProperty("location-y", ""+(int)frame.getLocation().getY());
		}
		if(ipAddress!=null&&!ipAddress.equals(prop.getProperty("ip-address"))) {
			prop.setProperty("ip-address", ipAddress);
		}
	}

	private void filesDropped(final java.util.List<Object> list) {
		//TODO prompt for server information  
		final int port = Port.DEFAULT;

		//open a thread to send the files.
		new Thread(){
			@Override
			public void run(){

				Socket socket = new Socket();
				try {
					socket.connect(new InetSocketAddress(ipAddress,port));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {
					ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());

					for (Object o : list) {
						if (o instanceof File)
							if (((File)o).exists()){
								//System.out.println("o.tostring = "+o.toString());
								oout.writeObject(new Marker.FileHeaderMarker(((File)o).getPath(),((File)o).getPath().replace("\\","_").replace("/", "-").replace(":", "_")));
								oout.writeObject(new ByteArray(FileIO.getByteArray(((File)o).getPath())));
								oout.writeObject(new Marker.GenericFooterMarker());
							}else{
								Logger.getLogger("com.bengreenier.blackhole").log(Level.INFO,"Dropped File doesn't exist");
							}

					}

					oout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}


}
