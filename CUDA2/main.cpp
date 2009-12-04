#include "MaintainPlanes.h"
#include <math.h>
#include <vector>
#include <stdio.h>
using namespace std;





int main(int argc, char** argv)
{
  MaintainPlanes mp;
  int M, N;
  double home_lat, home_lng, d;
  vector<double> positions;
  scanf("%d%d%lf%lf",&M,&N,&home_lat,&home_lng);
  for(int i = 0; i<M*(N+1)*N*2; i++){
    scanf("%lf",&d);
    positions.push_back(d);
  }
  vector<int> r = mp.schedule(M,N,home_lat,home_lng,positions);
  for(int i = 0; i<M*N; i++)
    printf("%d\n",r[i]);
  fflush(stdout);
}

