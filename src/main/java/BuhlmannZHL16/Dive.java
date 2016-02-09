package BuhlmannZHL16;

import java.util.LinkedList;

/**
 * Created by Jens on 17.01.2016.
 */
public class Dive {

    private int startTime;
    private double maxDepth;

    private LinkedList profile = new LinkedList();  //Divelog
    public DiveSettings settings;

    public DiveDataPoint last;

    public Dive(DiveSettings settings){
        this.maxDepth = 0;

        this.settings = settings;
        last = new DiveDataPoint(0, settings.getSurfacePressure());
        profile.addFirst(last);
    }

    public void setPoint(DiveDataPoint point){

        DiveDataPoint lastLogged = (DiveDataPoint) profile.getFirst();

        if ((point.getDepthInBar()-lastLogged.getDepthInBar())>(settings.getLogThreshold()/2) || (point.getDepthInBar()-lastLogged.getDepthInBar())<(-settings.getLogThreshold()/2)){
            profile.addFirst(point);
        }

        this.last = point;
    }

}
