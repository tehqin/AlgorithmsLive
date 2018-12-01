
import java.util.*;
import java.io.*;

public class F
{
   public static void main(String[] args) throws Exception
   {
      PrintWriter out = new PrintWriter(System.out);
      new F(new FastScanner(System.in), out);
      out.close();
   }

   static int MODO = 1_000_000_007;
  
   long[][] mult(long[][] mat1, long[][] mat2)
   {
      long[][] res = new long[2][2];

      for (int i=0; i<2; i++)
      {
         for (int j=0; j<2; j++)
         {
            for (int k=0; k<2; k++)
            {
               res[i][j] += (mat1[i][k] * mat2[k][j]) % MODO;
            }
            res[i][j] %= MODO;
         }
      }
      return res; 
   }

   long[] mult(long[][] mat, long[] vec)
   {
      long[] res = new long[2];

      for (int i=0; i<2; i++)
      {
         for (int k=0; k<2; k++)
         {
            res[i] += (mat[i][k] * vec[k]) % MODO;
         }
         res[i] %= MODO;
      }

      return res; 
   }

   long[][][] mats;

   void genMats(int maxE)
   {
      mats = new long[maxE+1][][];
      mats[0] = new long[][]{{1, 0}, 
                             {0, 1}};
      mats[1] = new long[][]{{0, 1}, 
                             {1, 1}};
   
      for (int e=2; e<=maxE; e++)
         mats[e] = mult(mats[e-1], mats[1]);
   }

   public F(FastScanner in, PrintWriter out)
   {
      int N = in.nextInt();
      int K = in.nextInt();

      genMats(K);

      SegmentTree st = new SegmentTree(N);

      while (K-->0)
      {
         char c = in.next().charAt(0);
         int i = in.nextInt()-1;
         int j = in.nextInt()-1;
         
         if (c == 'D')
         {
            st.add(1, i, j, 1);
         }
         else
         {
            out.println(st.sumQuery(1, i, j));
         }
      }
   }

   class SegmentTree
   {
      int[] delta;
      long[][] sum;
      int[] lo, hi;

      SegmentTree(int n)
      {
         delta = new int[4*n+1];
         sum = new long[4*n+1][2];
         lo = new int[4*n+1];
         hi = new int[4*n+1];
         init(1, 0, n-1);
      }

      void init(int i, int left, int right)
      {
         lo[i] = left;
         hi[i] = right;
         if (left == right)
         {
            sum[i][0] = 0; 
            sum[i][1] = 1; 
            return;
         }

         int m = (left+right)/2;
         init(2*i, left, m);
         init(2*i+1, m+1, right);

         update(i);
      }

      void prop(int i)
      {
         delta[2*i] += delta[i];
         delta[2*i+1] += delta[i];
         delta[i] = 0;
      }

      void update(int i)
      {
         long[] leftSum = mult(mats[delta[2*i]], sum[2*i]);
         long[] rightSum = mult(mats[delta[2*i+1]], sum[2*i+1]);

         sum[i][0] = leftSum[0] + rightSum[0];
         sum[i][1] = leftSum[1] + rightSum[1];
      }

      void add(int i, int left, int right, long x)
      {
         if (hi[i] < left || right < lo[i]) return;
         if (left <= lo[i] && hi[i] <= right)
         {
            delta[i] += x;
            return;
         }
      
         prop(i);

         add(2*i, left, right, x);
         add(2*i+1, left, right, x);
      
         update(i);
      }

      long sumQuery(int i, int left, int right)
      {
         if (hi[i] < left || right < lo[i])
            return 0L;
         
         if (left <= lo[i] && hi[i] <= right)
            return mult(mats[delta[i]], sum[i])[0];

         prop(i);

         long sum1 = sumQuery(2*i, left, right);
         long sum2 = sumQuery(2*i+1, left, right);

         update(i);

         return (sum1 + sum2) % MODO;
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
