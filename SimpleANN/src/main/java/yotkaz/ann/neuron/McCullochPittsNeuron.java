package yotkaz.ann.neuron;

import yotkaz.ann.neuron.Neuron;
import yotkaz.ann.function.SingleOutputFunction;

/**
 * Created by yotkaz on 28.12.15.
 */
public class McCullochPittsNeuron extends Neuron {

    public McCullochPittsNeuron(long id, SingleOutputFunction function) {
        super(id, function);
    }
}
