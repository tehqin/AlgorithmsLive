
import java.util.*;
import java.io.*;

public class A
{
   public static void main(String[] args) throws Exception
   {
      PrintWriter out = new PrintWriter(System.out);
      new A(new Scanner(System.in), out);
      out.close();
   }

   int N, M, K;
   public A(Scanner in, PrintWriter out)
   {
      M = in.nextInt();
      N = in.nextInt();
      K = in.nextInt();
      int[] savings = new int[N]; 
   
      int L = N/2;

      ArrayList<Integer> left = new ArrayList<>();
      ArrayList<Integer> right = new ArrayList<>();
      ArrayList<int[]> cross = new ArrayList<>();
      
      int badCost = 0;
      while (M-->0)
      {
         int i = in.nextInt()-1;
         int a = in.nextInt();
         int j = in.nextInt()-1;
         int b = in.nextInt();
     
         if (i < L && j < L)
            left.add((1<<i)|(1<<j));
         else if (L <= i && L <= j)
            right.add((1<<(i-L))|(1<<(j-L)));
         else if (i < j)
            cross.add(new int[]{i,j});
         else
            cross.add(new int[]{j,i});

         badCost += Math.max(a, b);
         if (a < b) 
            savings[i] += b-a;
         else
            savings[j] += a-b;
      }

      int[][] table = new int[N+1][1<<(N-L)];
      for (int[] tt : table)
         Arrays.fill(tt, Integer.MIN_VALUE);
      for (int mask2=0; mask2<(1<<(N-L)); mask2++)
      {
         boolean isOk = true;
         for (int m : right) if ((mask2 & m) == 0)
         {
            isOk = false;
            break;
         }
         if (!isOk) continue;
 
         int rightSavings = 0;
         int k2 = Integer.bitCount(mask2);
         for (int i=0; i<N-L; i++)
            if ((mask2&(1<<i)) > 0)
               rightSavings += savings[i+L];

         table[k2][mask2] = rightSavings;
      }

      for (int[] tt : table)
      {
         for (int mask2=(1<<(N-L))-1; mask2>0; mask2--)
         {
            for (int i=0; i<(N-L); i++)
            {
               int m1 = 1<<i;
               if ((m1&mask2) > 0)
                  tt[mask2-m1] = Math.max(tt[mask2-m1], tt[mask2]);
            }
         }
      }
      
      int maxSavings = Integer.MIN_VALUE;
      for (int mask1=0; mask1<(1<<L); mask1++)
      {
         boolean isOk = true;
         for (int m : left) if ((mask1 & m) == 0)
         {
            isOk = false;
            break;
         }
         if (!isOk) continue;

         int leftSavings = 0;
         int k1 = Integer.bitCount(mask1);
         for (int i=0; i<L; i++)
            if ((mask1&(1<<i)) > 0)
               leftSavings += savings[i];

         int mask2 = 0;
         for (int[] e : cross)
         {
            int m1 = 1 << e[0];            
            if ((m1&mask1) == 0)
               mask2 |= 1<<(e[1]-L);
         }
         if (k1 > K) continue;

         int rightSavings = table[K-k1][mask2];
         if (rightSavings < 0)
            continue;

         maxSavings = Math.max(maxSavings, leftSavings+rightSavings);
      }


      if (maxSavings == Integer.MIN_VALUE)
         out.println(-1);
      else
         out.println(badCost-maxSavings);
   }
}
