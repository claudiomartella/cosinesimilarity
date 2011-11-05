package org.acaro.cosine_similarity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.Text;

public class Txt2SequenceFileConverter {

	public static void serialize(String infile, String outfile) throws IOException {
		
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(outfile), conf);
		Path p = new Path(outfile);

		Writer writer = SequenceFile.createWriter(fs, conf, p, Text.class, ByteArrayWritable.class);
		
		BufferedReader br = new BufferedReader(new FileReader(infile));
		
		HashMap<Integer, Float> vectorMap = new HashMap<Integer, Float>();
		List<Integer> keysList = new LinkedList<Integer>();
		String line;
		while ((line = br.readLine()) != null) {
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos      = new DataOutputStream(bos);
			String[] fields = line.split("\t+");
			String id = fields[0];

			boolean hasOnlyZeros = true;
			for (int i = 1; i < fields.length; i++) {
				float val = Float.parseFloat(fields[i]);
				if (val != 0) {
					vectorMap.put(i, val);
					keysList.add(i);
					hasOnlyZeros = false;
				}
			}
			Float[] vector = new Float[vectorMap.size()];
			vectorMap.values().toArray(vector);

			if (!hasOnlyZeros) {
				double norm = norm(vector);
				
				dos.writeDouble(norm);
				for (int k : keysList) {
					
					float v = vectorMap.get(k);
					
					dos.writeInt(k);
					dos.writeFloat(v);
				}
			} 
			else {
				dos.writeDouble(0);
			}
			dos.close();
			vectorMap.clear();
			keysList.clear();

			Text key = new Text(id);
			ByteArrayWritable value = new ByteArrayWritable(bos.toByteArray());
			writer.append(key, value);
		}
		writer.close();
		br.close();
	}

	// return the inner product of this vector
	public static double dot(Float[] vector) {
		double sum = 0.0;
		for (int i = 0; i < vector.length; i++)
			if (vector[i] != 0)
				sum += (vector[i] * vector[i]);
		return sum;
	}

	// return the Euclidean norm of this Vector
	public static double norm(Float[] vector) {
		return Math.sqrt(dot(vector));
	}
	
	public static void main(String[] args) throws IOException {
		
		if (args.length != 2) {
			System.out.println("Usage: Txt2SequenceFileConverter <input file> <output file>");
			System.exit(-1);
		}
		
		serialize(args[0], args[1]);
	}
}
