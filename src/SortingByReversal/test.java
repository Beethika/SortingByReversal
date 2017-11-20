package SortingByReversal;

import java.util.ArrayList;

public class test
{
	ArrayList<String> chain;
	int inCycle[];
	//int hg[][]={{0,1,0,0,0,0,0},{1,0,1,0,0,1,1},{0,1,0,1,0,1,0},{0,0,1,0,1,0,0},{0,0,0,1,0,0,0},{0,1,1,0,0,0,0},{0,1,0,0,0,0,0}};;
	int hg[][]={{0,1,1,1},{1,0,1,1},{1,1,0,1},{1,1,1,0}};
	int n=4;
	public static void main(String[] args) 
	{
		test t=new test();
		t.findCycleinGraph();
		t.inCycle=new int[t.n];
		for(String i:t.chain)
		{
			//System.out.println(i);
			char c=i.charAt(i.length()-1);
			int in=i.indexOf(c);
			if(in!=i.length()-1)
			{
				for(int x=in;x<=i.length()-2;x++)
				{
					t.inCycle[i.charAt(x)-'0']=1;
				}
			}
		}
		for(int i:t.inCycle)
			System.out.println(i);
		
	}
	void findCycleinGraph()
	{
		chain=new ArrayList<String>();
		int visited[]= new int[n];
		DFS(0,-1,visited,"");
	}
		
	void DFS(int curr, int par, int visited[],String path)
	{
		path=path+curr;
		visited[curr]=1;
		//for(int x:visited)
			//System.out.print(x);
		System.out.println();
		for(int i=0;i<n;i++)
		{
			if(hg[curr][i]==1&&visited[i]==1&&i!=par)
			{
				path+=i;
				chain.add(path);
				break;
				//System.out.println(path);
				
			}
			else if(hg[curr][i]==1&&visited[i]==0&&i!=par)
				DFS(i, curr, visited,path);
		}
	}
}
