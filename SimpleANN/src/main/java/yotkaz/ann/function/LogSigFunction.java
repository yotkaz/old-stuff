package yotkaz.ann.function;

import yotkaz.ann.base.Synapse;

import java.util.List;

/**
 * Created by yotkaz on 29.12.15.
 */
public class LogSigFunction extends ActivationFunction {

    private double steepnessRatio;

    public LogSigFunction(double steepnessRatio) {
        this.steepnessRatio = steepnessRatio;
    }


    @Override
    public double calculate(double input) {
        return ONE / (ONE + Math.exp(0.0 - (steepnessRatio * input)));
    }

    @Override
    public double mergeInput(List<Synapse> input) {
        return MergeInputUtils.sumInput(input);
    }

    public void setSteepnessRatio(double steepnessRatio) {
        this.steepnessRatio = steepnessRatio;
    }

    public double getSteepnessRatio() {
        return steepnessRatio;
    }
}
