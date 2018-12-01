import java.util.*;
import java.util.regex.*;
import java.text.*;
import java.math.*;
import java.awt.geom.*;

class Prob implements Comparable<Prob>
{
	int mp;
	int ppm;
	int rt;
	
	public Prob(int m, int p, int r)
	{
		mp = m;
		ppm = p;
		rt = r;
	}
	
	long score(int t)
	{
		return mp-ppm*1L*t;
	}
	
	public int compareTo(Prob rhs)
	{
		long v1 = ppm*1L*rhs.rt;
		long v2 = rhs.ppm*1L*rt;
		
		if (v1 > v2)
			return -1;
		if (v1 < v2)
			return 1;
		return 0;
	}
	
	public String toString()
	{
		return String.format("PROB %d %d %d", mp, ppm, mp);
	}
}

public class TheProgrammingContestDivOne
{
	Prob[] vs;
	int[][] memo;
	int FT;
	int N;
	
	int go(int p, int t)
	{
		if (t >= FT)
			return 0;
		if (p >= N)
			return 0;
		
		if (memo[p][t] != -1)
			return memo[p][t];
	
		int res = Math.max(0, go(p+1, t));
		int nt = t + vs[p].rt;
		if (nt <= FT)
			res = (int)Math.max(res, vs[p].score(nt)+go(p+1, nt));
		memo[p][t] = res;
		return res;
	}

	public int find(int T, int[] maxPoints, int[] pointsPerMinute, int[] requiredTime)
	{
		FT = T;
		N = maxPoints.length;
		vs = new Prob[N];
		for (int i=0; i<N; i++)
			vs[i] = new Prob(maxPoints[i], pointsPerMinute[i], requiredTime[i]);
		Arrays.sort(vs);
		//System.out.println(Arrays.toString(vs));
		memo = new int[N][T];
		for (int[] mem : memo)
			Arrays.fill(mem, -1);
		return go(0, 0);
	}
	public static void main(String[] args)
	{
		long time;
		int answer;
		boolean errors = false;
		int desiredAnswer;
		
		
		time = System.currentTimeMillis();
		answer = new TheProgrammingContestDivOne().find(74, new int[]{502}, new int[]{2}, new int[]{47});
		System.out.println("Time: " + (System.currentTimeMillis()-time)/1000.0 + " seconds");
		desiredAnswer = 408;
		System.out.println("Your answer:");
		System.out.println("\t" + answer);
		System.out.println("Desired answer:");
		System.out.println("\t" + desiredAnswer);
		if (answer != desiredAnswer)
		{
			errors = true;
			System.out.println("DOESN'T MATCH!!!!");
		}
		else
			System.out.println("Match :-)");
		System.out.println();
		time = System.currentTimeMillis();
		answer = new TheProgrammingContestDivOne().find(40000, new int[]{100000, 100000}, new int[]{1, 100000}, new int[]{50000, 30000});
		System.out.println("Time: " + (System.currentTimeMillis()-time)/1000.0 + " seconds");
		desiredAnswer = 0;
		System.out.println("Your answer:");
		System.out.println("\t" + answer);
		System.out.println("Desired answer:");
		System.out.println("\t" + desiredAnswer);
		if (answer != desiredAnswer)
		{
			errors = true;
			System.out.println("DOESN'T MATCH!!!!");
		}
		else
			System.out.println("Match :-)");
		System.out.println();
		time = System.currentTimeMillis();
		answer = new TheProgrammingContestDivOne().find(75, new int[]{250, 500, 1000}, new int[]{2, 4, 8}, new int[]{25, 25, 25});
		System.out.println("Time: " + (System.currentTimeMillis()-time)/1000.0 + " seconds");
		desiredAnswer = 1200;
		System.out.println("Your answer:");
		System.out.println("\t" + answer);
		System.out.println("Desired answer:");
		System.out.println("\t" + desiredAnswer);
		if (answer != desiredAnswer)
		{
			errors = true;
			System.out.println("DOESN'T MATCH!!!!");
		}
		else
			System.out.println("Match :-)");
		System.out.println();
		time = System.currentTimeMillis();
		answer = new TheProgrammingContestDivOne().find(30, new int[]{100, 100, 100000}, new int[]{1, 1, 100}, new int[]{15, 15, 30});
		System.out.println("Time: " + (System.currentTimeMillis()-time)/1000.0 + " seconds");
		desiredAnswer = 97000;
		System.out.println("Your answer:");
		System.out.println("\t" + answer);
		System.out.println("Desired answer:");
		System.out.println("\t" + desiredAnswer);
		if (answer != desiredAnswer)
		{
			errors = true;
			System.out.println("DOESN'T MATCH!!!!");
		}
		else
			System.out.println("Match :-)");
		System.out.println();
		
		
		if (errors)
			System.out.println("Some of the test cases had errors :-(");
		else
			System.out.println("You're a stud (at least on the test data)! :-D ");
	}

}
//Powered by [KawigiEdit] 2.0!
