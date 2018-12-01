
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
         if (curQuery[0] == 1)
         {
            rmq.increment(curQuery[1], curQuery[2], curQuery[3]);
         }
         else
         {
            int value = rmq.minimum(curQuery[1], curQuery[2]);
            System.out.printf("Range minimum [%d, %d] = %d%n", curQuery[1], 
                              curQuery[2], value);
         }
      }
   }

   
   void runTest1(QueryInterface rmq)
   {
      runTestSmall(rmq, new int[][]{
               {1, 0, 3, 2},
               {1, 0, 7, 1},
               {1, 4, 7, 10},
               {1, 2, 5, 7},
               {2, 0, 7},
               {2, 1, 4},
               {2, 2, 5},
               {2, 0, 0},
               {2, 1, 1},
               {2, 2, 2},
               {2, 3, 3},
               {2, 4, 4},
               {2, 5, 5},
         });
   }


   int randint(int i, int j)
   {
      return ((int)(Math.random() * (j-i+1)))+i;
   }

   void runRandomTests(int size, int numTests, QueryInterface rmq1, QueryInterface rmq2)
   {
      boolean allPassed = true;

      for (int testNum=1; testNum<=numTests; testNum++)
      {
         int type = randint(1, 2);
         int[] range = new int[]{randint(0, size-1), randint(0, size-1)};
         Arrays.sort(range);

         if (testNum % 10000 == 0)
            System.out.printf("Test checkpoint: %d%n", testNum);
         if (type == 1)
         {
            int val = randint(1, 100);
            rmq1.increment(range[0], range[1], val);
            rmq2.increment(range[0], range[1], val);
         }
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
      runTest1(new RangeSlow(8));   
      System.out.println();
      runTest1(new SegmentTree(8));   
      */

      int size = 1_000_000;
      runRandomTests(size, size, new SegmentTree(size), new SegmentTree(size));
   }

   interface QueryInterface
   {
      public void increment(int i, int j, int val);
      public int minimum(int i, int j);
   }

   class RangeSlow implements QueryInterface
   {
      int[] arr;

      public RangeSlow(int n)
      {
         arr = new int[n];
      }

      public void increment(int i, int j, int val)
      {
         for (int k=i; k<=j; k++)
            arr[k] += val;
      }

      public int minimum(int i, int j)
      {
         int res = arr[i];
         for (int k=i+1; k<=j; k++)
            res = Math.min(res, arr[k]);
         return res; 
      }
   }

   class SegmentTree implements QueryInterface
   {
      int n;
      int[] lo, hi, min, delta;
      
      public SegmentTree(int n)
      {
         this.n = n;
         lo = new int[4*n+1];
         hi = new int[4*n+1];
         min = new int[4*n+1];
         delta = new int[4*n+1];
      
         init(1, 0, n-1);
      }
  
      public void increment(int a, int b, int val)
      {
         increment(1, a, b, val);
      }

      public int minimum(int a, int b)
      {
         return minimum(1, a, b);
      }

      void init(int i, int a, int b)
      {
         lo[i] = a;
         hi[i] = b;

         if (a == b)
            return;
      
         int m = (a+b)/2;
         init(2*i, a, m);
         init(2*i+1, m+1, b);

      }

      void prop(int i)
      {
         delta[2*i] += delta[i];
         delta[2*i+1] += delta[i];
         delta[i] = 0;
      }

      void update(int i)
      {
         min[i] = Math.min(min[2*i] + delta[2*i], min[2*i+1] + delta[2*i+1]);
      }

      void increment(int i, int a, int b, int val)
      {
         if (b < lo[i] || hi[i] < a)
            return;
      
         if (a <= lo[i] && hi[i] <= b)
         {
            delta[i] += val;
            return;
         }

         prop(i);

         increment(2*i, a, b, val);
         increment(2*i+1, a, b, val);

         update(i);
      }

      // CircularRMQ!
      int minimum(int i, int a, int b)
      {
         if (b < lo[i] || hi[i] < a)
            return Integer.MAX_VALUE;
         
         if (a <= lo[i] && hi[i] <= b)
            return min[i] + delta[i];
      
         prop(i);

         int minLeft = minimum(2*i, a, b);
         int minRight = minimum(2*i+1, a, b);

         update(i);

         return Math.min(minLeft, minRight);
      }
   }
}
