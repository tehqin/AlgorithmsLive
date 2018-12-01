
import java.util.*;
import java.io.*;

public class C
{
   public static void main(String[] args) throws Exception
   {
      FastScanner in = new FastScanner(new FileInputStream("roboherd.in"));
      PrintWriter out = new PrintWriter("roboherd.out");
      new C().run(in, out);
      out.close();
   }

   int N, K;
   int[][] controllers;
  
   long budget;
   int cowsFound;
   boolean canForm(int i, long cost)
   {
      if (cost > budget)
         return false;
  
      if (i == N)
      {
         cowsFound++;
         return cowsFound >= K;
      }
      
      if (controllers[i].length > 1 && controllers[i][1]+cost > budget)
      {
         cowsFound++;
         return cowsFound >= K; 
      }

      for (int v : controllers[i])
         if (canForm(i+1, cost+v))
            return true;
   
      return false;
   }

   long savings;
   void calcSavings(int i, int cost)
   {
      if (cost >= budget)
         return;
  
      if (i == N)
      {
         savings += (budget-cost);
         return;
      }
      
      if (controllers[i].length > 1 && controllers[i][1]+cost >= budget)
      {
         savings += (budget-cost);
         return;
      }

      for (int v : controllers[i])
         calcSavings(i+1, cost+v);
   }

   void run(FastScanner in, PrintWriter out)
   {
      N = in.nextInt();
      K = in.nextInt();

      long lo = 0, hi = 0;
      long baseline = 0;
      controllers = new int[N][];
      for (int i=0; i<N; i++)
      {
         int m = in.nextInt();

         controllers[i] = new int[m];
         for (int j=0; j<m; j++)
            controllers[i][j] = in.nextInt();
         Arrays.sort(controllers[i]);

         baseline += controllers[i][0];
         for (int j=m-1; j>=0; j--)
            controllers[i][j] -= controllers[i][0];
      
         hi += controllers[i][m-1];
      }
      
      Arrays.sort(controllers, (a, b) -> {
            for (int i=0; i<Math.min(a.length, b.length)-1; i++)
            {
               int d1 = a[i+1]-a[i];
               int d2 = b[i+1]-b[i];
               if (d1 != d2) 
                  return Integer.compare(d1, d2);
            }
            return Integer.compare(a.length, b.length); 
         });

      while (lo < hi)
      {
         budget = (lo+hi) / 2;
         cowsFound = 0;
         if (canForm(0, 0))
            hi = budget;
         else
            lo = budget+1;
      }

      long mostExpensive = hi + baseline;
      long overCost = mostExpensive * K;

      savings = 0;
      budget = hi;
      calcSavings(0, 0);

      long result = overCost - savings;
      out.println(result);
   }
}

class FastScanner{
   private InputStream stream;
   private byte[] buf = new byte[1024];
   private int curChar;
   private int numChars;

   public FastScanner(InputStream stream)
   {
      this.stream = stream;
   }

   int read()
   {
      if (numChars == -1)
         throw new InputMismatchException();
      if (curChar >= numChars){
         curChar = 0;
         try{
            numChars = stream.read(buf);
         } catch (IOException e) {
            throw new InputMismatchException();
         }
         if (numChars <= 0)
            return -1;
      }
      return buf[curChar++];
   }

   boolean isSpaceChar(int c)
   {
      return c==' '||c=='\n'||c=='\r'||c=='\t'||c==-1;
   }
   
   boolean isEndline(int c)
   {
      return c=='\n'||c=='\r'||c==-1;
   }

   int nextInt()
   {
      return Integer.parseInt(next());
   }
   
   long nextLong()
   {
      return Long.parseLong(next());
   }

   double nextDouble()
   {
      return Double.parseDouble(next());
   }

   String next(){
      int c = read();
      while (isSpaceChar(c))
         c = read();
      StringBuilder res = new StringBuilder();
      do{
         res.appendCodePoint(c);
         c = read();
      }while(!isSpaceChar(c));
      return res.toString();
   }
   
   String nextLine(){
      int c = read();
      while (isEndline(c))
         c = read();
      StringBuilder res = new StringBuilder();
      do{
         res.appendCodePoint(c);
         c = read();
      }while(!isEndline(c));
      return res.toString();
   }
}
