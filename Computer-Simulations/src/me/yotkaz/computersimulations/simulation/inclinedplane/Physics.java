package me.yotkaz.computersimulations.simulation.inclinedplane;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by yotkaz on 2015-01-20.
 */
public class Physics {

    public Physics(SolidState type, double timeInterval, double radius1, double radius2, double length, double mass, double gravity, double angle, double height, double staticFrictionCoefficient, double rollingResistanceCoefficient, double airResistanceCoefficient, double airDensity) {
        this.type = type;
        this.timeInterval = timeInterval;
        this.radius1 = radius1;
        this.radius2 = radius2;
        this.length = length;
        this.mass = mass;
        this.gravity = gravity;
        this.angle = angle;
        this.height = height;
        this.staticFrictionCoefficient = staticFrictionCoefficient;
        this.rollingResistanceCoefficient = rollingResistanceCoefficient;
        this.airResistanceCoefficient = airResistanceCoefficient;
        this.airDensity = airDensity;

        momentOfInertia = getMomentOfInertia(type);
        crossSectionalArea = getCrossSectionalArea(type);

        currentRollingResistance = (mass * gravity * Math.cos(Math.toRadians(angle)) * rollingResistanceCoefficient) / radius2;
        currentAngle = Math.toRadians(90 - angle);
        contactX = 0.0;
        contactY = height;
        centerX = 0.0 + radius2 * Math.cos(Math.toRadians(90 - angle));
        centerY = height + radius2 * Math.sin(Math.toRadians(90 - angle));

        if (currentRollingResistance > mass * gravity * Math.sin(Math.toRadians(angle))){
            JOptionPane.showMessageDialog(null, "Rolling resistance is too large, object will stay in the same place.\nRolling resistance = " + currentRollingResistance + "\nPulling force = " + mass * gravity * Math.sin(Math.toRadians(angle)));
        }
        else {
            simulate();
        }
    }

    // time list
    ArrayList<Double> time = new ArrayList<>();

    // x,y lists
    ArrayList<Double> xCenterOfMass = new ArrayList<>();
    ArrayList<Double> yCenterOfMass = new ArrayList<>();
    ArrayList<Double> xPointOfContact = new ArrayList<>();
    ArrayList<Double> yPointOfContact = new ArrayList<>();

    // forces, accelerations and velocities lists
    ArrayList<Double> centerVelocity = new ArrayList<>();
    ArrayList<Double> contactAngularVelocity = new ArrayList<>();
    ArrayList<Double> centerAcceleration = new ArrayList<>();
    ArrayList<Double> contactAngularAcceleration = new ArrayList<>();
    ArrayList<Double> slidingFriction = new ArrayList<>();
    ArrayList<Double> rollingResistance = new ArrayList<>();
    ArrayList<Double> airResistance = new ArrayList<>();

    // other lists
    ArrayList<Boolean> slidingHistory = new ArrayList<>();
    ArrayList<Double> angleList = new ArrayList<>();

    // input
    public enum SolidState {
        CYLINDER, CYLINDRICAL_SHELL, CYLINDRICAL_TUBE, BALL, HOLLOW_SPHERE
    }

    SolidState type;
    double timeInterval = 0.01;
    double radius1 = 0;
    double radius2 = 0;
    double length = 0;
    double mass;
    double gravity;
    double angle;
    double height;
    double staticFrictionCoefficient;
    double rollingResistanceCoefficient;
    double airResistanceCoefficient;
    double airDensity;

    // other
    double currentTime = 0.0;
    double momentOfInertia;
    double crossSectionalArea;
    double currentCenterVelocity = 0;
    double currentCenterAcceleration = 0;
    double currentAngularVelocity = 0;
    double currentAngularAcceleration = 0;
    double centerX, centerY;
    double contactX, contactY;
    double currentSlidingFriction;
    double currentAirResistance = 0;
    double currentRollingResistance;
    double currentAngle;
    boolean sliding;


    double getMomentOfInertia(SolidState type) {
        switch (type) {
            case CYLINDER:
                return 0.5 * mass * Math.pow(radius2, 2);
            case CYLINDRICAL_SHELL:
                return mass * Math.pow(radius2, 2);
            case CYLINDRICAL_TUBE:
                return 0.5 * mass * (Math.pow(radius1, 2) + Math.pow(radius2, 2));
            case BALL:
                return 0.4 * mass * (Math.pow(radius2, 2));
            case HOLLOW_SPHERE:
                return (2.0 / 3.0) * mass * (Math.pow(radius2, 2));
        }
        return 0;
    }

    double getCrossSectionalArea(SolidState type) {
        if (type == SolidState.CYLINDRICAL_TUBE || type == SolidState.CYLINDRICAL_SHELL || type == SolidState.CYLINDER) {
            return 2 * radius2 * length;
        }
        if (type == SolidState.BALL || type == SolidState.HOLLOW_SPHERE) {
            return Math.PI * Math.pow(radius2, 2);
        }
        return 0;
    }

    private void step(){
        currentCenterVelocity += (timeInterval * currentCenterAcceleration);
        currentAngularVelocity += (timeInterval * currentAngularAcceleration);

        currentAngle -= currentAngularVelocity * timeInterval;
        centerX += currentCenterVelocity * timeInterval * Math.cos(Math.toRadians(angle));
        centerY -= currentCenterVelocity * timeInterval * Math.sin(Math.toRadians(angle));
        contactX = centerX - radius2 * Math.cos(currentAngle);
        contactY = centerY - radius2 * Math.sin(currentAngle);

        currentAirResistance = 0.5 * airResistanceCoefficient * airDensity * crossSectionalArea * Math.pow(currentCenterVelocity, 2);

        double a1 = (mass * gravity * Math.sin(Math.toRadians(angle)) - currentRollingResistance - currentAirResistance) / (mass + (momentOfInertia / Math.pow(radius2, 2)));
        double a2 = (mass * gravity * Math.sin(Math.toRadians(angle)) - staticFrictionCoefficient * mass * gravity * Math.cos(Math.toRadians(angle)) - currentAirResistance) / mass;

        if(a1 >= a2) {
            sliding = false;
            currentCenterAcceleration = a1;
            currentAngularAcceleration = a1 / radius2;
            currentSlidingFriction = momentOfInertia * (a1 / Math.pow(radius2, 2));
        }
        else {
            sliding = true;
            currentCenterAcceleration = a2;
            currentAngularAcceleration = 0.0;
            currentSlidingFriction = staticFrictionCoefficient * mass * gravity * Math.cos(Math.toRadians(angle));
        }

        time.add(currentTime);
        xPointOfContact.add(contactX);
        yPointOfContact.add(contactY);
        xCenterOfMass.add(centerX);
        yCenterOfMass.add(centerY);
        centerVelocity.add(currentCenterVelocity);
        contactAngularVelocity.add(currentAngularVelocity);
        airResistance.add(currentAirResistance);
        rollingResistance.add(currentRollingResistance);
        centerAcceleration.add(currentCenterAcceleration);
        contactAngularAcceleration.add(currentAngularAcceleration);
        slidingFriction.add(currentSlidingFriction);
        slidingHistory.add(sliding);
        angleList.add(currentAngle);

        currentTime += timeInterval;
    }

    public void simulate(){
        for (int i = 0; centerY - radius2 * Math.sin(Math.toRadians(90 - angle)) >= 0; i++){
            step();
        }
    }
}