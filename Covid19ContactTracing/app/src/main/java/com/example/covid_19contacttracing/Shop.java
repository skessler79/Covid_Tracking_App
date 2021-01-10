package com.example.covid_19contacttracing;

/**
 * This class holds basic information about a shop.
 *
 * @author Selwyn Darryl Kessler
 * @author Theerapob Loo @ Loo Wei Xiong
 */
public class Shop
{
    /**
     * Holds the name of the shop.
     */
    private String name;

    /**
     * Holds the phone number of the shop.
     */
    private String phone;

    /**
     * Holds the shop status of the shop.
     */
    private ShopStatus status;

    /**
     * Holds the name of the manager of the shop.
     */
    private String manager;

    /**
     * Default constructor for shop class.
     */
    public Shop()
    {

    }

    /**
     * Parameterized constructor for Shop class.
     *
     * @param name Name of the shop.
     * @param phone Phone number of the shop.
     * @param status Status of the shop.
     * @param manager Manager's name of the shop.
     */
    public Shop(String name, String phone, ShopStatus status, String manager)
    {
        this.name = name;
        this.phone = phone;
        this.status = status;
        this.manager = manager;
    }

    /**
     * Returns the name of the shop.
     *
     * @return The name of the shop.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Sets the name of the shop.
     *
     * @param name The name of the shop to be set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the phone number of the shop.
     *
     * @return The phone number of the shop.
     */
    public String getPhone()
    {
        return this.phone;
    }

    /**
     * Sets the phone number of the shop.
     *
     * @param phone The phone number of the shop to be set.
     */
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    /**
     * Returns the status of the shop.
     *
     * @return The status of the shop.
     */
    public ShopStatus getStatus()
    {
        return this.status;
    }

    /**
     * Sets the status of the shop.
     *
     * @param status The status of the shop to be set.
     */
    public void setStatus(ShopStatus status)
    {
        this.status = status;
    }

    /**
     * Returns the manager of the shop.
     *
     * @return The manager of the shop.
     */
    public String getManager()
    {
        return this.manager;
    }

    /**
     * Sets the manager of the shop.
     *
     * @param manager The manager of the shop to be set.
     */
    public void setManager(String manager)
    {
        this.manager = manager;
    }

    /**
     * Returns the basic information of the shop as a string.
     *
     * @return The basic information of the shop as a string.
     */
    public String toString()
    {
        StringBuilder str = new StringBuilder("");
        str.append(this.name + " , ");
        str.append(this.phone + " , ");
        str.append(this.status + " , ");
        str.append(this.manager);
        return str.toString();
    }
}
