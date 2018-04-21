package com.github.boyeborg.spritz;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A consumer that consumes events of type {@literal <T>}.
 * 
 */
public class EventConsumer<T> {

	private List<DataLine<T>> dataLines;
	private List<CollectorPlugin<T>> plugins;
	private DataLine<T> currentLine;
	private String lineSeperator;
	private String resultSeperator;

	/**
	 * Constructor for the EventConsumer class.
	 * 
	 * @param plugins A list of the plugins that are used to collect data from the events.
	 * @param lineSeperator A string that will be inserted between each line when
	 *     {@link #toString()} is called. A common seperator is: {@literal "\n"}.
	 * @param resultSeperator A string that will be inserted between each result from the
	 *     plugins.
	 */
	public EventConsumer(
			List<CollectorPlugin<T>> plugins,
			String lineSeperator,
			String resultSeperator) {

		dataLines = new ArrayList<>();
		this.plugins = plugins;
		this.lineSeperator = lineSeperator;
		this.resultSeperator = resultSeperator;

		// Add first line
		newLine();
	}

	/**
	 * Adds a new line to the event consumer, and sets it as the current line.
	 * 
	 * @see #addEvent(Object)
	 */
	public void newLine() {
		currentLine = new DataLine<T>(plugins, resultSeperator);
		dataLines.add(currentLine);
	}

	/**
	 * Adds a event to the current line in the event consumer.
	 * 
	 * @see #newLine()
	 */
	public void addEvent(T event) {
		currentLine.addEvent(event);
	}

	/**
	 * Executes the preprocessing lifecycle of each plugin within each line. 
	 */
	public void preprocess() {
		dataLines.forEach(DataLine::preprocess);
	}

	/**
	 * Executes each plugin within each line.
	 */
	public void calculateResult() {
		dataLines.forEach(DataLine::compute);
	}

	/**
	 * Returns all the results as a two dimensional matrix; one array for each line.
	 * Each line array contains the results from each plugin (as a string).
	 * Both the lines and results from the plugins are returned in the same order as they were
	 * added.
	 * 
	 * @return The result of each line after calling {@link #calculateResult()}.
	 */
	public String[][] getResults() {
		String[][] results = new String[dataLines.size()][plugins.size()];

		for (int lineIndex = 0; lineIndex < dataLines.size(); lineIndex++) {
			DataLine<T> dataLine = dataLines.get(lineIndex);
			for (int pluginIndex = 0; pluginIndex < plugins.size(); pluginIndex++) {
				String result = dataLine.getResult(plugins.get(pluginIndex));
				results[lineIndex][pluginIndex] = result;
			}
		}

		return results;
	}

	/**
	 * Each line is seperated by the {@code lineSeperator} and each result by the
	 * {@code resultSeperator} provided to the constructor.
	 */
	@Override
	public String toString() {
		String names = plugins.stream()
				.map(CollectorPlugin<T>::getName)
				.collect(Collectors.joining(resultSeperator));
		
		String results = dataLines.stream()
				.map(DataLine::toString)
				.collect(Collectors.joining(lineSeperator));
		
		return names + lineSeperator + results;
	}
}
