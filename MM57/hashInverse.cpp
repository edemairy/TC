#include <sstream>
#include <iostream>
#include <cmath>
#include <assert.h>
using namespace std;
int main(int argc, char** argv) {
  
  long number;
  std::istringstream is( argv[1] );
  is >> number;
  long minBase = number & 0xFFFFFFFFFFFFFFE1;
  long maxBase = number | 0x00000000000000FE; 
  cout << "minBase = " << minBase << endl;
  cout << "maxBase = " << maxBase << endl;
    cout << "maxBase-minBase = " << fabs(maxBase-minBase) << endl;

  assert( fabs(maxBase-minBase) == (126-33+1) );
  long intMin = (ceil(minBase / 33))*33;
  long intMax = (floor(maxBase / 33))*33;
  cout << "intMin = " << intMin << endl;
  cout << "intMax = " << intMax << endl;
  
}
