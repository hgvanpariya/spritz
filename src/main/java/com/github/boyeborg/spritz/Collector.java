package com.github.boyeborg.spritz;

import com.github.boyeborg.spritz.exceptions.NotYetPostprocessedException;
import com.github.boyeborg.spritz.exceptions.NotYetPreprocessedException;
import com.github.boyeborg.spritz.exceptions.NotYetProcessedException;

/**
 * Collector that collects data from events for feature extraction. Often combined with other
 * collectors in batches.
 */
public interface Collector<T> {

	/**
	 * Lifecycle method executed before the {@link #process()} method of every collector of the
	 * same class is executed. This method is often used to calculate attributes based on
	 * the events added to the other collector objects of the same class (from other batches).
	 * Hence, the {@link #process()} method might not yield the expected result if more events
	 * are added to collectors of this class after this method has been executed.
	 * 
	 * @see #process()
	 * @see #postprocess()
	 */
	public void preprocess();

	/**
	 * Computes the result of the collector based on the events added and values from the
	 * {@link #preprocess()} method.
	 * 
	 * @see #preprocess()
	 * @see #postprocess()
	 */
	public void process();

	/**
	 * Lifecycle method executed after the {@link #process()} method of every collector of the
	 * same class has finnished. Usually used to calcualte values based on the results of other
	 * collector objects of the same class (from other batches), for example normalization
	 * factors.
	 * 
	 * @see #preprocess()
	 * @see #process()
	 */
	public void postprocess();

	/**
	 * Adds an event to the collector.
	 */
	public void addEvent(T event);

	/**
	 * Returns the result of the collector.
	 * 
	 * @see #preprocess()
	 * @see #process()
	 * @see #postprocess()
	 */
	public String getResult() throws
			NotYetPreprocessedException,
			NotYetProcessedException,
			NotYetPostprocessedException;

	/**
	 * Returns a new instance of the collector. This is used to create a new collector instance
	 * for each batch calculation.
	 */
	public Collector<T> create();
	
	/**
	 * Returns a short, descriptive and unique (amongst the other collectros) name without
	 * spaces.
	 */
	public String getSlug();
}
