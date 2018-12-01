import java.util.*;
import java.util.regex.*;
import java.text.*;
import java.math.*;
import java.awt.geom.*;

public class OrderOfTheHats
{
	public int minChanged(String[] spellChart)
	{
		int N = spellChart.length;
		int[] adjMat = new int[N];
		for (int i=0; i<N; i++)
			for (int j=0; j<N; j++)
				if (spellChart[i].charAt(j) == 'Y')
					adjMat[i] |= (1 << j);
		
		int[] bitCount = new int[1<<N];
		for (int mask=0; mask<(1<<N); mask++)
			bitCount[mask] = Integer.bitCount(mask);
		
		int oo = 987654321;
		int[] dp = new int[1 << N];
		Arrays.fill(dp, oo);
		dp[0] = 0;
		for (int mask=0; mask<dp.length; mask++)
		{
			for (int i=0; i<N; i++)
			{
				if (((1<<i)&mask) > 0) continue;
			
				int nxtMask = (1<<i)|mask;
				int rr = dp[mask] + bitCount[nxtMask & adjMat[i]];
				dp[nxtMask] = Math.min(dp[nxtMask], rr);
			}
		}
		
		return dp[(1<<N)-1];
	}
}
//Powered by [KawigiEdit] 2.0!
