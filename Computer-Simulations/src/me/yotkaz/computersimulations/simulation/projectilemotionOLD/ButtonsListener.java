package me.yotkaz.computersimulations.simulation.projectilemotionOLD;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by yotkaz on 2014-11-25.
 */
public class ButtonsListener implements ActionListener{

    UserInterface ui;

    InputParameters params;
    Simulation sim;

    void setCalculations(){
        params = new InputParameters(ui);
        sim = new Simulation(params);
    }

    public ButtonsListener(UserInterface ui){
        this.ui = ui;
        setCalculations();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String code = e.getActionCommand();
        switch (code){
            case "calculate":                   setCalculations();
                                                break;

            case "animation_distance_height":   sim.animationDistanceHeight(ui.getGraphContainer());
                                                break;

            case "distance_height":             sim.graphDistanceHeight(ui.getGraphContainer());
                                                break;

            case "time_speed":                  sim.graphTimeSpeed(ui.getGraphContainer());
                                                break;

            case "horizontal_vertical_speed":   sim.graphHorizontalSpeedVerticalSpeed(ui.getGraphContainer());
                                                break;


        }
    }
}
