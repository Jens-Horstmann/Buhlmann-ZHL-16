package BuhlmannZHL16;

/**
 * Created by Jens on 16.01.2016.
 */
public class Tissue {

    private double a;       //Buhlmann a value [bar]
    private double b;       //Buhlmann b value [bar]
    private double k;       //Buhlmann k value calculated from Half Time [1/s]

    private double load;    //Actual tissue load [bar]

    public Tissue(double a, double b, double k, double initialLoad){
        this.a = a;
        this.b = b;
        this.k = k;
        this.load=initialLoad;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getK() {
        return k;
    }



    public double getLoad() {
        return load;
    }

    public void setLoad(double load) {
        this.load = load;
    }

}
