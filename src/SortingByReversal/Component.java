package SortingByReversal;

import java.util.ArrayList;
import java.util.Iterator;

public class Component {
	ArrayList<Integer> cycleGroup;
	ArrayList<Integer> edgeGroup;
	Graph g;
	int type; // -1 for unoriented , 0 for trivial , 1 for oriented
	int subtype; // valid only when type =-1 i.e. unoriented tells simple hurdle i.e. 1 or super-hurdle i.e -1 or Protected Non-hurdle i.e. 0
	int min; //index of leftmost black edge in leftmost cycle
	int max; //index of rightmost black edge in rightmost cycle
	int id;
	Component() {}
	
	public Component(Graph graph) 
	{
		g=graph;
	}

	@Override
	public String toString()
	{
		String str="This Component has \n";
		Iterator<Integer> i= this.cycleGroup.iterator();
		while(i.hasNext())
			str=str+"Cycle: "+i.next()+"\n";
		str=str+"Type: "+type+"\n";
		if(type==-1)
			str=str+"Subtype: "+subtype+"\n";
		
		return str;
	}
	
	//constructs the component List based on the list of cycles given and their min & max value
	/*void constructComponent()
	{
		int n= g.cycleList.size();
		ArrayList<Component>componentList= new ArrayList<Component>();
		int visited[]=new int[n]; // holds the flag for the cycles already visited in graph
		int i=0;
		
		while(true)
		{
			//Component comp=new Component();
			cycleGroup=new ArrayList<Integer>() ;
			int cg=0; // counter to maintain the cycle no in a component
			int check=0;
			while(i<n)
			{
				if(visited[i]==1)
				{
					i++;
					continue;
				}
				if(cycleGroup.isEmpty()) //initial condition when no cycle in component then 1st unvisited cycle forms the component
				{
					cycleGroup.add(cg,i);
					cg++;
					visited[i]=1;
					min=g.cycleList.get(i).min;
					max=g.cycleList.get(i).max;
				}
				else if(g.cycleList.get(i).min>min&&g.cycleList.get(i).min<max&&g.cycleList.get(i).max>max) //all the intersecting cycles are included in the component
				{
					cycleGroup.add(cg,i);
					cg++;
					visited[i]=1;
					if(min>g.cycleList.get(i).min)
						min=g.cycleList.get(i).min;
					if(max<g.cycleList.get(i).max)
						max=g.cycleList.get(i).max;
				}
				i++;
			}
			// component is completed specify its type
			int ct=0;
			for(i=0;i<cg;i++)
				if(g.cycleList.get(cycleGroup.get(i)).type==-1)
					ct++;
			if(cg==1&&g.cycleList.get(cycleGroup.get(0)).type==0) // if only one cycle and that is trivial then component becomes trivial
				type=0;
			else if(ct==cg) // if all the cycles are unoriented then component is unoriented later specify its subtype
			{
				type=-1;
				constructEdgeGroupForComponent();
			}
			else 
				type=1; 
			
			g.componentList.add(this);
			for(i=0;i<n;i++)
				if(visited[i]==0)
				{
					check=1;
					break;
				}
			if(check==0) // when no unvisited cycle left then no more components possible
				break;
		}
		//this.identifyHurdle(componentList);
		//return componentList;
	}*/
	ArrayList<Component> constructComponent()
	{
		int n= g.cycleList.size();
		ArrayList<Component>componentList= new ArrayList<Component>();
		int visited[]=new int[n]; // holds the flag for the cycles already visited in graph
		int i=0;
		
		while(true)
		{
			Component comp=new Component();
			comp.cycleGroup=new ArrayList<Integer>() ;
			comp.edgeGroup=new ArrayList<Integer>() ;
			int compTrack[]=new int[g.Edge.length];
			int cg=0; // counter to maintain the cycle no in a component
			int check=0;
			while(i<n)
			{
				if(visited[i]==1)
				{
					i++;
					continue;
				}
				if(comp.cycleGroup.isEmpty()) //initial condition when no cycle in component then 1st unvisited cycle forms the component
				{
					comp.cycleGroup.add(cg,i);
					comp.edgeGroup.addAll(g.cycleList.get(i).EdgeList);
					cg++;
					visited[i]=1;
					comp.min=g.cycleList.get(i).min;
					comp.max=g.cycleList.get(i).max;
					if(comp.min==comp.max)
						break;
					for(int x:g.cycleList.get(i).EdgeList)
						compTrack[x]=1;
				}
				/*else if(g.cycleList.get(i).min>comp.min&&g.cycleList.get(i).min<comp.max&&(g.cycleList.get(i).max>comp.max))//all the intersecting cycles are included in the component
				{
					comp.cycleGroup.add(cg,i);
					for(int x:g.cycleList.get(i).EdgeList)
						compTrack[x]=1;
					cg++;
					visited[i]=1;
					if(comp.min>g.cycleList.get(i).min)
						comp.min=g.cycleList.get(i).min;
					if(comp.max<g.cycleList.get(i).max)
						comp.max=g.cycleList.get(i).max;
				}
				else if(g.cycleList.get(i).min>comp.min&&g.cycleList.get(i).min<comp.max&&(g.cycleList.get(i).max<comp.max))*/
				else
				{
					int flag=0;// to check  if any overlapping of edges
					for(int x=g.cycleList.get(i).min;x<=g.cycleList.get(i).max;x++)
					{
						if(compTrack[x]==1)
						{
							flag=1; // overlapping found means the cycle is part of this component
							break;
						}
					}
					if(flag==1) // when overlaps then make the cycle part of  this component
					{
						comp.cycleGroup.add(cg,i);
						comp.edgeGroup.addAll(g.cycleList.get(i).EdgeList);
						for(int x:g.cycleList.get(i).EdgeList)
							compTrack[x]=1;
						cg++;
						visited[i]=1;
						if(comp.min>g.cycleList.get(i).min)
							comp.min=g.cycleList.get(i).min;
						if(comp.max<g.cycleList.get(i).max)
							comp.max=g.cycleList.get(i).max;
					}
				}
				i++;
			}
			// component is completed specify its type
			int ct=0; // counter to keep  track  of unoriented cycles
			for(i=0;i<cg;i++)
				if(g.cycleList.get(comp.cycleGroup.get(i)).type==-1)
					ct++;
			if(cg==1&&g.cycleList.get(comp.cycleGroup.get(0)).type==0) // if only one cycle and that is trivial then component becomes trivial
				comp.type=0;
			else if(ct==cg) // if all the cycles are unoriented then component is unoriented later specify its subtype
			{
				comp.type=-1;
				
			}
			else 
				comp.type=1; 
			constructEdgeGroupForComponent(comp);
			componentList.add(comp);
			for(i=0;i<n;i++)
				if(visited[i]==0)
				{
					check=1;
					break;
				}
			if(check==0) // when no unvisited cycle left then no more components possible
				break;
		}
		//this.identifyHurdle(componentList);
		while(true)
		{
			int check=0;
			int compTrack[]=new int[g.Edge.length];
			for(int x=0;x<componentList.size()-1;x++)
			{
				Component c1=componentList.get(x);
				if(c1.edgeGroup.size()==1)
					continue;
				for(int j:c1.edgeGroup)
					compTrack[j]=1;
				for(int y=x+1;y<componentList.size();y++)
				{
					Component c2=componentList.get(y);
					if(c2.edgeGroup.size()==1)
						continue;
					else if(c2.min>c1.min&&c2.min<c1.max&&c2.max<c1.max)//all the intersecting component are included in the component
					{
						int flag=0;// to check  if any overlapping of edges
						for(int j=c2.min;j<=c2.max;j++)
						{
							if(compTrack[j]==1)
							{
								flag=1; // overlapping found means the cycle is part of this component
								break;
							}
						}
						if(flag==1) // when overlaps then make the cycle part of  this component
						{
							c1.cycleGroup.addAll(c2.cycleGroup);
							c1.edgeGroup.addAll(c2.edgeGroup);
							if(c1.min>c2.min)
								c1.min=c2.min;
							if(c1.max<c2.max)
								c1.max=c2.max;
							if(c1.type*c2.type==-1)
								c1.type=1;
							else if(c1.type*c2.type==1&&c1.type==-1)
								c1.type=-1;
							check=1;
							componentList.remove(y);
							break;
						}
					}
				}
				if(check==1)
					break;
			}
			if(check==0)
				break;
		}
		return componentList;
	}
	
	/*void identifyHurdle(ArrayList<Component>componentList)
	{
		int n=componentList.size();
		Component c,c1,c2,c3;
		Iterator<Component>i=componentList.iterator();
		if(n<=2)
		{
			while(i.hasNext())
				if((c=i.next()).type==-1)
					c.subtype=1;
			return;
		}
		c1=i.next();
		while(i.hasNext())
		{
			c2=i.next();
			/*if(i.hasNext())
				c2=i.next();
			else
			{
				if(c1.type==-1)
					c1.subtype=1;
				break;
			}*/
			/*if(c1.type==-1&&c2.type==-1&&c2.min>c1.min&&c2.max<c1.max)
			{
				if(i.hasNext())
					c3=i.next();
				else
				{
					if(c2.type==-1)
						c2.subtype=1;
					break;	
				}
				if(c3.type==-1)
				{
					c2.subtype=-1;
					c1.subtype=0;
				}
				else
				{
					c1.subtype=1;
					c2.subtype=1;
				}
					
			}
			else
			{
				if(c1.type==-1)
					c1.subtype=1;
				c1=c2;
			}
		}
	}*/
	
	void constructEdgeGroupForComponent(Component comp)
	{
		comp.edgeGroup= new ArrayList<Integer>();
		for(int i: comp.cycleGroup)
		{
			Cycle c=g.cycleList.get(i);
			comp.edgeGroup.addAll(c.EdgeList);	
			//System.out.println("edge group:"+comp.edgeGroup);
		}
	
		
	}
	
}
