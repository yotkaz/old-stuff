package yotkaz.ai.multiprocessor.scheduling.domain;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yotkaz
 *
 */
public class Task {

	Integer id;
	String name;

	public Task(int id) {
		this.id = id;
	}

	/** task and communication time */
	Map<Task, Integer> tasksBefore = new LinkedHashMap<Task, Integer>();

	/** execution time on different processors */
	Map<Processor, Integer> executionTimes = new LinkedHashMap<Processor, Integer>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Task, Integer> getTasksBefore() {
		return tasksBefore;
	}

	public void setTasksBefore(Map<Task, Integer> tasksBefore) {
		this.tasksBefore = tasksBefore;
	}

	public Map<Processor, Integer> getExecutionTimes() {
		return executionTimes;
	}

	public void setExecutionTimes(Map<Processor, Integer> executionTimes) {
		this.executionTimes = executionTimes;
	}

	@Override
	public String toString() {
		return name;
	}

	public String toStringExecutionTimes() {
		return name + ": " + executionTimes;
	}
	
	public String toStringTasksBefore() {
		return name + ": " + tasksBefore;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}