#include <vector>
#include <iostream>
using namespace std;
//#define DEBUG_VAR(A) cerr << #A << "=" << A << endl
#define DEBUG_VAR(A)


class MaintainPlanes {
  public:
    vector<int> schedule( int M, int N, double home_lat, double home_rlng, vector<double> positions ) {
       DEBUG_VAR(M);
       DEBUG_VAR(N);
       vector<int> result( N*M, 0 );
       for ( int i=0; i<(N*M); ++i ) {
         result[i] = i%N;
         DEBUG_VAR(result[i]);
       }
       DEBUG_VAR(result.size());
       return result;
    }

};
