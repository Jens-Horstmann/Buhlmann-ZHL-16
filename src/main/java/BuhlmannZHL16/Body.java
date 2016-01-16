package BuhlmannZHL16;

/**
 * Created by Jens on 16.01.2016.
 */
public class Body {

    public TissueCompartment [] N2Compartments;
    public TissueCompartment [] HeCompartments;

    public Body(double surfacePressure){
                                                    //a-value   //b-value   //k-value
        N2Compartments[0]  = new TissueCompartment( 1.1696,     0.5578,     0.00231049060186648);
        N2Compartments[1]  = new TissueCompartment( 1,          0.6514,     0.00144405662616655);
        N2Compartments[2]  = new TissueCompartment( 0.8618,     0.7222,     0.00092419624074659);
        N2Compartments[3]  = new TissueCompartment( 0.7562,     0.7825,     0.00062445691942338);
        N2Compartments[4]  = new TissueCompartment( 0.62,       0.8126,     0.00042786862997528);
        N2Compartments[5]  = new TissueCompartment( 0.5043,     0.8434,     0.00030163062687552);
        N2Compartments[6]  = new TissueCompartment( 0.441,      0.8693,     0.00021275235744627);
        N2Compartments[7]  = new TissueCompartment( 0.4,        0.891,      0.00015003185726406);
        N2Compartments[8]  = new TissueCompartment( 0.375,      0.9092,     0.00010598580742507);
        N2Compartments[9]  = new TissueCompartment( 0.35,       0.9222,     0.00007912639047488);
        N2Compartments[10] = new TissueCompartment( 0.3295,     0.9319,     0.00006177782357932);
        N2Compartments[11] = new TissueCompartment( 0.3065,     0.9403,     0.00004833662347001);
        N2Compartments[12] = new TissueCompartment( 0.2835,     0.9477,     0.00003787689511257);
        N2Compartments[13] = new TissueCompartment( 0.261,      0.9544,     0.00002962167438290);
        N2Compartments[14] = new TissueCompartment( 0.248,      0.9602,     0.00002319769680589);
        N2Compartments[15] = new TissueCompartment( 0.2327,     0.9653,     0.00001819283938478);



    }


}
