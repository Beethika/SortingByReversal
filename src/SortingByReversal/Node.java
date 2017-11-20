package SortingByReversal;

public class Node {
	int key;
	//Node black; //required or not??
	Node(int key)
	{
		this.key=key;
	}
	
	@Override
	public String toString()
	{
		String str=""+this.key;
		return str;
	}
}
