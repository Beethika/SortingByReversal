package SortingByReversal;

import java.util.ArrayList;
import java.util.TreeSet;

public class CommonInterval 
{
	static ArrayList<ArrayList<Integer>> intervals;
	public CommonInterval()
	{
		intervals=new ArrayList<ArrayList<Integer>>();
	}
	
	public void commonIntervalDetection(int in[], int n)
	{

		ArrayList<Integer> range;
		int[] finalPer= new int[n];
		for(int i=0;i<n;i++)
			finalPer[i]=i+1;
		int l=n,u=-1,temp;
		for(int x=0;x<n-1;x++)
		{
			l=u=finalPer[Math.abs(in[x])-1];
			for(int y=x+1;y<n;y++)
			{
				temp=finalPer[Math.abs(in[y])-1];
				l=l<temp?l:temp;
				u=u>temp?u:temp;
				if(Debug.ON)
					System.out.println("l: "+l+" u: "+u);
				if((u-l-(y-x))==0)
				{
					range=new ArrayList<Integer>() ;
					
					for(int i=0;i<u-l+1;i++)
					{
							range.add(finalPer[i+l-1]);
					}

					if(u-l+1!=in.length)
					{
						if(Debug.ON)
							System.out.print("\nCommon Interval :"+range);
						intervals.add(range);
						//return;
					}
						
				}
			}
		}
	}
	static int filter(TreeSet<Integer> seq)
	{
		for(ArrayList<Integer> range:intervals)
		{
			if(!range.containsAll(seq))
			{
				for(int s:seq)
				{
					if(range.contains(s)&&!seq.containsAll(range))
						return 1; // ovelapping sequence with common interval
				}
			}
		} 
		return 0;
	}
	/*public static void main(String ar[])
	{
		int in[]={-5,-2,-7,4,-8,3,6,-1};
		CommonInterval ci= new CommonInterval();
		ci.commonIntervalDetection(in, in.length);
		for(int i=0;i<ci.range.length;i++)
			System.out.print(" "+ci.range[i]);
	}*/
}
