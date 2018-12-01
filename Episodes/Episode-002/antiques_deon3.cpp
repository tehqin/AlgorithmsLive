/**
 * Fancy Antiques - Solution 2 by Deon Nicholas
 *
 * This does a recursive brute-force search over the stores.
 * It uses the same heuristic as Solution 1 (if a store is not 
 * chosen to be in the set, then all "neighboring" stores --
 * those which share a common item -- MUST be in the set).
 * This is provably fast, runs in O(N * 1.6181^M) where 1.6181 is
 * phi (i.e.: the recurrence follows the fibonacci numbers).
 *
 * However, it is a bit faster than Solution 1 because we employ another
 * little trick: as we are constructing the solution we keep a tentative
 * "cost". When we get to the end (i.e.: K stores), the tentative
 * cost is the real cost. We do this by always overestimating
 * the cost of the solution: at any time, assume an item will
 * be purchased at the more expensive store. We only switch
 * to the "cheaper" variant (and decrease our tentative cost by
 * the associated "slack" or difference between the stores)
 * when we specifically select the cheaper of the two stores
 * for that item. In the end, if the solution is valid,
 * then the costs will actually match up.
 *
 * Anyway, the solution below is easier to understand than
 * Solution 1 (less hacky), but harder to prove correct
 * because it does not make explicit use of the "heuristic".
 */
#include <cassert>
#include <algorithm>
#include <cstring>
#include <cmath>

using namespace std;

#define FOR(i,n) for(int i=0;i<(n);++i)

#define INF 1000000001
#define MAX_STORES 41
#define MAX_ITEMS 101

typedef unsigned long long ull;

// inputs
int A[MAX_ITEMS];
int P[MAX_ITEMS];
int B[MAX_ITEMS];
int Q[MAX_ITEMS];
int N,M,K;

// 
int slack[MAX_STORES];
bool must[MAX_STORES];

// brute force state variables
int cost;       // current cost of selection
int j;          // current store index

ull nbrs[MAX_STORES];
ull taken;

// find a solution with maximum "slack" / minimum "cost"
int brute_force() {
  if (j == M) return cost;

  // take store
  int ans_take = INF;
  if (K > 0)
  {
    cost -= slack[j]; // add
    K--;
    taken ^= (1ull << j);
    ++j;

      ans_take = brute_force();
    
    --j;
    taken ^= (1ull << j);
    K++;
    cost += slack[j];
  }

  // skip store
  int ans_skip = INF;
  bool can_skip = !(nbrs[j] & (~taken));
  if (can_skip)
  {
    ++j;
      ans_skip = brute_force();
    --j;
  }

  return min(ans_take, ans_skip);
}

// we say that the "slack" of a store (for a given item)
// is the amount you save for that item at that store
// (in comparison to the alternative). Or we set it to 0, if
// this store is the more expensive one for this item.
//
// the total slack of a store is the sum of slack values for
// all items sold at that store.
void add_item(int store, int cost_here, int cost_other, int item_idx) {
  slack[store] += max(0, cost_other - cost_here);
}

int main() {
  scanf("%d%d%d",&N,&M,&K);
  assert(1<=N&&N<=100);
  assert(1<=M&&M<=40);
  assert(1<=K&&K<=M);
  int ans = 0;
  FOR(i,N) {
    scanf("%d%d%d%d",&A[i],&P[i],&B[i],&Q[i]);
    assert(1<=A[i]&&A[i]<=M);
    assert(1<=B[i]&&B[i]<=M);
    assert(1<=P[i]&&P[i]<=10000000);
    assert(1<=Q[i]&&Q[i]<=10000000);
    A[i]--; B[i]--;
    add_item(A[i], P[i], Q[i], i);
    add_item(B[i], Q[i], P[i], i);
    ans += max(P[i], Q[i]);
    if (A[i] <= B[i]) nbrs[A[i]] |= 1ull << B[i];
    if (B[i] <= A[i]) nbrs[B[i]] |= 1ull << A[i];
  }

  ans += brute_force();
  assert(ans >= 0);
  if (ans >= INF) printf("-1\n");
  else printf("%d\n",ans);

  // the brute force should leave no side-effects
  assert(j == 0);
  assert(cost == 0);
}













