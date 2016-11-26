package me.yotkaz.computersimulations.simulation.projectilemotionOLD;

import java.util.ArrayList;

/**
 * Created by yotkaz on 2014-11-11.
 */
public class Physics {

    private ArrayList<Double> heightList = new ArrayList<>();
    private ArrayList<Double> distanceList = new ArrayList<>();
    private ArrayList<Double> speedList = new ArrayList<>();
    private ArrayList<Double> verticalSpeedList = new ArrayList<>();
    private ArrayList<Double> horizontalSpeedList = new ArrayList<>();
    private ArrayList<Double> airResistanceList = new ArrayList<>();
    private ArrayList<Double> timeList = new ArrayList<>();

    private double gravity;
    private double airDensity;
    private double dragCoefficient;
    private double angle;
    private double objectMass;
    private double objectArea;
    private double currentSpeed;
    private double currentVerticalSpeed;
    private double currentHorizontalSpeed;
    private double initialHeight;
    private double time = 0;
    private double timeInterval = 0.01;
    int            positionsCounter = 1;


    public ArrayList<Double> getHeightList() {
        return heightList;
    }
    public ArrayList<Double> getDistanceList() {
        return distanceList;
    }
    public ArrayList<Double> getSpeedList() {
        return speedList;
    }
    public ArrayList<Double> getVerticalSpeedList() {
        return verticalSpeedList;
    }
    public ArrayList<Double> getHorizontalSpeedList() {
        return horizontalSpeedList;
    }
    public ArrayList<Double> getAirResistanceList() {
        return airResistanceList;
    }
    public ArrayList<Double> getTimeList() {
        return timeList;
    }



    public void setGravity(double gravity) {
        this.gravity = gravity;
    }
    public void setAirDensity(double airDensity) {
        this.airDensity = airDensity;
    }
    public void setDragCoefficient(double dragCoefficient) {
        this.dragCoefficient = dragCoefficient;
    }
    public void setAngle(double angle) {
        this.angle = angle;
    }
    public void setObjectMass(double objectMass) {
        this.objectMass = objectMass;
    }
    public void setObjectArea(double objectArea) {
        this.objectArea = objectArea;
    }
    public void setCurrentSpeed(double currentSpeed) {
        this.currentSpeed = currentSpeed;
    }
    public void setCurrentVerticalSpeed(double currentVerticalSpeed) {
        this.currentVerticalSpeed = currentVerticalSpeed;
    }
    public void setCurrentHorizontalSpeed(double currentHorizontalSpeed) {
        this.currentHorizontalSpeed = currentHorizontalSpeed;
    }
    public void setInitialHeight(double initialHeight) {
        this.initialHeight = initialHeight;
    }
    public void setTimeInterval(double timeInterval) {
        this.timeInterval = timeInterval;
    }

    public double getTimeInterval() {
        return timeInterval;
    }


    public Physics(double timeInterval,double gravity, double angle, double objectMass, double objectArea, double initialSpeed, double initialHeight, double airDensity, double  dragCoefficient){
        setGravity(gravity);
        setAirDensity(airDensity);
        setDragCoefficient(dragCoefficient);
        setTimeInterval(timeInterval);
        setAngle(StrictMath.toRadians(angle));
        setObjectMass(objectMass);
        setObjectArea(objectArea);
        setInitialHeight(initialHeight);
        setCurrentHorizontalSpeed(StrictMath.cos(this.angle) * initialSpeed);
        setCurrentVerticalSpeed(StrictMath.sin(this.angle) * initialSpeed);
        setCurrentSpeed(initialSpeed);

        heightList.add(this.initialHeight);
        distanceList.add(0.0);
        timeList.add(0.0);
        double airResistance = (1 / 2.0) * airDensity * StrictMath.pow(currentSpeed, 2) * objectArea * dragCoefficient;
        airResistanceList.add(airResistance);
        verticalSpeedList.add(currentVerticalSpeed);
        horizontalSpeedList.add(currentHorizontalSpeed);
        speedList.add(currentSpeed);
    }

    private void calculateCurrentSpeeds(){
        double airResistance = (1 / 2.0) * airDensity * StrictMath.pow(currentSpeed, 2) * objectArea * dragCoefficient;
        angle = Math.atan2(currentVerticalSpeed, currentHorizontalSpeed);
        currentHorizontalSpeed -= ((airResistance * StrictMath.cos(angle)) / objectMass) * timeInterval;
        currentVerticalSpeed -= (gravity + ((airResistance * StrictMath.sin(angle)) / objectMass)) * timeInterval;
        currentSpeed = StrictMath.hypot(currentHorizontalSpeed, currentVerticalSpeed);

        airResistanceList.add(airResistance);
        verticalSpeedList.add(currentVerticalSpeed);
        horizontalSpeedList.add(currentHorizontalSpeed);
        speedList.add(currentSpeed);
    }

    private void calculateCurrentPositions(){
        time += timeInterval;
        heightList.add(heightList.get(positionsCounter - 1) + (currentVerticalSpeed * timeInterval));
        distanceList.add(distanceList.get(positionsCounter - 1) + (currentHorizontalSpeed * timeInterval));
        timeList.add(time);
        calculateCurrentSpeeds();
    }

    public void simulate(){
        while(heightList.get(positionsCounter - 1) >= 0){
            calculateCurrentPositions();
            positionsCounter++;
        }
    }
}