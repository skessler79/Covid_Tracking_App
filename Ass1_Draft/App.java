import java.util.ArrayList;

public class App
{
	public static void main(String[] args)
	{
		ArrayList<Customer> customers = new ArrayList<>();
		ArrayList<Admin> admins = new ArrayList<>();
		ArrayList<Shop> shops = new ArrayList<>();

		customers.add(new Customer("Alligator", "011-69696969", CustStatus.valueOf("Normal")));
		customers.add(new Customer("Bat", "011-1-1-1", CustStatus.valueOf("Normal")));
		customers.add(new Customer("Cat", "011-1-1-1", CustStatus.valueOf("Normal")));
		customers.add(new Customer("Dog", "011-1-1-1", CustStatus.valueOf("Normal")));
		customers.add(new Customer("Elephant", "011-1-1-1", CustStatus.valueOf("Normal")));

		admins.add(new Admin("Dick", "999"));

		shops.add(new Shop("Ah long Legit Lend3rs", "011011010101", ShopStatus.valueOf("Normal"), "Karen"));
		shops.add(new Shop("Bah Kut Teh", "011011010101", ShopStatus.valueOf("Normal"), "Aqel"));

		System.out.println(admins.get(0).viewAllCustomers(customers));
		System.out.println();
		System.out.println(admins.get(0).viewAllShops(shops));
		System.out.println();

		customers.get(0).checkIn(shops.get(0));
		customers.get(1).checkIn(shops.get(0));
		customers.get(2).checkIn(shops.get(1));
		// This guy checks in over an hour later
		customers.get(3).checkIn(shops.get(0));
		// This guy checks in over an hour earlier
		customers.get(4).checkIn(shops.get(0));
		
		System.out.println(shops.get(0).getCustomersString());
		System.out.println();
		System.out.println(customers.get(0).getHistoryString());
		System.out.println();

		admins.get(0).flag(customers.get(1));
		System.out.println(admins.get(0).viewAllCustomers(customers));
		System.out.println();
		System.out.println(admins.get(0).viewAllShops(shops));
	}
}