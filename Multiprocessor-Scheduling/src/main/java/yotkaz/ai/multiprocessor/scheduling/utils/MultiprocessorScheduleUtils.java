package yotkaz.ai.multiprocessor.scheduling.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import yotkaz.ai.multiprocessor.scheduling.domain.MultiprocessorSchedule;
import yotkaz.ai.multiprocessor.scheduling.domain.ProcessorSchedule;
import yotkaz.ai.multiprocessor.scheduling.domain.Task;

/**
 * @author yotkaz
 *
 */
public class MultiprocessorScheduleUtils {

	private static Random random = new Random();

	public static void randomSwapBetweenProcessors(MultiprocessorSchedule multiprocessorSchedule) {
		List<ProcessorSchedule> availableProcessorSchedules = new ArrayList<ProcessorSchedule>(multiprocessorSchedule);
		if (availableProcessorSchedules.size() < 2) {
			return;
		}
		ProcessorSchedule randomProcessorSchedule1 = getRandomProcessorSchedule(availableProcessorSchedules);
		ProcessorSchedule randomProcessorSchedule2 = getRandomProcessorSchedule(availableProcessorSchedules);
		if (randomProcessorSchedule1.isEmpty() || randomProcessorSchedule2.isEmpty()) {
			return;
		}
		Task randomTask1 = getRandomTask(randomProcessorSchedule1);
		Task randomTask2 = getRandomTask(randomProcessorSchedule2);
		randomProcessorSchedule2.add(randomTask1);
		randomProcessorSchedule1.add(randomTask2);
		multiprocessorSchedule.orderTasks();
	}

	private static ProcessorSchedule getRandomProcessorSchedule(List<ProcessorSchedule> list) {
		ProcessorSchedule result = list.get(random.nextInt(list.size()));
		list.remove(result);
		return result;
	}

	private static Task getRandomTask(List<Task> list) {
		Task result = list.get(random.nextInt(list.size()));
		list.remove(result);
		return result;
	}
	
}
