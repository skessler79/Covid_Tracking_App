import java.util.ArrayList;

public class Customer extends User
{
	private CustStatus status;
	private ArrayList<CustomerHistory> customerHistory;

	public Customer()
	{
		customerHistory = new ArrayList<>();
	}

	public Customer(String name, String phone, CustStatus status)
	{
		customerHistory = new ArrayList<>();
		this.name = name;
		this.phone = phone;
		this.status = status;
	}

	public CustStatus getStatus()
	{
		return this.status;
	}

	public void setStatus(CustStatus status)
	{
		this.status = status;
	}

	public void checkIn(Shop shop)
	{
		shop.checkIn(this);

		// Testing putposes only
		if(this.name.equals("Dog"))
		{
			customerHistory.add(new CustomerHistory(shop, (System.currentTimeMillis() / 1000L) + 3700));
		}
		else if(this.name.equals("Elephant"))
		{
			customerHistory.add(new CustomerHistory(shop, (System.currentTimeMillis() / 1000L) - 3700));
		}
		else
		{
			customerHistory.add(new CustomerHistory(shop, System.currentTimeMillis() / 1000L));
		}
		
	}

	public ArrayList<CustomerHistory> getHistory()
	{
		return this.customerHistory;
	}

	public String getHistoryString()
	{
		StringBuilder str = new StringBuilder("Customer History: ");
		for(int i = 0; i < customerHistory.size(); ++i)
		{
			str.append("\n" + String.valueOf(i + 1) + ".  ");
			str.append(customerHistory.get(i).toString());
		}
		return str.toString();
	}

	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder("");
		str.append(this.name + " , ");
		str.append(this.phone + " , ");
		str.append(this.status);
		return str.toString();
	}
}

