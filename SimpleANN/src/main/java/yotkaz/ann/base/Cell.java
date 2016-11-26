package yotkaz.ann.base;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yotkaz
 *
 */
public class Cell {

	private long id;
	
	private List<Synapse> input = new ArrayList<Synapse>();
	private List<Synapse> output = new ArrayList<Synapse>();

	public Cell(long id) {
		this.id = id;
	}
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * @return the input
	 */
	public List<Synapse> getInput() {
		return input;
	}

	/**
	 * @param input
	 *            the input to set
	 */
	public void setInput(List<Synapse> input) {
		this.input = input;
	}

	/**
	 * @return the output
	 */
	public List<Synapse> getOutput() {
		return output;
	}

	/**
	 * @param output
	 *            the output to set
	 */
	public void setOutput(List<Synapse> output) {
		this.output = output;
	}
}
