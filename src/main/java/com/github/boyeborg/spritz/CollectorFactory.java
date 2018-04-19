package com.github.boyeborg.spritz;

public interface CollectorFactory<T> {
	
	/**
	 * Returns a new instance of the collector.
	 * 
	 * @return A new instance of the collecotr.
	 */
	public Collector<T> generate();

}
