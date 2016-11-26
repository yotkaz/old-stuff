package me.yotkaz.computersimulations.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by yotkaz on 2015-01-07.
 */
public class SimulationChanger {

    Container container;

    public SimulationChanger(Container container){
        this.container = container;
        this.container.setLayout(new BorderLayout());
    }

    void change(JPanel simPanel){
        container.removeAll();
        container.add(simPanel, BorderLayout.CENTER);
        container.validate();
    }




}
