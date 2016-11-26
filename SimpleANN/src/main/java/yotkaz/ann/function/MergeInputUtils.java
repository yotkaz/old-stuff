package yotkaz.ann.function;

import yotkaz.ann.base.Synapse;

import java.util.List;

/**
 * Created by yotkaz on 28.12.15.
 */
public class MergeInputUtils {

    public static double sumInput(List<Synapse> input) {
        double result = 0.0;
        for (Synapse synapse : input) {
            if (!synapse.isInputEmpty()) {
                result += synapse.getInputValue();
            }
        }
        return result;
    }
}
