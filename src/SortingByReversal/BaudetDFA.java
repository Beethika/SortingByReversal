package SortingByReversal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class BaudetDFA
{
	int permutation[]; // initial permutation
	public ArrayList<Trace> i_trace; //holds the list of traces present
	Graph g;
	//Runtime r;
	BaudetDFA(){}
	
	public BaudetDFA(int seq[])
	{		
		this.permutation=seq;
	}
	
	public void sortingByReversal()
	{
		int per[]=new int[permutation.length];
		for(int i=0;i<permutation.length;i++)
			per[i]=permutation[i]; // initial permutation
		int in[]=this.permutationExpand(per, per.length); // expands the permutation i.e. 2i and 2i+1 to form the Breakpoint Graph
		g=new Graph(in,in.length,-1);
		if(g.unorientedComponents.isEmpty())
			Hurdle.ON=false;
		//System.out.println("distance is:"+g.d);
		i_trace=new ArrayList<Trace>();
		TreeSet<Integer> dummy= new TreeSet<Integer>(); //for flagging between different sub-words
		dummy.add(0);
		for(TreeSet<Integer> t: g.node) // in 1-trace the nodes are directly inserted
		{
			//long total=Runtime.getRuntime().totalMemory();
			//long free=Runtime.getRuntime().freeMemory();
			//System.out.println("Total memory: "+total+"\tFree memory: "+free+"\tUsed memory: "+(total-free));
			Trace trace= new Trace();
			trace.i_seq.add(dummy);
			trace.i_seq.add(t);
			trace.i_seq.add(dummy);
			//System.out.println("traceeeeeeeee"+trace);
			//i_trace.add(trace);
			for(int i=0;i<permutation.length;i++)
				per[i]=permutation[i]; // initial permutation
			per=this.locateRange(t, per);	// applying the reversal
			branchExpand(per, trace,g.d);
		}
	}
	
	void branchExpand(int[] per, Trace trace, int dist)
	{
		int per1[]=new int[per.length];
		for(int i=0;i<per.length;i++)
			per1[i]=per[i];
		if(Debug.ON)
		{
			System.out.print("\ninitial permutation is:");
			for(int i: per1)
				System.out.print(" "+i);
			System.out.println();
		}
		//per1=this.locateRange(rev, per1);	// applying the reversal				
		int in[]=this.permutationExpand(per1, per1.length); // expands the permutation i.e. 2i and 2i+1 to form the Breakpoint Graph
		Graph gnew=g;
		gnew=new Graph(in,in.length,dist); // new graph is formed with the modified permutation
		dist=gnew.d;
		if(dist==0)
		{
			/*//int i = 0,flag=0;
			for(Trace temp:i_trace)
			{										
				if(temp.i_seq.equals(trace.i_seq))
				{
					//i=i_trace.indexOf(temp);
					//flag=1;
					//break;
					return;
				}
			}
				// when trace already exists in the list of traces
			if(flag==1)
			{
				Trace temp=i_trace.get(i);
				temp.count+=trace.count; // increase the count of trace
				i_trace.set(i, temp); // change the trace to temp which has modified count value
				if(Debug.ON)
					System.out.println("\n modified i-sequence "+temp);
				flag=0;
				return;
			}*/
				
			if(Debug.ON)
				System.out.println("\n new i-sequence "+trace);
			i_trace.add(trace);
			Runtime.getRuntime().gc();
			return;
		}	
		Iterator<TreeSet<Integer>> i1=gnew.node.iterator();
		while(i1.hasNext())
		{
			TreeSet<Integer> t=i1.next();
			Trace seq1= new Trace();// new trace i.e. i+1-trace formed from i-trace
			for(SortedSet<Integer> s:trace.i_seq)
				seq1.i_seq.add(s);
			seq1.count=trace.count;
			int position=seq1.sequenceInsert(t);
			//System.out.println("traaaacccceee "+seq1);
			//System.out.println("\n i-sequence"+seq1);
			//int flag=0;
			//int i=0;
			if(position==0) // inserted at the end so can be expanded further
			{
				int per2[]=new int[per.length];
				for(int i=0;i<per.length;i++)
					per2[i]=per1[i];
				per2=this.locateRange(t, per2);
				branchExpand(per2, seq1,dist);
			}
			/*else // inserted mid way no need to expand just increase the count
			{
				// find the already existing trace and increase count of trace , but if does not exists then add it to i_trace
				for(Trace temp:i_trace)
				{
					if(temp.i_seq.containsAll(seq1.i_seq)&&(temp.i_seq.equals(seq1.i_seq)))
					{
						i=i_trace.indexOf(temp);
						flag=1;
						break;
					}
				}
				// when trace already exists in the list of traces
				if(flag==1)
				{
					//int i=new_i_trace.indexOf(seq1);
					Trace temp=i_trace.get(i);
					temp.count+=trace.count; // increase the count of trace
					i_trace.set(i, temp); // change the trace to temp which has modified count value
					if(Debug.ON)
						System.out.println("\n modified i-sequence "+temp);
					flag=0;
					//return;
				}
				// when trace does not exists just continue expanding it
				else
				{
					/*if(Debug.ON)
						System.out.println("\n new i-sequence "+seq1);
					//i_trace.add(trace);
					return;*/
					//branchExpand(per1, t, seq1,dist);
					//continue;
				//}
			//}
		}
		Runtime.getRuntime().gc();
	}
	
	
	// locates the sequence to be reversed in the permutation and reverses it along with flipping the sign
	int[] locateRange(SortedSet<Integer> seq,int per[])
	{
		if(Debug.ON)
		{
			System.out.println();
		
			System.out.print("inintial permutation per is:");
			for(int i: per)
				System.out.print(" "+i);
			System.out.println();
		
		}
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
		if(Debug.ON)
		{
			System.out.println();
		
			System.out.print("permutation per is:");
			for(int i: per)
				System.out.print(" "+i);
			System.out.println();
		
		}
		return per;
	}
	
	public void display_i_trace()
	{
		//System.out.println("\nFinal trace is:");
		int sum=0;
		for(Trace t: i_trace)
		{
			System.out.println(t);
			sum+=t.count;
		}
		System.out.println("Total no of solution is:"+sum);
		System.out.println("Total no of traces is:"+i_trace.size());
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

