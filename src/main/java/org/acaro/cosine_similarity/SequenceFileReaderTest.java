package org.acaro.cosine_similarity;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.Text;

public class SequenceFileReaderTest {

	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create("./prova.sf"), conf);
		Path p = new Path("./prova.sf");
		
		Reader reader = new SequenceFile.Reader(fs, p, conf);
		
		String oldK = "init";
		String oldV = "init";
		
		Text key = new Text();
		Text value = new Text();
		
		while (reader.next(key, value)) {
			System.out.println(oldK + " " + key.toString());
			System.out.println(oldV + " " + value.toString());
			oldK = key.toString();
			oldV = value.toString();
		}
		
		reader.close();
	}
}
