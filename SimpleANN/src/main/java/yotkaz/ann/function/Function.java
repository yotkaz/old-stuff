package yotkaz.ann.function;

import yotkaz.ann.base.Synapse;

import java.util.List;

/**
 * @author yotkaz
 *
 */
public interface Function {

	public final static double ONE = 1.0;
	public final static double ZERO = 0.0;

	public void calculate(List<Synapse> input, List<Synapse> output);

}
