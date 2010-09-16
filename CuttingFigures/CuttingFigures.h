#ifndef CUTTINGFIGURES_H
#define CUTTINGFIGURES_H

#include <vector>
#include <string>
#include <map>
#include <bitset>
#include <set>

using namespace std;


struct CuttingFigures {
    int W;
    int H;
    int K;
    int orderCount;
    int processedOrders;
    map<int, vector<bool> > plate;
    /*!
        \return Value ignored.
    */
    int init( vector<string> i_plate, int i_K, int i_orderCnt ) {
        K = i_K;
        orderCount = i_orderCnt;
        H = i_plate.size();
        W = i_plate[0].size();
        plate[0]   = vector<bool>( W+2, false );
        plate[H+1] = vector<bool>( W+2, false );
        for( int i = 1; i <= H; ++i ) {
            plate[i] = vector<bool>( W+2, false );
            for( int j=1; j<=W; ++j ){
                plate[i][j] = ( i_plate[i-1][j-1] == '.' );
            }
        }
        return 0;
    }

    vector<int>  processOrder( vector<string> i_figure, int i_income ) {

        vector<int> result(0);
        if (i_income < 50) return result;
        set<vector<int> > possible_result;

        int fh = i_figure.size();
        int fw = i_figure.at(0).length();

        for (int i = 1; i <= (H-fh+1); ++i ) {
            for (int j = 1; j <= (W-fw+1); ++j ) {
                bool ok = true;
                for (int a = 0; a < fh; ++a ) {
                    for (int b = 0; b < fw; ++b ) {
                        if ( i_figure[a][b] == 'X' ) {
                            if ( plate[i+a][j+b] == false ) {
                                ok=false;
                                goto end;
                            }
                        }
                    }
                }
                end:
                if (ok) {
                    vector<int> possibility(5,0);
                    possibility[3] = i-1;
                    possibility[4] = j-1;
                    possible_result.insert( possibility );
                }
            }
        }
        if ( !possible_result.empty() ) {
            result = *possible_result.begin();
            int i = result[3]+1;
            int j = result[4]+1;
            for (int a = 0; a < fh; ++a ) {
                for (int b = 0; b < fw; ++b ) {
                    if ( i_figure[a][b] == 'X' ) {
                        plate[i+a][j+b] = false;
                    }
                }
            }

        }
        return result;
    }
};

#endif // CUTTINGFIGURES_H
