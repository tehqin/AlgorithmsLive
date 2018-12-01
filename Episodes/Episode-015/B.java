
import java.util.*;
import java.io.*;

public class B
{
   public static void main(String[] args) throws Exception
   {
      PrintWriter out = new PrintWriter(System.out);
      new B(new FastScanner(System.in), out);
      out.close();
   }

   int N, K;
   int[] vs; 
   long[] sum;

   public B(FastScanner in, PrintWriter out)
   {
      N = in.nextInt();
      K = in.nextInt();
      vs = new int[N];
      for (int i=0; i<N; i++)
         vs[i] = in.nextInt();
  
      sum = new long[N+1];
      for (int i=0; i<N; i++)
         sum[i+1] = sum[i] + vs[i];

      long[] prev = new long[N+1];
      long[] cost = new long[N+1];

      Line[] dq = new Line[N+1];

      int[][] prevSplit = new int[K][N+1];

      for (int k=0; k<K; k++)
      {
         Arrays.fill(cost, 0);

         int fptr = 0, bptr = 0;
      
         for (int i=0; i<=N; i++)
         {
            while (fptr+1 < bptr && dq[fptr].getCost(sum[i]) <= dq[fptr+1].getCost(sum[i]))
               fptr++;
         
            if (fptr < bptr)
            {
               cost[i] = dq[fptr].getCost(sum[i]);
               prevSplit[k][i] = dq[fptr].i;
            }

            Line newLine = new Line(i, sum[i], prev[i]);
         
            while (fptr + 1 < bptr)
            {
               long t1 = dq[bptr-2].to(dq[bptr-1]);
               long t2 = dq[bptr-1].to(newLine);
            
               if (t1 < t2)
                  break;
               else
                  bptr--;
            }

            dq[bptr++] = newLine;

         }
         
         for (int i=0; i<=N; i++)
            prev[i] = cost[i];
      }

      out.println(prev[N]);

      int i = N;
      int k = K-1;

      ArrayDeque<Integer> stk = new ArrayDeque<>();
      while (k >= 0)
      {
         i = prevSplit[k][i];
         stk.push(i);
         k--;
      }

      StringBuilder sb = new StringBuilder();
      while (stk.size() > 0)
      {
         sb.append(stk.pop());
         sb.append(' ');
      }
      out.println(sb.toString().trim());
   }

   class Line
   {
      long a, b;
      int i;

      Line(int ii, long aa, long bb)
      {
         i = ii; a=aa; b=bb;
      }

      long getCost(long x)
      {
         return a * (x - a) + b;
      }

      long to(Line rhs)
      {
         long lo = Math.max(a, rhs.a);
         long hi = Integer.MAX_VALUE; 

         while (lo < hi)
         {
            long m = (lo+hi) / 2;
            long v1 = getCost(m);
            long v2 = rhs.getCost(m);

            if (v1 <= v2)
               hi = m;
            else
               lo = m+1;
         }

         return hi;
      }
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
