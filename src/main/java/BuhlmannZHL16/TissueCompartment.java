package BuhlmannZHL16;

/**
 * Created by Jens on 16.01.2016.
 */
public class TissueCompartment {

    private double a;       //Buhlmann a value
    private double b;       //Buhlmann b value
    private double k;       //Buhlmann k value calculated from Half Time

    private double load;    //Actual tissue load


    public TissueCompartment(double a, double b, double k, double initialLoad){
        this.a = a;
        this.b = b;
        this.k = k;
        this.load = initialLoad;
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
