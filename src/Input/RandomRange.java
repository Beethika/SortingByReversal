package Input;

import java.util.Random;

import SortingByReversal.*;

/** Generate random integers in a certain range. */
public final class RandomRange 
{
  
    static int START = 1;
    static int END = 18;
    static int DISTANCE=9;
    static int RANGE=10;
    private static final long MEGABYTE = 1024L * 1024L;

	  public static long bytesToMegabytes(long bytes)
	  {
	    return bytes / MEGABYTE;
	  }
  public static void main(String... aArgs)
  {
    log("Generating random integers in the range "+(START)+".. "+(END)+" at distance "+DISTANCE+"\n");
    
    int [][]input=new int[RANGE][END];
    Random random = new Random();
    //find out the cut between which to reverse the elements 
    for(int x=0;x<RANGE;x++)
    {	
        int per[]=new int[END];
        for(int i= START-1;i<END;i++)
        	per[i]=i+1;
    	int c1= showRandomInteger(START-1, END-1, random);
    	int c2= showRandomInteger(START-1, END-1, random);
    	int min=c1<c2?c1:c2;
    	int max=c1>c2?c1:c2;
    	//log(min+" "+max);
    	int j=max;
    	int t;
    	for(int i=min;i<=(min+max)/2;i++) // moves till half length between min and max index along with swapping the elements at index i and j with sign
    	{
    		t=per[i];
    		per[i]=-1*per[j];
    		per[j]=-1*t;
    		j--;
    	}
    	input[x]=per;
    }
  
    for(int dist=1;dist<DISTANCE;dist++)
    {
    	generatingInput(input, dist);
    }
    for(int i=0;i<RANGE;i++)
    {
    	for(int j=0;j<END;j++)
    	{
    		log(input[i][j]+" ");
    	}
    	log("\n");
    }
    generator(input);
  }
  public static void generatingInput(int[][]input,int dist)
  {
	  Random random = new Random();
	  for(int x=0;x<RANGE;x++)
	  {
		  int per[]=input[x];
		  int min= showRandomInteger(START-1, END-1, random);
		  int max= showRandomInteger(START-1, END-1, random);
		  int j=max;
		  int t;
		  for(int i=min;i<=(min+max)/2;i++) // moves till half length between min and max index along with swapping the elements at index i and j with sign
		  {
			  t=per[i];
			  per[i]=-1*per[j];
			  per[j]=-1*t;
			  j--;
		  }
		  int in[]=permutationExpand(per, END); 
		  Graph g= new Graph(in,in.length,-1);
		  if(g.d>dist)
		  {
			  input[x]=per;
		  }
		  else
			  x--;
	  }
  }
  public static int showRandomInteger(int aStart, int aEnd, Random aRandom){
	    if (aStart > aEnd) {
	      throw new IllegalArgumentException("Start cannot exceed End.");
	    }
	    //get the range, casting to long to avoid overflow problems
	    long range = (long)aEnd - (long)aStart + 1;
	    // compute a fraction of the range, 0 <= frac < range
	    long fraction = (long)(range * aRandom.nextDouble());
	    int randomNumber =  (int)(fraction + aStart);    
	    //log("Generated : " + randomNumber);
	    return randomNumber;
	  }
//expands the permutation to return sequence that could be fed to form breakpoint graph
	public static int[] permutationExpand(int in[],int n)
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
	
	public static void generator(int input[][])
	{
		long traceCount[]= new long[RANGE];
		long allSolution[]= new long[RANGE];
		long timeCountBFS[]= new long[RANGE];
		long timeCountBFSOT[]= new long[RANGE];
		long timeCountDFSOT[]= new long[RANGE];
		long memoryBFS[]= new long[RANGE];
		long memoryBFSOT[]= new long[RANGE];
		long memoryDFSOT[]= new long[RANGE];
		long startTime,endTime,totalTime,total,free,memory;
		int flagb =0, flagd=0,flagcb=0;
		for(int i=0;i<RANGE;i++)
		{
			int in[]=input[i];
			if(CI.ON)
			{
				CommonInterval comint=new CommonInterval();
				comint.commonIntervalDetection(in, END);
			}
			
		    Runtime runtime = Runtime.getRuntime();	 // Get the Java runtime object 
		    runtime.gc();	     // Run the garbage collector
		    if(flagcb==0)
			try
			{
		    System.out.println("\nUsing BFS");
			startTime = System.currentTimeMillis();
			BreadthFirstApproach b=new BreadthFirstApproach(in);
			b.sortingByReversal();
			//b.display_i_trace();
			//long a1=b.i_trace.size();
			endTime   = System.currentTimeMillis();
			totalTime = endTime - startTime;
			System.out.println("BFS ALL SOLUTION Time in milliseconds: "+totalTime);	
			timeCountBFS[i]=totalTime;
			total= runtime.totalMemory();
			free=runtime.freeMemory();
			memory = total-free;
			memoryBFS[i]=memory;
			allSolution[i]=b.sum;
			System.out.println("Total memory: "+total+"\tFree memory: "+free+"\tUsed memory in bytes: "+memory);
		    //System.out.println("Used memory is megabytes: " + bytesToMegabytes(memory));*/
			}
			catch(OutOfMemoryError e)
			{
				flagcb=1;
			}
		    
			runtime.gc();
			if(flagb==0)
			try
			{
				System.out.println("\nUsing BFS");
			startTime = System.currentTimeMillis();
			BreadthFirstApproach b1=new BreadthFirstApproach(in);
			b1.sortingByReversalOnlyTraces();
			//b.display_i_trace();
			//long a2=b1.i_trace.size();
			endTime   = System.currentTimeMillis();
			totalTime = endTime - startTime;
			System.out.println("BFS ONLY TRACE Time in milliseconds: "+totalTime);
			timeCountBFSOT[i]=totalTime;
			total= runtime.totalMemory();
			free=runtime.freeMemory();
			memory = total-free;
			memoryBFSOT[i]=memory;
			b1=null;
			System.out.println("Total memory: "+total+"\tFree memory: "+free+"\tUsed memory in bytes: "+memory);
			}
			catch(OutOfMemoryError e)
			{
				flagb=1;
			}
			
			runtime.gc();
			if(flagd==0)
			try
			{
			System.out.println("\nUsing DFS");
			startTime = System.currentTimeMillis();
			DepthFirstApproach d=new DepthFirstApproach(in);
			d.sortingByReversal();
			//d.display_i_trace();
			//long a3=d.i_trace.size();
			endTime   = System.currentTimeMillis();
			totalTime = endTime - startTime;
			System.out.println("DFS ONLY TRACE Time in milliseconds: "+totalTime+"\n");
			timeCountDFSOT[i]=totalTime;
			total= runtime.totalMemory();
			free=runtime.freeMemory();
			memory = total-free;
			memoryDFSOT[i]=memory;
			traceCount[i]=d.i_trace.size();
			d=null;
			runtime.gc();
			System.out.println("Total memory: "+total+"\tFree memory: "+free+"\tUsed memory in bytes: "+memory);
			System.out.println("Total trace: "+traceCount[i]);
			}
			catch(OutOfMemoryError e)
			{
				flagd=1;
			}
			//to compare trace size with the BFS solution
			/*if(a1==a2&&a2==a3)
				traceCount[i]=a1;
			else
			{
				log("there is a problem \nInput Sequence \n");
				for(int j=0;j<END;j++)
		    	{
		    		log(input[i][j]+" ");
		    	}
		    	log("\n");
				log("BFS All Solutions");
				b.display_i_trace();
				log("BFS All Traces");
				b1.display_i_trace();
				break;
			}*/
				
		}
		double totalmemoryBFS=0;
		double totalmemoryBFSOT=0;
		double totalmemoryDFSOT=0;
		double totaltimeBFS=0;
		double totaltimeBFSOT=0;
		double totaltimeDFSOT=0;
		double totaltrace=0;
		double totalsolution=0;
		double frac=(double)1/(double)RANGE;
		for(int i=0;i<RANGE;i++)
		{
			
			//totalmemoryBFS+=(double)(memoryBFS[i]*frac); 
			if(flagcb==0)
			{
				totalmemoryBFS+=(double)(memoryBFS[i]*frac); 
				totaltimeBFS+=(double)(timeCountBFS[i]*frac);
				totalsolution+=(double)(allSolution[i]*frac);
			}
			if(flagb==0)
			{
				totalmemoryBFSOT+=(double)(memoryBFSOT[i]*frac); 
				totaltimeBFSOT+=(double)(timeCountBFSOT[i]*frac);
			}
			if(flagd==0)
			{
				totalmemoryDFSOT+=(double)(memoryDFSOT[i]*frac); 
				totaltimeDFSOT+=(double)(timeCountDFSOT[i]*frac);
				totaltrace+=(double)(traceCount[i]*frac);
			}
			
			//log(" "+timeCountBFS[i]);
			//totaltimeBFS+=(double)(timeCountBFS[i]*.01); 
		}
		if(flagcb==1)
			log("\nBFS fails for this case");
		else
			log("\nAvg BFS Memory in bytes "+totalmemoryBFS+"\nAvg BFS Time in seconds "+(totaltimeBFS*0.001));
		if(flagb==1)
			log("\nBFS fails for this case");
		else
			log("\nAvg BFS ONly Traces Memory in bytes "+totalmemoryBFSOT+"\nAvg BFS Only Traces Time in seconds "+(totaltimeBFSOT*0.001));
		if(flagd==1)
			log("\nDFS fails for this case");
		else
		{
			log("\nAvg DFS ONly Traces Memory in bytes "+totalmemoryDFSOT+"\nAvg DFS Only Traces Time in seconds "+(totaltimeDFSOT*0.001));
			log("\nAvg Solution "+totalsolution);
			log("\nAvg Traces "+totaltrace);
		}
			 
		//log("Avg BFS Memory in bytes "+totalmemoryBFS+*/"\nAvg BFS ONly Traces Memory in bytes "+totalmemoryBFSOT+"\nAvg DFS Only Traces Memory in bytes "+totalmemoryDFSOT+"\n");
		//log(/*"Avg BFS Time in seconds "+(totaltimeBFS*0.001)+*/"\nAvg BFS Only Traces Time in seconds "+(totaltimeBFSOT*0.001)+"\nAvg DFS Only Traces Time in seconds "+(totaltimeDFSOT*0.001)+"\n");
		
	}
  
	  private static void log(String aMessage){
	    System.out.print(aMessage);
	  }
}
