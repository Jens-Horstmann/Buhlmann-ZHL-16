package BuhlmannZHL16;

/**
 * Created by Jens on 03.02.2016.
 */
public class SafetyStops {

    private SafetyStop [] stops = new SafetyStop[40];

    public SafetyStops(){
        for (int i=0; i<40; i++){
            stops[i].setStopDepth(5+3*i);
        }
    }

    public void calculateAscentRoute(){

    }

}
