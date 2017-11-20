package SortingByReversal;

import java.util.Scanner;

public class GeneratorClass
{
	 private static final long MEGABYTE = 1024L * 1024L;

	  public static long bytesToMegabytes(long bytes)
	  {
	    return bytes / MEGABYTE;
	  }
	  
	public static void main(String[] args) 
	{
		int n;
		Scanner sc= new Scanner(System.in);
		System.out.println("Enter no of gene in chromosome");
		n=sc.nextInt();
		int in[]=new int[n];
		System.out.println("Enter gene sequence of chromosome");
		int i;
		for(i=0;i<n;i++)
			in[i]=sc.nextInt();
		if(CI.ON)
		{
			CommonInterval comint=new CommonInterval();
			comint.commonIntervalDetection(in, n);
		}
		
	    Runtime runtime = Runtime.getRuntime();	 // Get the Java runtime object 
	    runtime.gc();	     // Run the garbage collector
		System.out.println("\nUsing BFS");
		long startTime = System.currentTimeMillis();
		BreadthFirstApproach b=new BreadthFirstApproach(in);
		b.sortingByReversal();
		b.display_i_trace();
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("ALL SOLUTION Time in milliseconds: "+totalTime);		
		long total= runtime.totalMemory();
		long free=runtime.freeMemory();
		long memory = total-free;
		System.out.println("Total memory: "+total+"\tFree memory: "+free+"\tUsed memory in bytes: "+memory);
	    System.out.println("Used memory is megabytes: " + bytesToMegabytes(memory));
	    
		runtime.gc();
	    //BreadthFirstApproach b=new BreadthFirstApproach(in);
		startTime = System.currentTimeMillis();
		b.sortingByReversalOnlyTraces();
		b.display_i_trace();
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("ONLY TRACE Time in milliseconds: "+totalTime);
		total= runtime.totalMemory();
		free=runtime.freeMemory();
		memory = total-free;
		System.out.println("Total memory: "+total+"\tFree memory: "+free+"\tUsed memory in bytes: "+memory);
		
		runtime.gc();
		free=runtime.freeMemory();
		System.out.println("free mem after bfs "+free);
		System.out.println("\nUsing DFS");
		startTime = System.currentTimeMillis();
		DepthFirstApproach d=new DepthFirstApproach(in);
		d.sortingByReversal();
		d.display_i_trace();
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("ONLY TRACE Time in milliseconds: "+totalTime);
		total= runtime.totalMemory();
		free=runtime.freeMemory();
		memory = total-free;
		System.out.println("Total memory: "+total+"\tFree memory: "+free+"\tUsed memory in bytes: "+memory);

	}

}
