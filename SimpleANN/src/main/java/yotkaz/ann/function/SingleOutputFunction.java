package yotkaz.ann.function;

import yotkaz.ann.base.Synapse;

import java.util.List;

/**
 * Created by yotkaz on 28.12.15.
 */
public abstract class SingleOutputFunction implements Function {

    public void calculate(List<Synapse> input, List<Synapse> output) {
        double outputSignal = calculate(input);
        for (Synapse synapse : output) {
            synapse.setOutputSignal(outputSignal);
        }
    }

    public abstract double calculate(List<Synapse> input);
}
