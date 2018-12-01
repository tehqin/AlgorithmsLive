import java.util.*;
import java.util.regex.*;
import java.text.*;
import java.math.*;
import java.awt.geom.*;

public class FloorBoards
{
	int W, H, oo = 987654321;
	boolean[][] blocked;
	Integer[][][][] memo;
	
	int go(int b, int i, int j, int mask)
	{
		if (i == W)
			return go(0, 0, j+1, mask);
		if (j == H)
			return 0;
		if (memo[b][i][j][mask] != null)
			return memo[b][i][j][mask];
	
		int res = oo;
		int missingCol = ((1 << W) - 1 - (1 << i)) & mask;
		
		if (blocked[i][j])
		{
			res = go(0, i+1, j, missingCol);
		}
		else
		{
			if (b == 1)
			   res = Math.min(res, go(b, i+1, j, missingCol));
			if ((mask & (1<<i)) > 0)
			   res = Math.min(res, go(0, i+1, j, mask));
			  
			res = Math.min(res, 1+go(1, i+1, j, missingCol));
			res = Math.min(res, 1+go(0, i+1, j, missingCol | (1 << i)));
		}
		
		return memo[b][i][j][mask] = res;
	}

	public int layout(String[] room)
	{
		H = room.length;
		W = room[0].length();
		
		memo = new Integer[2][W][H][1 << W];
		blocked = new boolean[W][H];
		for (int i=0; i<W; i++)
			for (int j=0; j<H; j++)
				blocked[i][j] = room[j].charAt(i) == '#';
		
		return go(0, 0, 0, 0);	
	}
}
//Powered by [KawigiEdit] 2.0!
