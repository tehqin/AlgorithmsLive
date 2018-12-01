
import java.util.*;
import java.io.*;

public class Flow
{
   public static void main(String[] args) throws Exception
   {
      PrintWriter out = new PrintWriter(System.out);
      new Flow(new FastScanner(System.in), out);
      out.close();
   }

   public Flow(FastScanner in, PrintWriter out)
   {
      int numNodes = in.nextInt();
      int numEdges = in.nextInt();
      int source = in.nextInt()-1;
      int sink = in.nextInt()-1;

      Solver solver = new Solver(numNodes, source, sink);
      for (int curEdge=0; curEdge<numEdges; curEdge++)
      {
         int i = in.nextInt()-1;
         int j = in.nextInt()-1;
         int cap = in.nextInt();
         solver.addEdge(i, j, cap);
      }

      out.println(solver.run());
   }
}

class Solver
{
   int numNodes, source, sink; 
   boolean[] seen;
   int[][] cap;

   public Solver(int numNodes, int source, int sink)
   {
      this.numNodes = numNodes;
      this.source = source;
      this.sink = sink;

      cap = new int[numNodes][numNodes];
      seen = new boolean[numNodes];
   }

   void addEdge(int i, int j, int c)
   {
      cap[i][j] += c;
   }

   int dfs(int i, int amount)
   {
      if (i == sink)
         return amount;
      seen[i] = true;

      for (int j=0; j<numNodes; j++)
      {
         if (cap[i][j] > 0 && !seen[j])
         {
            int sent = dfs(j, Math.min(amount, cap[i][j]));
            if (sent > 0)
            {
               cap[i][j] -= sent;
               cap[j][i] += sent;
               return sent;       
            }
         }
      }

      return 0;
   }

   int run()
   {
      int total = 0;
      int sent = -1;
      while (sent != 0)
      {
         Arrays.fill(seen, false); 
         sent = dfs(source, Integer.MAX_VALUE);
         total += sent;
      }

      return total;
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
