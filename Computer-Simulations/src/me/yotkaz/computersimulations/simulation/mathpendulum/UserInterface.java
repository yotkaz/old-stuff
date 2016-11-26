package me.yotkaz.computersimulations.simulation.mathpendulum;

import me.yotkaz.computersimulations.graph.SimpleGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

/**
 * Created by yotkaz on 2015-01-09.
 */
public class UserInterface extends JPanel{

    public UserInterface(){
        setLayout(new BorderLayout());

        prepareInputPanel();
        add(tabs, BorderLayout.CENTER);
        tabs.add("input", inputPanel);

        prepareResultsPanel();
        tabs.add("results", resultsPanel);

        prepareAmplitudeTimePanel();
        tabs.add("amplitude Graph", amplitudeTimePanel);

        prepareAnimationPanel();
        tabs.add("animation", animationPanel);
    }

    Physics physics;

    private JTabbedPane tabs = new JTabbedPane();


    private JPanel inputPanel = new JPanel();

    private JLabel timeIntervalLabel = new JLabel("time interval:");
    private JLabel angleLabel = new JLabel("initial angle:");
    private JLabel lineLengthLabel = new JLabel("line length:");
    private JLabel linearSpeedLabel = new JLabel("initial linear speed:");
    private JLabel dragCoefficientLabel = new JLabel("drag coefficient:");
    private JLabel airDensityLabel = new JLabel("air density:");
    private JLabel objectAreaLabel = new JLabel("object area:");
    private JLabel objectMassLabel = new JLabel("object mass:");
    private JLabel gravityLabel = new JLabel("gravity:");
    private JLabel angleLimitLabel = new JLabel("angle limit:");
    private JLabel linearSpeedLimitLabel = new JLabel("linear speed limit:");
    private JLabel stepLimitLabel = new JLabel("step limit (0 = unlimited):");

    private JTextField timeIntervalField = new JTextField("0.01", 10);
    private JTextField angleField = new JTextField("30.0", 10);
    private JTextField lineLengthField = new JTextField("5.0", 10);
    private JTextField linearSpeedField = new JTextField("0.0", 10);
    private JTextField dragCoefficientField = new JTextField("0.5", 10);
    private JTextField airDensityField = new JTextField("1.225", 10);
    private JTextField objectAreaField = new JTextField("1", 10);
    private JTextField objectMassField = new JTextField("1", 10);
    private JTextField gravityField = new JTextField("9.81", 10);
    private JTextField angleLimitField = new JTextField("0.01", 10);
    private JTextField linearSpeedLimitField = new JTextField("0.01", 10);
    private JTextField stepLimitField = new JTextField("0", 10);

    private JButton calculateButton = new JButton("calculate");

    private void prepareInputPanel(){
        inputPanel.setLayout(new BorderLayout());

        Box mainHorizontalBox = Box.createHorizontalBox();
        Box verticalBox = Box.createVerticalBox();

        JLabel[] inputLabels = {timeIntervalLabel, angleLabel, lineLengthLabel, linearSpeedLabel, dragCoefficientLabel, airDensityLabel, objectAreaLabel, objectMassLabel, gravityLabel, angleLimitLabel, linearSpeedLimitLabel, stepLimitLabel};
        JTextField[] inputFields = {timeIntervalField, angleField, lineLengthField, linearSpeedField, dragCoefficientField, airDensityField, objectAreaField, objectMassField, gravityField, angleLimitField, linearSpeedLimitField, stepLimitField};

        for(int i=0; i<inputLabels.length; i++){
            Box horizontalBox = Box.createHorizontalBox();
            inputFields[i].setMaximumSize(inputFields[i].getPreferredSize());
            horizontalBox.add(inputLabels[i]);
            horizontalBox.add(Box.createHorizontalGlue());
            horizontalBox.add(inputFields[i]);
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
            public void actionPerformed(ActionEvent e) {
                physics = inputToPhysics();
                prepareResultsPanel();
                prepareAmplitudeTimePanel();
                prepareAnimationPanel();
            }
        });
    }

    private Physics inputToPhysics(){
        return new Physics(
                Double.parseDouble(timeIntervalField.getText()),
                Double.parseDouble(angleField.getText()),
                Double.parseDouble(lineLengthField.getText()),
                Double.parseDouble(linearSpeedField.getText()),
                Double.parseDouble(dragCoefficientField.getText()),
                Double.parseDouble(airDensityField.getText()),
                Double.parseDouble(objectAreaField.getText()),
                Double.parseDouble(objectMassField.getText()),
                Double.parseDouble(gravityField.getText()),
                Double.parseDouble(angleLimitField.getText()),
                Double.parseDouble(linearSpeedLimitField.getText()),
                Double.parseDouble(stepLimitField.getText())
        );
    }

    JPanel amplitudeTimePanel = new JPanel();
    private void prepareAmplitudeTimePanel(){
        amplitudeTimePanel.setLayout(new BorderLayout());
        if(physics != null){
            amplitudeTimePanel.removeAll();
            amplitudeTimePanel.add(new SimpleGraph(physics.getTimeList(), physics.getxList(), "time", "amplitude"), BorderLayout.CENTER);
            amplitudeTimePanel.validate();
        }

    }

    JPanel resultsPanel = new JPanel();
    private void prepareResultsPanel(){
        resultsPanel.removeAll();
        resultsPanel.setLayout(new BorderLayout());
        Box horizontalBox = Box.createHorizontalBox();
        resultsPanel.add(horizontalBox, BorderLayout.NORTH);

        String results = "";
        if(physics != null){
            results += "<html>max x: " + Collections.max(physics.getxList());
            results += "<br>min x: " + Collections.min(physics.getxList());
            results += "<br>max y: " + Collections.max(physics.getyList());
            results += "<br>max period: " + Collections.max(physics.getPeriodList());
            results += "</html>";
        }

        horizontalBox.add(Box.createHorizontalGlue());
        JLabel label = new JLabel(results);
        label.setMaximumSize(label.getPreferredSize());
        horizontalBox.add(label);
        horizontalBox.add(Box.createHorizontalGlue());
        resultsPanel.validate();
    }

    JPanel animationPanel = new JPanel();
    private void prepareAnimationPanel(){
        if(physics != null){
            Animation animation = new Animation(physics);
            animation.prepareAnimation(animationPanel);
        }
    }


}