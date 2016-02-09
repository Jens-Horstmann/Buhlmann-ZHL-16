package BuhlmannZHL16;

/**
 * Created by Jens on 16.01.2016.
 */

public class ZHL16 {

    private TissueModel tissueModel;
//    private double surfacePressure;
//    private double depthPerBar;

//    private Gas gas;
//    private GradientFactors gf = new GradientFactors();
//    private static final double Pw = 0.0627;
//    private static final double flightPressure = 0.58;
//    private double maxOxygenPartialPressure;

//    private double currentDepthInBar;
//    private int leadingTissue;

    public ZHL16(DiveSettings settings){
        this.tissueModel = new TissueModel(getPalvN2(settings.getSurfacePressure(), settings));
    }

    private double getPalvN2(double depthInBar, DiveSettings settings){
        return settings.getGas().getN2Amount() * (depthInBar - settings.getPw());
    }

    public DiveDataPoint dive(double pressure, int diveTime, DiveSettings settings, DiveDataPoint lastpoint){

        int leadingStopDepth = 0;
        int leadingNdl = 0;
        double stopDepth = 0;
        double ndl = 5940;

        double R = (getPalvN2(pressure, settings) - getPalvN2(lastpoint.getDepthInBar(), settings)) / (diveTime-lastpoint.getTime());
        for (int i=0; i<16; i++){
            // set tissue loads
            double load = getPalvN2(lastpoint.getDepthInBar(), settings) + R * ((diveTime-lastpoint.getTime()) - 1/tissueModel.n2Compartments[i].getK()) - (getPalvN2(lastpoint.getDepthInBar(), settings) - tissueModel.n2Compartments[i].getLoad() - R/tissueModel.n2Compartments[i].getK()) * Math.exp(-tissueModel.n2Compartments[i].getK()*(diveTime-lastpoint.getTime()));
            tissueModel.n2Compartments[i].setLoad(load);

            // calc ndl
            if (calcToleratedTissueLoad(i, settings.getSurfacePressure(), settings.getGf().getHigh()) < getPalvN2(pressure, settings)) {
                double ndli = Math.log((calcToleratedTissueLoad(i, settings.getSurfacePressure(), settings.getGf().getHigh()) - getPalvN2(pressure, settings)) / (tissueModel.n2Compartments[i].getLoad() - getPalvN2(pressure, settings))) / -tissueModel.n2Compartments[i].getK();
                if (ndli < ndl) {
                    ndl = ndli;
                }
            }

            // calc first stop depth
            double stopDepthi = ((tissueModel.n2Compartments[i].getLoad() - settings.getGf().getLow() * tissueModel.n2Compartments[i].getA()) * tissueModel.n2Compartments[i].getB()) / (settings.getGf().getLow()-settings.getGf().getLow()*tissueModel.n2Compartments[i].getB()+tissueModel.n2Compartments[i].getB());
            if ( stopDepthi > stopDepth){
                stopDepth = stopDepthi;
                leadingStopDepth=i;
            }

        }
        System.out.println("DiveTime: " + diveTime + "s, Depth: " + pressure + "bar, NDL: " + ndl + "s, StopDepth: " + stopDepth + "bar, Leading NDL Tissue: " + leadingNdl + ", Leading Stop Depth Tissue: " + leadingStopDepth);
        return new DiveDataPoint(diveTime, pressure, ndl, stopDepth, leadingStopDepth, leadingNdl);

    }


    public double calcToleratedTissueLoad(int tissue, double depthInBar, double gf){
        return (depthInBar*(gf-gf*tissueModel.n2Compartments[tissue].getB()+tissueModel.n2Compartments[tissue].getB()))/tissueModel.n2Compartments[tissue].getB() + gf*tissueModel.n2Compartments[tissue].getA();
    }

    /*
    public double calcAscentCeiling(double depthInBar){
        double limit=0;
        for (int i=0; i<16; i++){
            double limiti = (tissueModel.n2Compartments[i].getLoad() - gf.gfCorrectA(depthInBar,tissueModel.n2Compartments[i].getA())) * gf.gfCorrectB(depthInBar,tissueModel.n2Compartments[i].getB());
            if ( limiti > limit){
                limit = limiti;
                leadingTissue=i;
            }
        }
        return limit;
    }
*/
/*
    public double calcNdl(double depthInBar){
        double ndl = 5940;
        for (int i=0; i<16; i++){
            if (calcToleratedTissueLoad(i,surfacePressure)<getPalvN2(depthInBar)){
                double ndli = Math.log((calcToleratedTissueLoad(i,surfacePressure)-getPalvN2(depthInBar))/(tissueModel.n2Compartments[i].getLoad()-getPalvN2(depthInBar)))/-tissueModel.n2Compartments[i].getK();
                if ( ndli < ndl){
                    ndl= ndli;
                }
            }
        }
        return ndl;
    }
*/
    public double getTissueLoad(int tissueNumber){
        return tissueModel.n2Compartments[tissueNumber].getLoad();
    }

    public double generateDecoStops(){
        double tts = 0;          //Time To Surface



        return tts;
    }

    public double getActualGf(double depthInBar){
        return 0;
    }

    public double barToMeter(double depthInBar, DiveSettings settings){
        return (depthInBar-settings.getSurfacePressure())*settings.getDepthPerBar();
    }

}
