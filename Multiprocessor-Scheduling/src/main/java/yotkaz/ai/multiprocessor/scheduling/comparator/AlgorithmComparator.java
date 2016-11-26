package yotkaz.ai.multiprocessor.scheduling.comparator;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import yotkaz.ai.multiprocessor.scheduling.domain.MultiprocessorSchedule;
import yotkaz.ai.multiprocessor.scheduling.domain.Processor;
import yotkaz.ai.multiprocessor.scheduling.domain.Task;
import yotkaz.ai.multiprocessor.scheduling.ga.MultiprocessorSchedulingGA;
import yotkaz.ai.multiprocessor.scheduling.sa.MultiprocessorSchedulingMultiSA;
import yotkaz.ai.multiprocessor.scheduling.sa.MultiprocessorSchedulingSA;
import yotkaz.ai.utils.ListUtils;

/**
 * @author yotkaz
 *
 *         TODO: clean up little mess in this class
 */
public class AlgorithmComparator {

	private static final String PROCESSOR_ID_PREFIX = "P_";
	private static final String TASK_ID_PREFIX = "T_";

	private Random random = new Random();

	private List<Processor> processors = new ArrayList<Processor>();
	private List<Task> tasks = new ArrayList<Task>();

	private int howMany;
	private boolean printTime;

	private int processorsCount;
	private int tasksCount;

	private int minExecutionTime;
	private int maxExecutionTime;
	private int minCommunicationTime;
	private int maxCommunicationTime;
	private int maxConnectionsToTask;

	private int gaPopulation;
	private int gaGenerations;
	private double saCoolingFactor;
	private double saInitialTemperature;
	private int saIterations;
	private double multiSACoolingFactor;
	private double multiSAInitialTemperature;
	private int multiSAInnerIterations;
	private int multiSAOuterIterations;

	public AlgorithmComparator(Properties properties) {
		this.howMany = Integer.parseInt(properties.getProperty("howMany"));
		this.printTime = Boolean.parseBoolean(properties.getProperty("printTime", "true"));
		this.processorsCount = Integer.parseInt(properties.getProperty("processorsCount"));
		this.tasksCount = Integer.parseInt(properties.getProperty("tasksCount"));
		this.minExecutionTime = Integer.parseInt(properties.getProperty("minExecutionTime"));
		this.maxExecutionTime = Integer.parseInt(properties.getProperty("maxExecutionTime"));
		this.minCommunicationTime = Integer.parseInt(properties.getProperty("minCommunicationTime"));
		this.maxCommunicationTime = Integer.parseInt(properties.getProperty("maxCommunicationTime"));
		this.maxConnectionsToTask = Integer.parseInt(properties.getProperty("maxConnectionsToTask"));
		this.gaPopulation = Integer.parseInt(properties.getProperty("GA.population"));
		this.gaGenerations = Integer.parseInt(properties.getProperty("GA.generations"));
		this.saCoolingFactor = Double.parseDouble(properties.getProperty("SA.coolingFactor"));
		this.saInitialTemperature = Double.parseDouble(properties.getProperty("SA.initialTemperature", "0.0"));
		this.saIterations = Integer.parseInt(properties.getProperty("SA.iterations"));
		this.multiSACoolingFactor = Double.parseDouble(properties.getProperty("MultiSA.coolingFactor"));
		this.multiSAInitialTemperature = Double
				.parseDouble(properties.getProperty("MultiSA.initialTemperature", "0.0"));
		this.multiSAInnerIterations = Integer.parseInt(properties.getProperty("MultiSA.innerIterations"));
		this.multiSAOuterIterations = Integer.parseInt(properties.getProperty("MultiSA.outerIterations"));
	}

	public void init() {
		initProcessors(processorsCount);
		initTasks(tasksCount);
		initTaskConnections();
	}

	public static void main(String[] args) {
		String propertiesFileName = "configuration.properties";
		AlgorithmComparator algorithmComparator = null;
		if (args.length >= 1) {
			propertiesFileName = args[0];
		}
		try (InputStream input = new FileInputStream(propertiesFileName)) {
			Properties properties = new Properties();
			properties.load(input);
			algorithmComparator = new AlgorithmComparator(properties);
		} catch (Exception e) {
			System.err.println("Cannot read properties file!");
			e.printStackTrace();
			System.exit(1);
		}
		algorithmComparator.start();
	}

	private void start() {
		System.out.println(
				"---------------------------------------------------------------------------------------------------");
		System.out.println(
				"|   GA value    | MultiSA value |   SA value    |||    GA time    | MultiSA time  |    SA time    |");
		System.out.println(
				"----------------|---------------|---------------|||---------------|---------------|---------------|");
		long start;
		long end;
		for (int i = 0; i < howMany; i++) {
			init();
			start = System.currentTimeMillis();
			MultiprocessorSchedulingGA testGA = new MultiprocessorSchedulingGA(processors, tasks, gaPopulation);
			MultiprocessorSchedule solutionGA = testGA.process(gaGenerations);
			end = System.currentTimeMillis();
			int valueGA = testGA.getSolutionValue(solutionGA);
			long timeGA = end - start;

			start = System.currentTimeMillis();
			MultiprocessorSchedulingMultiSA testMultiSA = (multiSAInitialTemperature > 1.0
					? new MultiprocessorSchedulingMultiSA(processors, tasks, multiSACoolingFactor,
							multiSAInitialTemperature)
					: new MultiprocessorSchedulingMultiSA(processors, tasks, multiSACoolingFactor));
			MultiprocessorSchedule solutionMultiSA = testMultiSA.process(multiSAOuterIterations,
					multiSAInnerIterations);
			end = System.currentTimeMillis();
			int valueMultiSA = testMultiSA.getSolutionValue(solutionMultiSA);
			long timeMultiSA = end - start;

			start = System.currentTimeMillis();
			MultiprocessorSchedulingSA testSA = (saInitialTemperature > 1.0
					? new MultiprocessorSchedulingSA(processors, tasks, saCoolingFactor, saInitialTemperature)
					: new MultiprocessorSchedulingSA(processors, tasks, saCoolingFactor));
			MultiprocessorSchedule solutionSA = testSA.process(saIterations);
			end = System.currentTimeMillis();
			int valueSA = testSA.getSolutionValue(solutionSA);
			long timeSA = end - start;

			System.out
					.println(
							"|" + getFormattedField(valueGA) + "|" + getFormattedField(valueMultiSA) + "|"
									+ getFormattedField(valueSA)
									+ (printTime ? ("|||" + getFormattedField(timeGA) + "|"
											+ getFormattedField(timeMultiSA) + "|" + getFormattedField(timeSA)) : "")
					+ "|");
		}
		System.out.println(
				"---------------------------------------------------------------------------------------------------");
		System.out.println("Values are CPU cycles. All time measurements are in milliseconds.");
	}

	private void initProcessors(int count) {
		processors.clear();
		for (int i = 0; i < count; i++) {
			Processor processor = new Processor(i);
			processor.setName(PROCESSOR_ID_PREFIX + i);
			processors.add(processor);
		}
	}

	private void initTasks(int count) {
		tasks.clear();
		for (int i = 0; i < count; i++) {
			Task task = new Task(i);
			task.setName(TASK_ID_PREFIX + i);
			for (Processor processor : processors) {
				task.getExecutionTimes().put(processor,
						minExecutionTime + random.nextInt(maxExecutionTime - minExecutionTime));
			}
			tasks.add(task);
		}
	}

	private void initTaskConnections() {
		List<Task> available = new ArrayList<Task>(ListUtils.getRandomSublist(tasks));
		while (true) {
			if (available.size() < 2) {
				return;
			}
			Task randomTask = available.get(random.nextInt(available.size()));
			available.remove(randomTask);
			for (Task beforeTask : ListUtils.getRandomSublist(available, maxConnectionsToTask)) {
				randomTask.getTasksBefore().put(beforeTask,
						minCommunicationTime + random.nextInt(maxCommunicationTime - minCommunicationTime));
			}
		}
	}

	/** TODO do it better */
	private String getFormattedField(long value) {
		String s = String.valueOf(value);
		StringBuilder sb = new StringBuilder();
		sb.append(" ").append(s);
		for (int i = 0; i < 14 - s.length(); i++) {
			sb.append(" ");
		}
		return sb.toString();
	}
}
