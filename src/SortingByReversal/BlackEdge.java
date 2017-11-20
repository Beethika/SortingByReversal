package SortingByReversal;


public class BlackEdge {
	Node left;
	Node right;
	
	public BlackEdge() {}
	
	public BlackEdge(Node node1, Node node2) {
		left=node1;
		right=node2;
	}

	@Override
	public String toString()
	{
		String str="";
		str=str+"Left Node: "+this.left+" Right Node: "+this.right+"\n";
		return str;
	}
}
