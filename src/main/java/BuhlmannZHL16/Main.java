package BuhlmannZHL16;

/**
 * Created by Jens on 16.01.2016.
 */


public class Main {


    public static void main(String[] args) {

        DiveSettings settings = new DiveSettings();
//        Dive currentDive = new Dive(settings);

        ZHL16 zhl16 = new ZHL16(settings, 0);

        zhl16.dive(4.6, 10);
        zhl16.dive(4.6, 250);
        zhl16.dive(4.6, 455);
        zhl16.dive(1, 465);

    }


}

