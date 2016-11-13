package BuhlmannZHL16;

/**
 * Created by Jens on 12.11.2016.
 */
public class MenuItem {

    private String name;
    private int minValue;
    private int maxValue;
    private int value;
    private double divider;
    private String unit;
    private String[] valueName = new String[10];
    private boolean showValue;

    public MenuItem(String name, int minValue, int maxValue, double divider, String unit, boolean showValue){
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.divider = divider;
        this.unit = unit;
        this.showValue = showValue;
        for(int i=0; i<10; i++){
            valueName[i] = "";
        }
    }

    public void nameValue(int value, String name){
        if (value<10) valueName[value] = name;
    }

    public String getValueString(){
        if(valueName[0].equals("")){
            if(divider != 1) return (value/divider) + unit;
            else return value + unit;
        }else {
            return valueName[value];
        }
    }

    public boolean isShowValue(){
        return showValue;
    }

    public double getValue(){
        return value/divider;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void incrValue(int adder){
        if(value + adder <= maxValue) value = value + adder;
    }

    public void incrValue(){
        if (value < maxValue) value++;
    }

    public void decrValue(int sub){
        if(value - sub >= minValue) value = value - sub;
    }

    public void decrValue(){
        if(value > minValue) value--;
    }

    public String getName() {
        return name;
    }
}
