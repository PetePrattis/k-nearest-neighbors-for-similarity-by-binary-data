package knn_questionnaire;

import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math;
import java.util.Collections;
import java.util.Objects;

public class knn_quest_binary {
	public static int[][] quest = new int[30][6];
		public static void main(String[] args){
			int[][] quest = {//the locations
					//{sea, mountain, plain, history, alternative tourism, nightlife},
					{1,0,1,1,1,0},
					{1,0,1,1,1,0},
					{1,0,1,1,1,1},
					{1,0,1,0,1,1},
					{1,0,1,1,1,1},
					{1,1,0,1,1,1},
					{0,0,1,1,1,1},
					{0,0,1,0,1,1},
					{0,0,1,1,1,0},
					{0,1,0,0,0,0},
					{1,0,1,1,1,1},
					{0,1,0,1,0,0},
					{1,0,1,1,1,0},
					{0,1,0,1,0,1},
					{1,1,0,0,1,1},
					{1,1,0,1,0,0},
					{1,1,0,0,1,1},
					{1,1,0,1,1,1},
					{1,1,0,1,1,1},
					{1,1,0,0,1,1},
					{1,0,1,1,1,1},
					{1,0,1,1,1,0},
					{1,0,1,0,1,1},
					{1,0,1,1,1,0},
					{1,0,1,0,1,0},
					{1,0,1,0,1,0}//new user
					};
		

			Scanner scan = new Scanner(System.in);
			System.out.println("Enter 0-25:");//pick 25
			
			
			int num = scan.nextInt();// the user index that we will check rest users for similarity
			
			System.out.println("Choose k: ");
			int k = scan.nextInt();
			//System.out.println(num);
			scan.close();
			
			//xristes
			ArrayList<Integer> user = new ArrayList<Integer>();
			//similar xristes
			ArrayList<Integer> suser = new ArrayList<Integer>(); //sorted
			//similarities
			ArrayList<Double> sim = new ArrayList<Double>();
			//similarities of similar users
			ArrayList<Double> ssim = new ArrayList<Double>(); //sorted 

			boolean hamming = false;
			for(int i=0; i<quest.length; i++) {
				//HAMMING and COSINE are best for binary dataset
				//hammingDist(quest, num, i, user, sim, ssim);
				cosineSimilarity(quest,i,num,user,sim,ssim);
			}
			
			Collections.sort(ssim);
			
			if (!hamming)
			{
				Collections.reverse(ssim); //descending
			}
		    
			
		    int remove = ssim.size() - k;
		    for (int i =0; i<remove;i++) {
		    	ssim.remove(ssim.size()-1); //remove locations sot hat remain only k similar
		    }
		    
		    boolean once = true;
		    for (int i=0; i<ssim.size(); i++) {
		    	for (int j=0; j<sim.size(); j++) {
		    		if(Math.floor(ssim.get(i)*10000) == Math.floor(sim.get(j)*10000) && once && !suser.contains(user.get(j))) {
		    			once = false;
		    			suser.add(user.get(j));
		    		}
		    	}
		    	once = true;
		    }
		    
		    System.out.println(user);
		    System.out.println(sim);
		    System.out.println(suser);
		    System.out.println(ssim);
		    
		   
		}
		
		public static void hammingDist (int [][] quest, int num, int i,ArrayList<Integer> user,ArrayList<Double> sim,ArrayList<Double> ssim) {
		
			//num = user to ignore
			//i = user to observe
			double distance = 0.0;
			boolean flag = true;
			for (int j=0; j <quest[0].length;j++) //for each location feature / characteristic
			{
				if (i != num) { 
					flag = true;
					if (quest[num][j] != quest[i][j])
						distance++;
				}
				else
				{
					flag = false;
				}
			}
			if (flag)
			{
				System.out.println("location: "+num+" has similarity to user's preferred location features : "+ i+" of value "+distance); //similarity to rest locations
				user.add(i);
				sim.add(distance);
				ssim.add(distance);
			}
		}
		
		public static void euclideanDist(int [][] quest,int num, int i, ArrayList<Integer> user,ArrayList<Double> sim,ArrayList<Double> ssim) {
 			double sumSquares = 0;
			boolean flag = true;
			for(int j=0; j<quest[0].length; j++) {
				if(i != num) {//euclidean
					double diff = 0;
					diff = quest[num][j] - quest[i][j]; 
					sumSquares += diff*diff; //to be eucledian
					flag = true;
				}
				else if (i == num) 
					flag = false;
			}
			if(flag) {
				double d = Math.sqrt(sumSquares); //eucledian dist
				double similarity = 1/d;
				if (Double.isInfinite(similarity)) {
					similarity = 1;
				}
			
				System.out.println("location: "+num+" has similarity to user's preferred location features : "+ i+" of value "+distance); //similarity to rest locations
				user.add(i);
				sim.add(similarity);
				ssim.add(similarity);
					
			//return similarity;
			}
		}
		
		public static void cosineSimilarity(int [][] quest, int i, int num, ArrayList<Integer> user,ArrayList<Double> sim,ArrayList<Double> ssim)
		{

			double sumProduct = 0;
			double sumASq = 0;
			double sumBSq = 0;
			double similarity;
			
			for(int j=0; j<quest[0].length; j++) {
				if(i != num) {
					sumProduct += quest[num][j]*quest[i][j];
					sumASq += quest[num][j]*quest[num][j];
					sumBSq += quest[i][j] * quest[i][j];
					}
			}
			if (sumASq == 0 && sumBSq == 0) {
				similarity = 0.0;
			}
			else {
				similarity = sumProduct / (Math.sqrt(sumASq) * Math.sqrt(sumBSq));
				System.out.println("location: "+num+" has similarity to user's preferred location features : "+ i+" of value "+similarity); //similarity to rest locations
			}
			
			
			user.add(i);
			sim.add(similarity);
			ssim.add(similarity);
			
		}
		
		public static void manhattanDistance(int [][] quest, int i, int num,ArrayList<Integer> user,ArrayList<Double> sim,ArrayList<Double> ssim )
		{
			int res =0, sum =0, res1=0, sum1=0;
			boolean flag = true;
			double total;
			for (int j=0; j<quest[0].length;j++)
			{
				if (i!=num) {
					flag = true;
					res += Math.abs(quest[num][j]*j - sum);
					//sum += Math.abs(quest[num][j]);
					sum += quest[num][j];
					res1 += Math.abs(quest[i][j]*j - sum);
					//sum1 += Math.abs(quest[i][j]);
					sum1 += quest[i][j];
					
				}
				else
					flag = false;
				
			}
			if (flag)
			{
				total = res + res1;
				System.out.println("location: "+num+" has similarity to user's preferred location features : "+ i+" of value "+total); //similarity to rest locations
				user.add(i);
				sim.add(total);
				ssim.add(total);
			}
		}
	
}
