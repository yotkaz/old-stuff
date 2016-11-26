package me.yotkaz.computersimulations.simulation.projectilemotionOLD;

import me.yotkaz.computersimulations.graph.SimpleGraph;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by yotkaz on 2014-11-25.
 */
public class Simulation {

    public void setPhysics(Physics physics) {
        this.physics = physics;
    }

    private Physics physics;

    Simulation(InputParameters inputParam){
        setPhysics(inputParam.getPhysics());
        this.physics.simulate();
    }

    private String getStats (){
        Object maxHeight = Collections.max(physics.getHeightList());
        Object maxSpeed = Collections.max(physics.getSpeedList());
        Object maxSpeedX = Collections.max(physics.getHorizontalSpeedList());
        Object maxSpeedY = Collections.max(physics.getVerticalSpeedList());
        Object maxResistance = Collections.max(physics.getAirResistanceList());

        String toStats = "<html>";
        toStats += "Time: " + physics.getTimeList().get(physics.getTimeList().size()-1);
        toStats += "<br>Total distance: " + physics.getDistanceList().get(physics.getDistanceList().size()-1);
        toStats += "<br>Max Height: " + (double)maxHeight + " at distance: " + physics.getDistanceList().get(physics.getHeightList().indexOf(maxHeight));
        toStats += "<br>Max Speed: " + (double)maxSpeed + " at distance: " + physics.getDistanceList().get(physics.getSpeedList().indexOf(maxSpeed)) + " and height: " + physics.getHeightList().get(physics.getSpeedList().indexOf(maxSpeed));
        toStats += "<br>Max Horizontal Speed: " + (double)maxSpeedX + " at distance: " + physics.getDistanceList().get(physics.getHorizontalSpeedList().indexOf(maxSpeedX)) + " and height: " + physics.getHeightList().get(physics.getHorizontalSpeedList().indexOf(maxSpeedX));
        toStats += "<br>Max Vertical Speed: " + (double)maxSpeedY + " at distance: " + physics.getDistanceList().get(physics.getVerticalSpeedList().indexOf(maxSpeedY)) + " and height: " + physics.getHeightList().get(physics.getVerticalSpeedList().indexOf(maxSpeedY));
        toStats += "<br>Max Air Resistance: " + (double)maxResistance + " at distance: " + physics.getDistanceList().get(physics.getAirResistanceList().indexOf(maxResistance)) + " and height: " + physics.getHeightList().get(physics.getAirResistanceList().indexOf(maxResistance));
        toStats += "</html>";
        return toStats;
    }

    void graphDistanceHeight(Container container){
        container.removeAll();
        container.add(new SimpleGraph(physics.getDistanceList(), physics.getHeightList(), "distance", "height"));
        container.validate();
    }

    void graphTimeSpeed(Container container){
        container.removeAll();
        container.add(new SimpleGraph(physics.getTimeList(), physics.getSpeedList(), "time", "speed"));
        container.validate();
    }

    void graphHorizontalSpeedVerticalSpeed(Container container){
        container.removeAll();
        container.add(new SimpleGraph(physics.getHorizontalSpeedList(), physics.getVerticalSpeedList(), "hor. speed", "ver. speed"));
        container.validate();
    }

    void animationDistanceHeight(final Container container){


        Thread thread = new Thread(new Runnable() { public void run() {
            Container animContainer = new Container ();
            JLabel stats = new JLabel("");
            animContainer.setLayout(new BorderLayout());
            container.removeAll();
            container.add(animContainer, BorderLayout.CENTER);
            container.add(stats, BorderLayout.SOUTH);
            container.validate();

            for(int i=0; i<physics.getDistanceList().size(); i++){

                ArrayList<Double> tempListDistance = new ArrayList<>();
                ArrayList<Double> tempListHeight = new ArrayList<>();
                for(int j=0; j<=i; j++){
                    tempListDistance.add(physics.getDistanceList().get(j));
                    tempListHeight.add(physics.getHeightList().get(j));
                }

                if(i%25 == 0) {
                    double speed = physics.getSpeedList().get(i);
                    double yspeed = physics.getVerticalSpeedList().get(i);
                    double xspeed = physics.getHorizontalSpeedList().get(i);

                    String toStats = "<html>";
                    toStats += "Time: " + physics.getTimeList().get(i);
                    toStats += "<br>Speed: " + speed + "  ##  xSpeed: " + xspeed + "  ##  ySpeed: " + yspeed;
                    toStats += "</html>";
                    stats.setText(toStats);
                }

                animContainer.removeAll();
                animContainer.add(new SimpleGraph(tempListDistance, tempListHeight, "distance", "height"), BorderLayout.CENTER);
                animContainer.validate();

                try {
                    Thread.sleep((long) (physics.getTimeInterval()*1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            animContainer.removeAll();
            stats.setText(getStats());
            animContainer.add(new SimpleGraph(physics.getDistanceList(), physics.getHeightList(), "distance", "height"), BorderLayout.CENTER);




            animContainer.validate();
        }});

        thread.start();


    }


}
