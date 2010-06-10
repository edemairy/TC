#include <vector>
#include <math.h>
using namespace std;

class Planarity {
  public:
    vector <int> untangle(int V, vector <int> edges) {
      vector<int> result(2*V);
      for (int i = 0; i < V; ++i ) {
        result[2*i]   = 350+100*cos( i*(2*M_PI / V) );
        result[2*i+1] = 350+100*sin( i*(2*M_PI / V) );
      }
      return result;
    }
};
