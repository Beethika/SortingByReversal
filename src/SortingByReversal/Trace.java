package SortingByReversal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class Trace // trace is represented as set of sequence and its count
{
	ArrayList<SortedSet<Integer>> i_seq;// holds the sequence of subwords in each trace
	int count;// holds the count of the trace when repeated solutions
	
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
	
	int sequenceInsert(SortedSet<Integer> curr_seq)
	{
		int start=0;
		int end=0;
		Iterator<SortedSet<Integer>> i1= i_seq.listIterator();
		SortedSet<Integer> seq = null;
		//ArrayList<SortedSet<Integer>> tail = null;
		TreeSet<Integer> dummy= new TreeSet<Integer>(); //for flagging between different sub-word of trace
		dummy.add(0);
		int index = 0; // holds the max index of the sub-word with which curr_seq overlaps  
		while(i1.hasNext()) //iterates over each reversal in the current trace
		{
			seq=i1.next(); 
			for(int j:seq) // iterates over each integer in the current reversal_sequence
			{
				if(curr_seq.contains(j)) // check for overlapping
				{
					if(!seq.containsAll(curr_seq)&&!curr_seq.containsAll(seq))// checks for overlapping and not completely contained sequence once found check after the delimeter index if not found insert at end
					{
						index=i_seq.indexOf(seq); // finds out the last index with which overlapped
						//tail=(ArrayList<SortedSet<Integer>>) i_seq.subList(i_seq.indexOf(seq), i_seq.size());
					}
				}
					
			}
		}
		int c=0;
		Iterator<SortedSet<Integer>> i2=i_seq.listIterator(index); // iterates from the position where the index was found 
		while(i2.hasNext())
		{
			seq=i2.next();
			//if(Debug.ON)
				//System.out.println("sequence within which curr seq to be inserted "+seq);
			// finds out the start and end index of the subword in which curr_seq has to be inserted 
			for(int j:seq)
			{
				// start end is marked with the help of the dummy variable whose position can easily be located
				if(j==0)
				{
					if(c==0) // when first dummy encountered marks the start
					{
						if(start==0)
							start=index; 
						else
							start=index+1;
						//if(Debug.ON)
							//System.out.println("start is "+start);
						c++;
					}
					else // when second dummy encountered marks the end
					{
						end=index+1;
						//if(Debug.ON)
							//System.out.println("end is "+end);
						if(lexicoOrder(start+1,end,curr_seq)==1) // for placing curr_seq between start and end index
							return 1; // when appended somewhere in middle
						else
						{
							int li=i_seq.lastIndexOf(dummy); // to check the last {0} encountered 
							//System.out.println("end is "+end+ " li is "+li);
							if(end+1==li)
								return 0; // when appended at the end of last subword
							else
								return -1; // when appended at the end of subword so not the end of trace 
						}
							
					}
				}
				index=i_seq.indexOf(seq);
			}
		}
		// when second dummy not encountered then it was the last sub-word so a new sub-word is added in the end along with dummy
		i_seq.add(curr_seq);
		i_seq.add(dummy);
		return 0; // when appended at the end
	}
	
	//places the source sequence at its correct position in the target sequence
	int lexicoOrder(int start, int end, SortedSet<Integer> source)
	{
		int i;
		// finds out the exact location on the basis of lexicographic ordering between start and end
		for( i=start;i<end;i++)
		{
			SortedSet<Integer> target=i_seq.get(i);
			if(lexicographic(source, target)==1) // when the target sequence becomes larger than source sequence means has to be inserted before it 
			{
				i_seq.add(i, source);
				return 1; // when appended somewhere in middle
			}
		}
		// when none becomes larger than source then its is appended at the end
		i_seq.add(i, source);
		return 0; // when appended at the end
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

