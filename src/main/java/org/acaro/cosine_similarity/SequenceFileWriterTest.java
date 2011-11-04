package org.acaro.cosine_similarity;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.Text;

public class SequenceFileWriterTest {

	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create("./prova.sf"), conf);
		Path p = new Path("./prova.sf");
		
		Text key   = new Text();
		Text value = new Text();
		
		Writer writer = SequenceFile.createWriter(fs, conf, p, Text.class, Text.class);
		
		for (int i = 0; i < 100; i++) {
			key.set(String.valueOf(i));
			value.set(String.valueOf(i));
			writer.append(key, value);
		}
		
		writer.close();
	}
}
