
import java.util.*;
import java.io.*;

public class TopSort
{
   public static void main(String[] args) throws Exception
   {
      PrintWriter out = new PrintWriter(System.out);
      new TopSort(new FastScanner(System.in), out);
      out.close();
   }

   public TopSort(FastScanner in, PrintWriter out)
   {
      Graph g = new Graph(in.nextInt());
      int M = in.nextInt(); 

      int[] indeg = new int[g.N];
      while (M-->0)
      {
         int i = in.nextInt()-1;
         int j = in.nextInt()-1;
         g.add(i,j);

         indeg[j]++;
      }

      PriorityQueue<Integer> q = new PriorityQueue<>();
      for (int i=0; i<g.N; i++)
         if (indeg[i] == 0)
            q.add(i);
   
      StringBuilder sb = new StringBuilder();
      while (q.size() > 0)
      {
         int i = q.poll();
         sb.append(i+1);
         sb.append(' ');

         for (int j : g.adj[i])
         {
            indeg[j]--;
         
            if (indeg[j] == 0)
               q.add(j);
         }
      }

      out.println(sb.toString().trim());
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
