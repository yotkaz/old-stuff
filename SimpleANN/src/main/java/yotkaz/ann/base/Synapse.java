package yotkaz.ann.base;

/**
 * @author yotkaz
 */
public class Synapse {

    private Cell sender;
    private Cell receiver;

    private Double outputSignal;
    private Double inputSignal;

    private double outputWeight;
    private double inputWeight;

    public Synapse(Cell sender, Cell receiver, double outputWeight, double inputWeight) {
        this.sender = sender;
        this.receiver = receiver;
        this.outputWeight = outputWeight;
        this.inputWeight = inputWeight;
    }

    public void transferSignal() {
        if (outputSignal != null) {
            inputSignal = outputSignal * outputWeight;
        }
    }

    /**
     * @return the sender
     */
    public Cell getSender() {
        return sender;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(Cell sender) {
        this.sender = sender;
    }

    /**
     * @return the receiver
     */
    public Cell getReceiver() {
        return receiver;
    }

    /**
     * @param receiver the receiver to set
     */
    public void setReceiver(Cell receiver) {
        this.receiver = receiver;
    }

    /**
     * @return the outputSignal
     */
    public double getOutputSignal() {
        return outputSignal;
    }

    /**
     * @param outputSignal the outputSignal to set
     */
    public void setOutputSignal(double outputSignal) {
        this.outputSignal = outputSignal;
    }

    /**
     * @return the inputSignal
     */
    public double getInputSignal() {
        return inputSignal;
    }

    /**
     * @param inputSignal the inputSignal to set
     */
    public void setInputSignal(double inputSignal) {
        this.inputSignal = inputSignal;
    }

    /**
     * @return the outputWeight
     */
    public double getOutputWeight() {
        return outputWeight;
    }

    /**
     * @param outputWeight the outputWeight to set
     */
    public void setOutputWeight(double outputWeight) {
        this.outputWeight = outputWeight;
    }

    /**
     * @return the inputWeight
     */
    public double getInputWeight() {
        return inputWeight;
    }

    /**
     * @param inputWeight the inputWeight to set
     */
    public void setInputWeight(double inputWeight) {
        this.inputWeight = inputWeight;
    }

    public double getInputValue() {
        return getInputSignal() * getInputWeight();
    }

    public double getOutputValue() {
        return getOutputSignal() * getOutputSignal();
    }

    public boolean isInputEmpty() {
        return inputSignal == null;
    }

    public boolean isOutputEmpty() {
        return outputSignal == null;
    }
}
