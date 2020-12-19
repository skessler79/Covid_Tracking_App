import java.util.ArrayList;

public class Shop
{
	private String name;
	private String phone;
	private ShopStatus status;
	private String manager;
	private ArrayList<ShopCheckIn> customers;

	public Shop()
	{
		customers = new ArrayList<>();
	}

	public Shop(String name, String phone, ShopStatus status, String manager)
	{
		customers = new ArrayList<>();
		this.name = name;
		this.phone = phone;
		this.status = status;
		this.manager = manager;
	}

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

	public ShopStatus getStatus()
	{
		return this.status;
	}

	public void setStatus(ShopStatus status)
	{
		this.status = status;
	}

	public String getManager()
	{
		return this.manager;
	}

	public void setManager(String manager)
	{
		this.manager = manager;
	}

	public void checkIn(Customer customer)
	{
		// Testing purposes only
		if(customer.name.equals("Dog"))
		{
			ShopCheckIn item = new ShopCheckIn(customer, (System.currentTimeMillis() / 1000L) + 3700);
			customers.add(item);
		}
		else if(customer.name.equals("Elephant"))
		{
			ShopCheckIn item = new ShopCheckIn(customer, (System.currentTimeMillis() / 1000L) - 3700);
			customers.add(item);
		}
		else
		{
			ShopCheckIn item = new ShopCheckIn(customer, System.currentTimeMillis() / 1000L);
			customers.add(item);
		}
	}

	public ArrayList<ShopCheckIn> getCustomers()
	{
		return this.customers;
	}

	public String getCustomersString()
	{
		StringBuilder str = new StringBuilder("Customer List : ");
		for(int i = 0; i < customers.size(); ++i)
		{
			str.append("\n" + String.valueOf(i + 1) + ".  ");
			str.append(customers.get(i).toString());
		}
		return str.toString();
	}

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