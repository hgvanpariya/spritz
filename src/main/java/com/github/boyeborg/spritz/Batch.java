package com.github.boyeborg.spritz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class Batch<T> {

	private List<Collector<T>> collectors;
	private Map<Collector<T>, String> results;
	private String dataSeperator;

	/**
	 * Constructor for the {@code Batch} class.
	 * 
	 * @param generators An array of the collectors used in this batch.
	 */
	public Batch(CollectorFactory<T>[] generators) {
		collectors = new ArrayList<>();

		for (CollectorFactory<T> generator : generators) {
			collectors.add(generator.generate());
		}
	}

	/**
	 * Adds an event to all the collectors within the {@code Batch}.
	 * 
	 * @param event The event to add to all collectors.
	 */
	public void addEvent(T event) {
		for (Collector<T> collector : collectors) {
			collector.addEvent(event);
		}
	}

	public void preprocess() {
		collectors.forEach(Collector<T>::preprocess);
	}

	public void process() {
		collectors.forEach(Collector<T>::process);
	}

	public void postprocess() {
		collectors.forEach(Collector<T>::postprocess);
	}

	/**
	 * Execute each collector and stores the results. Any previous results will be overwritten.
	 */
	public void compute() {

		// Delete previous results
		results = new HashMap<>();

		for (Collector<T> collector : collectors) {
			results.put(collector, collector.process());
		}
	}

	/**
	 * Returns the results from {@link #compute()}.
	 * 
	 * @return A {@code Map} of the collectors (key) and their respective results (value).
	 */
	public Map<Collector<T>, String> getResult() {
		return results;
	}

	/**
	 * Returns the result of the specified collector. Null if the collector is not found.
	 * 
	 * @param collector The collector to return the results for.
	 * 
	 * @return The result string from the collector, or null if the collector is not found.
	 */
	public String getResult(Collector<T> collector) {

		for (Entry<Collector<T>, String> entry : results.entrySet()) {
			if (entry.getKey().getClass().equals(getClass())) {
				return entry.getValue();
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return results.values().stream().collect(Collectors.joining(dataSeperator));
	}
}
