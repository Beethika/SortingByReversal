/*package SortingByReversal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class Trace
{
	ArrayList<SortedSet<Integer>> i_seq;
	int count;
	
	public Trace() 
	{
		i_seq=new ArrayList<SortedSet<Integer>>();
		count=1;
	}
	
	@Override
	public String toString()
	{
		String str="Trace:";
		for(SortedSet<Integer> seq:i_seq)
		{
			for(int j:seq)
			{
				if(j==0)
					str+="\t";
				else
					str+=" "+seq;
				break;
			}
		}
		str+="\nSize: "+count;
		return str;
	}
	
	void sequenceInsert(SortedSet<Integer> curr_seq)
	{
		int start=0;
		int end=0;
		Iterator<SortedSet<Integer>> i1= i_seq.iterator();
		SortedSet<Integer> seq = null;
		TreeSet<Integer> dummy= new TreeSet<Integer>();
		dummy.add(0);
		int c=0;
		while(i1.hasNext())
		{
			seq=i1.next();
			for(int j:seq)
			{
				if(j==0)
				{
					if(c==0)
					{
						//place in lexicographic order before this
						end=i_seq.indexOf(seq);
						lexicoOrder(start,end,curr_seq);
						return;
					}
				}
				if(curr_seq.contains(j))
					c++;
			}
			if(c>0) // means some parts overlap
			{
				if(!seq.containsAll(curr_seq)&&!curr_seq.containsAll(seq))// checks for overlapping and not completely contained sequence once found check after the delimeter index if not found insert at end
				//if(c!=curr_seq.size()&&c!=seq.size())
				{
					while(true)
					{
						seq=i1.next();
						for(int j:seq)
						{
							if(j==0)
							{
								if(!i1.hasNext())
								{
									i_seq.add(curr_seq);
									i_seq.add(dummy);
									return;
								}
								start=i_seq.indexOf(seq)+1;
								c=0;	
							}
							break;
						}
						if(c==0)
							break;
					}
				}
				else
					c=0;
			}
		}
	}
	
	void lexicoOrder(int start, int end, SortedSet<Integer> source)
	{
		int i;
		for( i=start;i<end;i++)
		{
			SortedSet<Integer> target=i_seq.get(i);
			if(lexicographic(source, target)==1)
			{
				i_seq.add(i, source);
				return;
			}
		}
		i_seq.add(i, source);
		return;
	}

	int lexicographic(SortedSet<Integer> s,SortedSet<Integer> t)// returns 1 if source element is smaller and has to be appended before target element
	{
		Iterator<Integer>i1=s.iterator();
    	Iterator<Integer>i2=t.iterator();
		while(i1.hasNext()&&i2.hasNext())
		{
			int e1=i1.next();
			int e2=i2.next();
			if(e1!=e2)
			{
				return e1>e2?0:1;
			}
		}
        return s.size()>t.size()?0:1;
	}
}
*/
