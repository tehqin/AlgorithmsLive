
import java.util.*;
import java.io.*;

public class E
{
   public static void main(String[] args) throws Exception
   {
      FastScanner in = new FastScanner(System.in);
      PrintWriter out = new PrintWriter(System.out);
      while (new E().solve(in, out));
      out.close();
   }

   boolean solve(FastScanner in, PrintWriter out)
   {
      int N = in.nextInt();
      int M = in.nextInt();

      int numSpareRows = in.nextInt();
      int numSpareDiodes = in.nextInt();

      if (N == 0 && M == 0 && numSpareRows == 0 && numSpareDiodes == 0)
         return false;

      int[][] badSum = new int[N][M+1];
      for (int i=0; i<N; i++)
      {
         for (int j=0; j<M; j++)
         {
            int v = 1-in.nextInt();
            badSum[i][j+1] = badSum[i][j] + v;
         }
      }

      int res = 0;

      for (int i=0; i<M; i++)
      {
         for (int j=i; j<M; j++)
         {
            Balancer ds = new Balancer(numSpareRows);

            int k2 = 0;
            for (int k1=0; k1<N; k1++)
            {
               while (k2 < N && ds.isStable(numSpareDiodes))
               {
                  int strip = badSum[k2][j+1] - badSum[k2][i];
                  ds.add(strip, k2);
                  k2++;
               }
            
               int h1 = k2-k1-1;
               if (ds.isStable(numSpareDiodes))
                  h1++;
            
               int rr = h1 * (j-i+1);
               res = Math.max(res, rr);

               int strip = badSum[k1][j+1] - badSum[k1][i];
               ds.remove(strip, k1);
            }
         }
      }

      out.println(res);
   
      return true;
   }
}

class Balancer
{
   int sumLower;
   int numSpareRow;
   TreeSet<OrdInt> lower;
   TreeSet<OrdInt> upper;

   Balancer(int numSpareRow)
   {
      sumLower = 0;
      this.numSpareRow = numSpareRow;
      lower = new TreeSet<>();
      upper = new TreeSet<>();
   }


   void add(int numDead, int row)
   {
      upper.add(new OrdInt(numDead, row));   

      if (upper.size() > numSpareRow ||
          (lower.size() > 0 &&
           lower.last().compareTo(upper.last()) > 0))
      {
         OrdInt ord = upper.pollFirst();
         lower.add(ord);
         sumLower += ord.value;
      }

      if (lower.size() > 0 && upper.size() < numSpareRow)
      {
         OrdInt ord = lower.pollLast();
         upper.add(ord);
         sumLower -= ord.value;
      }
   }

   void remove(int numDead, int row)
   {
      OrdInt toRemove = new OrdInt(numDead, row);
      if (lower.remove(toRemove))
      {
         sumLower -= numDead;
         return;
      }

      upper.remove(toRemove);
      if (lower.size() > 0)
      {
         OrdInt best = lower.pollLast();
         sumLower -= best.value;
         upper.add(best);
      }
   }
   
   boolean isStable(int numSpareDiode)
   {
      return sumLower <= numSpareDiode; 
   }
}


class OrdInt implements Comparable<OrdInt>
{
   int value, id;

   OrdInt(int value, int id)
   {
      this.value = value;
      this.id = id;
   }

   public int compareTo(OrdInt rhs)
   {
      if (value == rhs.value)
         return Integer.compare(id, rhs.id);
      return Integer.compare(value, rhs.value);
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
