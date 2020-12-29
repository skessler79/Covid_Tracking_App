package com.example.covid_19contacttracing;

import java.io.Serializable;

public class CustomerHistoryTest implements Serializable
{
    private Long time;
    private String shop;

    public CustomerHistoryTest()
    {}

    public CustomerHistoryTest(Long time, String shop)
    {
        this.shop = shop;
        this.time = time;
    }

    public String getShop()
    {
        return this.shop;
    }

    public long getTime()
    {
        return this.time;
    }

    public String toString()
    {
        StringBuilder str = new StringBuilder("");
        str.append(this.time + " , ");
        str.append(this.shop);
        return str.toString();
    }
}
