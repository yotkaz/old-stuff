package me.yotkaz.computersimulations.simulation.inclinedplane;

import me.yotkaz.computersimulations.graph.AdvancedGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by yotkaz on 2015-01-23.
 */
public class Animation {

    Physics physics;

    Animation(Physics physics){
        this.physics = physics;
    }

    ArrayList<Double> x = new ArrayList<>();
    ArrayList<Double> y = new ArrayList<>();
    ArrayList<Color> color = new ArrayList<>();

    Color pink = Color.PINK;
    Color red = Color.RED;
    Color yellow = Color.YELLOW;

    private void calculateCurrentLists(int index){
        x.clear();
        y.clear();

        for(int i = 0; i<500; i++){
            color.add(pink);
            x.add(((1 / Math.tan(Math.toRadians(physics.angle))) * physics.height * 0.002 * (500-i)));
            y.add(physics.height * 0.002 * i);
        }

        double currentAngleDegree = Math.toDegrees(physics.angleList.get(index)) + 180;
        for(double i=currentAngleDegree; i <360+currentAngleDegree; i++){
            for(double j=physics.radius1; j<=(physics.radius2); j+=((physics.radius2/25.0))){
                x.add((StrictMath.cos(Math.toRadians(i)) * j) + physics.xCenterOfMass.get(index));
                y.add((StrictMath.sin(Math.toRadians(i)) * j) + physics.yCenterOfMass.get(index));
                if(i > currentAngleDegree - (15) && i < currentAngleDegree + (15)) color.add(red);
                else color.add(yellow);
            }
        }


    }

    private class AnimationRunnable implements Runnable {
        JLabel statsLabel;
        Container graphContainer;

        public AnimationRunnable(JLabel statsLabel, Container graphContainer) {
            this.statsLabel = statsLabel;
            this.graphContainer = graphContainer;
        }

        double animationSpeed = 1;
        boolean stopped = true;
        boolean suspended = false;
        public void stop(boolean bool) {
            stopped = bool;
        }
        public void suspend(boolean bool) {
            suspended = bool;
            if(!bool){
                synchronized(this) {
                    notify();
                }
            }
        }

        public void run() {
            for (int i = 0; i < physics.xCenterOfMass.size() && i < physics.yCenterOfMass.size() && !stopped; i++) {
                try {
                    synchronized(this) {
                        while (suspended)
                            wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep((long) (physics.timeInterval * 1000 * animationSpeed));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                calculateCurrentLists(i);

                if(System.currentTimeMillis()%2==0){
                    String stats = "";
                    stats += "<html>";
                    stats += "t = " + physics.time.get(i);
                    stats += "<br>v(CM) = " + physics.centerVelocity.get(i);
                    stats += "<br>&omega = " + physics.contactAngularVelocity.get(i);
                    stats += "<br>a(CM) = " + physics.centerAcceleration.get(i);
                    stats += "<br>&epsilon = " + physics.contactAngularAcceleration.get(i);
                    stats += "<br><br>sliding friction = " + physics.slidingFriction.get(i);
                    stats += "<br>air resistance = " + physics.airResistance.get(i);
                    stats += "<br>rolling resistance = " + physics.rollingResistance.get(i);
                    stats += "<br>mgsin&theta = " +  physics.mass * physics.gravity * Math.sin(Math.toRadians(physics.angle));
                    stats += "</html>";
                    statsLabel.setText(stats);
                }

                graphContainer.removeAll();
                graphContainer.add(new AdvancedGraph(true, x, y, color, "x", "y"));
                graphContainer.validate();
            }
            stopped = true;
        }}

    public void prepareAnimation(JPanel parent){
        parent.removeAll();
        parent.setLayout(new BorderLayout());

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        parent.add(split);
        split.setDividerLocation(300);

        Container splitPaneRight = new Container ();
        splitPaneRight.setLayout(new BorderLayout());

        final Container graphContainer = new Container();
        graphContainer.setLayout(new BorderLayout());
        splitPaneRight.add(graphContainer, BorderLayout.CENTER);

        Box verticalBox = Box.createVerticalBox();
        JLabel statsLabel = new JLabel();

        Box leftBox = Box.createVerticalBox();
        leftBox.add(statsLabel);

        JButton startButton = new JButton("START");
        JButton pauseButton = new JButton("PAUSE/RESUME");
        JButton stopButton = new JButton("STOP");

        final AnimationRunnable runnable = new AnimationRunnable(statsLabel, graphContainer);

        final JButton speedDown = new JButton("<<<  x" + 1 / (runnable.animationSpeed * 2));
        final JButton speedUp = new JButton("x" + 1 / (runnable.animationSpeed / 2) + "  >>>");

        Box singleHorizontalBox = Box.createHorizontalBox();
        singleHorizontalBox.add(Box.createHorizontalGlue());
        singleHorizontalBox.add(startButton);
        singleHorizontalBox.add(speedDown);
        singleHorizontalBox.add(pauseButton);
        singleHorizontalBox.add(speedUp);
        singleHorizontalBox.add(stopButton);
        singleHorizontalBox.add(Box.createHorizontalGlue());
        verticalBox.add(singleHorizontalBox);
        splitPaneRight.add(verticalBox, BorderLayout.SOUTH);

        split.setRightComponent(splitPaneRight);
        split.setLeftComponent(leftBox);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(runnable.stopped){
                    runnable.stop(false);
                    runnable.animationSpeed = 1;
                    speedDown.setText("<<<  x" + 1 / ( runnable.animationSpeed * 2));
                    speedUp.setText("x" + 1 / (runnable.animationSpeed / 2) + "  >>>");
                    Thread thread = new Thread(runnable);
                    thread.start();
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runnable.stop(true);
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!runnable.suspended) runnable.suspend(true);
                else runnable.suspend(false);
            }
        });

        speedDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double newSpeed = runnable.animationSpeed * 2;
                runnable.animationSpeed = newSpeed;
                speedDown.setText("<<<  x" + 1 / ( newSpeed * 2));
                speedUp.setText("x" + 1 / (newSpeed / 2) + "  >>>");
            }
        });

        speedUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double newSpeed = runnable.animationSpeed / 2;
                runnable.animationSpeed = newSpeed;
                speedDown.setText("<<<  x" + 1 / (newSpeed * 2));
                speedUp.setText("x" + 1 / (newSpeed / 2) + "  >>>");
            }
        });
    }

}
