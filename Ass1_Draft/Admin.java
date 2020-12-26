import java.util.ArrayList;
import java.util.HashSet;

public class Admin extends User
{
	public Admin()
	{}

	public Admin(String name, String phone)
	{
		this.name = name;
		this.phone = phone;
	}

	public void flag(Customer customer)
	{
		customer.setStatus(CustStatus.valueOf("Case"));
		ArrayList<CustomerHistory> customerHistory = new ArrayList<>();
		customerHistory = customer.getHistory();

		HashSet<Shop> shops = new HashSet<>();

		for(CustomerHistory el : customerHistory)
		{
			shops.add(el.getShop());
		}

		for(Shop el : shops)
		{
			el.setStatus(ShopStatus.valueOf("Case"));

			ArrayList<ShopCheckIn> contacts = new ArrayList<>(el.getCustomers());
			for(int i = 0; i < contacts.size(); ++i)
			{
				if(contacts.get(i).getCustomer().equals(customer))
				{
					for(int j = i; j >= 0 && j < contacts.size() && contacts.get(i).getTime() <= (contacts.get(j).getTime() + 3600L); --j)
					{
						if(!contacts.get(j).getCustomer().equals(customer))
							contacts.get(j).getCustomer().setStatus(CustStatus.valueOf("Close"));
					}

					for(int j = i; j >= 0 && j < contacts.size() && contacts.get(i).getTime() >= (contacts.get(j).getTime() - 3600L); ++j)
					{
						if(!contacts.get(j).getCustomer().equals(customer))
							contacts.get(j).getCustomer().setStatus(CustStatus.valueOf("Close"));
					}
				}
			}
		}
	}

	public String viewAllCustomers(ArrayList<Customer> customers)
	{
		StringBuilder str = new StringBuilder("List of All Customers : ");
		for(int i = 0; i < customers.size(); ++i)
		{
			str.append("\n" + String.valueOf(i + 1) + ".  ");
			str.append(customers.get(i).toString());
		}
		return str.toString();
	}

	public String viewAllShops(ArrayList<Shop> shops)
	{
		StringBuilder str = new StringBuilder("List of All Shops : ");
		for(int i = 0; i < shops.size(); ++i)
		{
			str.append("\n" + String.valueOf(i + 1) + ".  ");
			str.append(shops.get(i).toString());
		}
		return str.toString();
	}
}