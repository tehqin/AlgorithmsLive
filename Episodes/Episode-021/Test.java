
import java.util.*;

public class Test
{
   public static void main(String[] args)
   {
      new Test(new Scanner(System.in));
   }


   Graph g;
   int cnt;
   int[] pre, low, stk;
   ArrayList<Integer> bridges;
   ArrayList<ArrayList<Integer>> comps;
   boolean[] art, used;

   int dfs(int i, int p, int id)
   {
      if (!used[id])
      {
         used[id] = true;
         stk[++stk[0]] = id; // Push onto a stack
      }

      if (pre[i] != -1)
      {
         low[p] = Math.min(low[p], pre[i]);
         return low[p]; 
      }

      pre[i] = cnt++;
      low[i] = pre[i];
      boolean hasFwd = false;

      for (Edge e : g.adj[i])
      {
         if (e.id == id) continue;

         if (dfs(e.j, i, e.id) < 0)
         {
            low[i] = Math.min(low[i], low[e.j]);
        
            if (low[e.j] == pre[e.j])
            {
               bridges.add(e.id+1); 
            }

            if (i != p ? low[e.j] >= pre[i] : hasFwd)
            {
               art[i] = true;
               makeComp(e.id).add(stk[stk[0]--] + 1); 
            }

            hasFwd = true;
         }
      }

      return -1;
   }

   ArrayList<Integer> makeComp(int id)
   {
      ArrayList<Integer> comp = new ArrayList<>();
      while (stk[stk[0]] != id)
         comp.add(stk[stk[0]--] + 1); // Pop off the stack

      comps.add(comp);
      return comp;
   }


   Test(Scanner in)
   {
      g = new Graph(in.nextInt());
      int M = in.nextInt();

      while (M-->0)
      {
         int i = in.nextInt()-1;
         int j = in.nextInt()-1;
         g.add(i, j);
      }

      stk = new int[g.M+2];
      pre = new int[g.N];
      low = new int[g.N];
      used = new boolean[g.M+1];
      Arrays.fill(pre, -1);
      bridges = new ArrayList<>();
      comps = new ArrayList<>();
      art = new boolean[g.N];

      for (int i=0; i<g.N; i++)
      {
         if (pre[i] == -1)
         {
            used[g.M] = false;

            dfs(i, i, g.M);   

            if (stk[0] > 1)
               makeComp(g.M);
            stk[0] = 0;
         }
      }

      System.out.printf("Bridges are: %s%n", bridges);

      System.out.println("------------");

      ArrayList<Integer> arts = new ArrayList<>();
      for (int i=0; i<g.N; i++)
         if (art[i])
            arts.add(i+1);

      System.out.printf("Art pts are: %s", arts);


      System.out.printf("%n%d 2-vertex-connected components found:%n", comps.size());
      for (ArrayList<Integer> comp : comps)
         System.out.println(comp);
   }

   class Graph
   {
      int N, M;
      ArrayList<Edge>[] adj;

      Graph(int NN)
      {
         M = 0;
         adj = new ArrayList[N=NN];
         for (int i=0; i<N; i++)
            adj[i] = new ArrayList<>();
      }

      void add(int i, int j)
      {
         adj[i].add(new Edge(j, M));
         adj[j].add(new Edge(i, M));
         M++;
      }
   }

   class Edge
   {
      int j, id;
  
      Edge(int jj, int ii)
      {
         j=jj; id=ii;
      }
   }
}
