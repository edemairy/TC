#ifndef READERS_H
#define READERS_H

#include <string>
#include <sstream>
using namespace std;

//void read(vector<string>& o_strings);

template<typename T> T read()
{
  T result;
  string readString;
  getline( cin, readString );
  istringstream is( readString );
  is >> result;
  return result;
}


#endif // READERS_H
