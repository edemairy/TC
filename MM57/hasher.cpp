#include <iostream>
#include <stdlib.h>
using namespace std;
long hash(const std::string& str){
        long hash = 5381;

        for(int i = 0; i<str.length(); i++) {
	    long prehash = hash*33;
            hash = prehash ^ str[i]; // '^' denotes XOR
	    
	    cout << "    " << i << " -> " << hash << endl;
	}
        return hash;
    }
int main(int argc, char** argv) {
  if (argc < 2) {
    cerr << "Format: " << argv[0] << " string_to_hash1 string_to_hash2 ..." << endl;
    exit(1);
  } else { 
   for (int i = 1; i<argc; ++i) {
     cout << "hash[\"" << argv[i] << "\"] = " << hash(argv[i]) << endl;  
   }
  }
}

