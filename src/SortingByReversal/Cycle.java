package SortingByReversal;

import java.util.ArrayList;
import java.util.Iterator;

public class Cycle 
{
	int type; // -1 for unoriented , 0 for trivial , 1 for oriented
	ArrayList<Integer> EdgeList; //list of edge nos that is contained in the cycle
	ArrayList<Integer> direction; // holds the direction of edge corresponding to in the EdgeList
	int min; //index of leftmost black edge
	int max; //index of rightmost black edge
	
	@Override
	public String toString()
	{
		String str="This Cycle has \n";
		Iterator<Integer> i= this.EdgeList.iterator();
		Iterator<Integer> j=this.direction.iterator();
		while(i.hasNext())
			str=str+"Edge: "+i.next()+" and Direction: "+j.next()+"\n";
		return str;
	}
	
	// it constructs all the possible cycle and maintains its entry in cycleList
	ArrayList<Cycle> constructCycle(BlackEdge Edge[])
	{
		int n= Edge.length;
		ArrayList<Cycle>cycleList=new ArrayList<Cycle>();
		int visited[]=new int[n]; // holds the flag for the edges already visited in graph
		int s=0; // node to be searched in the Edge[]
		while(true)
		{
			Cycle c=new Cycle();
			c.EdgeList= new ArrayList<Integer>();
			c.direction=new ArrayList<Integer>();
			int e=0; // counter to increase EdgeList[]
			int check=0; // to check requirement of creation of new cycle
			int i=0;
			while(i<n)
			{
				if(visited[i]==1)
				{
					i++;
					continue;
				}
				if(Edge[i].left.key==s||Edge[i].right.key==s)
				{
					c.EdgeList.add(e, i);
					//c.EdgeList.add(i);
					visited[i]=1; //to represent this edge is already traversed in some cycle
					if(e==0) // if its the first entry in the EdgeList of then its the start point of cycle 
						c.min=i;
					if(c.min>i)
						c.min=i;
					if(c.max<i)
						c.max=i;// each time rightmost index is maintained so that end point of cycle could be detected
					if(Edge[i].right.key==s)
					{
						c.direction.add(e,1); // when right to left traversal of edge then 1
						s=Edge[i].left.key;
					}
					else
					{
						c.direction.add(e,0); // when left to right traversal of edge then 0
						s=Edge[i].right.key;
					}
					
					if(s%2==0) //if current node is even then we have to find s+1 node if odd then s-1 node 
						s++;
					else
						s--;
					
					i=0;
					e++;
				}
				
				if(s==Edge[c.min].left.key) // to check completion of cycle
					break;
				i++;
			}
			
			// cycle is completed specify its type
			int cd=0;
			Iterator<Integer> i1=c.direction.iterator();
			while(i1.hasNext())
			{
				if(i1.next()==0)
					cd++;
			}
			if(e==1) // if only 1 edge in cycle then its trivial cycle
				c.type=0;
			else if(cd==e||cd==0) // if direction of all edges are 0 or 1 then unoriented
				c.type=-1;
			else 
				c.type=1;
			cycleList.add(c);
			
			for(i=0;i<n;i++) // to find the next unvisited node to start a new cycle 
				if(visited[i]==0)
				{
					check=1;
					s=Edge[i].left.key;
					break;
				}
			if(check==0) // all the edges have been visited so the process breaks
				break;
		
		}
		return cycleList;
	}
	

}
