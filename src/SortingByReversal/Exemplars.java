package SortingByReversal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Exemplars {

	int n;
	int in[];
	int s[][];
	int check_Dup_Set(int setCount, int eleCount)
	{
	    int i, j,count;
	    for(i=0;i<setCount;i++)
	    {
	     count =0;
	     for(j=0;j<eleCount;j++)
	     {
	       if(s[i][j]==s[setCount][j])
	       {
	        count++;
	       }
	     }
	     if(count==eleCount)
	     return 1;
	    }
	return (0);
	}


	void permute(int count, int setNo,int q,int r)
	{
		int flag=1,index = 0,i,j,k,DupFlag=0;
		
	    if(count!=n)
	    {
	        for(i=r;i<n;i++)
	        {
	            //check for duplicates
	            for(j=0;j<q;j++)
	            {
	                if(s[setNo][j]==in[i])
	                {
	                    flag=0;
	                    index=j;
	                    break;
	                }
	            }
	            
	            if(flag==1) // no duplicate found till now
	            {
	                s[setNo][q]=in[i];
	                permute(count+1,setNo,q+1,i+1);
	            }
	            else // the current element is already present in the array
	            {
	                // call permute to exclude current element
	                permute(count+1,setNo,q,i+1);

	                //call permute to include current element and exclude previous
	                for(k=0;k<q;k++)
	                {
	                    if(k<index) // for all the elements till the index of element already present just copy the elements
	                    {
	                        s[setNo+1][k]=s[setNo][k];
	                    }
	                    else	// exclude the previously present element and left shift the elements
	                    {
	                        s[setNo+1][k]=s[setNo][k+1];
	                    }
	                }
	                s[setNo+1][q-1]=in[i];
	                //to check the set is duplicate or not
	                DupFlag=check_Dup_Set(setNo+1,q-1);
	                if(DupFlag==0) // if not a duplicate then proceed otherwise the set is neglected
	                {
	                    permute(count+1,setNo+1,q,i+1);
	                }

	            }
	        }
	    }
	    // as soon as any set is completed all its elements are printed 
	    else
	    {
	        int c;
	        for(c=0;c<q;c++)
	        {
	            System.out.print(" "+s[setNo][c]);
	        }
	        System.out.println();
	    }
	}
	public static void main(String[] args) throws NumberFormatException, IOException 
	{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter no of gene in chromosome");
		Exemplars e=new Exemplars();
		e.n=Integer.parseInt(br.readLine());
		e.in=new int[e.n];
		System.out.println("Enter gene sequence of chromosome");
		int i;
		for(i=0;i<e.n;i++)
			e.in[i]=Integer.parseInt(br.readLine());
		e.s=new int[e.n][e.n];
		e.permute(0,0,0,0);
	}
	

}
