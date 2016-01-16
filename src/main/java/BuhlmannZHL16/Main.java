package BuhlmannZHL16;

/**
 * Created by Jens on 16.01.2016.
 */
public class Main {


    public static void main(String[] args) {
        ZHL16 zhl16 = new ZHL16();

        zhl16.dive(4.6, 10);
        zhl16.dive(4.6, 240);
        zhl16.dive(1, 10);

        for(int i=0; i<16; i++){
            System.out.println(zhl16.getTissueLoad(i));
        }


    }


}
