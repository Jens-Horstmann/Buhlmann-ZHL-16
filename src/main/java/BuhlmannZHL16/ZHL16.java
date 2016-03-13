package BuhlmannZHL16;

/**
 * Created by Jens on 16.01.2016.
 */

public class ZHL16 {

    private TissueModel tissueModel;
    private DiveDataPoint lastPoint;
    private DiveSettings diveSettings;
    private double maxDepth;

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

/*    private double haldaneTimePAmb(double pAmb, double gf, double a, double b, double k, double pTissue0, double pAlv){
        double pTissue = getPTol(pAmb, a, b, gf);

        double logArg = (pTissue - pAlv) / (pTissue0 - pAlv);
        if (logArg<0) callOut("LogArgument < 0 in HaldaneTimePAmb(). pTissue = " + pTissue + " pTissue0 = " + pTissue0 + " pAlv = " + pAlv);

        return haldaneTime(k, pTissue, pTissue0, pAlv);
    }
*/

    private double getPAmbTol(double pTissue, double gf, double a, double b){
        return ((pTissue-gf*a)*b)/(gf-gf*b+b);
    }

    private double getPTol(double pAmb, double a, double b, double gf){
        return a*gf + ((pAmb*(gf-gf*b+b))/b);
    }


    private double getNDL(double depthInBar){
        double ndl = 5940;
        for (int i=0; i<16; i++){
            double pTol = getPTol(diveSettings.getSurfacePressure(), tissueModel.n2Compartments[i].getA(), tissueModel.n2Compartments[i].getB(), diveSettings.getGf().getHigh());
            if (tissueModel.n2Compartments[i].getLoad() < pTol) { //only if compartment is under pTol otherwise NDL is -1 and other compartments are irrelevant
                if (pTol < getPalvN2(depthInBar)) {     //if pTol can be reached
                    double ndli = haldaneTime(tissueModel.n2Compartments[i].getK(), pTol, tissueModel.n2Compartments[i].getLoad(), getPalvN2(depthInBar));
                    if (ndli < ndl) {
                        ndl = ndli;
                    }
                }
            }else {
                ndl = -1;
                break;
            }
        }
        return ndl;
    }

    private TissueModel calcSafetyStops(TissueModel tissues, int currentStop){
        double stopTime = 0;

        TissueModel instanceTissues = new TissueModel(0);


        if(currentStop>0) {

            //calc time at current stop until next stop
            for (int i = 0; i < 16; i++) {
                instanceTissues.n2Compartments[i].setLoad(tissues.n2Compartments[i].getLoad());
                double pTol = getPTol(stops[currentStop-1].getStopDepth(), instanceTissues.n2Compartments[i].getA(),instanceTissues.n2Compartments[i].getB(),diveSettings.getGf().getGF(stops[currentStop-1].getStopDepth()));
                if (instanceTissues.n2Compartments[i].getLoad() > pTol) {       //stop time will not be calculated when tissue is under pTol (stopTime stays 0)
                    if (pTol > getPalvN2(stops[currentStop].getStopDepth())) {      //stopTime will only be calculated if pTol can be reached otherwise need to proceed to higher stop (stopTime stays 0)
                        if (instanceTissues.n2Compartments[i].getLoad() > getPalvN2(stops[currentStop].getStopDepth())) { //stopTime is only calculated if off gassing otherwise need to proceed to higher stop (stopTime stays 0)
                            double stopTimei = haldaneTime(instanceTissues.n2Compartments[i].getK(), pTol, instanceTissues.n2Compartments[i].getLoad(), getPalvN2(stops[currentStop].getStopDepth()));
                             if (stopTimei > stopTime) {
                                stopTime = stopTimei;
                            }
                        }
                    }
                }
            }

            if(stopTime > 0 ) {
                stops[currentStop].setActive(true);
                stops[currentStop].setStopTime(stopTime);
            }else{
                stops[currentStop].setActive(false);
            }


//                callOut("Stop: " + barToMeter(stops[currentStop].getStopDepth(), diveSettings) + "m for " + stopTime/60 + " Minutes");

                //calc offgassing during that time and set tissue loads
                setTissueLoadsSchreiner(instanceTissues, getPalvN2(stops[currentStop].getStopDepth()), 0, stopTime);

//                printActualGF(instanceTissues, stops[currentStop-1].getStopDepth());


            //if currentStop > surfacePressure recursion for next stop
            calcSafetyStops(instanceTissues, currentStop-1);
        }
        return instanceTissues;
    }

    private double getActualGF(double depthInBar){
        double actualGF=0;

        for ( int i=0; i<16;i++){
            double pTol = getPTol(depthInBar, tissueModel.n2Compartments[i].getA(),tissueModel.n2Compartments[i].getB(),1);
            double actualGFi = (tissueModel.n2Compartments[i].getLoad()-depthInBar)/(pTol-depthInBar);
            if (actualGFi>actualGF){
                actualGF=actualGFi;
            }
        }
        return actualGF;
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

        if (deepStop > diveSettings.getGf().getLowDepth()){
            diveSettings.getGf().setLowDepth(deepStop);
//            callOut("Low Depth: " + diveSettings.getGf().getLowDepth());
//            callOut("Tissue1: " + tissueModel.n2Compartments[0].getLoad());
        }

        //find deepest stop depth

        int n=0;
        do {
           n++;
        } while (diveSettings.getGf().getLowDepth() > meterToBar(diveSettings.stops[n], diveSettings));


//        diveSettings.getGf().setLowDepth(stops[n].getStopDepth());
//        callOut("Next Stop: " + meterToBar(diveSettings.stops[n], diveSettings));

        return n;
    }

    private void callOut(String string){
        System.out.println(string);
    }


    public DiveDataPoint dive(double pressure, double deltaT){

        int stopN = 0;
        double ndl = 5940;

        double R = (getPalvN2(pressure) - getPalvN2(lastPoint.getDepthInBar())) / (deltaT);
        // set tissue loads
        setTissueLoadsSchreiner(tissueModel, getPalvN2(lastPoint.getDepthInBar()), R, deltaT);

        //calc NDL
        ndl = getNDL(pressure);

        //calc first stop depth

         stopN = getDeepestStop();
            //calc stop times
         calcSafetyStops(tissueModel, stopN);

        double diveTime = lastPoint.getTime() + deltaT;

        int nextStop = getNextStop();

        if (pressure>maxDepth){
            maxDepth = pressure;
        }
        lastPoint = new DiveDataPoint(diveTime, pressure, ndl, stops[nextStop].getStopDepth(), stops[nextStop].getStopTime(), calcTts(nextStop), 0, getActualGF(lastPoint.getDepthInBar()), maxDepth, 0);

//        System.out.print("DiveTime: " + lastPoint.getTime() + "s, Depth: " + pressure + "bar, NDL: " + ndl + "min, Next Stop: " + stops[nextStop].getStopTime() + "' @" + barToMeter(stops[nextStop].getStopDepth(),diveSettings) + "m, TTS:" + calcTts(nextStop)/60);
//        printActualGF(tissueModel,pressure);
        return lastPoint;

    }

    private int getNextStop(){
        int nextStop = 0;
        for (int i=0; i<40;i++){
            if (stops[i].isActive()) nextStop = i;
        }

        return nextStop;
    }

    private double calcTts(int stopN){
        double tts=0;
        for(int i=1; i<=stopN;i++){
            tts += stops[i].getStopTime();
        }
        return tts;
    }

    public double getMaxAllowedDepth(){
        return diveSettings.getMaxPP02()/diveSettings.getGas().getOxygenAmount();
    }

    public double barToMeter(double depthInBar, DiveSettings settings){
        return (depthInBar-settings.getSurfacePressure())*settings.getDepthPerBar();
    }

    public double meterToBar(double depthInMeter, DiveSettings settings){
        return settings.getSurfacePressure() + depthInMeter/settings.getDepthPerBar();
    }

}
