package SortingByReversal;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeSet;

public class Graph {
	BlackEdge Edge[]; //complete list of edges in order of their occurrence in graph
	ArrayList<Cycle> cycleList; //complete list of cycles in order of their occurrence in graph
	ArrayList<Component> componentList; //complete list of component in order of their occurrence in graph
	ArrayList<Integer> unorientedComponents; // list of all unoriented component
	ArrayList<ArrayList<Component>> hurdleChain;
	int inCycle[];
	int degree[];
	
	int hg[][];
	TreeSet<TreeSet<Integer>> node;
	int n; // no of nodes in a graph
	int c; // no of cycles in graph
	int h; // no of hurdles in a graph
	int s; // no of super-hurdles in a graph
	int f; // fortress in graph 1 if present o/w 0
	public int d; // distance of graph
	
	Graph()	{}
	
	//this forms the initial EdgeList, cycleList and ComponentList of the Graph
	public Graph(int seq[],int n, int d)
	{
		//it takes the transformed input sequence and converts it into node pairs and saves in Edge table
		this.n=n/2-1;
		Edge=new BlackEdge[n/2]; //edges are half of sequence length as 2 are connected
		int j=0;
		for(int i=0;i<n;i=i+2)
		{
			BlackEdge e=new BlackEdge(new Node(seq[i]),new Node(seq[i+1]));
			Edge[j++]=e; 
		}
		/*-------------------------------------------------------------------------------------------------------------------*/
		cycleList=new Cycle().constructCycle(Edge);
		c=cycleList.size();
		componentList=(ArrayList<Component>) new Component(this).constructComponent();
		// assigning id's to components
		int id=0;
		for(Component comp:componentList)
			comp.id=id++;
		// construct a list for un-oriented components 
		int i=0;
		unorientedComponents= new ArrayList<Integer>();
		if(Hurdle.ON)
		{
			for(Component comp: componentList)
			{
				if(comp.type==-1)
					unorientedComponents.add(i);
				i++;
			}
		}
		if(!unorientedComponents.isEmpty())
		{	constrctHurdleGraph();
			hurdleDetection();
			//constructHurdleChain();
		}
		//displayCycle();
		//displayComponent();
		//ArrayList<ArrayList<Integer>> L=findSeperatingHurdle();
		//for(ArrayList<Integer> l:L)
		//	System.out.println(l);
		node=new TreeSet<TreeSet<Integer>>(new Comp()); 
		this.calculateDistance();
		if(!Hurdle.ON||(Hurdle.ON&&(this.d<d||d==-1)))
		//if(this.d<d||d==-1)
		{
			this.findReversal();
			if(Hurdle.ON)
			if(!unorientedComponents.isEmpty())
			{
				findNeutralReversal();
				if(f==0)
					findMergeReversalNoFortress();
			}
			if(Debug.ON)
			{
				System.out.println("Tree Node");
				for(TreeSet<Integer> t:node)
					System.out.print(" "+t);
				System.out.println();
			} 				
		}
	}
	
	// to calculate the distance of the graph
	void calculateDistance()
	{
		if(Hurdle.ON)
		{Iterator<Component>i1=componentList.iterator();
		while(i1.hasNext())
		{
			Component co;
			if((co=i1.next()).type==-1&&co.subtype!=0) // counting total no of hurdles present in graph
			{
				h++;
				if(co.subtype==-1) // check total no of super-hurdles 
					s++;
			}
		}
		if(h%2!=0&&h==s) // when all hurdles are super-hurdles and are odd in no.
			f=1;
		d=n+1-c+h+f; // distance of the graph
		}
		else
			d=n+1-c;
		if(Debug.ON)
			System.out.println("\n Distance is :"+d+" #cycle is :"+c+" #hurdle is "+h+" #fortress is :"+f+" #super-hurdle is :"+s);
	}
	
	// Displays the edges in the graph in the order of their occurrence
	void displayGraphEdges()
	{
		for(int i=0;i<Edge.length;i++)
			System.out.println("EDGE "+i+" "+Edge[i]);
	}
	
	// Displays the Cyle's details in the graph in the order of their occurrence
	void displayCycle()
	{
		int i=0;
		Iterator<Cycle>i1=cycleList.iterator();
		while(i1.hasNext())
		{
			System.out.println("CYCLE C"+i+"\n"+i1.next());
			i++;
		}
	}
	
	// Displays the Components's details in the graph in the order of their occurrence
	void displayComponent()
	{
		int i=0;
		Iterator<Component>i1=componentList.iterator();
		while(i1.hasNext())
		{
			System.out.println("COMPONENT COMP"+i+"\n"+i1.next());
			i++;
		}
	}
	
	//reversal on black edges belonging to the same oriented cycle 
	void findReversal()
	{
		//cut= new ArrayList<int[]>();
		//node=new TreeSet<TreeSet<Integer>>(new Comp()); //arranges and stores all 1-seq in lexicographic order
		for(Component co:componentList) //iterating over all the components present in graph
		//for(Cycle c:cycleList)
		{
			if(co.type==1) // for all the oriented components find out the diverging edges to perform cut reversal
			{
				for(Integer i:co.cycleGroup)  // iterating over all the cycles in oriented component
				{
					Cycle c= cycleList.get(i); // from cycleGroup index of cycle is found which can be checked in cycleList
					// to find all the diverging edges in particular cycle
					if(c.type==1)
						for(int e1=0;e1<c.direction.size()-1;e1++) 
							for(int e2=e1+1;e2<c.direction.size();e2++)
								if(c.direction.get(e1)!=c.direction.get(e2))
								{
									TreeSet<Integer> seq=this.find1Sequence(c,e1,e2);
									if(CI.ON)
									{
										
										if(CommonInterval.filter(seq)==0)
											node.add(seq);
									}
									else
										node.add(seq);
								}
				}
			}
		}
	}
	
	//reversal on black edges belonging to the same un-oriented cycle 
	void findNeutralReversal() 
	{
		ArrayList<Component> H= new ArrayList<Component>();
		if(f==0)
		{
			for(int i: unorientedComponents)
			{
				//Component comp= findComponentForEdge(i);
				Component comp= componentList.get(i);
				if(comp.subtype==1) // add all the simple hurdles within whose cycle cut can be performed
					H.add(comp);
			}

			if(s%2==1&&h==s+1) //avoid a cut reversal  when no. of super-hurdles are odd and there is only 1 simple-hurdle otherwise will lead to fortress 
				return;
		}
		else
		{
			//if(!hurdleChain.isEmpty())
			/*for(ArrayList<Component> temp:hurdleChain) //H <--pseudohurdle or a single protector
			{
				if(temp.size()==2)
				{
					for(Component comp:temp)
						H.add(comp);
				}
			}*/
			for(int i: unorientedComponents)
			{
				//Component comp= findComponentForEdge(i);
				Component comp= componentList.get(i);
				if(comp.subtype!=1) // applying neutral reversal on superhurdle or protected non-hurdle to remove fortress
					H.add(comp);
			}
		}
		for(Component co:H) //iterating over all the components present in H
		{
			for(Integer i:co.cycleGroup)  // iterating over all the cycles in selected components
			{
				Cycle c= cycleList.get(i); // from cycleGroup index of cycle is found which can be checked in cycleList
				// enumerating all pairs of edges belonging to the same cycle of a component in H
				for(int e1=0;e1<c.EdgeList.size()-1;e1++) 
					for(int e2=e1+1;e2<c.EdgeList.size();e2++)
					{
						TreeSet<Integer> seq=this.find1Sequence(c,e1,e2);
						//System.out.println(seq);
						if(CI.ON)
						{						
							if(CommonInterval.filter(seq)==0)
								node.add(seq);
						}
						else
							node.add(seq);
					}
			}
		}
	}
	
	void findMergeReversalNoFortress()
	{
		HashMap<Integer, Integer> S=findSeperatingHurdle();// pair of all the benign component and their separating hurdle
		ArrayList<Component> H= new ArrayList<Component>(); // all hurdles in graph
		for(int i: unorientedComponents)
		{
			//Component comp= findComponentForEdge(i);
			Component comp= componentList.get(i);
			if(comp.subtype!=0) 
				H.add(comp); // add all the hurdles in the graph
		}
		for(int x=0;x<H.size()-1;x++)
		{
			for(int y=x+1;y<H.size();y++)
			{
				Component i=H.get(x);
				Component k=H.get(y);
				if((s%2==1&&h==s+2&&i.subtype==1&&k.subtype==1)||(s%2==0&&h==s+1&&i.subtype*k.subtype==-1))
					continue;
				else
				{
					ArrayList<Component> J=new ArrayList<Component>(); //J is i or is separated by i
					ArrayList<Component> L=new ArrayList<Component>(); //L is k or is separated by k
					J.add(i);
					L.add(k);
					for (Entry<Integer, Integer> entry : S.entrySet())  // iterate over all the entries of HashMap
					{
					    int key = entry.getKey();
					    int value = entry.getValue();
					    if(value==i.id)				
					    	J.add(componentList.get(key)); // to add benign component separated by i
					    if(value==k.id)
					    	L.add(componentList.get(key)); // to add benign component separated by k
					}
					for(Component j:J)
					{
						for(Component l:L)
						{
							for(int e1:j.edgeGroup)
							{
								for(int e2:l.edgeGroup)
								{
									TreeSet<Integer> seq=this.find1SequenceNoCycle(e1, e2);
									if(CI.ON)
									{						
										if(CommonInterval.filter(seq)==0)
											node.add(seq);
									}
									else
										node.add(seq);
								}
							}
						}
					}
				}
			}
		}
	}
	
	void findMergeReversalFortress()
	{
		
	}
	
	// To find out all the elements involved while performing the merge, here no cycle detail given directly the edges are given
		TreeSet<Integer> find1SequenceNoCycle(int e1, int e2)
		{
			TreeSet<Integer> one_seq=new TreeSet<Integer>(); // to store elements in sorted order
			// we have the 2 index of BlackEdge between which the cut has to be performed
			int min=e1<e2?e1:e2;
			int max=e1>e2?e1:e2;
			for(int i=min;i<max;i++)
			{
				int k=Edge[i].right.key; // the key of rightmost node of BlackEdge
				if(k%2==0) 
					one_seq.add(k/2);
				else
					one_seq.add((k/2)+1);
			}
			/*System.out.println("the 1-seq");
			for(int i1:seq)
				System.out.println(" "+i1);*/
			return one_seq;
		}
	
	// To find out all the elements involved while performing the cut
	TreeSet<Integer> find1Sequence(Cycle c,int e1, int e2)
	{
		TreeSet<Integer> one_seq=new TreeSet<Integer>(); // to store elements in sorted order
		// we have the 2 index of BlackEdge between which the cut has to be performed
		int i=c.EdgeList.get(e1);
		int j=c.EdgeList.get(e2);
		int min=i<j?i:j;
		int max=i>j?i:j;
		for(i=min;i<max;i++)
		{
			int k=Edge[i].right.key; // the key of rightmost node of BlackEdge
			if(k%2==0) 
				one_seq.add(k/2);
			else
				one_seq.add((k/2)+1);
		}
		return one_seq;
	}
	
	void constrctHurdleGraph()
	{
		int n=componentList.size();
		int[][] temphg=new int[n][n];
		int size=unorientedComponents.size();
		hg=new int[size][size];
		Component comp;
		for(int i=0;i<n;i++)
		{
			if(unorientedComponents.contains(i)) // update entries only for un-oriented components as only they participate in hurdle graph
			{
				comp=componentList.get(i);
				for(int e:comp.edgeGroup)
				{
					if(e!=0&&e!=Edge.length-1) // all the inner edges not extreme condition varies for extreme situations
					{
						int c1=findComponentNumberForEdge(e-1); // find component for edge before it 
						if(c1!=i&&c1>=0) // if c1=-1 means that edge belonged to oriented cycle so entry not maintained in hurdle graph
							temphg[i][c1]=1;
						int c2=findComponentNumberForEdge(e+1); // find component for edge after it 
						if(c2!=i&&c2>=0)
							temphg[i][c2]=1;
					}
					else if(e==0)
					{
						int c2=findComponentNumberForEdge(e+1); // find component for edge after it
						if(c2!=i&&c2>=0)
							temphg[i][c2]=1;
					}
					else
					{
						int c1=findComponentNumberForEdge(e-1); // find component for edge before it 
						if(c1!=i&&c1>=0)
							temphg[i][c1]=1;
					}
				}		
			}
		}
		// as temphg[] has entry for both oriented as well as  unorientedd component so only the entries of unoriented components are copied here 
		for(int i=0;i<size;i++)
		{
			for(int j=0;j<size;j++)
			{
				hg[i][j]=temphg[unorientedComponents.get(i)][unorientedComponents.get(j)];
			}
		}
		if(Debug.ON)
		{
			System.out.println("Hurdle Graph:");
			for(int i=0;i<size;i++)
			{
				for(int j=0;j<size;j++)
				{
					System.out.print(hg[i][j]+" ");
				}
				System.out.println();
			}
		}
	}
	
	int findComponentNumberForEdge(int e)
	{
		Component comp;
		for(int i: unorientedComponents)
		{
			comp=componentList.get(i);
			if(comp.edgeGroup.contains(e))
				return i;
		}
		return -1;
	}
	
	void hurdleDetection()
	{
		int n=unorientedComponents.size();
		inCycle=new int[n];
		findCycleinGraph();
		degree=new int[n];
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)
			{
				if(hg[i][j]==1)
					degree[i]++;
			}
		}
		for(int i=0;i<n;i++)
		{
			Component com=componentList.get(unorientedComponents.get(i));
			if((inCycle[i]==1&&degree[i]==2)||(inCycle[i]==0&&degree[i]<2))
			{
				com.subtype=1; // simple hurdle
				if(degree[i]==1)
				{
					for(int j=0;j<n;j++)
					{
						if(hg[i][j]==1)
						{
							if((degree[j]==3&&inCycle[j]==1)||(degree[j]==2&&inCycle[j]==0))
								com.subtype=-1; // super hurdle
						}
					}
				}
				
			}
			else if((inCycle[i]==1&&degree[i]>2)||(inCycle[i]==0&&degree[i]==2))
				com.subtype=0; // protected non-hurdle
		}
	}
	
	void findCycleinGraph()
	{
		int n=hg.length;
		ArrayList<String> chain=new ArrayList<String>();
		int visited[]= new int[n];
		
		DFS(0,-1,visited,"",chain);
		
		for(String i:chain)
		{
			//System.out.println(i);
			char c=i.charAt(i.length()-1);
			int in=i.indexOf(c);
			if(in!=i.length()-1)
			{
				for(int x=in;x<=i.length()-2;x++)
				{
					inCycle[i.charAt(x)-'0']=1;
				}
			}
		}
		if(Debug.ON)
		{
			System.out.println("unoriented components forming a cycle");
			for(int i:inCycle)
				System.out.println(i);
		}
	}
	//generates all the possible paths in the graph from a particular vertex as the graph is connected so covers all the paths possible	
	void DFS(int curr, int par, int visited[],String path,ArrayList<String> chain)
	{
		int n=hg.length;
		path=path+curr;
		visited[curr]=1;
		/*for(int x:visited)
			System.out.print(x);
		System.out.println();*/
		for(int i=0;i<n;i++)
		{
			if(hg[curr][i]==1&&visited[i]==1&&i!=par)
			{
				path+=i;
				chain.add(path);
				//System.out.println(path);
				break;	
			}
			else if(hg[curr][i]==1&&visited[i]==0&&i!=par)
				DFS(i, curr, visited,path,chain);
		}
	}
	
	// to find the separating hurdle of a benign component
	HashMap<Integer, Integer> findSeperatingHurdle()
	{
		int start = 0;
		int mark[]=new int[componentList.size()];// marks for all the components
		//ArrayList<ArrayList<Integer>> L=new ArrayList<ArrayList<Integer>>(); // holds the list of benign component and its separating hurdle
		//ArrayList<Integer> temp = new ArrayList<Integer>(); // temporary variable to make an entry into L
		ArrayList<Integer> M=new ArrayList<Integer>();
		HashMap<Integer, Integer> L= new HashMap<Integer, Integer>();// holds the pair of benign component and its separating hurdle
		// creating labels for each edge
		String lab[]=new String[Edge.length];
		for(int i=0;i<Edge.length;i++)
		{
			Component comp=findComponentForEdge(i);
			//System.out.println("debug "+comp);
			if(comp.type>=0)
				lab[i]="B"+comp.id;
			else if(comp.subtype==0)
				lab[i]="N"+comp.id;
			else
				lab[i]="H"+comp.id;
		}
		for(int i=0;i<Edge.length;i++)
		{
			if(lab[i].startsWith("H"))
			{
				start=i;
				break;
			}
			
		}
		String currenth=lab[start];
		String comp;
		int intcomp;
		//for(int i= start+1;i<start+lab.length;i++)
		for(int i= start+1;i<lab.length;i++)
		{
			comp=lab[i];
			intcomp=Integer.parseInt(comp.substring(1));// integer part of comp
			if(comp.equals(currenth))
			{
				for(int c:M)
				{
					mark[c]=0;
					//temp.add(c);
					//temp.add(Integer.parseInt(currenth.substring(1)));
					//L.add(temp);
					L.put(c, Integer.parseInt(currenth.substring(1)));
				}
			}
			else if(comp.startsWith("H"))
			{
				currenth=comp;
				for(int c:M) // check in algorithm M same as list??????????????
					mark[c]=0;
			}
			else if(comp.startsWith("B")&&mark[intcomp]==0)
			{
				mark[intcomp]=1;
				M.add(intcomp);
			}
		}
		return L;
	}
	
	// to find the component for the particular edge
	Component findComponentForEdge(int e)
	{
		for(Component comp: componentList)
		{
			if(comp.edgeGroup.contains(e))
				return comp;
			else
				continue;
		}
		return null;
	}
	
	 void constructHurdleChain()
	 {
		 ArrayList<Integer> H= new ArrayList<Integer>(); // list of all the hurdles
		 int mark[]=new int[componentList.size()];
		 hurdleChain=new ArrayList<ArrayList<Component>>();
		 ArrayList<Component> temp= new ArrayList<Component>();
		 for(int i:unorientedComponents)
		 {
			 Component comp= componentList.get(i);
			 if(comp.subtype!=0)
				 H.add(i);
		 }
		 //for(int i:H)
		 int i=0;
		 while(i<hg.length)
		 {
		 	 if(H.contains(unorientedComponents.get(i))&&mark[i]==0)
		 	 {
		 	 	mark[i]=1;
			 	for(int j=0;j<hg.length;j++)
			 	{
				 	if(hg[i][j]==1&&mark[j]==0)
				 	{
					 	Component comp= componentList.get(unorientedComponents.get(j));
					 	if(comp.subtype!=0)
					 		temp.add(comp);
					 	else if(degree[j]==2&&inCycle[j]==0)
						 	temp.add(comp);
					 	else if(degree[j]>2&&inCycle[j]==1)
					 	{
						 	temp.add(comp);
						 	hurdleChain.add(temp);
						 	temp=new ArrayList<Component>();
						 	break;
					 	}
					 	else
					 	{
					 		i++;
					 		continue;
					 	}
					 	mark[j]=1;
					 	i=j;
					 	j=-1;
				 	}
				 }	
			 }
		 }
	 }
	
}

class Comp implements Comparator<TreeSet<Integer>>
{
    @Override
    public int compare(TreeSet<Integer> t1, TreeSet<Integer> t2) 
    {
    	Iterator<Integer>i1=t1.iterator();
    	Iterator<Integer>i2=t2.iterator();
		while(i1.hasNext()&&i2.hasNext())
		{
			int e1=i1.next();
			int e2=i2.next();
			if(e1!=e2)
			{
				return e1>e2?1:-1;
			}
		}
        return t1.size()>t2.size()?1:-1;
    }
}  
