package me.yotkaz.computersimulations.simulation.mathpendulum;

import java.util.ArrayList;

/**
 * Created by yotkaz on 2014-11-11.
 */
public class Physics {

    private ArrayList<Double> angleList = new ArrayList<>();
    private ArrayList<Double> xList = new ArrayList<>();
    private ArrayList<Double> yList = new ArrayList<>();
    private ArrayList<Double> linearSpeedList = new ArrayList<>();
    private ArrayList<Double> angularSpeedList = new ArrayList<>();
    private ArrayList<Double> linearAccelerationList = new ArrayList<>();
    private ArrayList<Double> angularAccelerationList = new ArrayList<>();
    private ArrayList<Double> airResistanceList = new ArrayList<>();
    private ArrayList<Double> timeList = new ArrayList<>();
    private ArrayList<Double> periodList = new ArrayList<>();

    public ArrayList<Double> getTimeList() {
        return timeList;
    }

    public ArrayList<Double> getAngleList() {
        return angleList;
    }

    public ArrayList<Double> getPeriodList() {
        return periodList;
    }

    public ArrayList<Double> getxList() {
        return xList;
    }

    public ArrayList<Double> getyList() {
        return yList;
    }

    public ArrayList<Double> getLinearSpeedList() {
        return linearSpeedList;
    }

    public ArrayList<Double> getAngularSpeedList() {
        return angularSpeedList;
    }

    public ArrayList<Double> getLinearAccelerationList() {
        return linearAccelerationList;
    }

    public ArrayList<Double> getAngularAccelerationList() {
        return angularAccelerationList;
    }

    public ArrayList<Double> getAirResistanceList() {
        return airResistanceList;
    }

    private double timeInterval;
    private double dragCoefficient;
    private double airDensity;
    private double objectArea;
    private double objectMass;
    private double gravity;
    private double time = 0;
    private double angle;
    private double lineLength;
    private double linearSpeed = 0;
    private double angularSpeed;
    private double airResistance;
    private double linearAcceleration;

    private double periodStart = 0;
    private double periodEnd = 0;

    public double getLineLength(){
        return lineLength;
    }

    public double getTimeInterval(){
        return timeInterval;
    }



    public Physics(double timeInterval, double angle, double lineLength, double linearSpeed, double dragCoefficient, double airDensity, double objectArea, double objectMass, double gravity, double angleLimit, double linearSpeedLimit, double stepLimit){
        this.timeInterval = timeInterval;
        this.dragCoefficient = dragCoefficient;
        this.airDensity = airDensity;
        this.objectArea = objectArea;
        this.objectMass = objectMass;
        this.gravity = gravity;
        this.angle = angle;
        this.linearSpeed = linearSpeed;
        this.lineLength = lineLength;

        while (this.angle > 180 || this.angle <-180){
            while (this.angle > 180) {
                this.angle = - (360 - this.angle);
            }

            while (this.angle < -180) {
                this.angle = (360 + this.angle);
            }
        }

        simulate(linearSpeedLimit, angleLimit, stepLimit);
    }

    private void calculateStep(){

        // time
        time += timeInterval;
        timeList.add(time);

        // speeds
        if (!(linearSpeedList.isEmpty())){
            linearSpeed += (linearAcceleration * timeInterval);
        }
        linearSpeedList.add(linearSpeed);

        angularSpeed = linearSpeed / lineLength;
        angularSpeedList.add(angularSpeed);

        // angle, x and y
        if (angleList.size() >= 1){
            angle -= Math.toDegrees(angularSpeed * timeInterval);
            if((angleList.get(angleList.size()-1)>0 && angle<0) || (angleList.get(angleList.size()-1)<0 && angle>0)){
                if(periodStart!=0){
                    periodEnd = time;
                    periodList.add(periodEnd - periodStart);
                    periodStart = time;
                }
                else{
                    periodStart = time;
                    periodList.add(0.0);
                }
            }
            else{
                if(periodStart!=0){
                    periodList.add(periodList.get(periodList.size()-1));
                }
                else{
                    periodList.add(0.0);
                }
            }
        }
        else{
            periodList.add(0.0);
        }
        angleList.add(angle);

        xList.add(StrictMath.sin(Math.toRadians(angle)) * lineLength);
        yList.add(lineLength - (StrictMath.cos(Math.toRadians(angle)) * lineLength));

        // air resistance and accelerations
        airResistance = 0.5 * airDensity * StrictMath.pow(linearSpeed, 2) * objectArea * dragCoefficient;
        airResistanceList.add(airResistance);

        if (linearSpeed>=0) {
            linearAcceleration = gravity * StrictMath.sin(Math.toRadians(angle)) - (airResistance / objectMass);
        }
        else
        {
            linearAcceleration = gravity * StrictMath.sin(Math.toRadians(angle)) + (airResistance / objectMass);
        }
        linearAccelerationList.add(linearAcceleration);

        if (angularSpeedList.size() <= 1){
            angularAccelerationList.add(angularSpeed / timeInterval);
        }
        else {
            angularAccelerationList.add((angularSpeed - angularSpeedList.get(angularSpeedList.size() - 2))/timeInterval); // element before the last element
        }
    }


    public void simulate(double linearSpeedLimit, double angleLimit, double stepLimit){
        int counter = 0;
        int emergencyCounter = 0;
        while ((Math.abs(linearSpeed)>=linearSpeedLimit || Math.abs(angle)>=angleLimit) && (counter <= stepLimit && emergencyCounter <= 100000)){
                calculateStep();
                if(stepLimit!=0) counter ++;
                emergencyCounter ++;
        }

    }
}