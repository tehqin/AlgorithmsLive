
import java.util.*;
import java.io.*;

public class E
{
   public static void main(String[] args) throws Exception
   {
      PrintWriter out = new PrintWriter(System.out);
      new E(new FastScanner(System.in), out);
      out.close();
   }

   public E(FastScanner in, PrintWriter out)
   {
      int N = in.nextInt();
      int K = in.nextInt();

      int[] vs = new int[N];
      for (int i=0; i<N; i++)
         vs[i] = in.nextInt();

      PriorityQueue<Integer> lower = new PriorityQueue<>(16, Collections.reverseOrder());
      PriorityQueue<Integer> upper = new PriorityQueue<>();

      int[][] cost = new int[N][N];
      for (int i=0; i<N; i++)
      {
         lower.clear();
         upper.clear();

         int sumLower = 0;
         int sumUpper = 0;

         for (int j=i; j<N; j++)
         {
            sumLower += vs[j];
            lower.add(vs[j]);

            if (lower.size() > upper.size()+1 ||
                (upper.size() > 0 && lower.peek().compareTo(upper.peek()) > 0))
            {
               int value = lower.poll();
               upper.add(value);

               sumLower -= value;
               sumUpper += value;
            }
         
            if (upper.size() > lower.size())
            {
               int value = upper.poll();
               lower.add(value);

               sumLower += value;
               sumUpper -= value;
            }

            int median = lower.peek();
            int costLower = lower.size() * median - sumLower;
            int costUpper = sumUpper - upper.size() * median;
         
            cost[i][j] = costLower + costUpper;
         }
      }

      int oo = 987654321;
      int[] dp = new int[N+1];
      Arrays.fill(dp, oo);
      dp[0] = 0;

      for (int k=0; k<K; k++)
         for (int i=N-1; i>=0; i--)
            for (int j=i; j<N; j++)
               dp[j+1] = Math.min(dp[j+1], dp[i] + cost[i][j]);
      
      out.println(dp[N]);
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
