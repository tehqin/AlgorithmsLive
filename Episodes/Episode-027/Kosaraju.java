
import java.util.*;
import java.io.*;

public class Kosaraju
{
   public static void main(String[] args) throws Exception
   {
      PrintWriter out = new PrintWriter(System.out);
      new Kosaraju(new FastScanner(System.in), out);
      out.close();
   }
   
   ArrayDeque<Integer> stk;
   boolean[] seen;
   Graph g;

   void dfs(int i)
   {
      // Cross edge case
      if (seen[i])
         return;
      seen[i] = true;

      for (int j : g.adj[i])
         dfs(j);
      stk.push(i);
   }

   public Kosaraju(FastScanner in, PrintWriter out)
   {
      g = new Graph(in.nextInt());
      int M = in.nextInt(); 

      while (M-->0)
      {
         int i = in.nextInt()-1;
         int j = in.nextInt()-1;
         g.add(i,j);
      }

      stk = new ArrayDeque<>();
      seen = new boolean[g.N];
      for (int i=0; i<g.N; i++)
         dfs(i);
   
      Graph rg = g.reverse();
      
      int numComps = 0;
      int[] ids = new int[g.N];
      Arrays.fill(ids, -1);

      ArrayDeque<Integer> q = new ArrayDeque<>();
      while (stk.size() > 0)
      {
         int i = stk.pop();
         if (ids[i] != -1)
            continue;
      
         out.printf("Found comp[%d]:", numComps+1);
         ids[i] = numComps;
         q.add(i);
         while (q.size() > 0)
         {
            i = q.poll();
            out.printf(" %d", i+1);

            for (int j : rg.adj[i])
               if (ids[j] == -1)
               {
                  ids[j] = numComps;
                  q.add(j);
               }
         }
         out.println();

         numComps++;
      }
   }
}

class Graph
{
   int N;
   ArrayList<Integer>[] adj;

   Graph(int NN)
   {
      N=NN;
      adj = new ArrayList[N];
      for (int i=0; i<N; i++)
         adj[i] = new ArrayList<>();
   }

   void add(int i, int j)
   {
      adj[i].add(j);
   }

   Graph reverse()
   {
      Graph rg = new Graph(N);
      for (int i=0; i<N; i++)
         for (int j : adj[i])
            rg.add(j,i);
      return rg;
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
