
import java.util.*;
import java.io.*;

public class F
{
   public static void main(String[] args) throws Exception
   {
      FastScanner in = new FastScanner(System.in);
      PrintWriter out = new PrintWriter(System.out);
      
      int tc = 1;
      while (new F().solve(tc++, in, out));
      
      out.close();
   }

   boolean solve(int tc, FastScanner in, PrintWriter out)
   {
      int N = in.nextInt();
      if (N == 0)
         return false;

      int C = in.nextInt();
      int D = in.nextInt();

      Machine[] machines = new Machine[N];
      for (int i=0; i<N; i++)
         machines[i] = new Machine(in);

      Arrays.sort(machines, (a,b) -> Integer.compare(a.day, b.day));

      
      TreeSet<Sol> hull = new TreeSet<>();
      
      long maxMoney = C;
      for (Machine m : machines)
      {
         //System.out.printf("Process Machine %d%n", m.day);
         while (hull.size() >= 2)
         {
            Sol fst = hull.first();
            Sol snd = hull.higher(fst);
         
            if (fst.moneyEarned(m.day-1) > snd.moneyEarned(m.day-1))
               break; 

            hull.pollFirst();
         }

         if (hull.size() > 0)
            maxMoney = Math.max(maxMoney, hull.first().moneyEarned(m.day-1));
         
         if (m.price > maxMoney)
            continue;
      
         long prevMoney = maxMoney - m.price + m.resale;
         Sol part = new Sol(m.day, m.moneyPerDay, prevMoney);

         Sol same = hull.ceiling(part);

         if (same != null && same.moneyPerDay == part.moneyPerDay)
         {
            if (same.moneyEarned(m.day) >= part.moneyEarned(m.day))
               continue;
            hull.remove(same);
         }

         Sol above = hull.higher(part);
         Sol below = hull.lower(part);

         if (above != null && below != null && below.to(part) >= part.to(above))
            continue;

         while (true)
         {
            Sol up1 = hull.higher(part);
            if (up1 == null) break;

            Sol up2 = hull.higher(up1);
            if (up2 == null) break;
         

            long t1 = part.to(up1);
            long t2 = up1.to(up2);

            if (t1 < t2) break;
            
            hull.remove(up1);
         }

         while (true)
         {
            Sol lo1 = hull.lower(part);
            if (lo1 == null) break;

            Sol lo2 = hull.lower(lo1);
            if (lo2 == null) break;
         
            
            long t1 = lo2.to(lo1);
            long t2 = lo1.to(part);

            if (t1 < t2) break;

            hull.remove(lo1);
         }

         hull.add(part);
      }

      for (Sol s : hull)
         maxMoney = Math.max(maxMoney, s.moneyEarned(D));

      System.out.printf("Case %d: %d%n", tc, maxMoney);

      return true;
   }
}

class Machine
{
   int day;
   int price;
   int resale;
   int moneyPerDay;

   Machine(FastScanner in)
   {
      day = in.nextInt();
      price = in.nextInt();
      resale = in.nextInt();
      moneyPerDay = in.nextInt();
   }
}

class Sol implements Comparable<Sol>
{
   int start;
   int moneyPerDay;
   long prevMoney;

   Sol(int ss, int mm, long pp)
   {
      start = ss;
      moneyPerDay = mm;
      prevMoney = pp;
   }

   long moneyEarned(long day)
   {
      long delta = day-start;
      return moneyPerDay * delta + prevMoney;
   }

   long to(Sol rhs)
   {
      long lo = Math.max(start, rhs.start);
      long hi = lo;

      while (moneyEarned(hi) >= rhs.moneyEarned(hi))
      {
         lo = hi;
         hi *= 2;
      }
   
      while (lo < hi)
      {
         long m = (lo+hi) / 2;

         if (moneyEarned(m) >= rhs.moneyEarned(m))
            lo = m+1;
         else
            hi = m;
      }

      return hi;
   }

   public int compareTo(Sol rhs)
   {
      return Integer.compare(moneyPerDay, rhs.moneyPerDay);
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
