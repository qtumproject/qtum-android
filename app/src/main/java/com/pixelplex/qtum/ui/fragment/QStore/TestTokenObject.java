package com.pixelplex.qtum.ui.fragment.QStore;

import com.pixelplex.qtum.R;

import java.util.Random;

/**
 * Created by kirillvolkov on 29.06.17.
 */

public class TestTokenObject {

    public static String[] names = {"My Super Token", "Smart Contract", "Crowdsale", "Awesome Name","My Token"};
    public static String[] types = {"Standart Token", "Smart Contract", "Crowdsale"};
    public static float[] costs = {1000000, 0.2f, 230, 0.01f, 400, 500, 10.3f, 45.5f};

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

    public int getRandom(int min, int max){
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }

}
