package com.hip.Search;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.File;
import java.io.IOException;

public class SearchReducer extends Reducer<Text,Text, Text, Text> {
    public void reduce(Text key,Iterable<Text> values, Context context) throws IOException,InterruptedException{
        int num=0;
        int bonus=0;
        int count=0;
        String io = "";
        for(Text row:values){
            num += 1;
            File file=new File(row.toString());
            count = row.toString().length();
            if(count>256){
                bonus+=1;
            }
            num+=1;
            io = file.getName()+"  ";
        }
        context.write(key,new Text(io));
    }
}
