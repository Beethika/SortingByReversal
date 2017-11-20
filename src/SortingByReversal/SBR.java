package SortingByReversal;
/**
	 * @author Amritanjali
	 *
	 */

import java.util.*;
import java.io.*;

public class SBR {

	
		//Variable Declaration
		int chromosome_count;
		int gene_count;//number of genes in input
		int count_gene;//number of genes in annotated genome
		//ArrayList <Integer> array = new ArrayList<Integer>();
		//ArrayList<array> arr = new ArrayList<array>();		
		Integer[] genome_seq;//permutation
		Integer[][] genome_adj;//adjacency matrix
		Integer[][] black_edge;//black edges in each cycle
		Integer[][] solution_set;//next 1-sequences of reversals
		SBR gSort;
		/**
		 * @Constructor GenomeSort
		 */
		
		public SBR(String genome) {
			
			System.out.println(genome);
			gSort = new SBR();
			gSort.chromosome_count =0;
			gSort.gene_count =0;
			//arr.add(3);
			//arr.
		}
		
		
		public SBR() {
			
		}

	
		/**
		 * @param args
		 */
		public static void main(String[] args) {
			
			// TODO Auto-generated method stub
			SBR genSort = new SBR("Sorting by Reversals Program");
			
			//Accepting Genome Input from User
			genSort.genome_seq();
			//Integer[][] solutionList;
									
					long startTime = System.currentTimeMillis();
					
					genSort.plot_adjacency_matrix();
					
					int c = genSort.detect_cycle();
					//System.out.println("Number of cycles:"+c);
					
					//genSort.plot_adjacency_matrix();
					
					int count = genSort.detect_cut_points(c);
					
					//System.out.println("count="+count+"\n");
					genSort.list_sequences(count,c);
					
					long endTime = System.currentTimeMillis();
					System.out.println("Total execution time is :"+ 
							(endTime-startTime)+ "ms");
								
		}
		
		/**
		 * This method accepts Genome Input from User
		 */
		public void genome_seq(){
			
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		    String genome_list=null;
		    
		    
		    
		    try{
		    //System.out.println("Please enter the number of chromosomes in the genome");
		    //gSort.chromosome_count = Integer.parseInt(in.readLine());
		    gSort.chromosome_count = 1;
		    System.out.println("Please enter the number of genes in the chromosome");
		    gSort.gene_count =  2* Integer.parseInt(in.readLine())+2;
			
			
		    gSort.genome_seq = new Integer [gSort.gene_count];
		    gSort.genome_seq[0]=0;
		    gSort.genome_seq[gSort.gene_count-1]=gSort.gene_count-1;
			System.out.println("Please enter the order of the genes in the chromosome");
			//for (int i=0;i< gSort.chromosome_count; i++){
				//System.out.println("Please enter the Genes in " + (i+1) + " Chromosome");
				genome_list = in.readLine();
				StringTokenizer st = new StringTokenizer(genome_list," ");
				int j = 1;
				gSort.count_gene=2;//an extra element is added at each end of the chromosome
				while(st.hasMoreElements()){
					int t = Integer.parseInt(st.nextToken());
					if(t>0){
						gSort.genome_seq[j] = 2*t - 1;
						gSort.genome_seq[j+1] = 2*t;
					}else{
						gSort.genome_seq[j] = 2*(-t);
						gSort.genome_seq[j+1] = 2*(-t) -1;
					}
					j=j+2;
					gSort.count_gene += 2;
				}
				
			//}
			
			
			//initialising solution set
			gSort.solution_set = new Integer[500][500];
			for(int i=0;i<500;i++)
				for(j=0;j<500;j++){
					gSort.solution_set[i][j]=0;
					
				}
				
			//Start : This piece of code is to print the input matrix
			System.out.println("Annotated Gene Order");
			//for(int i=0;i<gSort.chromosome_count;i++){
				
				for(int i=0;i<gSort.gene_count;i=i+1){
					
					if(gSort.genome_seq[i] == null){
						break;
					}
					//int t = gSort.genome_seq[i][j]/2;
					System.out.print(gSort.genome_seq[i] + " ");
				}
				System.out.println();
			//}
			System.out.println();
			//END : This piece of code is to print the input matrix
		    }
			catch(IOException ie){
				    	
				System.out.println("Improper Input");
				    	
			}
			catch(Exception ex){
					   
					   System.out.println("Improper Input : Breakpoint Graph can't be formed");
					   
			}
		}
		
		/**
		 * This method is for plotting the adjacency matrix
		 */
		
		public void plot_adjacency_matrix(){
			
			//START : Plotting the Adjacency Matrix
			gSort.genome_adj = new Integer[gSort.count_gene][gSort.count_gene];
			//gSort.black_edge = new Integer[gSort.count_gene][gSort.count_gene];
			

			//Initialize
			for(int i=0;i<gSort.count_gene;i++)
				for(int j=0;j<gSort.count_gene;j++){
					gSort.genome_adj[i][j]=0;
					//gSort.black_edge[i][j]=0;
			}



			//Plotting Black Edges
			
			//for(int i=0;i<gSort.chromosome_count;i++)
					for(int j=0;j<gSort.gene_count;j=j+2){
						if(gSort.genome_seq[j]!=null && gSort.genome_seq[j+1]!=null ){
							
						int m=gSort.genome_seq[j];
				        int n=gSort.genome_seq[j+1];
				        //System.out.println("\n"+m+","+n);
				        gSort.genome_adj[m][n]=1;
				        gSort.genome_adj[n][m]=1;
				         }
					}
			
			//Plotting Grey Edge
			
			for(int i=0;i<gSort.count_gene;i=i+2)                
			{
				if(gSort.genome_adj[i][i+1]!=1)
				gSort.genome_adj[i][i+1]=2;
				gSort.genome_adj[i+1][i]=2;
			}  
			
//			     
			    //START : Displaying the Adjacency Matrix
		    /* System.out.println("\nAdjacency Matrix");
				for(int i=0;i<gSort.count_gene;i++){
					System.out.println("");
					for(int j=0;j<gSort.count_gene;j++){
						System.out.print(gSort.genome_adj[i][j] + " ");
					}
				}*/
				//END : Displaying the Adjacency Matrix		
		}
		
		/**
		 * This method is for detecting diverging pair of black edges in same cycle
		 */
			
		public int detect_cut_points(int cycle_count){
			
			//START : Add reversal intervals corresponding to diverging pairs in each cycle
			int count=0,m=0,n=0;		
			for(int i=0; i<cycle_count;i++){
				for(int j=0;j<gSort.count_gene/2;j++){
					if(gSort.black_edge[i][j]!=0){
					for(int k=j+1; k<gSort.count_gene/2;k++)
						if(gSort.black_edge[i][k]!=0 && gSort.black_edge[i][j]!= gSort.black_edge[i][k]){
							m=2*j+1;
							n=2*k;
							//int s=safe_reversal(m,n);
							//if(s == 0)continue;//not safe reversal
							gSort.solution_set[count][0]=m;
							gSort.solution_set[count++][1]=n;
							//System.out.println("m="+m+" n="+n);
						}
					}
				}
			}
			//int[] temp;
			//int k;
			//System.out.println("next 1-sequences are-");
			for(int i=0;i<count;i++){
				m=gSort.solution_set[i][0];
				n=gSort.solution_set[i][1];
				/*temp=new int[(n-m+1)/2];
				k=0;
				System.out.print("{");
				for(int j=m;j<=n;j=j+2)
					temp[k++]=(gSort.genome_seq[j]+1)/2;
				sort(temp, k);
				for(int j=0;j<k;j++)System.out.print(temp[j]+" ");
					//System.out.print((gSort.genome_seq[j]+1)/2 + " ");
				System.out.println("}");*/
			}
						
			return count;
		}
		
		
		
		/* Method to display an array in sorted order*/
		public void sort(int[] arr, int n)
		{
			for(int i=0;i<n-1;i++)
				for(int j=i;j<n;j++)
					if(arr[i]>arr[j]){
						int t=arr[i];
						arr[i]=arr[j];
						arr[j]=t;
					}
			//for(int i=0;i<n;i++)System.out.print(arr[i]+" ");
			//return arr;
		}
		
		/**
		 * This method list all sorting sequences of reversals
		 * count- number of elements in the set 1-sequences of reversals
		 * cycle- number of cycles in the breakpoint graph of the input permutation
		 */
		
		 public void list_sequences(int count, int cycle){
			 //int m,n;
			 int n1=0;
			 Integer [] input_seq;
			 int D; 		///reversal distance
			 //int D=4;
			 System.out.println("Number of cycles="+cycle);
			 D = gSort.count_gene/2-cycle; 
			 System.out.println("Reversal-distance="+D);
			 
			 Integer [][] temp1;
			 Integer [][] temp2;
			 Integer [][][] temp;//list of reversal-sequences in the solution space
			 temp1 = new Integer[500][500];
			 temp2 = new Integer[500][500];
			 temp = new Integer[500][D][gSort.count_gene];
			 
				for(int i=0;i<500;i++)
					for(int j=0;j<500;j++){
						temp1[i][j]=0;
						temp2[i][j]=0;
						
					}
				for(int i=0;i<500;i++)
					for(int j=0;j<D;j++)
						for(int k=0;k<gSort.count_gene;k++)
							temp[i][j][k]=0;
				
				//copying first 1-sequence of solutions
				 for(int i=0;i<count;i++){
					 for(int j=0;j<2;j++)
					 
						 temp1[i][j]= gSort.solution_set[i][j];
					
				 }
				 //copying input matrix
				 input_seq = new Integer [gSort.count_gene];
				 //for(int i=0;i<gSort.chromosome_count;i++){
					 for(int j=0;j<gSort.count_gene;j++)
					 
						 input_seq[j]= gSort.genome_seq[j];
					
				 //}
				 
				 //D=4;
				 				 
				 	for(int i=1;i<=D;i++){
				 	for(int j=0;j<count;j++){
				 		int l=0;
				 		for(int k=0; k<2*i; k=k+2,l++){
							int m = temp1[j][k];
							int n = temp1[j][k+1];
							int q=0;
							for(int p=m;p<=n;p=p+2){
								temp[j][l][q++]=(gSort.genome_seq[p]+1)/2;
								
								System.out.print("j=" + j + " " + (gSort.genome_seq[p]+1)/2 + " ");
							}
							while(q<gSort.count_gene)temp[j][l][q++]=0;
							this.reverse_gene(m,n);
							//sort(temp, q);
				 		}
				 		
				 		//applying i-sequences in jth solution
						/*for(int k=0; k<2*i; k=k+2){
							int m = temp1[j][k];
							int n = temp1[j][k+1];
							this.reverse_gene(m,n);
							
						}*/
						if(i<D){
						//find next 1-sequence and concatenate to get (i+1)-sequences	
						this.plot_adjacency_matrix();
						int c = this.detect_cycle();
						int n = this.detect_cut_points(c);
						for(int p=0;p<n;p++){
							for(int q=0; q<2*i; q++)
								temp2[p+n1][q]= temp1[j][q];
								//System.out.print("next 1-sequences: ");
								for(int q=0;q<2;q++){
									
									temp2[p+n1][2*i+q]= gSort.solution_set[p][q];
									//System.out.print(temp2[p+n1+j][4+q]+" ");
								}
								//System.out.println();
							 }
							n1+=n;//sum of (i+1)-sequences generated from previous i-sequences
							//System.out.println("j="+j+", Count="+n1);
						}
							//for(int p=0;p<gSort.chromosome_count;p++){
								 for(int q=0;q<gSort.count_gene;q++)
								 
									 gSort.genome_seq[q]= input_seq[q];
								
							 //}
							
						}
				 		if(n1>0)count=n1;
						n1=0;
						//copy all (i+1)-sequences
						for(int p=0;p<count;p++){
							for(int q=0;q<2*(i+1);q++){
						 
							temp1[p][q]= temp2[p][q];
							System.out.print("("+temp1[p][q]+","+temp1[p][q+1]+","+temp1[p][q+2]+","+temp1[p][q+3]+")");
							}
							System.out.println();
						}
					
				 }
				 	
				System.out.println("Total number of d-sequences obtained:"+count);
				
				for(int i=0;i<count;i++){
					 System.out.print((i+1)+ ". ");
					 for(int j=0;j<D;j=j+1){
						 System.out.print(" {");
						 int k=0;
						 while(temp[i][j][k]!=0 && k<gSort.count_gene)System.out.print(temp[i][j][k++]+" ");
						 System.out.print("}");
					 }
					 System.out.println();
				}
				
			 //}catch(Exception e){}
		}
		
		/**
		 * This method reverses the genes in a chromosome
		 */
		public void reverse_gene(int m, int n){
			
			try {
			 int []swap_gene = new int[n-m+1];
			 int k=0;
			 for(int i=m;i<=n;i++)
				 swap_gene[k++]=gSort.genome_seq[i];
			 for(int i=m;i<=n;i++)gSort.genome_seq[i]=swap_gene[--k];
			 //System.out.println("m="+m+",n="+n);
			 //display the new permutation after reversal
			 //System.out.print("New Permutation:");
			 //for(int i=0;i<gSort.count_gene;i++)System.out.print(gSort.genome_seq[i]+" ");
			 //System.out.println();
			//END: Display the Genome Matrix
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			
			
		}
		
		
	public int detect_cycle(){
			
			 int[] visited;
			 int start=1;
		     int m=0,n=0,k=0;
		     int cycle_count=0;
		     
		     try{
		    	visited = new int[gSort.count_gene];
		    	for(int i =0;i<gSort.count_gene;i++)visited[i]=0;//initialize visited array
		    	gSort.black_edge = new Integer[gSort.count_gene/2][gSort.count_gene/2];
		    	for(int i=0;i<gSort.count_gene/2;i++)
					for(int j=0;j<gSort.count_gene/2;j++)gSort.black_edge[i][j]=0;
		     loop:
		     for(int i=0;i<gSort.count_gene;i++)
				for(int j=0;j<gSort.count_gene;j++){
		     				if(gSort.genome_adj[i][j]==1){
		     					start=i;
		     					visited[i]=1;
		     					visited[j]=1;
		     					//find the position of element i and j in the permutation
		     					//if i precedes j in the kth black edge than assign black_edge[cycle_count][k]=1 else 2
		     					for(int p=0;p<gSort.count_gene;p++){
		     						if(gSort.genome_seq[p]== i)m=p;
		     						if(gSort.genome_seq[p]== j)n=p;
		     					}
		     					if(n > m)gSort.black_edge[cycle_count][(m+1)/2]=1;
		     					else gSort.black_edge[cycle_count][(n+1)/2]=2;
		     					k=j;
		     					//System.out.println("start="+start+",k="+k);
		     					break loop;
		     				}
					}
		     if(k==0)System.out.println("error");
		     boolean flag = true;
		     //int n=0;
		     while(flag==true ){
		    	 for(int j=0;j<gSort.count_gene;j++)
		 	     	if( gSort.genome_adj[k][j]==2 ){
		 	     		k=j;
		 	     		//System.out.println(" grey k="+k);
		 	     		break;
		 	     		}
		    	 
		 	     	if(k==start){
		 	     		flag=false; 
		 	     		cycle_count++;
		 	     		for(int i=0;i<gSort.count_gene;i++){
		 	     			if(visited[i]==0){
		 	     				start=i;
		 	     				//System.out.println("start="+start);
		 	     				flag=true;
		 	     				break;
		 	     			}
		 	     		}
		 	     		if(flag==false)break;
		 	     		else k=start;
		 	     		
		 	     	}
		 	     	
		 	     	for(int j=0;j<gSort.count_gene;j++)
			 	     	if(gSort.genome_adj[k][j]==1){
			 	     		visited[k]=1;
			 	     		visited[j]=1;
			 	     		for(int p=0;p<gSort.count_gene;p++){
	     						if(gSort.genome_seq[p]== k)m=p;
	     						if(gSort.genome_seq[p]== j)n=p;
	     					}
			 	     		//System.out.println("m="+m+" n="+n);
	     					if(n > m)gSort.black_edge[cycle_count][(m+1)/2]=1;
	     					else gSort.black_edge[cycle_count][(n+1)/2]=2;
			 	     		k=j;
			 	     		//System.out.println("black k="+k);
			 	     		
			 	     		break;
			 	     		}
		 	     	
		 	     	
		     }
		     /*//direction of black edges  '0' does not belong to the cycle  '1' left-right '2' right-left
		     for(int i=0;i<cycle_count;i++){
		    	 System.out.println("\nBlack Edges in Cycle"+(i+1)+":" );
		         for(int j=0;j<gSort.count_gene/2;j++)System.out.println(gSort.black_edge[i][j]+" ");
		     }*/
		     }catch(Exception e){System.out.println("Exception"+e); return 0;}
		     
		     return cycle_count;
		}

}
