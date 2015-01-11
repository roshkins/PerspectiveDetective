package com.rashirashi;

import java.awt.image.BufferedImage;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Image;
import com.leapmotion.leap.ImageList;

public class InitBit {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	Controller controller = new Controller();
	controller.setPolicy(Controller.PolicyFlag.POLICY_IMAGES);
	
	
	 Frame frame = controller.frame();
	 if(frame.isValid()){
	   ImageList images = frame.images();
	   for(Image image : images)
	   {
	     //Processing PImage class
	     BufferedImage camera = [image.id()];
	     camera = createImage(image.width(), image.height(), RGB);
	     camera.loadPixels();
	     
	     //Get byte array containing the image data from Image object
	     byte[] imageData = image.data();

	     //Copy image data into display object, in this case PImage defined in Processing
	     for(int i = 0; i < image.width() * image.height(); i++){
	       r = (imageData[i] & 0xFF) << 16; //convert to unsigned and shift into place
	       g = (imageData[i] & 0xFF) << 8;
	       b = imageData[i] & 0xFF;
	       camera.pixels[i] =  r | g | b; 
	     }
	     
	     //Show the image
	     camera.updatePixels();
	     image(camera, 640 * image.id(), 0);  
	   }
	} 
	}

}
