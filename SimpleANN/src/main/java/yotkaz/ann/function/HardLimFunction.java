package yotkaz.ann.function;

import yotkaz.ann.base.Synapse;

import java.util.List;

/**
 * Created by yotkaz on 28.12.15.
 */
public class HardLimFunction extends ActivationFunction {

    private double threshold = 0;

    public HardLimFunction(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public double calculate(double input) {
        return input >= threshold ? ONE : ZERO;
    }

    @Override
    public double mergeInput(List<Synapse> input) {
        return MergeInputUtils.sumInput(input);
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}
