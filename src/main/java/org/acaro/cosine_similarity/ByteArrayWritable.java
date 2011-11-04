package org.acaro.cosine_similarity;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class ByteArrayWritable 
implements Writable {
	
	private byte[] data;
	
	public ByteArrayWritable(byte[] data) {
		set(data);
	}
	
	public void set(byte[] data) {
		this.data = data;
	}
	
	public void readFields(DataInput in) 
	throws IOException {
		
		int len   = in.readInt();
		this.data = new byte[len];
		in.readFully(this.data);
	}

	public void write(DataOutput out) 
	throws IOException {

		if (this.data == null)
			throw new IllegalStateException("uninitialized Object");
		
		out.writeInt(this.data.length);
		out.write(this.data);
	}
	
	public byte[] getBytes() {
		return this.data;
	}
}
