package com.matthewmichaud.blackhole.graphics;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Animation extends JPanel {
	BufferedImage image;
	BufferedImage[] frames;
	int current_frame;
	int fps = 24;
	int width = 128;
	int height = 128;
	boolean isAnimating = true;
	
	public Animation(BufferedImage image, int[] frame_position, int width, int height) {
		this.image = image;
		frames = new BufferedImage[frame_position.length/2];
		for(int i = 0; i < frames.length*2; i+=2) {
			frames[i/2] = image.getSubimage(frame_position[i], frame_position[i+1], width, height);
		}
		this.width = width;
		this.height = height;
		current_frame=0;
		animate.start();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC, 1.0f));
        g2d.drawImage(frames[current_frame],
                0, 0, 
                null);
    }
	
	public void setFPS(int fps) {
		this.fps = fps;
	}
	public int getFPS() {
		return fps;
	}
	public void setAnimate(boolean isAnimating) {
		this.isAnimating = isAnimating;
	}
	public boolean getAnimate() {
		return isAnimating;
	}
	
	Thread animate = new Thread() {
		boolean running = true;
		@Override
		public void run() {
			while(running) {
				if(isAnimating) {
					current_frame++;
					if(current_frame >= frames.length) {
						current_frame = 0;
					}
					repaint();
					wait((double)1/fps);
				}
			}
		}
		private void wait(double n){
	        long t0, t1;
	        t0 =  System.currentTimeMillis();
	        do{
	            t1 = System.currentTimeMillis();
	        }
	        while ((t1 - t0) < (n * 1000));
	    }
	};
}
