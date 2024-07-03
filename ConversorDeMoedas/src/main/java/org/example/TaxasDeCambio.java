package org.example;

import com.google.gson.JsonObject;

public class TaxasDeCambio {

    private String provider;
    private String base;
    private String date;
    private JsonObject rates;


    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public JsonObject getRates() {
        return rates;
    }

    public void setRates(JsonObject rates) {
        this.rates = rates;
    }
}
