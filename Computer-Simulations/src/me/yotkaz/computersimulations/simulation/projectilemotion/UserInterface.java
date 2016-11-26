package me.yotkaz.computersimulations.simulation.projectilemotion;

import me.yotkaz.computersimulations.graph.SimpleGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by yotkaz on 2015-01-22.
 */

public class UserInterface extends JPanel {

    public UserInterface(){
        setLayout(new BorderLayout());
        prepareInputPanel();
        prepareCalculateButton();
        tabs.add(inputPanel, "input");
        tabs.add(statsPanel, "results");
        tabs.add(distanceHeightGraph, "distance / height");
        tabs.add(speedsGraph, "x speed / y speed");
        tabs.add(animationPanel, "animation");
        add(tabs, BorderLayout.CENTER);
    }

    Physics physics;

    JTabbedPane tabs = new JTabbedPane();
    JPanel inputPanel = new JPanel();

    private JLabel      timeIntervalLabel = new JLabel("Time Interval:"),
            angleLabel = new JLabel("angle:"),
            objectMassLabel = new JLabel("object mass:"),
            objectAreaLabel = new JLabel("object area:"),
            initialSpeedLabel = new JLabel("initial speed:"),
            initialHeightLabel = new JLabel("initial height:"),
            gravityLabel = new JLabel("gravity:"),
            airDensityLabel = new JLabel("air density:"),
            dragCoefficientLabel = new JLabel("drag coefficient:");

    public JTextField   timeIntervalField = new JTextField("0.005", 7),
            angleField = new JTextField("45.0", 7),
            objectMassField = new JTextField("1.0", 7),
            objectAreaField = new JTextField("0.0314", 7),
            initialSpeedField = new JTextField("150.0", 7),
            initialHeightField = new JTextField("5.0", 7),
            gravityField = new JTextField("9.81", 7),
            airDensityField = new JTextField("1.225", 7),
            dragCoefficientField = new JTextField("0.5", 7);

    private JButton calculateButton = new JButton("calculate");

    private JLabel[] labels = {timeIntervalLabel, angleLabel, objectMassLabel, objectAreaLabel, initialSpeedLabel, initialHeightLabel, gravityLabel, airDensityLabel, dragCoefficientLabel};
    private JTextField[] textFields = {timeIntervalField, angleField, objectMassField, objectAreaField, initialSpeedField, initialHeightField, gravityField, airDensityField, dragCoefficientField};

    private void prepareInputPanel(){
        inputPanel.setLayout(new BorderLayout());

        Box mainHorizontalBox = Box.createHorizontalBox();
        Box verticalBox = Box.createVerticalBox();

        for(int i=0; i<labels.length; i++){
            Box horizontalBox = Box.createHorizontalBox();
            textFields[i].setMaximumSize(textFields[i].getPreferredSize());
            horizontalBox.add(labels[i]);
            horizontalBox.add(Box.createHorizontalGlue());
            horizontalBox.add(textFields[i]);
            horizontalBox.setMaximumSize(new Dimension(500, 50));
            verticalBox.add(horizontalBox);
        }

        verticalBox.add(Box.createVerticalStrut(5));
        Box singleButtonBox = Box.createHorizontalBox();
        singleButtonBox.add(Box.createHorizontalGlue());
        singleButtonBox.add(calculateButton);
        singleButtonBox.add(Box.createHorizontalGlue());
        verticalBox.add(singleButtonBox);

        mainHorizontalBox.add(Box.createHorizontalGlue());
        mainHorizontalBox.add(verticalBox);
        mainHorizontalBox.add(Box.createHorizontalGlue());
        inputPanel.add(mainHorizontalBox, BorderLayout.NORTH);

        prepareCalculateButton();
    }

    private void prepareCalculateButton(){
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                physics = inputToPhysics();
                prepareDistanceHeightGraph();
                prepareSpeedsGraph();
                prepareStatsPanel();
                prepareAnimationPanel();
            }
        });
    }

    JPanel distanceHeightGraph = new JPanel();
    private void prepareDistanceHeightGraph(){
        distanceHeightGraph.setLayout(new BorderLayout());
        if(physics != null){
            distanceHeightGraph.removeAll();
            distanceHeightGraph.add(new SimpleGraph(physics.getDistanceList(), physics.getHeightList(), "distance", "height"));
            distanceHeightGraph.validate();
        }

    }

    JPanel speedsGraph = new JPanel();
    private void prepareSpeedsGraph(){
        speedsGraph.setLayout(new BorderLayout());
        if(physics != null){
            speedsGraph.removeAll();
            speedsGraph.add(new SimpleGraph(physics.getHorizontalSpeedList(), physics.getVerticalSpeedList(), "x speed", "y speed"));
            speedsGraph.validate();
        }
    }

    JPanel statsPanel = new JPanel();
    private void prepareStatsPanel(){
        statsPanel.removeAll();
        statsPanel.setLayout(new BorderLayout());
        Box horizontalBox = Box.createHorizontalBox();
        statsPanel.add(horizontalBox, BorderLayout.NORTH);

        String results = "";
        if(physics != null){
            results += "<html>";
            results += "total time: " + physics.getTimeList().get(physics.getTimeList().size() - 1);
            results += "<br>total distance: " + physics.getDistanceList().get(physics.getDistanceList().size() - 1);
            results += "</html>";
        }

        horizontalBox.add(Box.createHorizontalGlue());
        JLabel label = new JLabel(results);
        label.setMaximumSize(label.getPreferredSize());
        horizontalBox.add(label);
        horizontalBox.add(Box.createHorizontalGlue());
        statsPanel.validate();
    }

    JPanel animationPanel = new JPanel();
    private void prepareAnimationPanel(){
        if(physics != null){
            Animation animation = new Animation(physics);
            animation.prepareAnimation(animationPanel);
        }
    }

    Double[] tryParseDouble(String string){
        Double[] arrayToReturn = new Double[2];
        arrayToReturn[0] = 0.0;
        try {
            arrayToReturn[1] = Double.parseDouble(string);
        } catch (NumberFormatException e) {
            arrayToReturn[0] = 1.0; // ERROR CODE
        }
        return arrayToReturn;
    }

    private Physics inputToPhysics(){
        Double[] timeInterval = tryParseDouble(timeIntervalField.getText());
        if(timeInterval[0]!=0 || timeInterval[1]<=0) {
            System.err.println("timeInterval Error");
            return null;
        }

        Double[] angle = tryParseDouble(angleField.getText());
        if(angle[0]!=0 || angle[1]<0 || angle[1]>90){
            System.err.println("angle Error");
            return null;
        }

        Double[] objectMass = tryParseDouble(objectMassField.getText());
        if(objectMass[0]!=0 || objectMass[1]<=0){
            System.err.println("objectMass Error");
            return null;
        }

        Double[] objectArea = tryParseDouble(objectAreaField.getText());
        if(objectArea[0]!=0 || objectArea[1]<=0){
            System.err.println("objectArea Error");
            return null;
        }

        Double[] initialSpeed = tryParseDouble( initialSpeedField.getText());
        if(initialSpeed[0]!=0 || initialSpeed[1]<0){
            System.err.println("initialSpeed Error");
            return null;
        }

        Double[] initialHeight = tryParseDouble(initialHeightField.getText());
        if(initialHeight[0]!=0){
            System.err.println("initialHeight Error");
            return null;
        }

        Double[] gravity = tryParseDouble(gravityField.getText());
        if(gravity[0]!=0 || gravity[1]<=0){
            System.err.println("gravity Error");
            return null;
        }

        Double[] airDensity = tryParseDouble(airDensityField.getText());
        if(airDensity[0]!=0 || airDensity[1]<0){
            System.err.println("airDensity Error");
            return null;
        }

        Double[] dragCoefficient = tryParseDouble(dragCoefficientField.getText());
        if(dragCoefficient[0]!=0 || dragCoefficient[1]<0){
            System.err.println("dragCoefficient Error");
            return null;
        }

        return new Physics(timeInterval[1], gravity[1], angle[1], objectMass[1], objectArea[1], initialSpeed[1], initialHeight[1], airDensity[1], dragCoefficient[1]);
    }

}