
import java.util.*;

public class DesertWind
{
   public int daysNeeded(String[] theMap)
   {
      int H = theMap.length;
      int W = theMap[0].length();

      int oo = 987654321;
      int[][] dist = new int[W][H];
      
      int goalX = -1;
      int goalY = -1;
      boolean[][] blocked = new boolean[W][H];
      for (int i=0; i<W; i++)
      {
         for (int j=0; j<H; j++)
         {
            dist[i][j] = oo;
            char c = theMap[j].charAt(i);
            if (c == 'X')
               blocked[i][j] = true;
            else if (c == '@')
            {
               goalX = i;
               goalY = j;
            }
            else if (c == '*')
            {
               dist[i][j] = 0;
            }
         }
      }
       
      // Bellman ford
      boolean updated = true;
      while (updated)
      {
         updated = false;
      
         for (int i=0; i<W; i++)
         {
            for (int j=0; j<H; j++)
            {
               if (blocked[i][j])
                  continue;
            
               int cheapestNeighbor1 = oo;
               int cheapestNeighbor2 = oo;
            
               for (int dx=-1; dx<=1; dx++)
               {
                  for (int dy=-1; dy<=1; dy++)
                  {
                     if (dx == 0 && dy == 0)
                        continue;

                     int ni = i+dx;
                     int nj = j+dy;
					 if (ni < 0 || ni >= W || nj < 0 || nj >= H)
                        continue;
                     if (blocked[ni][nj])
                        continue;
                  
                     int cost = dist[ni][nj] + 1;
                     if (cost <= cheapestNeighbor1)
                     {
                        cheapestNeighbor2 = cheapestNeighbor1;
                        cheapestNeighbor1 = cost;
                     }
                     else if (cost < cheapestNeighbor2)
                     {
                        cheapestNeighbor2 = cost;
                     }
                  }
               }

               int newCost = Math.min(cheapestNeighbor1+2, cheapestNeighbor2);
               if (newCost < dist[i][j])
               {
                  updated = true;
                  dist[i][j] = newCost; 
               }
            }
         }
      }

      if (dist[goalX][goalY] == oo)
         return -1;
      return dist[goalX][goalY];
   }
}
