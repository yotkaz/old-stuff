package me.yotkaz.computersimulations.graph;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.HashMap;

/**
 * Created by yotkaz on 2014-11-14.
 */
public abstract class Graph extends JPanel {

    public abstract void drawGraph(Graphics g);

    private boolean squaredGrid = false;

    private double  margin = 10;
    private double  unitSeparatorSize = 2;

    private String  xAxisName = "",
                    yAxisName = "";

    private double  xAxisStart = 0, xAxisEnd = 0,
                    yAxisStart = 0, yAxisEnd = 0;

    private double  xAxisStartOrg = 0, xAxisEndOrg = 0, yAxisStartOrg = 0, yAxisEndOrg = 0;

    private double  xAxisDifference, yAxisDifference,
                    xAxisMinIntervalPixels = 40, yAxisMinIntervalPixels = 20,
                    xAxisUnitInterval = 1, yAxisUnitInterval = 1,
                    xUnitSizeInPixels = 1, yUnitSizeInPixels = 1,
                    xAxisCoordinate = 1, yAxisCoordinate = 1;

    HashMap <Double, Double> xIntervalsPixelsMap = new HashMap <>(),
                            yIntervalsPixelsMap = new HashMap <>();

    private Color backgroundColor = Color.BLACK, foregroundColor = Color.WHITE, gridColor = Color.decode("333366"), drawColor = Color.RED, textColor = Color.decode("#33CCCC");


    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }
    public void setDrawColor(Color drawColor) {
        this.drawColor = drawColor;
    }
    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
    }


    public void setSquaredGrid(boolean squaredGrid) {
        this.squaredGrid = squaredGrid;
    }
    public void setMargin(double addMargin) {
        this.margin = addMargin;
    }
    public void setUnitSeparatorSize(double unitSeparatorSize) {
        this.unitSeparatorSize = unitSeparatorSize;
    }


    public void setXAxisUnitInterval(double xAxisUnitInterval) {
        this.xAxisUnitInterval = xAxisUnitInterval;
    }
    public void setYAxisUnitInterval(double yAxisUnitInterval) {
        this.yAxisUnitInterval = yAxisUnitInterval;
    }
    public void setXAxisMinIntervalPixels(double xAxisMinIntervalPixels) {
        this.xAxisMinIntervalPixels = xAxisMinIntervalPixels;
    }

    public void setYAxisMinIntervalPixels(double yAxisMinIntervalPixels) {
        this.yAxisMinIntervalPixels = yAxisMinIntervalPixels;
    }

    public void setXAxisName(String xAxisName) {
        this.xAxisName = xAxisName;
    }
    public void setYAxisName(String yAxisName) {
        this.yAxisName = yAxisName;
    }

    public double getxAxisDifference() {
        return xAxisDifference;
    }
    public double getyAxisDifference() {
        return yAxisDifference;
    }
    public double getxAxisStart() {
        return xAxisStart;
    }
    public double getxAxisEnd() {
        return xAxisEnd;
    }
    public double getyAxisStart() {
        return yAxisStart;
    }
    public double getyAxisEnd() {
        return yAxisEnd;
    }



    private void calculateAxesDifferences(){
        xAxisDifference = xAxisEnd - xAxisStart;
        yAxisDifference = yAxisEnd - yAxisStart;
    }

    private void calculateUnitSizeToPixels(){
        xUnitSizeInPixels = this.getWidth() / xAxisDifference;
        yUnitSizeInPixels = this.getHeight() / yAxisDifference;
        if(squaredGrid){
            if(xUnitSizeInPixels<yUnitSizeInPixels) {
                yUnitSizeInPixels = xUnitSizeInPixels;
                yAxisStart = yAxisStartOrg;
                yAxisStart -= ((this.getHeight() - yUnitSizeInPixels * yAxisDifference) * 0.5) / yUnitSizeInPixels ;
            }
            else {
                xUnitSizeInPixels = yUnitSizeInPixels;
                xAxisStart = xAxisStartOrg;
                xAxisStart -= ((this.getWidth() - xUnitSizeInPixels * xAxisDifference) * 0.5) / xUnitSizeInPixels ;
            }
        }
    }

    private void calculateAxesCoordinates(){
        if(xAxisEnd==0) yAxisCoordinate = getWidth();
        else yAxisCoordinate = 0 - (xAxisStart * xUnitSizeInPixels);

        if(yAxisEnd==0) xAxisCoordinate = 0;
        else xAxisCoordinate = getHeight() + (yAxisStart * yUnitSizeInPixels);
    }

    private void calculateIntervalScale(){
        calculateUnitSizeToPixels();
        while(xUnitSizeInPixels * xAxisUnitInterval >= xAxisMinIntervalPixels){
            xAxisUnitInterval /= 2;
            calculateUnitSizeToPixels();
        }
        while(yUnitSizeInPixels * yAxisUnitInterval >= yAxisMinIntervalPixels){
            yAxisUnitInterval /= 2;
            calculateUnitSizeToPixels();
        }
        while(xUnitSizeInPixels * xAxisUnitInterval < xAxisMinIntervalPixels){
            xAxisUnitInterval *= 2;
            calculateUnitSizeToPixels();
        }
        while(yUnitSizeInPixels * yAxisUnitInterval < yAxisMinIntervalPixels){
            yAxisUnitInterval *= 2;
            calculateUnitSizeToPixels();
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void calculateUnitIntervalsToPixels(){
        calculateAxesCoordinates();
        calculateIntervalScale();

        xIntervalsPixelsMap.clear();
        yIntervalsPixelsMap.clear();

        double currentUnitPosition = yAxisCoordinate;
        double unitCounter = 0;
        while(currentUnitPosition <= getWidth() - margin){
            if(unitCounter != 0) xIntervalsPixelsMap.put(unitCounter, currentUnitPosition);
            currentUnitPosition += xAxisUnitInterval * xUnitSizeInPixels;
            unitCounter += xAxisUnitInterval;
        }

        currentUnitPosition = yAxisCoordinate;
        unitCounter = 0;
        while(currentUnitPosition >= margin){
            if(unitCounter != 0) xIntervalsPixelsMap.put(unitCounter, currentUnitPosition);
            currentUnitPosition -= xAxisUnitInterval * xUnitSizeInPixels;
            unitCounter -= xAxisUnitInterval;
        }

        currentUnitPosition = xAxisCoordinate;
        unitCounter = 0;
        while(currentUnitPosition <= getHeight() - margin){
            if(unitCounter != 0) yIntervalsPixelsMap.put(unitCounter, currentUnitPosition);
            currentUnitPosition += yAxisUnitInterval * yUnitSizeInPixels;
            unitCounter -= yAxisUnitInterval;
        }

        currentUnitPosition = xAxisCoordinate;
        unitCounter = 0;
        while(currentUnitPosition >= margin){
            if(unitCounter != 0) yIntervalsPixelsMap.put(unitCounter, currentUnitPosition);
            currentUnitPosition -= yAxisUnitInterval * yUnitSizeInPixels;
            unitCounter += yAxisUnitInterval;
        }
    }

    public double xUnitsToPixels(double unitsValue){
        return unitsValue* xUnitSizeInPixels + yAxisCoordinate;
    }

    public double yUnitsToPixels(double unitsValue){
        return xAxisCoordinate - (unitsValue * yUnitSizeInPixels);
    }


    public void setAxes(double xAxisStart, double xAxisEnd, double yAxisStart, double yAxisEnd){
        if(xAxisStart<xAxisEnd){
            this.xAxisStart=xAxisStart;
            this.xAxisEnd=xAxisEnd;
        }
        else{
            this.xAxisStart=xAxisEnd;
            this.xAxisEnd=xAxisStart;
        }
        if(yAxisStart<yAxisEnd){
            this.yAxisStart=yAxisStart;
            this.yAxisEnd=yAxisEnd;
        }
        else{
            this.yAxisStart=yAxisEnd;
            this.yAxisEnd=yAxisStart;
        }

        if(this.xAxisStart<0 && this.xAxisEnd<0) this.xAxisEnd=0;
        if(this.xAxisStart>0 && this.xAxisEnd>0) this.xAxisStart=0;
        if(this.yAxisStart<0 && this.yAxisEnd<0) this.yAxisEnd=0;
        if(this.yAxisStart>0 && this.yAxisEnd>0) this.yAxisStart=0;

        xAxisStartOrg = xAxisStart;
        xAxisEndOrg = xAxisEnd;
        yAxisStartOrg = yAxisStart;
        yAxisEndOrg = yAxisEnd;

        calculateAxesDifferences();
    }

    public void drawGrid(Graphics g){
        Graphics2D graph2D = (Graphics2D) g;

        for(double key : xIntervalsPixelsMap.keySet()) {
            graph2D.draw(new Line2D.Double(xIntervalsPixelsMap.get(key),0, xIntervalsPixelsMap.get(key), getHeight()));
        }

        for(double key : yIntervalsPixelsMap.keySet()) {
            graph2D.draw(new Line2D.Double(0, yIntervalsPixelsMap.get(key), getWidth(), yIntervalsPixelsMap.get(key)));
        }
    }


    public void drawAxes(Graphics g){
        Graphics2D graph2D = (Graphics2D) g;
        graph2D.draw(new Line2D.Double(0, xAxisCoordinate, this.getWidth(), xAxisCoordinate)); // axis X
        graph2D.draw(new Line2D.Double(yAxisCoordinate, 0, yAxisCoordinate, this.getHeight())); // axis Y
    }

    private void drawAxesNames(Graphics g){
        Graphics2D graph2D = (Graphics2D) g;

        AffineTransform fontAT = new AffineTransform();
        Font theFont = graph2D.getFont();
        fontAT.rotate(90*(Math.PI/180));
        Font theDerivedFont = theFont.deriveFont(fontAT);
        graph2D.setFont(theDerivedFont);
        graph2D.drawString(yAxisName, (float)(yAxisCoordinate + unitSeparatorSize/2 + 5), 5);
        graph2D.setFont(theFont);

        graph2D.drawString(xAxisName, 5, (float)(xAxisCoordinate - unitSeparatorSize/2 - 5));
    }

    private void drawUnitsStrings(Graphics g){
        Graphics2D graph2D = (Graphics2D) g;

        for(double key : xIntervalsPixelsMap.keySet()) {
            int stringWidth = (int) graph2D.getFontMetrics().getStringBounds(key + "", graph2D).getWidth();
            int stringHeight = (int) graph2D.getFontMetrics().getStringBounds(key + "", graph2D).getHeight();
            graph2D.drawString(key + "", (float)(xIntervalsPixelsMap.get(key)-stringWidth/2), (float)(xAxisCoordinate+unitSeparatorSize/2+stringHeight-2));
        }

        for(double key: yIntervalsPixelsMap.keySet()) {
            int stringWidth = (int) graph2D.getFontMetrics().getStringBounds(key + "", graph2D).getWidth();
            int stringHeight = (int) graph2D.getFontMetrics().getStringBounds(key + "", graph2D).getHeight();
            graph2D.drawString(key + "",(float)(yAxisCoordinate-stringWidth-unitSeparatorSize/2-2),(float)(yIntervalsPixelsMap.get(key)+stringHeight/3));
        }
    }

    public void drawUnits(Graphics g){
        Graphics2D graph2D = (Graphics2D) g;

        for(double key : xIntervalsPixelsMap.keySet()) {
            graph2D.draw(new Line2D.Double(xIntervalsPixelsMap.get(key), xAxisCoordinate - (1.0 / 2.0) * unitSeparatorSize, xIntervalsPixelsMap.get(key), xAxisCoordinate + (1.0 / 2.0) * unitSeparatorSize));
        }

        for(double key : yIntervalsPixelsMap.keySet()) {
            graph2D.draw(new Line2D.Double(yAxisCoordinate - (1.0 / 2.0) * unitSeparatorSize, yIntervalsPixelsMap.get(key), yAxisCoordinate + (1.0 / 2.0) * unitSeparatorSize, yIntervalsPixelsMap.get(key)));
        }
    }


    private void drawBackground(Graphics g){
        Graphics2D graph2D = (Graphics2D) g;
        graph2D.setColor(backgroundColor);
        graph2D.fillRect(0, 0, getWidth(), getHeight());
        graph2D.setColor(foregroundColor);
    }


    @Override
    protected void paintComponent(Graphics g) {
        calculateUnitSizeToPixels();
        calculateAxesCoordinates();
        calculateUnitIntervalsToPixels();


        super.paintComponent(g);

        drawBackground(g);

        g.setColor(gridColor);
        drawGrid(g);

        g.setColor(foregroundColor);
        drawAxes(g);
        drawUnits(g);

        g.setColor(textColor);
        drawAxesNames(g);
        drawUnitsStrings(g);

        g.setColor(drawColor);
        drawGraph(g);

        g.setColor(foregroundColor);
    }

    public Graph(double addMargin, double xAxisStart, double xAxisEnd, double yAxisStart, double yAxisEnd, double xUnitInterval, double yUnitInterval, double xAxisMinIntervalPixels, double yAxisMinIntervalPixels, double unitSeparatorSize, String xAxisName, String yAxisName, Color backgroundColor, Color foregroundColor, Color gridColor, Color textColor, Color drawColor){
        setAxes(xAxisStart - xUnitInterval, xAxisEnd + xUnitInterval, yAxisStart - yUnitInterval, yAxisEnd + yUnitInterval);

        setXAxisName(xAxisName);
        setYAxisName(yAxisName);

        setXAxisUnitInterval(xUnitInterval);
        setYAxisUnitInterval(yUnitInterval);
        setXAxisMinIntervalPixels(xAxisMinIntervalPixels);
        setYAxisMinIntervalPixels(yAxisMinIntervalPixels);

        setMargin(addMargin);
        setUnitSeparatorSize(unitSeparatorSize);

        setBackgroundColor(backgroundColor);
        setForegroundColor(foregroundColor);
        setGridColor(gridColor);
        setDrawColor(drawColor);
        setTextColor(textColor);
    }

    public Graph(double xAxisStart, double xAxisEnd, double yAxisStart, double yAxisEnd, String xAxisName, String yAxisName){
        setAxes(xAxisStart - xAxisUnitInterval, xAxisEnd + xAxisUnitInterval, yAxisStart - yAxisUnitInterval, yAxisEnd + yAxisUnitInterval);

        setXAxisName(xAxisName);
        setYAxisName(yAxisName);
    }
}