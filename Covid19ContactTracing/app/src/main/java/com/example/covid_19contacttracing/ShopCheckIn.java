package com.example.covid_19contacttracing;

public class ShopCheckIn
{
    private Customer customer;
    private long time;

    public ShopCheckIn()
    {}

    public ShopCheckIn(Customer customer, long time)
    {
        this.customer = customer;
        this.time = time;
    }

    public Customer getCustomer()
    {
        return this.customer;
    }

    public long getTime()
    {
        return this.time;
    }

    public String toString()
    {
        return this.customer.toString() + " , " + String.valueOf(this.time);
    }
}
