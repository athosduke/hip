package com.hip;

import com.hip.Preprocess.PreprocessMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import com.hip.Search.SearchMapper;
import com.hip.Filter.FilterMapper;
import org.apache.hadoop.util.ReflectionUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;


public class ImageProcessing {
    public static void main(String[] args) throws Exception{

        //preprocessing job first created to merge the images to one
        Configuration conf = new Configuration();
        String[] myargs = new GenericOptionsParser(conf,args).getRemainingArgs();
        Job job_preprocess = new Job(conf,"preprocess");
        //setup for input/output
        Path preprocess_input = new Path(myargs[1]);
        Path preprocess_output = new Path(myargs[2]);
        FileInputFormat.addInputPath(job_preprocess,preprocess_input);
        job_preprocess.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setOutputPath(job_preprocess, preprocess_output);
        //setup for job_preprocess
        job_preprocess.setJarByClass(ImageProcessing.class);
        job_preprocess.setMapperClass(PreprocessMapper.class);
        job_preprocess.setMapOutputKeyClass(Text.class);
        job_preprocess.setOutputKeyClass(Text.class);
        job_preprocess.setMapOutputValueClass(BytesWritable.class);
        job_preprocess.setOutputValueClass(BytesWritable.class);
        //wait
        job_preprocess.waitForCompletion(true);

        //switch case for search / filter
        String sw = myargs[0];
        sw = sw.replaceAll("\\s", "");
        if (sw=="filter"){
            //set up for job filter
            conf = new Configuration();
            Job job_filter = new Job(conf,"filter");
            //setup for input/output
            Path filter_input = new Path(myargs[2]);
            Path filter_output = new Path(myargs[3]);
            job_filter.setInputFormatClass(SequenceFileInputFormat.class);
            FileInputFormat.addInputPath(job_filter,filter_input);
            job_filter.setOutputFormatClass(SequenceFileOutputFormat.class);
            FileOutputFormat.setOutputPath(job_filter,filter_output);
            //setup for job_preprocess
            job_filter.setJarByClass(ImageProcessing.class);
            job_filter.setMapperClass(FilterMapper.class);
            job_filter.setMapOutputKeyClass(Text.class);
            job_filter.setOutputKeyClass(Text.class);
            job_filter.setMapOutputValueClass(BytesWritable.class);
            job_filter.setOutputValueClass(BytesWritable.class);
            //wait
            job_filter.waitForCompletion(true);
            //SequenceFile to Images
            conf = new Configuration();
            FileSystem fileSystem = FileSystem.get(conf);
            Path imgPath = filter_output;
            SequenceFile.Reader reader = new SequenceFile.Reader(fileSystem,imgPath,conf);
            //get img from reader
            Text key = (Text) ReflectionUtils.newInstance(reader.getKeyClass(),conf);
            BytesWritable value = (BytesWritable) ReflectionUtils.newInstance(reader.getValueClass(),conf);
            //store the image
            while(reader.next(key,value)){
                byte[] b = value.copyBytes();
                String t = key.toString();
                ByteArrayInputStream bis = new ByteArrayInputStream(b);
                BufferedImage bufferedImage = ImageIO.read(bis);
                String out = myargs[3]+"/"+t+".jpg";
                ImageIO.write(bufferedImage,"jpg",new File(out));
            }
            reader.close();
        }
        else if(sw=="search"){
            //set up for job filter
            conf = new Configuration();
            Job job_search = new Job(conf,"search");
            //setup for input/output
            Path search_input = new Path(myargs[2]);
            Path search_output = new Path(myargs[3]);
            job_search.setInputFormatClass(SequenceFileInputFormat.class);
            FileInputFormat.addInputPath(job_search,search_input);
            FileOutputFormat.setOutputPath(job_search,search_output);
            //setup for job_preprocess
            job_search.setJarByClass(ImageProcessing.class);
            job_search.setMapperClass(SearchMapper.class);
            job_search.setMapOutputKeyClass(Text.class);
            job_search.setOutputKeyClass(Text.class);
            job_search.setMapOutputValueClass(Text.class);
            job_search.setOutputValueClass(Text.class);
            //wait
            job_preprocess.waitForCompletion(true);
        }
    }
}
