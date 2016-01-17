package BuhlmannZHL16;

/**
 * Created by Jens on 16.01.2016.
 */

public class ZHL16 {

    private TissueModel tissueModel;
    private double surfacePressure;
    private double depthPerBar;

    private Gas gas;
    private GradientFactors gf = new GradientFactors();
    private static final double Pw = 0.0627;
    private static final double flightPressure = 0.58;
    private double maxOxygenPartialPressure;

    private double currentDepthInBar;
    private int leadingTissue;



    public ZHL16(DiveSettings settings, Dive dive){

    }


    public ZHL16(){
        this(0.21);
    }

    public ZHL16(double oxygenAmount){
        this(oxygenAmount, 0);
    }

    public ZHL16(double oxygenAmount, double heAmount){
        this(oxygenAmount, heAmount, 1.4, 0.3, 0.8);
    }

    public ZHL16(double oxygenAmount, double heAmount, double maxOxygenPartialPressure, double gfLow, double gfHigh){
        this(oxygenAmount, heAmount, 1, 10, maxOxygenPartialPressure, gfLow, gfHigh);
    }

    public ZHL16(double oxygenAmount, double heAmount, double surfacePressure, double depthPerBar, double maxOxygenPartialPressure, double gfLow, double gfHigh){
        this.gas = new Gas(oxygenAmount,heAmount);
        this.surfacePressure = surfacePressure;
        this.depthPerBar = depthPerBar;
        this.maxOxygenPartialPressure = maxOxygenPartialPressure;
        this.gf.setLow(gfLow);
        this.gf.setHigh(gfHigh);
        this.gf.setHighDepth(this.surfacePressure);
        this.gf.setLowDepth(4 + this.surfacePressure);
        this.tissueModel = new TissueModel(getPalvN2(surfacePressure));
        this.currentDepthInBar = this.surfacePressure;
    }

    private double getPalvN2(double depthInBar){
        return gas.getN2Amount() * (depthInBar - Pw);
    }

    public void dive(double pressure, int deltaSeconds){
        double R = (getPalvN2(pressure) - getPalvN2(currentDepthInBar))/deltaSeconds;
        for (int i=0; i<16; i++){
            double load = getPalvN2(currentDepthInBar) + R * (deltaSeconds - 1/tissueModel.n2Compartments[i].getK()) - (getPalvN2(currentDepthInBar) - tissueModel.n2Compartments[i].getLoad() - R/tissueModel.n2Compartments[i].getK()) * Math.exp(-tissueModel.n2Compartments[i].getK()*deltaSeconds);
            tissueModel.n2Compartments[i].setLoad(load);
        }
        currentDepthInBar = pressure;

    }

    public double calcToleratedTissueLoad(int tissue, double depthInBar){
        return depthInBar/gf.gfCorrectB(depthInBar, tissueModel.n2Compartments[tissue].getB()) + gf.gfCorrectA(depthInBar, tissueModel.n2Compartments[tissue].getA());
    }

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

    public double getTissueLoad(int tissueNumber){
        return tissueModel.n2Compartments[tissueNumber].getLoad();
    }

    public double generateDecoStops(){
        double tts = 0;          //Time To Surface



        return tts;
    }

    public double getActualGf(double depthInBar){
        return ()
    }

    public double barToMeter(double depthInBar){
        return (depthInBar-surfacePressure)*depthPerBar;
    }

}
