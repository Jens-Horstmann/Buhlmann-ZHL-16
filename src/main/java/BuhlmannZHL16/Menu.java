package BuhlmannZHL16;

/**
 * Created by Jens on 12.11.2016.
 */
public class Menu {

    public MenuItem item[] = new MenuItem[10];
    private int numItems = 0;

    public Menu(){
        addItem("O2 amount: ", 21, 50, 1, "%", true);
        addItem("Max PPO2: ", 12, 18, 10, "Bar", true);
        addItem("GF low: " , 0, 49, 1, "%", true);
        addItem("GF high: ", 50, 100, 1, "%", true);
        addItem("Exit",1,1,1,"", false);
    }

    private void addItem(String name, int minValue, int maxValue, int divider, String unit, boolean showValue){
        item[numItems] = new MenuItem(name, minValue, maxValue, divider, unit, showValue);
        numItems++;
    }

    public int getNumItems(){
        return  numItems;
    }


}
