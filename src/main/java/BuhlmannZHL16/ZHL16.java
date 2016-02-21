package BuhlmannZHL16;

/**
 * Created by Jens on 16.01.2016.
 */

public class ZHL16 {

    private TissueModel tissueModel;
    private DiveDataPoint lastPoint;
    private DiveSettings diveSettings;

    private SafetyStop[] stops;


    public ZHL16(DiveSettings settings, long currentTimeStamp){
        this.diveSettings = new DiveSettings(settings.getSurfacePressure(), settings.getDepthPerBar(), settings.getGf(), settings.getGas(), settings.getMaxPP02());

        this.tissueModel = new TissueModel(getPalvN2(diveSettings.getSurfacePressure()));
        this.lastPoint = new DiveDataPoint(currentTimeStamp, settings.getSurfacePressure());
        stops = new SafetyStop[40];
        updateSafetyStops();
    }

    public void updateSettings(DiveSettings settings){
        this.diveSettings = settings;
        updateSafetyStops();
    }

    private void updateSafetyStops(){
        for (int i=0; i<40;i++){
            stops[i]= new SafetyStop(meterToBar(diveSettings.stops[i], diveSettings));
        }

    }

    public DiveSettings getDiveSettings(){
        return diveSettings;
    }

    private double getPalvN2(double depthInBar){
        return diveSettings.getGas().getN2Amount() * (depthInBar - diveSettings.getPw());
    }

    private double haldaneTime(double k, double pTissue, double pTissue0, double pAlv){  //How long does it take from pTissue0 to pTissue when breathing pAlv.
        double logArg = (pTissue - pAlv) / (pTissue0 - pAlv);
        if (logArg<0) callOut("LogArgument < 0 in HaldaneTime(). pTissue = " + pTissue + " pTissue0 = " + pTissue0 + " pAlv = " + pAlv);
        return -(1 / k) * Math.log((pTissue - pAlv) / (pTissue0 - pAlv));                         //...
    }

    private double haldaneTimePAmb(double pAmb, double gf, double a, double b, double k, double pTissue0, double pAlv){
        return haldaneTime(k, getPTol(pAmb, a, b, gf), pTissue0, pAlv);
    }

    private double getPAmbTol(double pTissue, double gf, double a, double b){
        return ((pTissue-gf*a)*b)/(gf-gf*b+b);
    }

    private double getPTol(double pAmb, double a, double b, double gf){
        return a*gf + (pAmb*(gf-gf*b+b))/b;
    }


    private double getNDL(double depthInBar){
        double ndl = 5940;
        for (int i=0; i<16; i++){
            if (getPTol(diveSettings.getSurfacePressure(), tissueModel.n2Compartments[i].getA(), tissueModel.n2Compartments[i].getB(), diveSettings.getGf().getHigh()) < getPalvN2(depthInBar)) {
                double ndli = haldaneTimePAmb(diveSettings.getSurfacePressure(), diveSettings.getGf().getHigh(), tissueModel.n2Compartments[i].getA(), tissueModel.n2Compartments[i].getB(), tissueModel.n2Compartments[i].getK(), tissueModel.n2Compartments[i].getLoad(), getPalvN2(depthInBar));
                if (ndli < ndl) {
                    ndl = ndli;
                }
            }
        }
        return ndl;
    }
/*
    private double getStandardStopTime(double stopDepth, double shallowerStopDepth){
        double stopTime = 0;
        for (int i=0; i<16; i++){
                double stopTimei = haldaneTimePAmb(shallowerStopDepth, diveSettings.getGf().getGF(shallowerStopDepth), tissueModel.n2Compartments[i].getA(), tissueModel.n2Compartments[i].getB(), tissueModel.n2Compartments[i].getK(), getPTol(stopDepth, tissueModel.n2Compartments[i].getA(),tissueModel.n2Compartments[i].getB(), diveSettings.getGf().getGF(stopDepth)), getPalvN2(stopDepth));
                if (stopTimei > stopTime) {
                    stopTime = stopTimei;
                }
        }
        callOut("StopDepth: " + stopDepth + ": " + stopTime/60 + "Minutes" + "; GF:" + diveSettings.getGf().getGF(stopDepth));
        return stopTime;
    }
*/

    private TissueModel calcSafetyStops(TissueModel tissues, int currentStop){
        double stopTime = 0;

        TissueModel instanceTissues = new TissueModel(0);

        stops[currentStop].setActive(true);

        if(currentStop>0) {

            //calc time at current stop until next stop
            for (int i = 0; i < 16; i++) {
                instanceTissues.n2Compartments[i].setLoad(tissues.n2Compartments[i].getLoad());
                if (instanceTissues.n2Compartments[i].getLoad() > getPalvN2(stops[currentStop].getStopDepth())) {
                    callOut("Tissue: " + i);
                    double stopTimei = haldaneTimePAmb(stops[currentStop - 1].getStopDepth(), diveSettings.getGf().getGF(stops[currentStop-1].getStopDepth()), instanceTissues.n2Compartments[i].getA(), instanceTissues.n2Compartments[i].getB(), instanceTissues.n2Compartments[i].getK(), instanceTissues.n2Compartments[i].getLoad(), getPalvN2(stops[currentStop].getStopDepth()));
double x = diveSettings.getGf().getGF(stops[currentStop-1].getStopDepth());
                    if (stopTimei > stopTime) {
                        stopTime = stopTimei;
                    }
                }
            }
            stops[currentStop].setStopTime(stopTime);
            callOut("Stop: " + currentStop + " for " + stopTime/60 + "Minutes");

            //calc offgassing during that time and set tissue loads
            setTissueLoadsSchreiner(instanceTissues, getPalvN2(stops[currentStop].getStopDepth()), 0, stopTime);
//            setTissueLoadsSchreiner(instanceTissues, getPalvN2(stops[currentStop].getStopDepth()), diveSettings.getMaxAscentRate(), (stops[currentStop].getStopDepth()-stops[currentStop-1].getStopDepth())/diveSettings.getMaxAscentRate());

            //if currentStop > surfacePressure recursion for next stop
            calcSafetyStops(instanceTissues, currentStop-1);
        }

        return instanceTissues;
    }

    private void setTissueLoadsSchreiner(TissueModel tissues, double pAlv, double R, double time){
        double k;
        double pTissue0;
        for (int i=0; i<16; i++){
            k = tissues.n2Compartments[i].getK();
            pTissue0 = tissues.n2Compartments[i].getLoad();
            tissues.n2Compartments[i].setLoad(pAlv+R*(time-(1/k))-(pAlv-pTissue0-(R/k)) * Math.exp(-k*time));
        }
    }

    private int getDeepestStop(){
        double deepStop = 0;

        //calc theoretical deepest Stop
        for ( int i=0; i<16; i++){
            double depthi = getPAmbTol(tissueModel.n2Compartments[i].getLoad(), diveSettings.getGf().getLow(), tissueModel.n2Compartments[i].getA(), tissueModel.n2Compartments[i].getB());
            if (depthi > deepStop){
                deepStop = depthi;
            }
        }
        //find deepest stop depth

        int n=0;
        while (deepStop > meterToBar(diveSettings.stops[n], diveSettings)){
            n++;
        }

//        callOut("Next Stop: " + meterToBar(diveSettings.stops[n], diveSettings));

        return n;
    }

    private void callOut(String string){
        System.out.println(string);
    }


    public DiveDataPoint dive(double pressure, long currentTimeStamp){

        int leadingStopDepth = 0;
        int leadingNdl = 0;
        int stopN = 0;
        double ndl = 5940;

        double R = (getPalvN2(pressure) - getPalvN2(lastPoint.getDepthInBar())) / (currentTimeStamp-lastPoint.getTime());
            // set tissue loads
            setTissueLoadsSchreiner(tissueModel, getPalvN2(lastPoint.getDepthInBar()), R, currentTimeStamp-lastPoint.getTime());

            //calc NDL
            ndl = getNDL(pressure);

            //calc first stop depth

            stopN = getDeepestStop();
            if (stopN>0) {
                diveSettings.getGf().setLowDepth(stops[stopN].getStopDepth());

                //calc stop times
                calcSafetyStops(tissueModel, stopN);
            }
        lastPoint = new DiveDataPoint(currentTimeStamp, pressure, ndl, stopN, leadingStopDepth, leadingNdl);

        System.out.println("DiveTime: " + currentTimeStamp + "s, Depth: " + pressure + "bar, NDL: " + ndl + "s, StopDepth: " + stopN + ". Stop, Leading NDL Tissue: " + leadingNdl + ", Leading Stop Depth Tissue: " + leadingStopDepth);

        return lastPoint;

    }


    public double barToMeter(double depthInBar, DiveSettings settings){
        return (depthInBar-settings.getSurfacePressure())*settings.getDepthPerBar();
    }

    public double meterToBar(double depthInMeter, DiveSettings settings){
        return settings.getSurfacePressure() + depthInMeter/settings.getDepthPerBar();
    }

}
