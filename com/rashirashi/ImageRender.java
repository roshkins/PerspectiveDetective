package com.rashirashi;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Canvas;
import java.awt.Graphics2D;
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

	public static final int		WIDTH				= 1280, HEIGHT = 720;

	public int					low, high;

	private boolean				running				= true;
	private Thread				thread;

	static Toolkit				toolkit				= Toolkit.getDefaultToolkit();
	private BufferedImage		imageB;
	private BufferedImage		imageC;
	public int[]				pixels;
	int							backColor			= 0x000000;
	private boolean				scale				= true;
	public int[] 				doubledBufferedPixels;

	public static void main(String[] args)
	{
		// Init camera args
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

	private int cameraNumber;
	public ImageRender()
	{
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		imageB = new BufferedImage(WIDTH, HEIGHT/3, BufferedImage.TYPE_INT_RGB);
		imageC = new BufferedImage(WIDTH, HEIGHT/3, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) imageB.getRaster().getDataBuffer()).getData();
		doubledBufferedPixels = ((DataBufferInt) imageC.getRaster().getDataBuffer()).getData();
		
		//Leap Image Constructor
		this.cameraNumber = cameraNumber;
	
	}

    public static BufferedImage joinBufferedImage(BufferedImage img1,BufferedImage img2) {

        //do some calculate first
        int offset  = 5;
        int wid = img1.getWidth()+img2.getWidth()+offset;
        int height = Math.max(img1.getHeight(),img2.getHeight())+offset;
        //create a new buffer and draw two image into the new image
        BufferedImage newImage = new BufferedImage(wid,height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        //fill background
        g2.setPaint(Color.BLACK);
        g2.fillRect(0, 0, wid, height);
        //draw image
        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, img1.getWidth()+offset, 0);
        g2.dispose();
        return newImage;
    }
	
	private void start()
	{
		run();
	}

	public void run()
	{
		Controller controller = new Controller();
		controller.setPolicy(Controller.PolicyFlag.POLICY_IMAGES);
		
		 
		 
		 int[] fullScreenData = doubledBufferedPixels;
		while (running)
		{
			
			 Frame frame = controller.frame();
			// images[0];
			// images[1];
			 
			 if(frame.isValid() && frame.images().count() >= 2){
			   ImageList images = frame.images();
			   
			   
			   
			  
			   
			  Image image = images.get(0);
			     //Processing PImage class
			    // BufferedImage camera = [image.id()];
			   //  camera = createImage(image.width(), image.height(), RGB);
			  //////   camera.loadPixels();
			     
			     //Get byte array containing the image data from Image object
			     
			     byte[] imageData = image.data();
			     
			     BufferedImage imageB = new BufferedImage(WIDTH/2, HEIGHT/3, BufferedImage.TYPE_INT_RGB);
				 BufferedImage imageC = new BufferedImage(WIDTH/2, HEIGHT/3, BufferedImage.TYPE_INT_RGB);
				 
				 int[] imageBPixels = ((DataBufferInt) imageB.getRaster().getDataBuffer()).getData();
				 int[] imageCPixels = ((DataBufferInt) imageC.getRaster().getDataBuffer()).getData();

			     //Copy image data into display object, in this case PImage defined in Processing
			     for(int i = 0; i < image.width() * image.height(); i++){
			      int r = (imageData[i] & 0xFF) << 16; //convert to unsigned and shift into place
			       int g = (imageData[i] & 0xFF) << 8;
			      int  b = imageData[i] & 0xFF;
			       imageBPixels[i] =  r | g | b; 
			     
			     }
			     
			     
			     //Show the image
			//      camera.updatePixels();
			//     image(camera, 640 * image.id(), 0);  
			   
//
//			   for(int x = 0; x < Math.min(pixels.length, WIDTH/2)-1; x++){
//				for(int y = 0; y < HEIGHT; y++){
//					int p = pixels[x * image.width()/3 + y];
//					fullScreenData[x * WIDTH/2 + y] = p;
//				}
//			   }
			   
			    image = images.get(1);
			     //Processing PImage class
			    // BufferedImage camera = [image.id()];
			   //  camera = createImage(image.width(), image.height(), RGB);
			  //////   camera.loadPixels();
			     
			     //Get byte array containing the image data from Image object
			     
			     imageData = image.data();

			     //Copy image data into display object, in this case PImage defined in Processing
			     for(int i = 0; i < image.width() * image.height(); i++){
			      int r = (imageData[i] & 0xFF) << 16; //convert to unsigned and shift into place
			       int g = (imageData[i] & 0xFF) << 8;
			      int  b = imageData[i] & 0xFF;
			       imageCPixels[i] =  r | g | b; 
			     
			     }
			     
//				   for(int x = 0; x < WIDTH/2-1; x++){
//					for(int y = 0; y < HEIGHT; y++){
//						
//						int newPix = pixels[x * image.width()/3 + y];
//						fullScreenData[((x + WIDTH / 2) * WIDTH) + y] = newPix;
//					}
//				   }
			     
			     draw(joinBufferedImage(imageB, imageC));
			 }
			   
			  
			 }
			
			
			
		}	
	

	public void draw(BufferedImage displayedImage)
	{
		BufferStrategy bs = this.getBufferStrategy();

		if (bs == null)
		{
			createBufferStrategy(3);
			return;
		}

		// 0x00B803
		Graphics g = bs.getDrawGraphics();
		g.drawImage(displayedImage, 0, 0, WIDTH, HEIGHT, null);

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
