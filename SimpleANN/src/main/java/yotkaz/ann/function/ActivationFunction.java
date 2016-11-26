package yotkaz.ann.function;

import yotkaz.ann.base.Synapse;

import java.util.List;

/**
 * Created by yotkaz on 28.12.15.
 */
public abstract class ActivationFunction extends SingleOutputFunction {

    @Override
    public double calculate(List<Synapse> input) {
        double inputValue = mergeInput(input);
        return calculate(inputValue);
    }

    public abstract double calculate(double input);

    public abstract double mergeInput(List<Synapse> input);

}
