public class CustomerHistory
{
	private Shop shop;
	private long time;

	public CustomerHistory()
	{}

	public CustomerHistory(Shop shop, long time)
	{
		this.shop = shop;
		this.time = time;
	}

	public Shop getShop()
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
		str.append(this.shop.getName());
		return str.toString();
	}
}