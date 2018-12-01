
import java.util.*;
import java.io.*;

public class H
{
   public static void main(String[] args) throws Exception
   {
      FastScanner in = new FastScanner(System.in);
      PrintWriter out = new PrintWriter(System.out);
      int T = in.nextInt();
      for (int tc=1; tc<=T; tc++)
         new H(tc, in, out);
      out.close();
   }

   public H(int tc, FastScanner in, PrintWriter out)
   {
      Graph g = new Graph(in.nextInt());
      int M = in.nextInt();
      while (M-->0)
      {
         int i = in.nextInt()-1;
         int j = in.nextInt()-1;
         g.add(i, j);
      }

      Tarjan scc = new Tarjan(g);

      if (scc.numSCCs == 1)
         out.println(0);
      else
      {
         int[] indeg = new int[scc.numSCCs];
         int[] outdeg = new int[scc.numSCCs];

         for (int i=0; i<g.N; i++)
            for (int j : g.adj[i])
               if (scc.id[i] != scc.id[j])
               {
                  outdeg[scc.id[i]]++;
                  indeg[scc.id[j]]++;
               }
         
         int numSrcs = 0;
         int numSnks = 0;
         for (int i=0; i<scc.numSCCs; i++)
         {
            if (indeg[i] == 0)
               numSrcs++;
            if (outdeg[i] == 0)
               numSnks++;
         }
         out.println(Math.max(numSrcs, numSnks));
      }

      Graph meta = new Graph(scc.numSCCs);
      for (int i=0; i<g.N; i++)
         for (int j : g.adj[i])
            if (scc.id[i] != scc.id[j])
               meta.add(scc.id[i], scc.id[j]);
   }
}

class Tarjan
{
   Graph g;
   int[] pre;
   int[] id;
   int[] low;
   ArrayDeque<Integer> stk;
   int preorderCounter;
   int numSCCs;

   Tarjan(Graph gg)
   {
      g=gg;
      stk = new ArrayDeque<>();
      pre = new int[g.N];
      id = new int[g.N];
      low = new int[g.N];

      numSCCs = 0;
      preorderCounter = 0;
      Arrays.fill(pre, -1);
   
      for (int i=0; i<g.N; i++)
         if (pre[i] == -1)
            dfs(i);
   }

   void dfs(int i)
   {
      pre[i] = preorderCounter++; 
      low[i] = pre[i];
      stk.push(i);

      for (int j : g.adj[i])
      {
         if (pre[j] == -1)
            dfs(j);
      
         low[i] = Math.min(low[i], low[j]);
      }

      // Are we root of the SCC?
      if (low[i] == pre[i])
      {
         while (true)
         {
            int j = stk.pop();
            id[j] = numSCCs;
            low[j] = g.N;
         
            if (i == j)
               break;
         }
         numSCCs++;
      }
   }
}

class Graph
{
   int N;
   ArrayList<Integer>[] adj;

   Graph(int NN)
   {
      adj = new ArrayList[N=NN];
      for (int i=0; i<N; i++)
         adj[i] = new ArrayList<>();
   }

   void add(int i, int j)
   {
      adj[i].add(j);
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
