package me.yotkaz.computersimulations.graph;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by yotkaz on 2015-01-20.
 */
public class AdvancedGraph extends Graph {

    public AdvancedGraph(double xAxisStart, double xAxisEnd, double yAxisStart, double yAxisEnd, String xAxisName, String yAxisName) {
        super(xAxisStart, xAxisEnd, yAxisStart, yAxisEnd, xAxisName, yAxisName);
    }

    public AdvancedGraph(boolean squaredGrid, ArrayList<Double> listX, ArrayList<Double> listY, ArrayList<Color> colorList, String xAxisName, String yAxisName){
        this(Collections.min(listX), Collections.max(listX), Collections.min(listY), Collections.max(listY), xAxisName, yAxisName);
        setListX(listX);
        setListY(listY);
        setColorList(colorList);
        if(squaredGrid) setSquaredGrid(true);
        setAxes(getxAxisStart() - getxAxisDifference() * 0.1, getxAxisEnd() + getxAxisDifference() * 0.1, getyAxisStart() - getyAxisDifference() * 0.05, getyAxisEnd() + getyAxisDifference() * 0.05);

    }

    private ArrayList<Double> listX = new ArrayList<>();
    private ArrayList<Double> listY = new ArrayList<>();
    private ArrayList<Color> colorList = new ArrayList<>();

    public void setListX(ArrayList<Double> listX) {
        this.listX = listX;
    }
    public void setListY(ArrayList<Double> listY) {
        this.listY = listY;
    }
    public void setColorList(ArrayList<Color> colorList) { this.colorList = colorList; }



    @Override
    public void drawGraph(Graphics g) {
        Graphics2D graph2D = (Graphics2D) g;
        Color tempColor = g.getColor();
        for(int i=0; i<listX.size(); i++){
            g.setColor(colorList.get(i));
            graph2D.draw(new Line2D.Double(xUnitsToPixels(listX.get(i)), yUnitsToPixels(listY.get(i)), xUnitsToPixels(listX.get(i)), yUnitsToPixels(listY.get(i))));
        }
        g.setColor(tempColor);
    }
}
