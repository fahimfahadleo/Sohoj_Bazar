package com.sohojbazar.sohojbazar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class chart {
    private String itemname,itemquantity,itemprice,itemurl;
    static HashMap<String,JSONObject> productmap = new HashMap<>();

    public chart(String itemname, String itemquantity, String itemprice, String itemurl) throws JSONException {
        this.itemname = itemname;
        this.itemquantity = itemquantity;
        this.itemprice = itemprice;
        this.itemurl = itemurl;
        JSONObject jsonObject =new JSONObject();
        jsonObject.put("name",itemname);
        jsonObject.put("quantity",itemquantity);
        jsonObject.put("price",itemprice);
        jsonObject.put("url",itemurl);
        productmap.put(itemname,jsonObject);
    }

    public chart(){

    }


    public HashMap returnmap(){
        return productmap;
    }
}
