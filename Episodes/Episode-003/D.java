
import java.util.*;
import java.io.*;

public class D
{
   public static void main(String[] args) throws Exception
   {
      PrintWriter out = new PrintWriter(System.out);
      new D(new FastScanner(System.in), out);
      out.close();
   }

   BitSet build(String s)
   {
      int[] freq = new int[26];
      BitSet bs = new BitSet();

      for (int i=0; i<s.length(); i++)
      {
         Arrays.fill(freq, 0);

         SString cur = new SString();
         for (int j=i; j<s.length(); j++)
         {
            int index = s.charAt(j)-'a'; 
            cur.add(index, 1);

            for (int h : cur.hvs)
               bs.set(h);
         }
      }

      return bs;
   }

   public D(FastScanner in, PrintWriter out)
   {
      String s1 = in.next(); 
      String s2 = in.next(); 

      BitSet bs = build(s1);
      //System.out.println(bs.cardinality());
     
      int ans = 0;
      int[] freq = new int[26];
      for (int i=0; i<s2.length(); i++)
      {
         Arrays.fill(freq, 0);

         SString cur = new SString();
         for (int j=i; j<s2.length(); j++)
         {
            int index = s2.charAt(j)-'a'; 
            cur.add(index, 1);
            cur.len = j-i+1;

            boolean passed = true;
            for (int h : cur.hvs)
               if (!bs.get(h))
                  passed = false;
            
            if (passed)
            {
               ans = Math.max(cur.length(), ans); 
            }
         }
      }
      out.println(ans);

   }
}

class SString
{
   static int charSpace = 4001;
   static int[] primes = {1_000_000_007, 1_000_000_009, 1_000_000_023, 1_000_000_027, 1_000_000_033, 1_000_000_097, 1_000_001_699, 1_000_011_679};
   static int[][] modPows;

   static {
      modPows = new int[primes.length][26];
      for (int i=0; i<primes.length; i++)
      {
         long exp = 1;
         for (int k=0; k<26; k++)
         {
            exp *= 4001;
            exp %= primes[i];
            modPows[i][k] = (int)exp;
         }
      }
   }

   int len;
   int[] hvs;

   SString()
   {
      hvs = new int[primes.length];
      for (int i=0; i<primes.length; i++)
         hvs[i] = i;
      len = 0;
   }

   void add(int x, int v)
   {
      int ord = v+1;
      for (int i=0; i<primes.length; i++)
         hvs[i] = (int)((hvs[i] + ord * (long) modPows[i][x]) % primes[i]);
   }

   public int length()
   {
      return len;
   }

   public String toString()
   {
      return Arrays.toString(hvs);
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
