package SortingByReversal;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class BreadthFirstApproach 
{
	int permutation[]; // initial permutation
	public ArrayList<Trace> i_trace; //holds the list of traces present
	PrintWriter out;
	BreadthFirstApproach(){}
	public int sum;
	public BreadthFirstApproach(int seq[])
	{		
		this.permutation=seq;
		try {
			out = new PrintWriter(new FileWriter("output.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sortingByReversal() 
	{
		int per[]=new int[permutation.length];
		for(int i=0;i<permutation.length;i++)
			per[i]=permutation[i]; // initial permutation
		int in[]=this.permutationExpand(per, per.length); // expands the permutation i.e. 2i and 2i+1 to form the Breakpoint Graph
		Graph g=new Graph(in,in.length,-1);
		if(g.unorientedComponents.isEmpty())
			Hurdle.ON=false;
		System.out.println("distance is:"+g.d);
		i_trace=new ArrayList<Trace>();
		TreeSet<Integer> dummy= new TreeSet<Integer>(); //for flagging between different sub-words
		dummy.add(0);
		for(TreeSet<Integer> t: g.node) // in 1-trace the nodes are directly inserted
		{
			Trace trace= new Trace();
			trace.i_seq.add(dummy);
			trace.i_seq.add(t);
			trace.i_seq.add(dummy);
			//System.out.println("traceeeeeeeee"+trace);
			i_trace.add(trace);
		}
		//System.exit(0);
		
		Graph gnew=g;		
		for(int j=1;j<g.d;j++) // the traces are calculated distance times
		//while(gnew.d>0)
		{
			
			if(Debug.ON)
				System.out.println("-----------------------j is "+j+"-----------------------------");
				//System.out.println("Cycle Description: "); gnew.displayCycle();
				//System.out.println("Component Description:"); gnew.displayComponent();
			ArrayList<Trace> new_i_trace= new ArrayList<Trace>();
			
			//the reversals of the previous traces are applied on the initial permutation one by one and on the basis of it new trace is formed
			for(Trace seq:i_trace) // loop on set of traces to evaluate upon each trace
			{
				for(int i=0;i<permutation.length;i++)
					per[i]=permutation[i]; // initial permutation
				if(Debug.ON)
				{
					System.out.print("\ninitial permutation is:");
					for(int i: permutation)
						System.out.print(" "+i);
					System.out.println();
				}
				for(SortedSet<Integer> rev: seq.i_seq) // reversals from i_sequences are worked upon 1 by 1
				{
					int flag=0;
					for(int i:rev) // to avoid reversal with dummy variable
						if(i==0)
						{
							flag=1;
							break;
						}
					if(flag==1)
						continue;
					per=this.locateRange(rev, per);	// apply reversal				
				}
				in=this.permutationExpand(per, per.length); // expands the permutation i.e. 2i and 2i+1 to form the Breakpoint Graph
				gnew=new Graph(in,in.length,g.d-j+1); // new graph is formed with the modified permutation
				for(TreeSet<Integer> t: gnew.node)
				{
					Trace seq1= new Trace();// new trace i.e. i+1-trace formed from i-trace
					for(SortedSet<Integer> s:seq.i_seq)
						seq1.i_seq.add(s);
					seq1.count=seq.count;
					seq1.sequenceInsert(t); // inserted in normal form order
					//System.out.println("\n i-sequence"+seq1);
					int flag=0;
					int i=0;
					// to check new trace formed already exists or not, if exists then increase count of trace otherwise add it to the set of trace 
					for(Trace temp:new_i_trace)
					{
						if(temp.i_seq.containsAll(seq1.i_seq)&&(temp.i_seq.equals(seq1.i_seq)))
						{
							i=new_i_trace.indexOf(temp);
							flag=1;
							break;
						}
					}
					// when trace already exists in the list of traces
					if(flag==1)
					{
						//int i=new_i_trace.indexOf(seq1);
						Trace temp=new_i_trace.get(i);
						temp.count+=seq.count; // increase the count of trace
						new_i_trace.set(i, temp); // change the trace to temp which has modified count value
						if(Debug.ON)
							System.out.println("\n modified i-sequence "+temp);
						flag=0;
					}
						// when trace does not exists just add it to the set of traces
					else
					{
						if(Debug.ON)
							System.out.println("\n new i-sequence "+seq1);
						new_i_trace.add(seq1);
					}				
				}			
			}
			if(!new_i_trace.isEmpty())
				i_trace=new_i_trace;
			if(Debug.ON)
				this.display_i_trace();
		}
		this.display_i_trace();
		
	}
	
	public void sortingByReversalOnlyTraces() 
	{
		int per[]=new int[permutation.length];
		for(int i=0;i<permutation.length;i++)
			per[i]=permutation[i]; // initial permutation
		int in[]=this.permutationExpand(per, per.length); // expands the permutation i.e. 2i and 2i+1 to form the Breakpoint Graph
		Graph g=new Graph(in,in.length,-1);
		if(g.unorientedComponents.isEmpty())
			Hurdle.ON=false;
		//System.out.println("distance is:"+g.d);
		i_trace=new ArrayList<Trace>();
		TreeSet<Integer> dummy= new TreeSet<Integer>(); //for flagging between different sub-words
		dummy.add(0);
		for(TreeSet<Integer> t: g.node) // in 1-trace the nodes are directly inserted
		{
			Trace trace= new Trace();
			trace.i_seq.add(dummy);
			trace.i_seq.add(t);
			trace.i_seq.add(dummy);
			//System.out.println("traceeeeeeeee"+trace);
			i_trace.add(trace);
		}
		//System.exit(0);
		
		Graph gnew=g;		
		for(int j=1;j<g.d;j++) // the traces are calculated distance times
		//while(gnew.d>0)
		{
			
			if(Debug.ON)
				System.out.println("-----------------------j is "+j+"-----------------------------");
				//System.out.println("Cycle Description: "); gnew.displayCycle();
				//System.out.println("Component Description:"); gnew.displayComponent();
			ArrayList<Trace> new_i_trace= new ArrayList<Trace>();
			
			//the reversals of the previous traces are applied on the initial permutation one by one and on the basis of it new trace is formed
			for(Trace seq:i_trace) // loop on set of traces to evaluate upon each trace
			{
				for(int i=0;i<permutation.length;i++)
					per[i]=permutation[i]; // initial permutation
				/*if(Debug.ON)
				{
					System.out.print("\ninitial permutation is:");
					for(int i: permutation)
						System.out.print(" "+i);
					System.out.println();
				}*/
				for(SortedSet<Integer> rev: seq.i_seq) // reversals from i_sequences are worked upon 1 by 1
				{
					int flag=0;
					for(int i:rev) // to avoid reversal with dummy variable
						if(i==0)
						{
							flag=1;
							break;
						}
					if(flag==1)
						continue;
					per=this.locateRange(rev, per);	// apply reversal				
				}
				in=this.permutationExpand(per, per.length); // expands the permutation i.e. 2i and 2i+1 to form the Breakpoint Graph
				gnew=new Graph(in,in.length,g.d-j+1); // new graph is formed with the modified permutation
				for(TreeSet<Integer> t: gnew.node)
				{
					Trace seq1= new Trace();// new trace i.e. i+1-trace formed from i-trace
					for(SortedSet<Integer> s:seq.i_seq)
						seq1.i_seq.add(s);
					seq1.count=seq.count;
					int position=seq1.sequenceInsert(t); // inserted in normal form order
					//System.out.println("\n i-sequence"+seq1);
					if(position==0) // inserted at the end
					{
						/*int flag=0;
					//int i=0;
					// to check new trace formed already exists or not, if exists then increase count of trace otherwise add it to the set of trace 
					for(Trace temp:new_i_trace)
					{
						if(temp.i_seq.containsAll(seq1.i_seq)&&(temp.i_seq.equals(seq1.i_seq)))
						{
							//i=new_i_trace.indexOf(temp);
							flag=1;
							break;
						}
					}
					// when trace already exists in the list of traces
					/*if(flag==1)
					{
						int i=new_i_trace.indexOf(seq1);
						Trace temp=new_i_trace.get(i);
						temp.count+=seq.count; // increase the count of trace
						new_i_trace.set(i, temp); // change the trace to temp which has modified count value
						if(Debug.ON)
							System.out.println("\n modified i-sequence "+temp);
						flag=0;
					}
						// when trace does not exists just add it to the set of traces
					else*/
					//if(flag!=1)// add only when trace does not exists in the list
					{
						if(Debug.ON)
							System.out.println("\n new i-sequence "+seq1);
						new_i_trace.add(seq1);
					}
					}
					
				}			
			}
			if(!new_i_trace.isEmpty())
				i_trace=new_i_trace;
			if(Debug.ON)
				this.display_i_trace();
		}
		
	}
	// locates the sequence to be reversed in the permutation and reverses it along with flipping the sign
	int[] locateRange(SortedSet<Integer> seq,int per[])
	{
		Integer[] s=new Integer[seq.size()];
		int i1=-1;
		for(Integer in:seq)
			s[++i1]=in;
		if(Debug.ON)
		{
			System.out.print("permutation s is:");
			for(int i: s)
				System.out.print(" "+i);
		}
		int min=per.length,max=0;// holds  the min and max index of the sequence in the permutation 
		for(int i=0;i<s.length;i++)
		{
			for(int j=0;j<per.length;j++)
			{
				if(Math.abs(per[j])==s[i])
				{
					if(min>j)
						min=j;
					if(max<j)
						max=j;
				}
			}
		}
		int j=max;
		int t;
		for(int i=min;i<=(min+max)/2;i++) // moves till half length between min and max index along with swapping the elements at index i and j with sign
		{
			t=per[i];
			per[i]=-1*per[j];
			per[j]=-1*t;
			j--;
		}
		/*if(Debug.ON)
		{
			System.out.println();
		
			System.out.print("permutation per is:");
			for(int i: per)
				System.out.print(" "+i);
			System.out.println();
		
		}*/
		return per;
	}
	
	/*public void display_i_trace()
	{
		System.out.println("\nFinal trace is:");
		int sum=0;
		for(Trace t: i_trace)
		{
			System.out.println(t);
			sum+=t.count;
		}
		System.out.println("Total no of solution is:"+sum);
		System.out.println("Total no of traces is:"+i_trace.size());
	}*/
	public void display_i_trace()
	{
		//System.out.println("\nFinal trace is:");
		sum=0;
		for(Trace t: i_trace)
		{
			System.out.println(t);
			sum+=t.count;
		}
		//System.out.println("Total no of solution is:"+sum);
		//System.out.println("Total no of traces is:"+i_trace.size());
	}
	
	// expands the permutation to return sequence that could be fed to form breakpoint graph
	int[] permutationExpand(int in[],int n)
	{
		int i;
		int seq[]=new int[2*n+2];
		seq[0]=0;
		seq[2*n+1]=2*n+1;
		int j=1;
		for(i=0;i<n;i++)
		{
			if(in[i]<0)
			{
				seq[j++]=2*Math.abs(in[i]);
				seq[j++]=2*Math.abs(in[i])-1;
			}
			else
			{
				seq[j++]=2*in[i]-1;
				seq[j++]=2*in[i];
			}
		}
		return seq;
	}
}
