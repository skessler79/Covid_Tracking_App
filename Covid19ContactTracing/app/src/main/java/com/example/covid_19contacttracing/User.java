package com.example.covid_19contacttracing;


/**
 * This class consists of basic information of a user.
 *
 * @author Selwyn Darryl Kessler
 * @author Theerapob Loo @ loo Wei Xiong
 */
abstract class User
{
    /**
     * Holds the user name.
     */
    protected String name;

    /**
     * Holds the user phone number.
     */
    protected String phone;

    /**
     * Holds the user email.
     */
    protected String email;

    /**
     * Returns the name of the user.
     *
     * @return The name of the user.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name The name of the user to be set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the phone number of the user.
     *
     * @return The phone number of the user.
     */
    public String getPhone()
    {
        return this.phone;
    }

    /**
     * Sets the phone number of the user.
     *
     * @param phone The phone number of the user to be set.
     */
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    /**
     * Returns the email address of the user.
     *
     * @return The email address of the user.
     */
    public String getEmail()
    {
        return this.email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The email address of the user to be set.
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * Returns the basic information of the user as a string.
     *
     * @return The basic information of the user as a string.
     */
    public String toString()
    {
        StringBuilder str = new StringBuilder("");
        str.append(this.name + " , ");
        str.append(this.phone + " , ");
        str.append(this.email + " , ");
        return str.toString();
    }
}
