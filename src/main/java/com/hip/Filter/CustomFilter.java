package com.hip.Filter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CustomFilter {
    private BufferedImage image;
    public CustomFilter(BufferedImage bufferedImage){
        this.image = bufferedImage;
    }
    public BufferedImage customFilter(){
        //for testing, I apply a gray scale filter below
        //users can customize their own filters here
        int width = image.getWidth();
        int height = image.getHeight();
        for(int i=0; i<height; i++) {
            for(int j=0; j<width; j++) {
                Color c = new Color(image.getRGB(j, i));
                int red = (int)(c.getRed() * 0.299);
                int green = (int)(c.getGreen() * 0.587);
                int blue = (int)(c.getBlue() *0.114);
                Color graycolor = new Color(red+green+blue,
                        red+green+blue,red+green+blue);
                image.setRGB(j,i,graycolor.getRGB());
            }
        }
        return image;
    }
}
