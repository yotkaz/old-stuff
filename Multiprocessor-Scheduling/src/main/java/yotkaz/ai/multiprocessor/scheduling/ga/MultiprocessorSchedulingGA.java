package yotkaz.ai.multiprocessor.scheduling.ga;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import yotkaz.ai.ga.AbstractGeneticAlgorithm;
import yotkaz.ai.multiprocessor.scheduling.domain.MultiprocessorSchedule;
import yotkaz.ai.multiprocessor.scheduling.domain.Processor;
import yotkaz.ai.multiprocessor.scheduling.domain.ProcessorSchedule;
import yotkaz.ai.multiprocessor.scheduling.domain.Task;
import yotkaz.ai.multiprocessor.scheduling.utils.MultiprocessorScheduleUtils;
import yotkaz.ai.utils.MapUtils;

/**
 * Multiprocessor Scheduling - Genetic Algorithm
 * @author yotkaz
 */
public class MultiprocessorSchedulingGA extends AbstractGeneticAlgorithm<MultiprocessorSchedule> {

	private Map<MultiprocessorSchedule, Integer> solutionsMap = new LinkedHashMap<MultiprocessorSchedule, Integer>();

	private Random random = new Random();

	private List<Processor> processors;
	private List<Task> tasks;
	private int populationSize;

	public MultiprocessorSchedulingGA(List<Processor> processors, List<Task> tasks, int populationSize) {
		this.populationSize = populationSize;
		this.processors = processors;
		this.tasks = tasks;
	}

	@Override
	public void init() {
		for (int i = 0; i < populationSize; i++) {
			MultiprocessorSchedule solution = new MultiprocessorSchedule();
			solution.initRandom(tasks, processors);
			solutionsMap.put(solution, null);
		}

	}

	@Override
	public void crossovers() {
		List<MultiprocessorSchedule> available = new ArrayList<MultiprocessorSchedule>(solutionsMap.keySet());
		for (int i = 0; i < populationSize; i += 2) {
			MultiprocessorSchedule solution1 = getRandomSolution(available);
			available.remove(solution1);
			MultiprocessorSchedule solution2 = getRandomSolution(available);
			available.remove(solution2);
			if (solution1 == null || solution2 == null) {
				break;
			}
			Childs childs = crossover(solution1, solution2);
			solutionsMap.put(childs.getFirstChild(), null);
			solutionsMap.put(childs.getSecondChild(), null);
		}

	}

	@Override
	public void mutations() {
		int solutionsSize = solutionsMap.size();
		int mutationsCount = random.nextInt(solutionsSize);
		List<MultiprocessorSchedule> available = new ArrayList<MultiprocessorSchedule>(solutionsMap.keySet());
		for (int i = 0; i < mutationsCount; i++) {
			MultiprocessorSchedule randomSolution = available.get(random.nextInt(available.size()));
			MultiprocessorScheduleUtils.randomSwapBetweenProcessors(randomSolution);
		}
	}

	@Override
	public void selection() {
		for (Entry<MultiprocessorSchedule, Integer> entry : solutionsMap.entrySet()) {
			entry.setValue(entry.getKey().calculateTotalExecutionTime());
		}
		solutionsMap = MapUtils.sortByValue(solutionsMap);
		Iterator<Entry<MultiprocessorSchedule, Integer>> iterator = solutionsMap.entrySet().iterator();
		int counter = 0;
		while (iterator.hasNext()) {
			iterator.next();
			if (counter >= populationSize) {
				iterator.remove();
			}
			counter++;
		}
	}

	/** always do all iterations */
	@Override
	public boolean isSatisfyingSolution() {
		return false;
	}

	@Override
	public MultiprocessorSchedule chooseSolution() {
		Iterator<MultiprocessorSchedule> iterator = solutionsMap.keySet().iterator();
		return iterator.hasNext() ? iterator.next() : null;
	}

	private MultiprocessorSchedule getRandomSolution(List<MultiprocessorSchedule> solutionsList) {
		return solutionsList.isEmpty() ? null : solutionsList.get(random.nextInt(solutionsList.size()));
	}

	private Childs crossover(MultiprocessorSchedule parent1, MultiprocessorSchedule parent2) {
		int crossoverPoint = random.nextInt(getSizeOfLongestProcessorSchedule(parent1, parent2));
		Childs childs = new Childs();
		MultiprocessorSchedule child1 = new MultiprocessorSchedule().initEmpty(processors);
		MultiprocessorSchedule child2 = new MultiprocessorSchedule().initEmpty(processors);
		childs.setFirstChild(child1);
		childs.setSecondChild(child2);
		for (int i = 0; i < processors.size(); i++) {
			ProcessorSchedule processorSchedule1 = parent1.get(i);
			ProcessorSchedule processorSchedule2 = parent2.get(i);
			child1.get(i).addAll(processorSchedule1.subList(0,
					processorSchedule1.size() < crossoverPoint ? processorSchedule1.size() : crossoverPoint));
			child2.get(i).addAll(processorSchedule2.subList(0,
					processorSchedule2.size() < crossoverPoint ? processorSchedule2.size() : crossoverPoint));
		}
		crossCopyGenes(parent2, child1, crossoverPoint);
		crossCopyGenes(parent1, child2, crossoverPoint);
		child1.orderTasks();
		child2.orderTasks();
		return childs;
	}

	private void crossCopyGenes(MultiprocessorSchedule parent, MultiprocessorSchedule child, int crossoverPoint) {
		for (int i = 0; i < processors.size(); i++) {
			ProcessorSchedule childProcessorSchedule = child.get(i);
			ProcessorSchedule parentProcessorSchedule = parent.get(i);
			for (Task task : parentProcessorSchedule) {
				if (!containsTask(child, task)) {
					childProcessorSchedule.add(task);
				}
			}
		}
	}

	private boolean containsTask(MultiprocessorSchedule multiprocessorSchedule, Task task) {
		for (ProcessorSchedule processorSchedule : multiprocessorSchedule) {
			if (processorSchedule.contains(task)) {
				return true;
			}
		}
		return false;
	}

	private int getSizeOfLongestProcessorSchedule(MultiprocessorSchedule... multiprocessorSchedules) {
		int maxSize = 0;
		for (MultiprocessorSchedule multiprocessorSchedule : multiprocessorSchedules) {
			for (ProcessorSchedule processorSchedule : multiprocessorSchedule) {
				if (processorSchedule.size() > maxSize) {
					maxSize = processorSchedule.size();
				}
			}
		}
		return maxSize;
	}

	public Map<MultiprocessorSchedule, Integer> getSolutionsMap() {
		return solutionsMap;
	}
	
	public int getSolutionValue(MultiprocessorSchedule solution) {
		return solutionsMap.get(solution);
	}

	private static class Childs {

		MultiprocessorSchedule firstChild;
		MultiprocessorSchedule secondChild;

		public MultiprocessorSchedule getFirstChild() {
			return firstChild;
		}

		public void setFirstChild(MultiprocessorSchedule firstChild) {
			this.firstChild = firstChild;
		}

		public MultiprocessorSchedule getSecondChild() {
			return secondChild;
		}

		public void setSecondChild(MultiprocessorSchedule secondChild) {
			this.secondChild = secondChild;
		}

	}

}