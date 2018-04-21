package com.github.boyeborg.spritz.example;

import com.github.boyeborg.spritz.CollectorPlugin;

import java.util.ArrayList;
import java.util.List;

public class SumCollectorPlugin implements CollectorPlugin<ExampleEventObject> {
	
	private List<ExampleEventObject> events;

	public SumCollectorPlugin() {
		events = new ArrayList<>();
	}

	@Override
	public void addEvent(ExampleEventObject event) {
		events.add(event);
	}

	@Override
	public CollectorPlugin<ExampleEventObject> create() {
		return new SumCollectorPlugin();
	}

	@Override
	public void preprocess() {/** Not needed */}

	@Override
	public String getName() {
		return "sum";
	}

	@Override
	public String execute() {
		int sum = events.stream().mapToInt(ExampleEventObject::getNumber).sum();
		return Integer.toString(sum);
	}
	
}
