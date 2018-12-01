/**
 * Fancy Antiques - Solution 1 by Deon Nicholas
 *
 * The main idea is similar to the "minimum cost vertex cover" problem.
 * Each store is a node, each item is an edge between a pair of stores.
 * We want to select a set of K nodes (stores) such that each
 * edge (item) is covered by a node in the set. This is a vertex cover.
 * We want to minimize a certain cost function of the vertex cover.
 *
 * The trick is: For any node x, either:
 *   a) x must be in the set, or 
 *   b) all neighbors of x must be in the set.
 *
 * You can do a recursive brute force search, exploiting this fact as
 * a heuristic. If F(m) is the number of combinations you need to check for
 * a graph with m nodes, then you try to:
 *   a) put x in the set, delete it, and recurse (= F(m-1) choices), OR
 *   b) put all neighors of x in the set, delete them and x (<= F(m-2) choices)
 * 
 * (Note: This ignores nodes with 0-edges or self-loops, which can be handled
 * separately as special cases.)
 *
 * So you can prove that F(m) <= F(m-1) + F(m-2). So the number of choices
 * does not grow larger than the fibonacci numbers. This works within
 * time for m <= 40. The whole solution is something like O(N * 1.618^M) and
 * generally faster in practice (you can apparently get O(1.2738^M)).
 *
 * NOTE: My solution below uses bit hacks. I use 64-bit integers to represent
 * bitsets to make things a little less slow. Not sure if these hacks
 * were necessary though.
 */
#include <cassert>
#include <algorithm>
#include <cstring>
#include <cmath>

using namespace std;

#define INF 2000000001

#define MAXN 101
#define MAXM 41
#define MAXK MAXM
#define MAX_NODE MAXM
#define MAX_ITEMS MAXN

// Bit hacks
typedef unsigned long long BITMASK;
#define has_bit(bit_mask, x) ((bit_mask) & (1ULL << (x)))
#define turn_on_bit(bit_mask, x) (bit_mask |= (1ULL << (x)))
#define turn_off_bit(bit_mask, x) (bit_mask &= (~(1ULL << (x))))
#define smallest_on_bit(bit_mask) (__builtin_ctzll((bit_mask) & (-(bit_mask))))

// The "graph"
BITMASK neighbors[MAX_NODE];
bool must[MAX_NODE];
inline void add_edge(int a, int b) {
	neighbors[a] |= (1ULL << b);
	neighbors[b] |= (1ULL << a);
}

// inputs
int A[MAX_ITEMS];
int P[MAX_ITEMS];
int B[MAX_ITEMS];
int Q[MAX_ITEMS];
int N,M,K;

int backtrack(BITMASK alive_stores, BITMASK chosen_stores) {
	int sz = __builtin_popcountll(chosen_stores);
	if (sz > K) return INF;	// invalid set
	assert((chosen_stores & alive_stores) == 0);	// no store is both alive and chosen

	// Base case. End once you have K stores (or no stores left to pick).
	// Check if this is a valid cover
	if (sz == K || alive_stores == 0) {
		int total_cost = 0;

		int a_cost, b_cost, i_cost;
		for(int i=0; i<N; ++i) {
			a_cost = has_bit(chosen_stores, A[i]) ? P[i] : INF;
			b_cost = has_bit(chosen_stores, B[i]) ? Q[i] : INF;

			i_cost = min(a_cost, b_cost);
			if (i_cost == INF) return INF;
			total_cost += i_cost;
		}

		return total_cost;
	}

	// Pick the untouched store with smallest index j
	int j = smallest_on_bit(alive_stores);
	assert(j >= 0);
	assert(has_bit(alive_stores, j));
	assert(((alive_stores >> j) << j) == alive_stores);

	int ans = INF;

	// Try with j in the set
	{
		BITMASK new_alive_stores = alive_stores;
		BITMASK new_chosen_stores = chosen_stores;
		
		turn_on_bit(new_chosen_stores, j);		// choose store j
		turn_off_bit(new_alive_stores, j);		// delete store j
		
		ans = min(ans, backtrack(new_alive_stores, new_chosen_stores));
	}

	// Try all neighbors of j in the set
	{
		BITMASK new_alive_stores = alive_stores;
		BITMASK new_chosen_stores = chosen_stores;
		turn_off_bit(new_alive_stores, j);

		int k;
		for(BITMASK alive_neighbors = (neighbors[j] & alive_stores);
				alive_neighbors;
				alive_neighbors = alive_neighbors&(alive_neighbors - 1)) {
			k = smallest_on_bit(alive_neighbors);
			turn_on_bit(new_chosen_stores, k);
			turn_off_bit(new_alive_stores, k);
		}

		assert((new_chosen_stores & new_alive_stores) == 0);	// no store is both alive and chosen
		ans = min(ans, backtrack(new_alive_stores, new_chosen_stores));
	}

	return ans;
}

int main() {
	scanf("%d%d%d",&N,&M,&K);
	assert(1<=N&&N<=100);
	assert(1<=M&&M<=40);
	assert(1<=K&&K<=M);
	for(int i=0; i<N; ++i) {
		scanf("%d%d%d%d",&A[i],&P[i],&B[i],&Q[i]);
		assert(1<=A[i]&&A[i]<=M);
		assert(1<=B[i]&&B[i]<=M);
		assert(1<=P[i]&&P[i]<=10000000);
		assert(1<=Q[i]&&Q[i]<=10000000);
		A[i]--; B[i]--;	// 0-indexing
		add_edge(A[i],B[i]);
		if (A[i] == B[i]) must[A[i]] = true;	// store has both copies of an item, we must visit it
	}

	BITMASK alive_stores = (1ULL << M) - 1;	// bitmask of "alive" stores
	BITMASK chosen_stores = 0;				// bitmask of "chosen" stores

	// Handle the special cases, or else our solution could be horrible.
	for(int j=0; j<M; ++j) {
		// Special case 1: If a store has no items (node has no edges), then ignore it
		if (!neighbors[j]) turn_off_bit(alive_stores, j);

		// Special case 2: If a store has both copies of an item, then we must visit it
		if (must[j]) turn_on_bit(chosen_stores, j), turn_off_bit(alive_stores, j);
	}

	// Do the backtrack
	int ans = backtrack(alive_stores, chosen_stores);

	if (ans >= INF) printf("-1\n");
	else printf("%d\n", ans);
}














