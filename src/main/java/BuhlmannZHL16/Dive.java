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

    public Dive(DiveSettings settings){
        this.maxDepth = 0;
        this.settings = settings;
        profile.addFirst(new DiveDataPoint(0, settings.getSurfacePressure()));
    }

    public void setPoint(DiveDataPoint point){
        DiveDataPoint lastLogged = (DiveDataPoint) profile.getFirst();
        if ((point.getDepthInBar()-lastLogged.getDepthInBar())>(settings.getLogThreshold()/2) || (point.getDepthInBar()-lastLogged.getDepthInBar())<(-settings.getLogThreshold()/2)){
            profile.addFirst(point);
        }
    }
}
