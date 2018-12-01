
import java.util.*;
import java.io.*;

public class P
{
   public static void main(String[] args) throws Exception
   {
      FastScanner in = new FastScanner(System.in);
      PrintWriter out = new PrintWriter(System.out);
      new P(in, out);
      out.close();
   }

   int W, H, numTerms;
   char[][] grid;
   int[][][] ids;

   int[] dr = {-1, 0, 1, 0};
   int[] dc = {0, -1, 0, 1};

   Graph g;

   boolean in_bounds(int i, int j)
   {
      return 0 <= i && i < H && 0 <= j && j < W;
   }

   int neg(int i)
   {
      if (i < numTerms)
         return i + numTerms;
      return i - numTerms;
   }

   void add_imp(int i, int j)
   {
      g.add(i, j);
   }

   void add_or(int i, int j)
   {
      //System.out.printf("%d %d or %d %d%n", i / numTerms, i % numTerms, j / numTerms, j % numTerms);
      add_imp(neg(i), j);
      add_imp(neg(j), i);
   }

   void add_xor(int i, int j)
   {
      add_or(i, j);
      add_or(neg(i), neg(j));
   }

   void force_var(int i)
   {
      add_imp(neg(i), i);
   }

   public P(FastScanner in, PrintWriter out)
   {
      H = in.nextInt();
      W = in.nextInt();
      grid = new char[H][];
      for (int i=0; i<H; i++)
         grid[i] = in.next().toCharArray();
   
      ids = new int[4][H][W];
      for (int[][] iii : ids)
         for (int[] ii : iii)
            Arrays.fill(ii, -1);

      boolean passed = true;
      numTerms = 0; 
      for (int i=0; i<H; i++)
      {
         for (int j=0; j<W; j++)
         {
            int numBlackNeighbors = 0;
            for (int d=0; d<4; d++)
            {
               int ni = i + dr[d];
               int nj = j + dc[d];
               if (in_bounds(ni, nj))
               {
                  if (grid[i][j] == 'B' && grid[ni][nj] == 'W')
                     ids[d][i][j] = numTerms++;
               
                  if (grid[ni][nj] == 'B')
                     numBlackNeighbors++;
               }
            }

            if (grid[i][j] == 'W' && numBlackNeighbors == 0)
               passed = false;
         }
      }

      //out.printf("numTerms == %d%n", numTerms);
      g = new Graph(numTerms * 2);
      for (int i=0; i<H; i++)
      {
         for (int j=0; j<W; j++)
         {
            for (int d=0; d<2; d++)
            {
               int id1 = ids[d][i][j];      
               int id2 = ids[d+2][i][j];      

               if (id1 != -1 && id2 != -1)
                  add_xor(id1, id2);
               else if (id1 != -1)
                  force_var(id1);
               else if (id2 != -1)
                  force_var(id2);
            }

            if (grid[i][j] == 'B')
            {
               for (int d=0; d<2; d++)
                  if (ids[d][i][j] == -1 && ids[d+2][i][j] == -1)
                     passed = false;
            }
         }
      }

      ArrayList<Integer> neigh = new ArrayList<>();
      for (int i=0; i<H; i++)
      {
         for (int j=0; j<W; j++)
         {
            if (grid[i][j] != 'W') continue;
  
            neigh.clear();

            for (int d=0; d<4; d++)
            {
               int ni = i + dr[d];
               int nj = j + dc[d];
               if (in_bounds(ni, nj) && grid[ni][nj] == 'B')
                  neigh.add(ids[(d+2)%4][ni][nj]);
            }

            //out.printf("%d %d%n", i, j);
            //out.println(neigh);

            for (int x=0; x<neigh.size(); x++)
            {
               for (int y=x+1; y<neigh.size(); y++)
               {
                  int id1 = neigh.get(x); 
                  int id2 = neigh.get(y); 

                  add_or(neg(id1), neg(id2));
               }
            }
         }
      }

      Tarjan scc = new Tarjan(g);
      for (int i=0; i<numTerms; i++)
         if (scc.id[i] == scc.id[i+numTerms])
            passed = false;

      int numWhite = 0;
      int numBlack = 0;
      for (int i=0; i<H; i++)
      {
         for (int j=0; j<W; j++)
         {
            if (grid[i][j] == 'W')
               numWhite++;
            else if (grid[i][j] == 'B')
               numBlack++;
         }
      }

      if (numBlack * 2 != numWhite)
         passed = false;

      out.println(passed ? "YES" : "NO");
   }
}

class Tarjan
{
   Graph g;
   int[] pre, id, low;
   ArrayDeque<Integer> stk;
   int preorderCounter, numSCCs;

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
