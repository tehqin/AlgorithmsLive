
import java.util.*;
import java.io.*;

public class A
{
   public static void main(String[] args) throws Exception
   {
      PrintWriter out = new PrintWriter(System.out);
      new A(new FastScanner(System.in), out);
      out.close();
   }

   int N;
   ArrayList<Edge>[] adj;
   int[] incValue;
   int globalSum;

   int[] colorCount;
   void dfs(int i, int p)
   {
      for (Edge e : adj[i])
         colorCount[e.color]++; 
   
      for (Edge e : adj[i])
      {
         if (e.j == p)
         {
            if (colorCount[e.color] > 1)
            {
               globalSum++;
               incValue[i]--;
            }
         }
         else
         {
            if (colorCount[e.color] > 1)
            {
               incValue[e.j]++;
            }
         }
      }
      
      for (Edge e : adj[i])
         colorCount[e.color]--; 
   
      for (Edge e : adj[i]) if (e.j != p)
      {
         incValue[e.j] += incValue[i];
         dfs(e.j, i);
      }
   }

   public A(FastScanner in, PrintWriter out)
   {
      N = in.nextInt();
      adj = new ArrayList[N];
      for (int i=0; i<N; i++)
         adj[i] = new ArrayList<>();

      for (int u=1; u<N; u++)
      {
         int i = in.nextInt()-1;
         int j = in.nextInt()-1;
         int c = in.nextInt();
         adj[i].add(new Edge(j, c));
         adj[j].add(new Edge(i, c));
      }

      globalSum = 0;
      incValue = new int[N];
      colorCount = new int[N];
      dfs(0, 0);
   
      int res = 0;
      for (int i=0; i<N; i++)
         if (globalSum+incValue[i] == 0)
            res++;
      out.println(res);
      for (int i=0; i<N; i++)
         if (globalSum+incValue[i] == 0)
            out.println(i+1);   
   }

   class Edge
   {
      int j, color;

      Edge(int jj, int cc)
      {
         j=jj;
         color = cc;
      }
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
