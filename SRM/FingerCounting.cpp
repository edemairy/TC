#include <vector>
#include <list>
#include <map>
#include <set>
#include <deque>
#include <stack>
#include <bitset>
#include <algorithm>
#include <functional>
#include <numeric>
#include <utility>
#include <sstream>
#include <iostream>
#include <iomanip>
#include <cstdio>
#include <cmath>
#include <cstdlib>
#include <ctime>

using namespace std;

#define all(c) c.begin(), c.end()
#define forall(c, it) for( typeof((c).begin()) it = (c).begin(); it != (c).end(); ++it )
// Containers with find (set, map)
#define present(c, v) ((c).find(v) != (c).end())
// Containers without embedded find (vector, list)
#define cpresent(c, v) ((c).count(v))

class FingerCounting {
public:
	int maxNumber(int, int);
};

int FingerCounting::maxNumber(int w, int m) {
	int r = 0;
	switch(w) {
	  case 1 :
	  case 5 : r = m*8+w-1; 
	              break;
	  case 2 :
	  case 3 :
	  case 4 : int a = w-1;
	               r = (m/2)*8+a+(m%2)*2*(5-w);
	               break;
	}
	return r;
}


//Powered by [KawigiEdit] 2.0!
