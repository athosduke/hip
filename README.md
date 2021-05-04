## **CS 540 Final Project : Image Processing on Hadoop**
**Author**
Songmeng Wang (swang297@uic.edu)

**Overview**
-
This is an image processing framework intented to handle large-scale images with a relatively short processing time. It’s designed fordevelopers who need to deal with massive images. (ex. analyzing images on socialmedia, item recognition for images on search engine, etc.) It provided a time-saving framework for image processing to use on Apache-Hadoop: you can simply insert/replace your image processing file (for search/filter) to the correct folder and has the processing working.

**Why use Image Processing on Hadoop?**
-
First, Hadoop has an advantage in processing massive data using Hadoop HDFS and MapReduce. Hadoop itself can handle massive data prettly well, but there is a deficiency when dealing with images. Image is relatively small data type. Most of the time it will not exceed 3 MB. HDFS distributed data many dataNode, where each dataNode is 64M. It's ineffiency to have one image (3MB) occupy an entire dataNode (64M). This framework preprocess the images and merge multiple images to a larger SequenceFile (hadoop.io.SequenceFile) including the RGB data and the file name. This improve the performance on Hadoop when processing images.

**Prerequisite**
-
Apache-Hadoop  [[link]](https://hadoop.apache.org/releases.html)
Gradle   [[link]](https://gradle.org/)

**Installation Steps**
-
Details in "instruction.pdf"

**Testing**
-
Details in "testcase.pdf"
Output for Search Test (detecting similar images) in [ /output_search/out.txt ]
Ouptut for Filter Test (grey-scale imagas) in [ /output_filter/ ]

**Code Structure**
-
 - **ImageProcessing.java  -> main** 
	 - Input: Arguments "search/filter, inputPath, outputPath1, outputPath2"
	 - Output: images or textfiles (jpg/txt)
	 - construct Hadoop MapReduce Jobs
 - **Preprocess**
	 - Input: Input Images
	 - Output: <key,value> - <file name, image RGB data> - <Text, ByteWritable>
	 - Mapper: Read the input image data and write <file name, RGB value> to Content<key,value>
	 - Merge the image information and converted to a SequnceFile
 - **Search**
	 - Input: (key,value) -  (file name. image RGB data) - (Text, ByteWritable)
	 - Output: (key,value) - (file name. image search data) - (Text, Text)
	 - Mapper: convert the image RGB data to byte array and calculate the search value using the customSearch.  Write (file name. image search value) to Content(key,value)
	 - Reducer: select from the image search value and keep the corresponding file name. Write <selected file name, selected iamge search value> to content(key,value)
 - **Filter**
	 - Input: (key,value) - (file name. image RGB data) - (Text, ByteWritable)
	 - Output: (key,value) - (file name. Images) - (Text, ByteWritable)
	 - Mapper: convert the ByteWritable to appropriate data type, apply customFilter to the images. Write (file name, filted image data) to Content(key,value)

**More Information**
-
- Image Processing Framework [[link]](https://www.overleaf.com/project/60528ad77f1a3328fe495236)
 
**Reference**
-
- /src/main/java/hip/Search/Skein512.java copy from [skein512](http://www.h2database.com/skein/Skein512.java)
