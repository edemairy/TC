#include "StreetSales.h" 
#include <iostream>
#include <sstream>
using namespace std;


template <class T>
T readLine() {
  T result;
  string nbElementsString;
  getline( cin, nbElementsString );
  istringstream is( nbElementsString );
  is >> result;
}

int main( int argc, char** argv ) {
    StreetSales streetsales;
    int H = readLine<int>();
    vector<string> districtMap(H);
    for (int i=0; i<H; i++)
        districtMap[i] = readLine<string>();
    int W = districtMap[0].size();
    int G = readLine<int>();
    vector<int> warehousePrices(G);
    for (int i=0; i<G; i++)
        warehousePrices[i] = readLine<int>();
    int C = readLine<int>();
    int S = readLine<int>();
    streetsales.init(districtMap, warehousePrices, C, S);

   for (int day=0; day < 3000; day++)
   {
       vector<int> visitedHouses(H*W);
       for (int i=0; i < H*W; i++)
           visitedHouses[i] = readLine<int>();
       vector<string> route = streetsales.dayTrade(visitedHouses);
       int r = route.size();
       cout << r << endl;
       for (int i=0; i < r; i++)
           cout << route[i] << endl;
   }

   cout << flush;
}
