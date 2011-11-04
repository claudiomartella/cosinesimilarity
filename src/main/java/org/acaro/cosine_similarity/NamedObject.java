package org.acaro.cosine_similarity;

public class NamedObject<A> {

	private final String name;
	private final A data;
	
	public NamedObject(String name, A data) {
		this.name = name;
		this.data = data;
	}
	
	public String getName() {
		return this.name;
	}
	
	public A getData() {
		return this.data;
	}
}
