
import java.util.*;
import java.io.*;

public class H
{
   public static void main(String[] args) throws Exception
   {
      FastScanner in = new FastScanner(System.in);
      PrintWriter out = new PrintWriter(System.out);
      while (new H().solve(in, out));
      out.close();
   }

   int N, counter;
   int[] invp, next;
   ArrayList<Integer>[] adj;

   void dfs(int i)
   {
      int pre = counter++;
      invp[pre] = i;
   
      for (int j : adj[i])
         dfs(j);

      next[pre] = counter;
   }

   boolean solve(FastScanner in, PrintWriter out)
   {
      N = in.nextInt();
      int K = in.nextInt();
      if (N == 0 && K == 0)
         return false;

      counter = 0;
      invp = new int[N];
      next = new int[N];
      adj = new ArrayList[N];
      for (int i=0; i<N; i++)
         adj[i] = new ArrayList<>();
   
      int[] reward = new int[N];
      int root = -1;
      for (int i=0; i<N; i++)
      {
         int p = in.nextInt()-1;
         if (p == -1)
            root = i;
         else
            adj[p].add(i);
         reward[i] = in.nextInt();
      }

      dfs(root);

      int[] cur = new int[N+1];
      int[] nxt = new int[N+1];
      Arrays.fill(cur, Integer.MIN_VALUE);
      cur[0] = 0;

      for (int k=0; k<K; k++)
      {
         Arrays.fill(nxt, Integer.MIN_VALUE);
         for (int i=0; i<N; i++)
         {
            // i - node I'm on
            // k - num taken
            
            cur[i+1] = Math.max(cur[i+1], cur[i]);
            nxt[next[i]] = Math.max(nxt[next[i]], cur[i] + reward[invp[i]]);
         }
         
         for (int i=0; i<=N; i++)
            cur[i] = nxt[i];
      }

      int res = 0;
      for (int d : cur)
         res = Math.max(res, d);
      out.println(res);

      return true;
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
