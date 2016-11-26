package me.yotkaz.computersimulations.simulation.projectilemotionOLD;

import javax.swing.*;
import java.awt.*;

/**
 * Created by yotkaz on 2014-11-14.
 */
public class UserInterface extends JPanel {

    private BorderLayout mainBorderLayout = new BorderLayout();

    private Box         fieldsVerticalBox = Box.createVerticalBox();

    public Container getGraphContainer() {
        return graphContainer;
    }

    private Container   graphContainer = new Container();

    private JLabel      timeIntervalLabel = new JLabel("Time Interval:"),
                        angleLabel = new JLabel("Angle:"),
                        objectMassLabel = new JLabel("Object Mass:"),
                        objectAreaLabel = new JLabel("Object Area:"),
                        initialSpeedLabel = new JLabel("Initial Speed:"),
                        initialHeightLabel = new JLabel("Initial Height:"),
                        gravityLabel = new JLabel("Gravity:"),
                        airDensityLabel = new JLabel("Air Density:"),
                        dragCoefficientLabel = new JLabel("Drag Coefficient:");

    public JTextField   timeIntervalField = new JTextField("0.005", 7),
                        angleField = new JTextField("45.0", 7),
                        objectMassField = new JTextField("1.0", 7),
                        objectAreaField = new JTextField("0.0314", 7),
                        initialSpeedField = new JTextField("150.0", 7),
                        initialHeightField = new JTextField("5.0", 7),
                        gravityField = new JTextField("9.81", 7),
                        airDensityField = new JTextField("1.225", 7),
                        dragCoefficientField = new JTextField("0.5", 7);

    private JButton     calculateButton = new JButton("Calculate");
    private JButton     graphDistanceHeightButton = new JButton("Graph: Distance and Height");
    private JButton     graphTimeSpeedButton = new JButton("Graph: Time and Speed");
    private JButton     graphHorizontalVerticalSpeedButton = new JButton("Graph: X and Y Speed");
    private JButton     animationDistanceHeightButton = new JButton("Animation");

    private JButton[] buttonArray = {calculateButton, graphDistanceHeightButton, graphTimeSpeedButton, graphHorizontalVerticalSpeedButton, animationDistanceHeightButton};
    private JLabel[] labels = {timeIntervalLabel, angleLabel, objectMassLabel, objectAreaLabel, initialSpeedLabel, initialHeightLabel, gravityLabel, airDensityLabel, dragCoefficientLabel};
    private JTextField[] textFields = {timeIntervalField, angleField, objectMassField, objectAreaField, initialSpeedField, initialHeightField, gravityField, airDensityField, dragCoefficientField};

    private void prepareButtons(){
        ButtonsListener buttonsListener = new ButtonsListener(this);

        calculateButton.setActionCommand("calculate");
        calculateButton.addActionListener(buttonsListener);

        animationDistanceHeightButton.setActionCommand("animation_distance_height");
        animationDistanceHeightButton.addActionListener(buttonsListener);

        graphDistanceHeightButton.setActionCommand("distance_height");
        graphDistanceHeightButton.addActionListener(buttonsListener);

        graphTimeSpeedButton.setActionCommand("time_speed");
        graphTimeSpeedButton.addActionListener(buttonsListener);

        graphHorizontalVerticalSpeedButton.setActionCommand("horizontal_vertical_speed");
        graphHorizontalVerticalSpeedButton.addActionListener(buttonsListener);

    }

    private void createFieldsBox(){
        if(labels.length == textFields.length){
            fieldsVerticalBox.add(Box.createRigidArea(new Dimension(0,5)));
            for(int i=0; i<labels.length; i++){
                textFields[i].setMaximumSize(textFields[i].getPreferredSize());
                Box singleHorizontalBox = Box.createHorizontalBox();
                singleHorizontalBox.add(Box.createRigidArea(new Dimension(5,0)));
                singleHorizontalBox.add(labels[i]);
                singleHorizontalBox.add(Box.createHorizontalGlue());
                singleHorizontalBox.add(Box.createRigidArea(new Dimension(10, 0)));
                singleHorizontalBox.add(textFields[i]);
                fieldsVerticalBox.add(singleHorizontalBox);
            }

            for (JButton button : buttonArray) {
                Container singleHorizontalBox = new Container();
                singleHorizontalBox.setLayout(new BorderLayout());
                singleHorizontalBox.add(button, BorderLayout.NORTH);
                singleHorizontalBox.setMaximumSize(new Dimension((int) singleHorizontalBox.getMaximumSize().getWidth(), (int) singleHorizontalBox.getPreferredSize().getHeight()));
                fieldsVerticalBox.add(singleHorizontalBox);
            }

            add(fieldsVerticalBox, BorderLayout.WEST);

        }
        else System.err.println("\'labels\' array doesn't equal \'textFields\' array! Check simulation GUI.");
    }

    public void createGUI(){
        setLayout(mainBorderLayout);
        createFieldsBox();
        graphContainer.setLayout(new BorderLayout());
        add(graphContainer, BorderLayout.CENTER);
    }



    public UserInterface(){
        prepareButtons();
        createGUI();
    }

}
