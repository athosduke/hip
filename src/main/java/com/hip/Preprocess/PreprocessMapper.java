package com.hip.Preprocess;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.commons.io.IOUtils;

import java.io.IOException;


public class PreprocessMapper extends Mapper<Object, Text, Text, BytesWritable> {
    private String io[];
    private int num;
    public void map(Object key,Text value,Context context) throws IOException,InterruptedException{
        //initialize
        Configuration conf = new Configuration();
        updateIO(value.toString());

        //get image
        for(int i=0;i<num;i++) {
            Path path = new Path(io[i].toString());
            FileSystem fileSystem = FileSystem.get(conf);
            FSDataInputStream fs = null;
            fs = fileSystem.open(path);
            fs.seek(0);
            //write image to new value
            BytesWritable bytesWritable = new BytesWritable(IOUtils.toByteArray(fs));
            value.set(io[i]);
            context.write(value,bytesWritable);
            IOUtils.closeQuietly(fs);
        }

    }
    private void updateIO(String str){
        io =str.split("\n");
        num = io.length;
    }
}
