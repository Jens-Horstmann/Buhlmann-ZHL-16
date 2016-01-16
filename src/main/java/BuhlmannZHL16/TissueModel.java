package BuhlmannZHL16;

/**
 * Created by Jens on 16.01.2016.
 */
public class TissueModel {

    public Tissue[] n2Compartments = new Tissue[16];

    public TissueModel(double n2InitialLoad){
                                        //a-value[bar]  //b-value[bar]  //k-value [1/s]
        n2Compartments[0]  = new Tissue( 1.1696,         0.5578,         0.00231049060186648, n2InitialLoad);
        n2Compartments[1]  = new Tissue( 1,              0.6514,         0.00144405662616655, n2InitialLoad);
        n2Compartments[2]  = new Tissue( 0.8618,         0.7222,         0.00092419624074659, n2InitialLoad);
        n2Compartments[3]  = new Tissue( 0.7562,         0.7825,         0.00062445691942338, n2InitialLoad);
        n2Compartments[4]  = new Tissue( 0.62,           0.8126,         0.00042786862997528, n2InitialLoad);
        n2Compartments[5]  = new Tissue( 0.5043,         0.8434,         0.00030163062687552, n2InitialLoad);
        n2Compartments[6]  = new Tissue( 0.441,          0.8693,         0.00021275235744627, n2InitialLoad);
        n2Compartments[7]  = new Tissue( 0.4,            0.891,          0.00015003185726406, n2InitialLoad);
        n2Compartments[8]  = new Tissue( 0.375,          0.9092,         0.00010598580742507, n2InitialLoad);
        n2Compartments[9]  = new Tissue( 0.35,           0.9222,         0.00007912639047488, n2InitialLoad);
        n2Compartments[10] = new Tissue( 0.3295,         0.9319,         0.00006177782357932, n2InitialLoad);
        n2Compartments[11] = new Tissue( 0.3065,         0.9403,         0.00004833662347001, n2InitialLoad);
        n2Compartments[12] = new Tissue( 0.2835,         0.9477,         0.00003787689511257, n2InitialLoad);
        n2Compartments[13] = new Tissue( 0.261,          0.9544,         0.00002962167438290, n2InitialLoad);
        n2Compartments[14] = new Tissue( 0.248,          0.9602,         0.00002319769680589, n2InitialLoad);
        n2Compartments[15] = new Tissue( 0.2327,         0.9653,         0.00001819283938478, n2InitialLoad);



    }


}
