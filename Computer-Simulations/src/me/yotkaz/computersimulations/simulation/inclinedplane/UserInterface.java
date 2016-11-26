package me.yotkaz.computersimulations.simulation.inclinedplane;

import me.yotkaz.computersimulations.graph.AdvancedGraph;
import me.yotkaz.computersimulations.graph.SimpleGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

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
        tabs.add(contactPointGraph, "contactPoint");
        tabs.add(animationPanel, "animation");
        add(tabs, BorderLayout.CENTER);
    }

    Physics physics;

    Physics.SolidState type;
    int disabled1 = 1000;
    int disabled2 = 1000;

    JTabbedPane tabs = new JTabbedPane();
    JPanel inputPanel = new JPanel();

    JLabel typeLabel = new JLabel("object type: ");
    JLabel timeIntervalLabel = new JLabel ("time interval: ");
    JLabel radius1Label = new JLabel("radius 1: ");
    JLabel radius2Label = new JLabel("radius 2: ");
    JLabel lengthLabel = new JLabel("length: ");
    JLabel massLabel = new JLabel("mass: ");
    JLabel gravityLabel = new JLabel("gravity: ");
    JLabel angleLabel = new JLabel("angle: ");
    JLabel heightLabel = new JLabel("height: ");
    JLabel staticFrictionCoefficientLabel = new JLabel("static friction coefficient: ");
    JLabel rollingResistanceCoefficientLabel = new JLabel("rolling resistance coefficient: ");
    JLabel airResistanceCoefficientLabel = new JLabel("air resistance coefficient: ");
    JLabel airDensityLabel = new JLabel("air density: ");

    String[] typeNames = {"ball", "hollow sphere", "cylinder", "cylindrical shell", "cylindrical tube"};
    JComboBox typesComboBox= new JComboBox(typeNames);

    JTextField timeIntervalField = new JTextField("0.05", 15);
    JTextField radius1Field = new JTextField("1", 15);
    JTextField radius2Field = new JTextField("2", 15);
    JTextField lengthField = new JTextField("10", 15);
    JTextField massField = new JTextField("100", 15);
    JTextField gravityField = new JTextField("9.81", 15);
    JTextField angleField = new JTextField("30", 15);
    JTextField heightField = new JTextField("50", 15);
    JTextField staticFrictionCoefficientField = new JTextField("0.5", 15);
    JTextField rollingResistanceCoefficientField = new JTextField("0.015", 15);
    JTextField airResistanceCoefficientField = new JTextField("0.5", 15);
    JTextField airDensityField = new JTextField("1.225", 15);

    JButton calculateButton = new JButton("calculate");

    JLabel[] labels = {typeLabel, timeIntervalLabel, radius1Label, radius2Label, lengthLabel, massLabel, gravityLabel, angleLabel, heightLabel, staticFrictionCoefficientLabel, rollingResistanceCoefficientLabel, airResistanceCoefficientLabel, airDensityLabel};
    JComponent[] fields = {typesComboBox, timeIntervalField, radius1Field, radius2Field, lengthField, massField, gravityField, angleField, heightField, staticFrictionCoefficientField, rollingResistanceCoefficientField, airResistanceCoefficientField, airDensityField};

    private void prepareInputPanel(){
        inputPanel.setLayout(new BorderLayout());
        final Box verticalBox = Box.createVerticalBox();

        typesComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputPanel.removeAll();
                verticalBox.removeAll();

                disabled1 = 1000;
                disabled2 = 1000;

                JComboBox combo = (JComboBox) e.getSource();
                switch ((String) combo.getSelectedItem()){
                    case "ball":
                        disabled1 = 4;
                        disabled2 = 2;
                        type = Physics.SolidState.BALL;
                        break;
                    case "hollow sphere":
                        disabled1 = 4;
                        disabled2 = 2;
                        type = Physics.SolidState.HOLLOW_SPHERE;
                        break;
                    case "cylinder":
                        disabled1 = 2;
                        type = Physics.SolidState.CYLINDER;
                        break;
                    case "cylindrical shell":
                        disabled1 = 2;
                        type = Physics.SolidState.CYLINDRICAL_SHELL;
                        break;
                    case "cylindrical tube":
                        type = Physics.SolidState.CYLINDRICAL_TUBE;
                }

                for(int i=0; i<labels.length; i++){
                    if(i != disabled1 && i != disabled2){
                        Box horizontalBox = Box.createHorizontalBox();
                        fields[i].setMaximumSize(fields[i].getPreferredSize());
                        horizontalBox.add(labels[i]);
                        horizontalBox.add(Box.createHorizontalGlue());
                        horizontalBox.add(fields[i]);
                        horizontalBox.setMaximumSize(new Dimension(600, 30));
                        verticalBox.add(horizontalBox);
                    }
                }
                verticalBox.add(Box.createVerticalStrut(5));
                Box singleButtonBox = Box.createHorizontalBox();
                singleButtonBox.add(Box.createHorizontalGlue());
                singleButtonBox.add(calculateButton);
                singleButtonBox.add(Box.createHorizontalGlue());
                verticalBox.add(singleButtonBox);
                inputPanel.add(verticalBox, BorderLayout.NORTH);
                inputPanel.validate();
            }
        });
        typesComboBox.setSelectedIndex(0);
    }

    private void prepareCalculateButton(){
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                physics = inputToPhysics();
                prepareContactPointGraph();
                prepareStatsPanel();
                prepareAnimationPanel();
            }
        });
    }

    JPanel contactPointGraph = new JPanel();
    private void prepareContactPointGraph(){
        contactPointGraph.setLayout(new BorderLayout());
        if(physics != null){
            contactPointGraph.removeAll();
            ArrayList<Color> colorList = new ArrayList<>();
            Color yellow = Color.YELLOW;
            Color pink = Color.PINK;
            ArrayList<Double> x = new ArrayList<>();
            ArrayList<Double> y = new ArrayList<>();
            for(int i = 0; i<physics.xPointOfContact.size(); i++){
                colorList.add(yellow);
                x.add(physics.xPointOfContact.get(i));
                y.add(physics.yPointOfContact.get(i));
            }
            for(int i = 0; i<1000; i++){
                colorList.add(pink);
                x.add(((1 / Math.tan(Math.toRadians(physics.angle))) * physics.height * 0.001 * (1000-i)));
                y.add(physics.height * 0.001 * i);
            }
            contactPointGraph.add(new AdvancedGraph(true, x, y, colorList, "x", "y"), BorderLayout.CENTER);
            contactPointGraph.validate();
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
            results += "total time: " + physics.time.get(physics.time.size() - 1);
            results += "<br>max linear speed (center of mass): " + Collections.max(physics.centerVelocity);
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

    private Physics inputToPhysics(){
        double r1, r2, len;
        r2 = Double.parseDouble(radius2Field.getText());
        if(disabled1 == 2 || disabled2 == 2) r1 = 0;
        else r1 = Double.parseDouble(radius1Field.getText());
        if(disabled1 == 4 || disabled2 == 4) len = 0;
        else len = Double.parseDouble(lengthField.getText());
        if(r1>r2) r2 = r1;

        return new Physics(
                type,
                Double.parseDouble(timeIntervalField.getText()),
                r1,
                r2,
                len,
                Double.parseDouble(massField.getText()),
                Double.parseDouble(gravityField.getText()),
                Double.parseDouble(angleField.getText()),
                Double.parseDouble(heightField.getText()),
                Double.parseDouble(staticFrictionCoefficientField.getText()),
                Double.parseDouble(rollingResistanceCoefficientField.getText()),
                Double.parseDouble(airResistanceCoefficientField.getText()),
                Double.parseDouble(airDensityField.getText())
        );
    }

}