
import java.util.*;
import java.io.*;

public class HappyTrails
{
   public static void main(String[] args) throws Exception
   {
      PrintWriter out = new PrintWriter(System.out);
      new HappyTrails(new FastScanner(System.in), out);
      out.close();
   }

   int N, M, oo = 987654321;
   DisjointSet ds;
   Edge[] edges;

   Partition createPartition(int[] choices)
   {
      ds.reset();

      int mstCost = 0;
      int[] mstEdges = new int[N-1];
      Arrays.fill(mstEdges, -1);

      int ptr = 0;
      for (int x=0; x<M; x++)
      {
         if (choices[x] == 1)
         {
            Edge e = edges[x];
            ds.union(e.i, e.j);
            mstEdges[ptr++] = x;
            mstCost += e.w;
         }
      }

      for (int x=0; x<M; x++)
      {
         if (ds.numComps == 1)
            break;

         if (choices[x] == 0)
         {
            Edge e = edges[x];
            if (ds.find(e.i) != ds.find(e.j))
            {
               ds.union(e.i, e.j);
               mstEdges[ptr++] = x;
               mstCost += e.w;
            }
         }
      }

      if (ds.numComps > 1)
         return null;
   
      Arrays.sort(mstEdges);
      return new Partition(choices, mstCost, mstEdges);
   }

   int solve(int K)
   {
      PriorityQueue<Partition> q = new PriorityQueue<>();
      q.add(createPartition(new int[M]));

      while (q.size() > 0)
      {
         Partition p = q.poll();
         K--;

         if (K == 0)
            return p.mstCost;


         for (int x=0; x<N-1; x++)
         {
            if (p.choices[p.mstEdges[x]] == 0)
            {
               int[] choices = Arrays.copyOf(p.choices, M);
               choices[p.mstEdges[x]] = -1;

               for (int y=0; y<x; y++)
                  choices[p.mstEdges[y]] = 1;

               Partition nxt = createPartition(choices);
               if (nxt != null)
                  q.add(nxt);
            }
         }
      }


      return -1;   
   }

   public HappyTrails(FastScanner in, PrintWriter out)
   {
      N = in.nextInt();
      M = in.nextInt();
      int K = in.nextInt();
      ds = new DisjointSet(N);

      edges = new Edge[M];
      for (int u=0; u<M; u++)
         edges[u] = new Edge(in.nextInt()-1, in.nextInt()-1, in.nextInt());
      Arrays.sort(edges);
   
      out.println(solve(K));
   }
}

class Edge implements Comparable<Edge>
{
   int i, j, w;

   Edge(int ii, int jj, int ww)
   {
      i=ii;
      j=jj;
      w=ww;
   }

   public int compareTo(Edge rhs)
   {
      return Integer.compare(w, rhs.w);
   }
}

class Partition implements Comparable<Partition>
{
   int[] choices;
   int mstCost;
   int[] mstEdges;

   Partition(int[] choices, int mstCost, int[] mstEdges)
   {
      this.choices = choices;
      this.mstCost = mstCost;
      this.mstEdges = mstEdges;
   }

   public int compareTo(Partition rhs)
   {
      return Integer.compare(mstCost, rhs.mstCost);
   }

}

class DisjointSet
{
   int numComps;
	int[] p, r;

	DisjointSet(int s) 
   {
		p = new int[s];
		r = new int[s];
      reset();
   }

   void reset()
   {
      numComps = p.length;
		for(int i=0; i<p.length; i++)
      {
			p[i] = i;
         r[i] = 0;
      }
   }

	void union(int x, int y)
   {
		int a = find(x);
		int b = find(y);
		if(a==b) return;

      numComps--;
		if(r[a] == r[b])
			r[p[b]=a]++;
		else 
			p[a]=p[b]=r[a]<r[b]?b:a;
	}

	int find(int x) 
   {
      if (p[x] == x)
         return x;
		return p[x] = find(p[x]);
	}
}

class FastScanner
{
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
      if (curChar >= numChars)
      {
         curChar = 0;
         try
         {
            numChars = stream.read(buf);
         } 
         catch (IOException e) 
         {
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
   
   String next()
   {
      int c = read();
      while (isSpaceChar(c))
         c = read();
      StringBuilder res = new StringBuilder();
      do
      {
         res.appendCodePoint(c);
         c = read();
      } while(!isSpaceChar(c));
      return res.toString();
   }
   
   String nextLine()
   {
      int c = read();
      while (isEndline(c))
         c = read();
      
      StringBuilder res = new StringBuilder();
      do
      {
         res.appendCodePoint(c);
         c = read();
      } while(!isEndline(c));
      return res.toString();
   }
}
