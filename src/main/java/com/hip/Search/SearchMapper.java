package com.hip.Search;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class SearchMapper extends Mapper<Text, BytesWritable, Text, Text> {
    String search;
    public void map(Text key,BytesWritable value,Context context) throws IOException,InterruptedException{
        //initialize
        byte[] bytes = value.getBytes();
        customSearch(bytes);
        Text newvalue = new Text(search);
        //writeback
        context.write(key,newvalue);
    }
    private void customSearch(byte[] arr) {
        CustomSearch customsearch = new CustomSearch(arr);
        search = customsearch.customsearch();
    }
}
