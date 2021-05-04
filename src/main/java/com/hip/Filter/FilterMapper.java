package com.hip.Filter;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FilterMapper extends Mapper<Text, BytesWritable, Text, BytesWritable> {
    private BufferedImage bufferedImage;
    public void map(Text key,BytesWritable value,Context context) throws IOException,InterruptedException{
        //convert byteswritable to bufferedimage
        byte[] bytes = key.copyBytes();
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        bufferedImage = ImageIO.read(bis);
        //apply filter
        applyFilter();
        //write back
        byte[] bytesback = buffered2byte(bufferedImage);
        value.set(bytesback,0,bytesback.length);
        context.write(key,value);
    }
    private byte[] buffered2byte(BufferedImage buf) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(buf,"jpg",bos);
        bos.flush();
        byte[] bytes = bos.toByteArray();
        return bytes;
    }
    private void applyFilter(){
        //apply custom filter here
        //if needed to apply filter to array data
        //use bufferedImage2arr and then reverse to buffered image
        //int[][][] inputarr = bufferedImage2arr(bufferedImage);
        //CustomFilter customFilter = new CustomFilter(inputarr);
        //inputarr = CustomFilter.customFilter(inputarr);
        //bufferedImage = arr2bufferedImage(inputarr);
        CustomFilter myFilter = new CustomFilter(bufferedImage);
        bufferedImage = myFilter.customFilter();
    }
    private int[][][] bufferedImage2arr(BufferedImage img){
        //convert to array
        int imgHeight = img.getHeight();
        int imgWidth = img.getWidth();
        int[][][] imgarr = new int[imgWidth][imgHeight][3];
        for(int i=0;i<imgHeight;i++){
            for(int j=0;j<imgWidth;j++){
                int rgb = img.getRGB(i,j);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb) & 0xFF;
                imgarr[i][j][0] = r;
                imgarr[i][j][1] = g;
                imgarr[i][j][2] = b;
            }
        }
        return imgarr;
    }
    private BufferedImage arr2bufferedImage(int[][][] arr)throws IOException{
        //convert to BufferedImage
        int imgHeight = arr[0].length;
        int imgWidth = arr.length;
        byte [] arrimg = new byte[imgHeight];
        for(int i=0;i<imgWidth;i++){
            int rgb = 0;
            for(int j=0;j<imgHeight;j++) {
                int r = (arr[i][j][0] << 16);
                int g = (arr[i][j][1] << 8);
                int b = (arr[i][j][2]);
                rgb += r&g&b;
            }
            rgb = rgb/imgHeight;
            arrimg[i] = (byte)rgb;
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(arrimg);
        BufferedImage buf = ImageIO.read(bis);
        return buf;
    }
}
