package BuhlmannZHL16;

/**
 * Created by Jens on 16.01.2016.
 */

public class Gas {
    private double oxygenAmount; //in Percent 0.21 = 21%
    private double heAmount;
    private double n2Amount;


    public Gas(){               //normal Air mix
        this(0.21);
    }

    public Gas(double oxygenAmount){    //nitrox mixes
        this(oxygenAmount,0);
    }

    public Gas(double oxygenAmount, double heAmount){       //tec mixes
        this.oxygenAmount = oxygenAmount;
        this.heAmount = heAmount;
        this.n2Amount = 1-this.oxygenAmount-this.heAmount;
    }

    public double getN2Amount() {
        return n2Amount;
    }
    public double getheAmount() {
        return heAmount;
    }

    public double getOxygenAmount() {
        return oxygenAmount;
    }

}
