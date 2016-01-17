package BuhlmannZHL16;

import java.util.LinkedList;

/**
 * Created by Jens on 17.01.2016.
 */
public class Dive {

    private int startTime;
    private double maxDepth;

    private LinkedList profile = new LinkedList();

    private DiveDataPoint last;

    public Dive(){
        this.maxDepth = 0;
    }

    public double getDepthInBar(){
        return last.getDepthInBar();
    }

    public void setPoint(DiveDataPoint point){
        
    }

}
