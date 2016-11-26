package me.yotkaz.computersimulations.simulation.mathpendulum;

import me.yotkaz.computersimulations.graph.SimpleGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static javax.swing.JSplitPane.VERTICAL_SPLIT;

/**
 * Created by yotkaz on 2015-01-09.
 */
public class Animation {

    Physics physics;

    Animation(Physics physics){
        this.physics = physics;
    }

    ArrayList<Double> currentListX = new ArrayList<>();
    ArrayList<Double> currentListY = new ArrayList<>();

    public void calculateCurrentLists(int index){
            currentListX.clear();
            currentListY.clear();

            for(double i=0; i<360; i++){
                for(double j=0; j<=(physics.getLineLength()/150.0); j+=((physics.getLineLength()/150.0)*0.1)){
                    currentListX.add((StrictMath.sin(Math.toRadians(i)) * j) + physics.getxList().get(index));
                    currentListY.add((StrictMath.cos(Math.toRadians(i)) * j) + physics.getyList().get(index));
                }
            }

            for(double i=0; i<=physics.getLineLength(); i+=(physics.getLineLength()*0.005)){
                currentListX.add(StrictMath.sin(Math.toRadians(physics.getAngleList().get(index))) * i);
                currentListY.add(physics.getLineLength() - (StrictMath.cos(Math.toRadians(physics.getAngleList().get(index))) * i));
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
            for (int i = 0; i < physics.getxList().size() && i < physics.getyList().size() && !stopped; i++) {
                try {
                    synchronized(this) {
                        while (suspended)
                            wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep((long) (physics.getTimeInterval() * 1000 * animationSpeed));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                calculateCurrentLists(i);

                if(System.currentTimeMillis()%2==0){
                    String stats = "";
                    stats += "<html>time: " + physics.getTimeList().get(i);
                    stats += "<br>angle: " + physics.getAngleList().get(i);
                    stats += "<br>x: " + physics.getxList().get(i);
                    stats += "<br>y: " + physics.getyList().get(i);
                    stats += "<br>linear speed: " + physics.getLinearSpeedList().get(i);
                    stats += "<br>angular speed: " + physics.getAngularSpeedList().get(i);
                    stats += "<br>linear acceleration: " + physics.getLinearAccelerationList().get(i);
                    stats += "<br>angular acceleration: " + physics.getAngularAccelerationList().get(i);
                    stats += "<br>air resistance: " + physics.getAirResistanceList().get(i);
                    stats += "</html>";
                    statsLabel.setText(stats);
                }

                graphContainer.removeAll();
                graphContainer.add(new SimpleGraph(currentListX, currentListY, "x", "height"));
                graphContainer.validate();
        }
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