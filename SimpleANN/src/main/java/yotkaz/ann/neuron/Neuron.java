package yotkaz.ann.neuron;

import yotkaz.ann.base.Cell;
import yotkaz.ann.base.Synapse;
import yotkaz.ann.function.Function;

/**
 * @author yotkaz
 *
 */
public class Neuron extends Cell {

	private Function function;

	public Neuron(long id, Function function) {
		super(id);
		this.function = function;
	}

	public void calculate() {
		function.calculate(getInput(), getOutput());
	}
	
	public void sendSignal() {
		for (Synapse synapse : getOutput()) {
			synapse.transferSignal();
		}
	}

	/**
	 * @return the function
	 */
	public Function getFunction() {
		return function;
	}

	/**
	 * @param function
	 *            the function to set
	 */
	public void setFunction(Function function) {
		this.function = function;
	}
}
