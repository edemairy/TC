#include <vector>
#include <iostream>
#include <cmath>
using namespace std;
#define DEBUG_VAR(A) cerr << #A << "=" << A << endl
//#define DEBUG_VAR(A)

const double RADIUS_EARTH = 6378.1;

//! \param phi_s latitude of s
//! \param phi_l latitude of l
//! \param lambda_s longitude of s
//! \param lambad_f latitude of f
double host_spherical_distance_1( double r, double lambda_s, double phi_s, double lambda_f, double phi_f )
{
  double delta_sigma = acos( sin( phi_s ) * sin( phi_f ) + cos( phi_s ) * cos( phi_f ) * cos( lambda_f - lambda_s ) );
  return (r*delta_sigma);
}

inline double cost_one_travel( double dist ) {
   return( (dist < 4500 ) ? ( dist ) : ( dist*2 ) );
}
double compute_score( const vector<int>& sol, int M, int N, double home_lat, double home_rlng, const vector<double>& positions_radians )
{
    double result = 0;
    const double div = (1./ (M*(N+1) ) );
    DEBUG_VAR(div);
    double dist = 0;
    for ( int current_cycle = 0; current_cycle < M; ++current_cycle ) {
       int nb_night = current_cycle * N;
       dist = host_spherical_distance_1( RADIUS_EARTH, 0, 0, positions_radians[ 2*nb_night ], positions_radians[ 2*nb_night+1 ] );
       DEBUG_VAR(dist);
       result += cost_one_travel( dist ) * div;
       DEBUG_VAR(result);
       for ( int current_night = 0; current_night < (N-1); ++current_night ) {
           nb_night++;
           dist = host_spherical_distance_1( RADIUS_EARTH, positions_radians[ 2*nb_night ], positions_radians[ 2*nb_night+1 ],
                    positions_radians[ 2*nb_night+2 ], positions_radians[ 2*nb_night+3 ] );
           result += cost_one_travel( dist ) * div;
       }
       nb_night++;
       dist += host_spherical_distance_1( RADIUS_EARTH, 0, 0, positions_radians[ 2*nb_night ], positions_radians[ 2*nb_night+1 ] );
       result += cost_one_travel( dist ) * div;
   }
   return result;
}

void convert_positions( const vector<double>& positions_degrees, vector<double>& positions_radians )
{

    for ( int i = 0; i < positions_degrees.size(); ++i ) {
      positions_radians[ i ] = ( (positions_degrees[i] / 360) * M_PI );
    }
}

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
       vector<double> positions_radians( positions.size() );
       convert_positions( positions, positions_radians );
       double score = compute_score( result, M, N, home_lat, home_rlng, positions_radians );
       DEBUG_VAR(10000./score);
       return (result);
    }

};
