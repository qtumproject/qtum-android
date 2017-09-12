package org.qtum.wallet.ui.fragment.qstore;

import org.qtum.wallet.R;

import java.util.Random;


public class TestTokenObject {

    private static String[] names = {"My Super Token", "Smart Contract", "Crowdsale", "Awesome Name","My Token"};
    private static String[] types = {"Standart Token", "Smart Contract", "Crowdsale"};
    private static float[] costs = {1000000, 0.2f, 230, 0.01f, 400, 500, 10.3f, 45.5f};

    public int icon;
    public String name;
    public String type;
    public float cost;

    public TestTokenObject(){
        generateCost();
        generateName();
        generateType();
        getIconByType();
    }

    private void generateType(){
        type = types[getRandom(0,types.length)];
    }

    private void generateName(){
        name = names[getRandom(0,names.length)];
    }

    private void generateCost(){
        cost = costs[getRandom(0,costs.length)];
    }

    private void getIconByType(){
        switch (type){
            case "Standart Token":
                icon = R.drawable.ic_supertoken;
                break;
            case "Smart Contract":
                icon = R.drawable.ic_smart_contract;
                break;
            case "Crowdsale":
                icon = R.drawable.ic_crowdsale;
                name = "Crowdsale";
                default:
                    break;
        }
    }

    private int getRandom(int min, int max){
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }

}
