#ifndef STREETSALES_H
#define STREETSALES_H

#include <vector>
#include <string>
#include <sstream>
#include <set>
#include <iostream>
#include <stdlib.h>
#define STAT(A) cerr << "DEBUG: " << #A << " = " << A << endl;

const int DEBUG=1;
using namespace std;


typedef pair<int,int> Coord;
vector <string> districtMap;
vector <int> warehousePrices;
int C;
int S;
int G;
int W;
int H;

int dir4x[] = { -1, 0, 1, 0 };
int dir4y[] = {  0,-1, 0, 1 };
struct House {
    House( int i_x, int i_y, int i_attraction ):
      x(i_x), y(i_y), attraction(G,i_attraction)
    {
    }

    int x;
    int y;
    vector<int> attraction;
    set<int>    goodsInterest;
    set<Coord>  adjacentStreets() {
      set<Coord> result;
      for (int i=0; i<4; ++i) {
          int cx = x + dir4x[i];
          int cy = y + dir4y[i];
          if ( ( cx < 0 ) || (cy < 0) || (cx > H) || (cy > W ) ) break;
          if ( districtMap[cx][cy] == '.' ) result.insert( make_pair(cx,cy) );
      }
      return result;
    }

    static bool isA(char car) { return car == 'X'; }
    static bool isIsolated(int i, int j) {
        bool result =
                ( (i>0)? !isA(districtMap[i-1][j]) : true ) &&
                ( (j>0)? !isA(districtMap[i][j-1]) : true ) &&
                ( (i<(H-1))? !isA(districtMap[i+1][j]) : true ) &&
                ( (j<(W-1))? !isA(districtMap[i][j+1]) : true );
        return result;
    }


};

vector<House> houses;

class StreetSales {
  public:
    //! \param i_districtMap the map of the district districtMap; 'X' marks a house and '.' marks a piece of street.
    //! You can walk on the streets, and you can trade at the house only if you stand at a street cell which is
    //! horizontally or vertically adjacent to it.
    //!   - The numbers of rows H and columns W in the area will be between 20 and 100, inclusive (except for seed=1).
    //!   - The number of elements in preferences of each house will be between 1 and G, inclusive.
    //!  \param i_warehousePrices warehouse prices per unit of goods of each type.
    //!   - warehouse price for each type of goods will be between 70 and 100, inclusive.
    //!   - You can figure out the number of distinct goods you can trade G as the number of elements in warehousePrices;
    //!   - G will be between 2 and 10, inclusive.
    //!   -	GOODS_TYPEs of one house will be chosen as the first elements of a random permutation of 0..G-1, so they all will be distinct.
    //!   -	STOP_PROB will be between 0 and 10, inclusive (in percent).
    //!   -	MAX_PRICE will be calculated as warehouse price for this type of goods plus random bonus 10..25. Isolated houses will get an extra bonus of 5..15.
    //! \param i_C the number of units of goods you can carry.
    //!    	C will be approximately from 0.5 to 1.0 of the number of houses in the area.
    //! \param i_S the number of steps you can perform per day.
    //!        	S will be approximately from 0.125 to 0.25 of the number of cells in the area.
    //! \return ignored.
    //!
    int init(vector <string> i_districtMap, vector <int> i_warehousePrices, int i_C, int i_S) {
        districtMap = i_districtMap;
        warehousePrices = i_warehousePrices;
        G = warehousePrices.size();
        C = i_C;
        S = i_S;
        H = districtMap.size();
        W = districtMap[0].length();

        for (int i=0; i<H; ++i ) {
            for (int j=0; j<W; ++j ) {
                if (House::isA(districtMap[i][j])) {
                    houses.push_back(House(i,j,1+( House::isIsolated(i,j) ? 1: 0 ) ));
                }
            }
        }
        return 0;
    }
    //! \param i_visitedHouses It gives you the results of the previous day as vector <int> visitedHouses.
    //!   Element i*W+j of visitedHouses will describe the result of visiting the house in row i and column j
    //!   and will be -2 if no trade was attempted (or there is no house at this cell), -1 if customers of
    //!   this house bought nothing, or the index of the type of goods (0..G-1) they bought otherwise.
    //! \return Must follow the format:
    //!      - first G elements must be formatted as "QUANTITY PRICE" and give the (integer) quantity of units
    //!        of each type of goods (in order) that you want to purchase from the warehouse for this day and
    //!        the (integer) price you are setting for one unit of this type of goods for this day.
    //!      - the rest of the elements (at most S+1) must be formatted as "ROW COL" and describe the sequence
    //!        of movements and trade actions. First element gives coordinates of the cell you want to start
    //!        your day with. Next elements denote either moving to street cell (ROW, COL) or trading at
    //!        house cell (ROW, COL). Each next cell must be adjacent to previous one. Trading at a house doesn't
    //!        change your position. You can move only on street cells. You can trade at each house at most once.
    vector <string> dayTrade(vector <int> i_visitedHouses) {
        if (DEBUG) {
            for (int i = 0; i<H; ++i) {
                for (int j = 0; j<W; ++j) {
                    switch (i_visitedHouses[i*W+j]) {
                    case -2: break;
                    case -1: cerr << "house (" << i << "," << j << ") bought nothing" << endl; break;
                        default :cerr << "house (" << i << "," << j << ") bought of good " << i_visitedHouses[i*W+j] << endl; break;
                    }
                }
            }
        }
      vector<string> result;
      for (int i = 0; i < G; ++i ) {
          ostringstream os;
       //   int quantity = ( C/G + ( (C%G)<i ? 1 : 0 ) );
          int quantity = 1;
       //   STAT(quantity)
          os <<  quantity << " " << ( warehousePrices[i] + 1 );
          result.push_back( os.str() );
      }
      //! Step 0 : pick a house at random.
      int currentHouse = rand() % houses.size();
      Coord coord = *houses[currentHouse].adjacentStreets().begin();
      ostringstream os;
      os << coord.first << " " << coord.second;
      result.push_back( os.str() );
      os.str("");
      os << houses[currentHouse].x << " " << houses[currentHouse].y;
      result.push_back( os.str() );
      int curx = coord.first;
      int cury = coord.second;
      for ( int step = 2; step < S; ++step ) {
          if ( (curx>0) && (districtMap[curx-1][cury]=='.') ) {
              curx--;
          } else if ( (cury>0) && (districtMap[curx][cury-1]=='.') ) {
              cury--;
          } else if ( (curx<H) && (districtMap[curx+1][cury]=='.') ) {
              curx++;
          } else if ( (cury<W) && (districtMap[curx][cury+1]=='.') ) {
              cury++;
          }
          if (step>=S) break;
          os.str("");
          os << curx << " " << cury;
          result.push_back( os.str() );
          if ( (curx>0) && (districtMap[curx-1][cury]=='X') ) {
              os.str("");
              os << curx-1 << " " << cury;
              result.push_back( os.str() );
              step++;
          } else if ( (cury>0) && (districtMap[curx][cury-1]=='X') ) {
              os.str("");
              os << curx << " " << cury-1;
              result.push_back( os.str() );
              step++;
          } else if ( (curx<H) && (districtMap[curx+1][cury]=='X') ) {
              os.str("");
              os << curx+1 << " " << cury;
              result.push_back( os.str() );
              step++;
          } else if ( (cury<W) && (districtMap[curx][cury+1]=='X') ) {
              os.str("");
              os << curx << " " << cury+1;
              result.push_back( os.str() );
              step++;
          }

          //          ostringstream os;

        //! Choose an attractive

      }
      if (result.size()>((unsigned int)S+G))
          result.resize(S+G);
      result.resize(G+2);
      return result;
    }
};
#endif
