package yotkaz.ai.multiprocessor.scheduling.domain;

import java.util.ArrayList;

public class ProcessorSchedule extends ArrayList<Task> {
	private static final long serialVersionUID = 1L;

	Processor processor;

	public ProcessorSchedule(Processor processor) {
		this.processor = processor;
	}

	public Processor getProcessor() {
		return processor;
	}

	public void setProcessor(Processor processor) {
		this.processor = processor;
	}
	
}