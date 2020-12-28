package com.example.covid_19contacttracing;

public class User
{
    protected String name;
    protected String phone;


    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPhone()
    {
        return this.phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String toString()
    {
        StringBuilder str = new StringBuilder("");
        str.append(this.name + " , ");
        str.append(this.phone + " , ");
        return str.toString();
    }
}
