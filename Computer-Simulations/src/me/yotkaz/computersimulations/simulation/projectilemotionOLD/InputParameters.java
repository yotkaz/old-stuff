package me.yotkaz.computersimulations.simulation.projectilemotionOLD;

/**
 * Created by yotkaz on 2014-11-25.
 */
public class InputParameters {



    private UserInterface ui;

    public void setUi(UserInterface ui) {
        this.ui = ui;
    }
    public UserInterface getUi() {
        return ui;
    }

    InputParameters(UserInterface ui){
        setUi(ui);
    }

    Double[] tryParseDouble(String string){
        Double[] arrayToReturn = new Double[2];
        arrayToReturn[0] = 0.0;
        try {
            arrayToReturn[1] = Double.parseDouble(string);
        } catch (NumberFormatException e) {
            arrayToReturn[0] = 1.0; // ERROR CODE
        }
        return arrayToReturn;
    }

    Physics getPhysics() {

        Double[] timeInterval = tryParseDouble(ui.timeIntervalField.getText());
        if(timeInterval[0]!=0 || timeInterval[1]<=0) {
            System.err.println("timeInterval Error");
            return null;
        }

        Double[] angle = tryParseDouble(ui.angleField.getText());
        if(angle[0]!=0 || angle[1]<0 || angle[1]>90){
            System.err.println("angle Error");
            return null;
        }

        Double[] objectMass = tryParseDouble(ui.objectMassField.getText());
        if(objectMass[0]!=0 || objectMass[1]<=0){
            System.err.println("objectMass Error");
            return null;
        }

        Double[] objectArea = tryParseDouble(ui.objectAreaField.getText());
        if(objectArea[0]!=0 || objectArea[1]<=0){
            System.err.println("objectArea Error");
            return null;
        }

        Double[] initialSpeed = tryParseDouble(ui.initialSpeedField.getText());
        if(initialSpeed[0]!=0 || initialSpeed[1]<0){
            System.err.println("initialSpeed Error");
            return null;
        }

        Double[] initialHeight = tryParseDouble(ui.initialHeightField.getText());
        if(initialHeight[0]!=0){
            System.err.println("initialHeight Error");
            return null;
        }

        Double[] gravity = tryParseDouble(ui.gravityField.getText());
        if(gravity[0]!=0 || gravity[1]<=0){
            System.err.println("gravity Error");
            return null;
        }

        Double[] airDensity = tryParseDouble(ui.airDensityField.getText());
        if(airDensity[0]!=0 || airDensity[1]<0){
            System.err.println("airDensity Error");
            return null;
        }

        Double[] dragCoefficient = tryParseDouble(ui.dragCoefficientField.getText());
        if(dragCoefficient[0]!=0 || dragCoefficient[1]<0){
            System.err.println("dragCoefficient Error");
            return null;
        }

        return new Physics(timeInterval[1], gravity[1], angle[1], objectMass[1], objectArea[1], initialSpeed[1], initialHeight[1], airDensity[1], dragCoefficient[1]);
    }
}
