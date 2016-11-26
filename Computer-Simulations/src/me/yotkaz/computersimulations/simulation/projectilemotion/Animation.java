package me.yotkaz.computersimulations.simulation.projectilemotion;

import me.yotkaz.computersimulations.graph.SimpleGraph;

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

    private void calculateCurrentLists(int index){
        x.clear();
        y.clear();

        for(int i=0; i<=index; i++){
            x.add(physics.getDistanceList().get(i));
            y.add(physics.getHeightList().get(i));
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
            for (int i = 0; i < physics.getDistanceList().size() && i < physics.getHeightList().size() && !stopped; i++) {
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
                    stats += "<html>";
                    stats += "t = " + physics.getTimeList().get(i);
                    stats += "<br>x = " + physics.getDistanceList().get(i);
                    stats += "<br>y = " + physics.getHeightList().get(i);
                    stats += "<br>xSpeed = " + physics.getHorizontalSpeedList().get(i);
                    stats += "<br>ySpeed = " + physics.getVerticalSpeedList().get(i);
                    stats += "</html>";
                    statsLabel.setText(stats);
                }

                graphContainer.removeAll();
                graphContainer.add(new SimpleGraph(x, y, "distance", "height"));
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
