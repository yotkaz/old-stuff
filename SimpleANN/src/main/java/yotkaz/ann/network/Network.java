package yotkaz.ann.network;

import java.util.ArrayList;
import java.util.List;

import yotkaz.ann.neuron.Neuron;

/**
 * @author yotkaz
 *
 */
public abstract class Network {

	private List<Neuron> neurons = new ArrayList<Neuron>();
	
	public void step() {
		transferSignals();
		calculate();
	}

	private void transferSignals() {
		beforeTransfer();
		for (Neuron neuron : neurons) {
			neuron.sendSignal();
		}
		afterTransfer();
	}

	private void calculate() {
		beforeCalculation();
		for (Neuron neuron : neurons) {
			neuron.calculate();
		}
		afterCalculation();
	}
	
	/** executed before calculation */
	public void beforeCalculation() {
		
	}

	/** executed after calculation */
	public void afterCalculation() {
		
	}
	
	/** executed before signals transfer */
	public void beforeTransfer() {
		
	}

	/** executed after signals transfer */
	public void afterTransfer() {
		
	}
	
	public abstract void build();

	public List<Neuron> getNeurons() {
		return neurons;
	}
}
