#include <string>
#include <iostream>
#include <vector>
#include <sstream>
#include "SpaceMedkit.h"
using namespace std;

vector<string> readStringArray() {
  vector<string> result;
  string nbElementsString;
  getline( cin, nbElementsString );
  int nbElements;
  istringstream is( nbElementsString );
  is >> nbElements;

  for (int i=0; i< nbElements; i++ ) {
    string currentElementString;
    getline( cin, currentElementString );
    result.push_back( currentElementString );
  }
  return result;
}

double parseDouble( const string& i_string ) {
  double result;
  istringstream is(i_string);
  is >> result;
  return result;
}

#define STATUS(A) cerr << #A << "= \"" << A << "\"" << endl;

int main(int argc, char** argv) {
  vector<string> availableResources = readStringArray();
  vector<string> requiredResources  = readStringArray();
  vector<string> missions           = readStringArray();

  string PString;
  getline( cin, PString );
  STATUS( PString );
  double P = parseDouble(PString);

  string CString;
  getline( cin, CString );
  STATUS( CString );
  double C = parseDouble(CString);

  SpaceMedkit spaceMedKit;
  vector<string> output = spaceMedKit.getMedkit(availableResources, requiredResources, missions, P, C);
  cout << output.size() << endl;
  STATUS( output.size() );
  for(int i = 0; i < output.size(); i++){
    cout << output[i] << endl;
    STATUS( output[i] );
  }
}
