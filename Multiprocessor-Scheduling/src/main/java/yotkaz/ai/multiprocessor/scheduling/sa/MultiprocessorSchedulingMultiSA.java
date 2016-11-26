package yotkaz.ai.multiprocessor.scheduling.sa;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import yotkaz.ai.multiprocessor.scheduling.domain.MultiprocessorSchedule;
import yotkaz.ai.multiprocessor.scheduling.domain.Processor;
import yotkaz.ai.multiprocessor.scheduling.domain.Task;
import yotkaz.ai.utils.MapUtils;

/**
 * Multiprocessor Scheduling - Multi Simulated Annealing
 * 
 * @author yotkaz
 */
public class MultiprocessorSchedulingMultiSA {

	private static Random random = new Random();

	private Map<MultiprocessorSchedule, Integer> solutionsMap = new LinkedHashMap<>();

	private List<Processor> processors;
	private List<Task> tasks;
	private double coolingFactor;
	private double temperature;

	public MultiprocessorSchedulingMultiSA(List<Processor> processors, List<Task> tasks, double coolingFactor) {
		this(processors, tasks, coolingFactor, (double) random.nextInt());
	}

	public MultiprocessorSchedulingMultiSA(List<Processor> processors, List<Task> tasks, double coolingFactor,
			double initialTemperature) {
		this.processors = processors;
		this.tasks = tasks;
		this.coolingFactor = coolingFactor;
		this.temperature = initialTemperature;
	}

	public int getSolutionValue(MultiprocessorSchedule solution) {
		return solutionsMap.get(solution);
	}
	
	public MultiprocessorSchedule process(int maxOuterIterations, int maxInnerIterations) {
		for (int i = 0; i < maxOuterIterations; i++) {
			MultiprocessorSchedulingSA sa = new MultiprocessorSchedulingSA(processors, tasks, coolingFactor, temperature);
			MultiprocessorSchedule solution = sa.process(maxInnerIterations);
			solutionsMap.put(solution, sa.getSolutionValue(solution));
		}
		solutionsMap = MapUtils.sortByValue(solutionsMap);
		Iterator<MultiprocessorSchedule> iterator = solutionsMap.keySet().iterator();
		return iterator.hasNext() ? iterator.next() : null;
	}

}
