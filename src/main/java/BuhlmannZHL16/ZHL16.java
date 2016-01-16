package BuhlmannZHL16;

/**
 * Created by Jens on 16.01.2016.
 */
public class ZHL16 {

    private TissueModel tissueModel;
    private double surfacePressure;
    private double depthPerBar;

    private Gas gas;
    private GradientFactors gf;
    private static final double Pw = 0.0627;
    private static final double flightPressure = 0.58;
    private double maxOxygenPartialPressure;

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
        this.gf.low = gfLow;
        this.gf.high = gfHigh;
    }

}
