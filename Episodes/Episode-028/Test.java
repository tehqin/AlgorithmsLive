
import java.util.*;

public class Test
{
   public static void main(String[] args)
   {
      new Test();
   }

   void runTestSmall(QueryInterface rmq, int[][] queries)
   {
      for (int[] curQuery : queries)
      {
         int value = rmq.minimum(curQuery[0], curQuery[1]);
         System.out.printf("Range minimum [%d, %d] = %d%n", curQuery[0], 
                           curQuery[1], value);
      }
   }

   
   void runTest1(QueryInterface rmq)
   {
      runTestSmall(rmq, new int[][]{
               {0, 7},
               {1, 4},
               {2, 5},
               {0, 0},
               {1, 1},
               {2, 2},
               {3, 3},
               {4, 4},
               {5, 5},
         });
   }

   int randint(int i, int j)
   {
      return ((int)(Math.random() * (j-i+1)))+i;
   }

   int[] genRandArray(int size)
   {
      int[] res = new int[size];
      for (int i=0; i<size; i++)
         res[i] = randint(1, 2 * size);

      //System.out.printf("Created array: %s%n", Arrays.toString(res));
      return res;
   }

   void runRandomTests(int size, int numTests, QueryInterface rmq1, QueryInterface rmq2)
   {
      boolean allPassed = true;

      for (int testNum=1; testNum<=numTests; testNum++)
      {
         int[] range = new int[]{randint(0, size-1), randint(0, size-1)};
         Arrays.sort(range);

         if (testNum % 10000 == 0)
            System.out.printf("Test checkpoint: %d%n", testNum);
         else
         {
            int r1 = rmq1.minimum(range[0], range[1]); 
            int r2 = rmq2.minimum(range[0], range[1]); 
            if (r1 != r2)
            {
               System.out.printf("Failure %d vs %d%n", r1, r2);
               allPassed = false;
            }
         }
      }

      if (allPassed)
         System.out.println("All tests passed!");
   }

   Test()
   {
      /*
      int[] arr = genRandArray(8);
      runTest1(new RangeSlow(arr));   
      System.out.println();
      runTest1(new SparseTable(arr));   
      */

      int size = 10_000_000;
      int[] arr = genRandArray(size);
      runRandomTests(size, size, new SparseTable(arr), new SparseTable(arr));
   }

   interface QueryInterface
   {
      public int minimum(int i, int j);
   }

   class RangeSlow implements QueryInterface
   {
      int[] arr;

      public RangeSlow(int[] arr)
      {
         this.arr = arr;
      }

      public int minimum(int i, int j)
      {
         int res = arr[i];
         for (int k=i+1; k<=j; k++)
            res = Math.min(res, arr[k]);
         return res; 
      }
   }

   class SparseTable implements QueryInterface
   {
      int maxk, n;
      int[][] table, idx;

      public SparseTable(int[] arr)
      {
         n = arr.length;
         //   10111100001
         //   10000000000
         maxk = Integer.numberOfTrailingZeros(Integer.highestOneBit(n));
         
         idx = new int[maxk+1][n];
         table = new int[maxk+1][n];
         for (int i=0; i<n; i++)
         {
            table[0][i] = arr[i];
            idx[0][i] = i;
         }

         for (int k=1; k<=maxk; k++)
         {
            for (int i=0; i<n; i++)
            {
               int j = i+(1<<(k-1));
               if (j < n)
               {
                  if (table[k-1][i] < table[k-1][j])
                  {
                     idx[k][i] = idx[k-1][i];
                     table[k][i] = table[k-1][i];
                  }
                  else
                  {
                     idx[k][i] = idx[k-1][j];
                     table[k][i] = table[k-1][j];
                  }

               }
            }
         }
      }
  
      public int minimum(int a, int b)
      {
         int len = (b-a+1);
         int k = Integer.numberOfTrailingZeros(Integer.highestOneBit(len));
         
         int m = b - (1<<k) + 1;
         //return Math.min(table[k][a], table[k][m]);
         if (table[k][a] < table[k][m])
            return idx[k][a];
         return idx[k][m];
      }
   }
}
