package me.yotkaz.computersimulations.graph;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by yotkaz on 2014-11-25.
 */
public class SimpleGraph extends Graph{

    private ArrayList<Double> listAxisX = new ArrayList<>();
    private ArrayList<Double> listAxisY = new ArrayList<>();

    public void setListAxisX(ArrayList<Double> listAxisX) {
        this.listAxisX = listAxisX;
    }
    public void setListAxisY(ArrayList<Double> listAxisY) {
        this.listAxisY = listAxisY;
    }

    public SimpleGraph(ArrayList<Double> listAxisX, ArrayList<Double> listAxisY, double xAxisStart, double xAxisEnd, double yAxisStart, double yAxisEnd, String xAxisName, String yAxisName) {
        super(xAxisStart, xAxisEnd, yAxisStart, yAxisEnd, xAxisName, yAxisName);
        if(listAxisX.size() == listAxisY.size()){
            setListAxisX(listAxisX);
            setListAxisY(listAxisY);
        }
    }

    public SimpleGraph(ArrayList<Double> listAxisX, ArrayList<Double> listAxisY, String xAxisName, String yAxisName){
        this(listAxisX, listAxisY, Collections.min(listAxisX), Collections.max(listAxisX), Collections.min(listAxisY), Collections.max(listAxisY), xAxisName, yAxisName);
        setAxes(getxAxisStart() - getxAxisDifference() * 0.1, getxAxisEnd() + getxAxisDifference() * 0.1, getyAxisStart() - getyAxisDifference() * 0.05, getyAxisEnd() + getyAxisDifference() * 0.05);
    }

    @Override
    public void drawGraph(Graphics g) {
        Graphics2D graph2D = (Graphics2D) g;
        for(int i=0; i<listAxisX.size(); i++){
            graph2D.draw(new Line2D.Double(xUnitsToPixels(listAxisX.get(i)), yUnitsToPixels(listAxisY.get(i)), xUnitsToPixels(listAxisX.get(i)), yUnitsToPixels(listAxisY.get(i))));
        }
    }
}
