package BuhlmannZHL16;

/**
 * Created by Jens on 17.01.2016.
 */

public class DiveSettings {

    private double surfacePressure;
    private double depthPerBar;
    private GradientFactors gf = new GradientFactors();
    private Gas gas = new Gas();
    private double maxPP02;

    public DiveSettings(){
        this(1.0, 10, new GradientFactors(), new Gas(), 1.4);
    }

    public DiveSettings(double surfacePressure, double depthPerBar, GradientFactors gf, Gas gas, double maxPP02) {
        this.surfacePressure = surfacePressure;
        this.depthPerBar = depthPerBar;
        this.gf = gf;
        this.gas = gas;
        this.maxPP02 = maxPP02;
    }

    public void setSurfacePressure(double surfacePressure) {
        this.surfacePressure = surfacePressure;
    }

    public void setDepthPerBar(double depthPerBar) {
        this.depthPerBar = depthPerBar;
    }

    public void setGf(GradientFactors gf) {
        this.gf = gf;
    }

    public void setGas(Gas gas) {
        this.gas = gas;
    }

    public void setMaxPP02(double maxPP02) {
        this.maxPP02 = maxPP02;
    }

    public double getDepthPerBar() {
        return depthPerBar;
    }

    public GradientFactors getGf() {
        return gf;
    }

    public Gas getGas() {
        return gas;
    }

    public double getMaxPP02() {
        return maxPP02;
    }

    public double getSurfacePressure() {
        return surfacePressure;
    }
}
