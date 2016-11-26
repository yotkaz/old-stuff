package yotkaz.ai.multiprocessor.scheduling.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * @author yotkaz
 *
 */
public class MultiprocessorSchedule extends ArrayList<ProcessorSchedule> {
	private static final long serialVersionUID = 1L;

	private Random random = new Random();

	public MultiprocessorSchedule copy() {
		MultiprocessorSchedule result = new MultiprocessorSchedule();
		for (ProcessorSchedule processorSchedule : this) {
			ProcessorSchedule resultPart = new ProcessorSchedule(processorSchedule.getProcessor());
			result.add(resultPart);
			for (Task task : processorSchedule) {
				resultPart.add(task);
			}
		}
		return result;
	}
	
	public MultiprocessorSchedule initRandom(List<Task> tasks, List<Processor> processors) {
		initEmpty(processors);
		for (Task task : tasks) {
			this.get(random.nextInt(this.size())).add(task);
		}
		orderTasks();
		return this;
	}

	public MultiprocessorSchedule initEmpty(List<Processor> processors) {
		this.clear();
		for (Processor processor : processors) {
			this.add(new ProcessorSchedule(processor));
		}
		return this;
	}

	public void orderTasks() {
		List<Task> allTasks = new ArrayList<Task>();
		List<Task> added = new ArrayList<Task>();
		Map<ProcessorSchedule, List<Task>> addedMap = new HashMap<ProcessorSchedule, List<Task>>();
		for (ProcessorSchedule processorSchedule : this) {
			allTasks.addAll(processorSchedule);
			addedMap.put(processorSchedule, new LinkedList<Task>());
		}
		for (Task task : allTasks) {
			maybeAddTask(task, added, addedMap);
		}
		for (ProcessorSchedule processorSchedule : this) {
			List<Task> toAdd = addedMap.get(processorSchedule);
			processorSchedule.clear();
			processorSchedule.addAll(toAdd);
		}
	}

	private void maybeAddTask(Task task, List<Task> added, Map<ProcessorSchedule, List<Task>> addedMap) {
		if (!added.contains(task)) {
			for (Task taskBefore : task.getTasksBefore().keySet()) {
				maybeAddTask(taskBefore, added, addedMap);
			}
			for (ProcessorSchedule processorSchedule : this) {
				if (processorSchedule.contains(task)) {
					addedMap.get(processorSchedule).add(task);
					added.add(task);
				}
			}
		}
	}

	public int calculateTotalExecutionTime() {
		Map<Task, Integer> started = new LinkedHashMap<Task, Integer>();
		Map<Task, Integer> finished = new LinkedHashMap<Task, Integer>();
		Map<ProcessorSchedule, Task> currentTaskMap = new HashMap<ProcessorSchedule, Task>();
		Map<ProcessorSchedule, Iterator<Task>> iteratorMap = new HashMap<ProcessorSchedule, Iterator<Task>>();

		for (ProcessorSchedule processorSchedule : this) {
			iteratorMap.put(processorSchedule, processorSchedule.iterator());
		}

		int time = 0;
		while (true) {
			boolean allFinished = true;
			for (ProcessorSchedule processorSchedule : this) {
				Task task = currentTaskMap.get(processorSchedule);
				Iterator<Task> iterator = iteratorMap.get(processorSchedule);
				if (task == null) {
					if (iterator.hasNext()) {
						task = iterator.next();
						currentTaskMap.put(processorSchedule, task);
					} else {
						continue;
					}
				}
				if (started.containsKey(task)
						&& time - started.get(task) >= task.getExecutionTimes().get(processorSchedule.getProcessor())) {
					finished.put(task, time);
					if (iterator.hasNext()) {
						task = iterator.next();
						currentTaskMap.put(processorSchedule, task);
					} else {
						currentTaskMap.put(processorSchedule, null);
						continue;
					}
				}
				if (!started.containsKey(task) && canStart(task, time, finished)) {
					started.put(task, time);
				}
				allFinished = false;
			}
			if (allFinished) {
				break;
			}
			time++;
		}

		return time;
	}

	private boolean canStart(Task task, Integer time, Map<Task, Integer> finished) {
		boolean result = true;
		for (Entry<Task, Integer> entry : task.getTasksBefore().entrySet()) {
			if (!finished.containsKey(entry.getKey()) || time < finished.get(entry.getKey()) + entry.getValue()) {
				return false;
			}
		}
		return result;
	}

}