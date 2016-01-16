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

    private double lastPressure;

    public ZHL16(){
        this(0.21);
    }

    public ZHL16(double oxygenAmount){
        this(oxygenAmount, 0);
    }

    public ZHL16(double oxygenAmount, double heAmount){
        this(oxygenAmount, heAmount, 1.4, 0.3, 0.85);
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
        tissueModel = new TissueModel(getPalvN2(surfacePressure));
        lastPressure = this.surfacePressure;
    }

    private double getPalvN2(double depthInBar){
        return gas.getN2Amount() * (depthInBar - Pw);
    }

    public void dive(double pressure, int deltaSeconds){
        double R = (getPalvN2(pressure) - getPalvN2(lastPressure))/deltaSeconds;
        for (int i=0; i<16; i++){
            double load = getPalvN2(lastPressure) + R * (deltaSeconds - 1/tissueModel.n2Compartments[i].getK()) - (getPalvN2(lastPressure) - tissueModel.n2Compartments[i].getLoad() - R/tissueModel.n2Compartments[i].getK()) * Math.exp(-tissueModel.n2Compartments[i].getK()*deltaSeconds);
            tissueModel.n2Compartments[i].setLoad(load);
        }
        lastPressure = pressure;
    }

    private double getToleratedTissueLoad(int tissue, double depthInBar){
        return depthInBar/(1/((gf.getGF(depthInBar)/tissueModel.n2Compartments[tissue].getB()) - gf.getGF(depthInBar) + 1)) + tissueModel.n2Compartments[tissue].getA()* gf.getGF(depthInBar);
    }


    public int getNdl(){
        return 0;
    }

    public double getTissueLoad(int tissueNumber){
        return tissueModel.n2Compartments[tissueNumber].getLoad();
    }

}
