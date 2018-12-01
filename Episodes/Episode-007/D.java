
import java.util.*;
import java.io.*;

public class D
{
   public static void main(String[] args)
   {
      FastScanner in = new FastScanner(System.in);
      PrintWriter out = new PrintWriter(System.out);
      while (new D().run(in, out));
      out.close();
   }

   int id_cnt = 0;
   HashMap<String, Integer> mmp = new HashMap<>();
   int getID(String s)
   {
      Integer rr = mmp.get(s);
      if (rr != null) return rr;
      mmp.put(s, id_cnt);
      return id_cnt++;
   }

   int N, M, oo=987654321;
   int[][] cost;

   boolean run(FastScanner in, PrintWriter out)
   {
      N = in.nextInt();
      M = in.nextInt();

      if (N == 0 && M == 0)
         return false;

      cost = new int[N][N];
      for (int[] cc : cost)
         Arrays.fill(cc, oo);
      for (int i=0; i<N; i++)
         cost[i][i] = 0;
      for (int i=0; i<N; i++)
         getID(in.next());
      
      while (M-->0)
      {
         int i = getID(in.next());
         int j = getID(in.next());
         int c = in.nextInt();

         cost[i][j] = cost[j][i] = Math.min(cost[i][j], c);
      }

      for (int k=0; k<N; k++)
         for (int i=0; i<N; i++)
            for (int j=0; j<N; j++)
               cost[i][j] = Math.min(cost[i][j], cost[i][k]+cost[k][j]);

      TreeSet<Integer> ts = new TreeSet<>();
      int[][] roads = new int[4][2];
      for (int i=0; i<4; i++)
      {
         for (int j=0; j<2; j++)
         {
            roads[i][j] = getID(in.next());  
            ts.add(roads[i][j]);
         }
      }

      int nTerm = ts.size();

      int[] group = new int[nTerm];
      int cnt = 0;
      for (int v : ts)
         group[cnt++] = v;

      
      // Compute steiner trees
      int[][] minCost = new int[N][1<<nTerm];
      for (int[] mm : minCost)
         Arrays.fill(mm, oo);
      for (int i=0; i<nTerm; i++)
         minCost[group[i]][1<<i] = 0;

      for (int mask=0; mask<(1<<nTerm); mask++)
      {
         for (int i=0; i<N; i++)
         {
            for (int ss=mask; ss>0; ss=(ss-1)&mask)
               minCost[i][mask] = Math.min(minCost[i][mask],
                                   minCost[i][ss] + minCost[i][mask-ss]);
            
            if (minCost[i][mask] < oo)
            {
               for (int j=0; j<N; j++)
                  minCost[j][mask] = Math.min(minCost[j][mask],
                                        minCost[i][mask]+cost[i][j]);
            }
         }
      }

      int[] minTotal = new int[1<<nTerm];
      Arrays.fill(minTotal, oo);
      for (int mask=0; mask<(1<<nTerm); mask++)
         for (int i=0; i<N; i++)
            minTotal[mask] = Math.min(minTotal[mask], minCost[i][mask]);

      int[] dp = new int[1<<4];
      Arrays.fill(dp, oo);
      dp[0] = 0;

      for (int mask=0; mask<(1<<4); mask++)
      {
         if (dp[mask] == oo)
            continue;
      
         for (int smask=0; smask<(1<<4); smask++)
         {
            if ((mask&smask) > 0)
               continue;
         
            int joinMask = 0;
            for (int i=0; i<4; i++)
            {
               if (((1<<i)&smask) > 0)
               {
                  int x = Arrays.binarySearch(group, roads[i][0]);
                  int y = Arrays.binarySearch(group, roads[i][1]);

                  joinMask |= (1<<x) | (1<<y);
               }
            }

            dp[mask | smask] = Math.min(dp[mask | smask], dp[mask] + minTotal[joinMask]);
         }
      }

      out.println(dp[(1<<4)-1]);

      return true;
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
