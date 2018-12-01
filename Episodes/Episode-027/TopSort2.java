
import java.util.*;
import java.io.*;

public class TopSort2
{
   public static void main(String[] args) throws Exception
   {
      PrintWriter out = new PrintWriter(System.out);
      new TopSort2(new FastScanner(System.in), out);
      out.close();
   }
   
   ArrayDeque<Integer> stk;
   int[] state; // 0 - unmarked, 1 - on call stack, 2 - done forever
   Graph g;

   boolean dfs(int i)
   {
      // Cross edge case
      if (state[i] == 2)
         return false;
  
      // Back edge case
      if (state[i] == 1)
         return true;

      state[i] = 1;
      for (int j : g.adj[i])
         if (dfs(j))
            return true;
   
      state[i] = 2;
      stk.push(i);
   
      return false;
   }

   public TopSort2(FastScanner in, PrintWriter out)
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
      state = new int[g.N];
      boolean hasCycle = false;
      for (int i=0; i<g.N; i++)
         if (state[i] == 0)
            hasCycle |= dfs(i);
   
      if (hasCycle)
      {
         out.printf("Not a DAG!!!!%n");
         return;
      }

      while (stk.size() > 0)
      {
         out.printf("%d ", stk.pop()+1); 
      }
      out.println();
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
