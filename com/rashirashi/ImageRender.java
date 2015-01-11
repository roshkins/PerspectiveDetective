package com.rashirashi;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Canvas;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Image;
import com.leapmotion.leap.ImageList;

import javax.swing.JFrame;

public class ImageRender extends Canvas implements Runnable
{

	private static final long	serialVersionUID	= 1L;

	public static final int		WIDTH				= 640, HEIGHT = 720;

	public int					low, high;

	private boolean				running				= true;
	private Thread				thread;

	static Toolkit				toolkit				= Toolkit.getDefaultToolkit();
	private BufferedImage		image;
	public int[]				pixels;
	int							backColor			= 0x000000;
	private boolean				scale				= true;

	public static void main(String[] args)
	{
		ImageRender display = new ImageRender();
		JFrame frame = new JFrame();
		frame.add(display);
		frame.setTitle("Perspective Detective");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension d = toolkit.getScreenSize();

		frame.setLocation((d.width - WIDTH) / 2, (d.height - HEIGHT) / 2);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.pack();
		display.start();
		
		//ImageList images = frame.images();
		//camera.updatePixels();
		//image(camera, 640 * image.id, 0)
	}

	public ImageRender()
	{
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		image = new BufferedImage(WIDTH, HEIGHT/3, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		
		//Leap Image Constructor
	
	}

	private void start()
	{
		run();
	}

	public void run()
	{
		Controller controller = new Controller();
		controller.setPolicy(Controller.PolicyFlag.POLICY_IMAGES);
		
		 int[] doubledBufferPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		
		while (running)
		{
			
			 Frame frame = controller.frame();
			 if(frame.isValid()){
			   ImageList images = frame.images();
			   for(Image image : images)
			   {
			     //Processing PImage class
			    // BufferedImage camera = [image.id()];
			   //  camera = createImage(image.width(), image.height(), RGB);
			  //////   camera.loadPixels();
			     
			     //Get byte array containing the image data from Image object
			     byte[] imageData = image.data();

			     //Copy image data into display object, in this case PImage defined in Processing
			     for(int i = 0; i < image.width() * image.height(); i++){
			      int r = (imageData[i] & 0xFF) << 16; //convert to unsigned and shift into place
			       int g = (imageData[i] & 0xFF) << 8;
			      int  b = imageData[i] & 0xFF;
			       doubledBufferPixels[i] =  r | g | b; 
			     }
			     
			     //Show the image
			//     camera.updatePixels();
			//     image(camera, 640 * image.id(), 0);  
			   }
			   pixels = doubledBufferPixels;
			   draw();
			 }
			
//			for (int x = 0; x < WIDTH * HEIGHT; x++)
//			{
//				pixels[x] = backColor;
//			}
			
			
		}	
	}

	public void draw()
	{
		BufferStrategy bs = this.getBufferStrategy();

		if (bs == null)
		{
			createBufferStrategy(3);
			return;
		}

		// 0x00B803
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);

		g.dispose();
		bs.show();
	}

	public void fillArea(int x1, int x2, int y1, int y2, int color)
	{
		for (int x = x1; x < x2; x++)
		{
			for (int y = y1; y < y2; y++)
			{
				pixels[y * WIDTH + x] = color;
			}
		}
	}

}
