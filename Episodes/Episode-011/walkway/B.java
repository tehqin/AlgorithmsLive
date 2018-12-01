
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

   public B(FastScanner in, PrintWriter out)
   {
      int N = in.nextInt();
      int C = in.nextInt();

      long bestPrev = 0;

      int fptr = 0;
      int bptr = 0;
      Sol[] dq = new Sol[N];

      for (int i=0; i<N; i++)
      {
         int x = in.nextInt();

         Sol part = new Sol(x, bestPrev+C);
     
         while (bptr-fptr >= 2 && dq[bptr-2].to(dq[bptr-1]) >= dq[bptr-2].to(part))
            bptr--;
         dq[bptr++] = part;


         while (fptr+1 < bptr && dq[fptr].getCost(x) >= dq[fptr+1].getCost(x))
            fptr++;
      
         bestPrev = dq[fptr].getCost(x); 
      }

      out.println(bestPrev);
   }
}

class Sol
{
   int x;
   long prevCost;

   Sol(int xx, long pp)
   {
      x=xx; prevCost=pp;
   }

   long getCost(long y)
   {
      long d = (x-y);
      return d*d + prevCost;
   
   }

   // Time that rhs overtakes this
   long to(Sol rhs)
   {
      long lo = rhs.x;
      long hi = rhs.x;
  
      while (getCost(hi) < rhs.getCost(hi))
      {
         lo = hi+1;
         hi *= 2;
      }

      while (lo < hi)
      {
         long m = (lo+hi)/2;

         if (getCost(m) >= rhs.getCost(m))
            hi = m;
         else
            lo = m+1;
      }

      return hi;
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
