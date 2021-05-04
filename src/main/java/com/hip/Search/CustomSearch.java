package com.hip.Search;

public class CustomSearch {
    private byte[] bytes;
    public CustomSearch(byte[] bytes){
        this.bytes = bytes;
    }
    public String customsearch(){
        //get string using Skein512
        String str = "";
        byte[] hashcode = new byte[512];
        Skein512.hash(bytes,hashcode);
        for (int i=0;i<hashcode.length;i++){
            str += Integer.toString( ( hashcode[i] & 0xFF )).substring( 1 );
        }
        return str;
    }
}
