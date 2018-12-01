
import java.util.*;
import java.io.*;

public class I
{
   public static void main(String[] args) throws Exception
   {
      PrintWriter out = new PrintWriter(System.out);
      new I(new FastScanner(System.in), out);
      out.close();
   }

   int N, D;
   int[] depth;
   int[][] par;
   ArrayList<Integer>[] adj;

   int walk(int i, int k)
   {
      for (int d=0; d<=D && i != -1; d++)
         if (((1<<d) & k) > 0)
            i = par[d][i];
      return i;
   }

   int lca(int i, int j)
   {
      if (depth[i] > depth[j])
         i = walk(i, depth[i]-depth[j]);
      if (depth[j] > depth[i])
         j = walk(j, depth[j]-depth[i]);
      if (i == j)
         return i;

      for (int d=D; d>=0; d--)
      {
         if (par[d][i] != par[d][j])
         {
            i = par[d][i];
            j = par[d][j];
         }
      }

      return par[0][i];
   }

   int dist(int i, int j)
   {
      return depth[i] + depth[j] - 2 * depth[lca(i,j)];
   }

   public I(FastScanner in, PrintWriter out)
   {
      N = in.nextInt();
      adj = new ArrayList[N];
      for (int i=0; i<N; i++)
         adj[i] = new ArrayList<>();

      for (int e=1; e<N; e++)
      {
         int i = in.nextInt()-1;
         int j = in.nextInt()-1;
         adj[i].add(j);
         adj[j].add(i);
      }


      // 10100001010101
      // 10000000000000
      D = Integer.numberOfTrailingZeros(Integer.highestOneBit(N));
      par = new int[D+1][N];
      depth = new int[N];
      for (int[] dd : par)
         Arrays.fill(dd, -1);

      ArrayDeque<Integer> q = new ArrayDeque<>();
      boolean[] seen = new boolean[N];
      q.add(0);
      seen[0] = true;

      while (q.size() > 0)
      {
         int i = q.poll();
         for (int j : adj[i]) if (!seen[j])
         {
            seen[j] = true;
            q.add(j);
            par[0][j] = i;
            depth[j] = depth[i]+1;
         }
      }

      for (int d=1; d<=D; d++)
      {
         for (int i=0; i<N; i++)
         {
            int mid = par[d-1][i];
            if (mid != -1)
               par[d][i] = par[d-1][mid];
         }
      }

      long res = 0;
      for (int i=1; i<=N; i++)
         for (int j=i+i; j<=N; j+=i)
            res += dist(i-1, j-1)+1;
      out.println(res);
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
