package yotkaz.ai.multiprocessor.scheduling.sa;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import yotkaz.ai.multiprocessor.scheduling.domain.MultiprocessorSchedule;
import yotkaz.ai.multiprocessor.scheduling.domain.Processor;
import yotkaz.ai.multiprocessor.scheduling.domain.Task;
import yotkaz.ai.multiprocessor.scheduling.utils.MultiprocessorScheduleUtils;
import yotkaz.ai.utils.MapUtils;

/**
 * Multiprocessor Scheduling - Simulated Annealing
 * 
 * @author yotkaz
 */
public class MultiprocessorSchedulingSA {

	private static Random random = new Random();

	private List<SimpleEntry<MultiprocessorSchedule, Integer>> solutions = new ArrayList<>();
	private Map<MultiprocessorSchedule, Integer> solutionsMap = new LinkedHashMap<>();
	
	private List<Processor> processors;
	private List<Task> tasks;
	private double coolingFactor;
	private double temperature;

	public MultiprocessorSchedulingSA(List<Processor> processors, List<Task> tasks, double coolingFactor) {
		this(processors, tasks, coolingFactor, (double) random.nextInt());
	}

	public MultiprocessorSchedulingSA(List<Processor> processors, List<Task> tasks, double coolingFactor,
			double initialTemperature) {
		this.processors = processors;
		this.tasks = tasks;
		this.coolingFactor = coolingFactor;
		this.temperature = initialTemperature;
	}

	public MultiprocessorSchedule process(int maxIterations) {
		MultiprocessorSchedule currentSolution = new MultiprocessorSchedule().initRandom(tasks, processors);
		solutions.add(new SimpleEntry<MultiprocessorSchedule, Integer>(currentSolution, currentSolution.calculateTotalExecutionTime()));
		for (int i = 0; i < maxIterations && temperature > 1.0; i++) {
			currentSolution = currentSolution.copy();
			MultiprocessorScheduleUtils.randomSwapBetweenProcessors(currentSolution); // neighbourhood
			int fit = currentSolution.calculateTotalExecutionTime();
			if (fit < solutions.get(solutions.size() - 1).getValue()) {
				solutions.add(new SimpleEntry<MultiprocessorSchedule, Integer>(currentSolution, fit));
			} else if (random.nextDouble() < getTemperature(fit)) {
				solutions.add(new SimpleEntry<MultiprocessorSchedule, Integer>(currentSolution, fit));
			}
			temperature *= coolingFactor * temperature;
		}
		Map<MultiprocessorSchedule, Integer> temporaryMap = new HashMap<>();
		for (SimpleEntry<MultiprocessorSchedule, Integer> entry : solutions) {
			temporaryMap.put(entry.getKey(), entry.getValue());
		}
		solutionsMap = MapUtils.sortByValue(temporaryMap);
		Iterator<MultiprocessorSchedule> iterator = solutionsMap.keySet().iterator();
		return iterator.hasNext() ? iterator.next() : null;
	}
	
	private double getTemperature(int fit) {
		return Math.pow(Math.E, (fit - solutions.get(solutions.size() - 1).getValue()) / temperature);	
	}
	
	public int getSolutionValue(MultiprocessorSchedule solution) {
		return solutionsMap.get(solution);
	}
	
}
