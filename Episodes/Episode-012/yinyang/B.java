
import java.util.*;
import java.io.*;

public class B
{
   public static void main(String[] args) throws Exception
   {
      FastScanner in = new FastScanner(new FileInputStream("yinyang.in"));
      PrintWriter out = new PrintWriter(new File("yinyang.out"));
      //FastScanner in = new FastScanner(System.in);
      //PrintWriter out = new PrintWriter(System.out);
      new B(in, out);
      out.close();
   }

   int N;
   boolean[] blocked;
   int[] parent, subtreeSum;
   ArrayDeque<Integer> q = new ArrayDeque<>();
   ArrayList<Edge>[] adj;

   void calcSum(int i, int p)
   {
      parent[i] = p;
      subtreeSum[i] = 1;

      for (Edge e : adj[i])
      {
         if (e.j != p && !blocked[e.j])
         {
            calcSum(e.j, i); 
            subtreeSum[i] += subtreeSum[e.j];
         }
      }
   }

   int offset;
   int[] depthSeen;
   ArrayDeque<Path> pathList;

   void findPaths(int i, int p, int sum)
   {
      pathList.add(new Path(sum, depthSeen[sum+offset] > 0));   

      depthSeen[sum+offset]++;

      for (Edge e : adj[i])
      {
         if (e.j == p) continue;
         if (blocked[e.j]) continue;

         findPaths(e.j, i, e.w+sum);
      }

      depthSeen[sum+offset]--;
   }

   int[] pathSat, pathUnsat;
   long solveTree(int root, int compSize)
   {
      for (int i=offset-compSize+1; i<=offset+compSize-1; i++)
      {
         pathSat[i] = 0;
         pathUnsat[i] = 0;
      }

      long res = 0;
      for (Edge e : adj[root])
      {
         if (blocked[e.j])
            continue;

         pathList.clear();
         findPaths(e.j, root, e.w);
         
         for (Path p : pathList)
         {
            if (p.t || p.w == 0)
               res += pathUnsat[offset-p.w];
            res += pathSat[offset-p.w];

            if (p.w == 0 && p.t)
               res++;
         }


         for (Path p : pathList)
         {
            if (p.t)
               pathSat[p.w+offset]++;
            else
               pathUnsat[p.w+offset]++;
         }
      }

      return res;
   }


   long go(int entryPoint)
   {
      calcSum(entryPoint, entryPoint); 

      int centroid = entryPoint;
      int bestSize = subtreeSum[entryPoint];

      int compSize = 0; 
      q.add(entryPoint);
      while (q.size() > 0)
      {
         int i = q.poll();
         compSize++;
         
         int size = subtreeSum[entryPoint] - subtreeSum[i];

         for (Edge e : adj[i])
         {
            if (e.j != parent[i] && !blocked[e.j])
            {
               size = Math.max(size, subtreeSum[e.j]);
               q.add(e.j);
            }
         }

         if (size < bestSize)
         {
            centroid = i;
            bestSize = size;
         }
      }

      long ways = solveTree(centroid, compSize);

      blocked[centroid] = true;
      for (Edge e : adj[centroid])
         if (!blocked[e.j])
            ways += go(e.j);
   
      return ways; 
   }

   public B(FastScanner in, PrintWriter out)
   {
      N = in.nextInt();
      parent = new int[N];
      subtreeSum = new int[N];
      blocked = new boolean[N];
      adj = new ArrayList[N];
      for (int i=0; i<N; i++)
         adj[i] = new ArrayList<>();
   

      offset = N;
      pathSat = new int[2*N+1];
      pathUnsat = new int[2*N+1];
      pathList = new ArrayDeque<>();
      depthSeen = new int[2*N+1];

      for (int u=1; u<N; u++)
      {
         int i = in.nextInt()-1;
         int j = in.nextInt()-1;

         int w = 2*in.nextInt()-1;

         adj[i].add(new Edge(j, w));
         adj[j].add(new Edge(i, w));
      }

      long res = go(0);
      out.println(res);
   }
}

class Edge
{
   int j, w;

   Edge(int jj, int ww)
   {
      j=jj; w=ww;
   }
}

class Path
{
   int w;
   boolean t;

   Path(int ww, boolean tt)
   {
      w=ww;
      t=tt;
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
