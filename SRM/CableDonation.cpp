#include <vector>
#include <list>
#include <map>
#include <set>
#include <deque>
#include <stack>
#include <bitset>
#include <algorithm>
#include <functional>
#include <numeric>
#include <utility>
#include <sstream>
#include <iostream>
#include <iomanip>
#include <cstdio>
#include <cmath>
#include <cstdlib>
#include <ctime>

using namespace std;

class CableDonation {
public:
	int cable(vector <string>);
};
int lc(char c) {
  if (c=='0') {
    return 0;
  };
  if ((c>='a') && (c<='z')) {
    return (1+(c-'a'));
  };
  if ((c>='A') && (c<='Z')) {
    return (27+(c-'A'));
  };

}
int dirs[50][50];
int l[50][50];
int CableDonation::cable(vector <string> lengths) {
    fill(&dirs[0][0],&dirs[50][50], -1);
	int ns = lengths.size();
	int rt = 0;
	for (int i=0; i<ns; ++i) {
	  rt += lc(lengths[i][i]);
	  lengths[i][i]= '0';
	}
	for (int i=0; i<ns; ++i) {
		for (int j=0; j<ns; ++j) {
		  if (lc(lengths[i][j] != 0)) {
		  l[i][j] = lc(lengths[i][j]);
		  } else {
		  		  l[i][j] = 1000000;
		  }
		}
	}
	int mt = 0;
	int maxt = 0;
	for (int i=0; i<ns; ++i) {
		for (int j=i+1; j<ns; ++j) {
		  for (int k=0; k<ns; k++) {
		    if  ((l[i][k] != 0) && (l[k][j] != 0) && ((l[i][k]+l[k][j]) < l[i][j] ) ){
		      l[i][j] = l[j][i] = (l[i][k]+l[k][j]) ;
		      dirs[i][j] = dirs[j][i] = k;
		    }
		  }
		  if (l[i][j] == 1000000) {
		    return -1;
		  } 
	  	}
  	}
	int t = 0;  	
	for (int i=0; i<ns; ++i) {
		set<int> si;
		for (int j=i+1; j<ns; ++j) {
			si.insert(dirs[i][j]);
		}
		for (int j=i; j<ns; ++j) {
			if (i==j) {
			  t+= lc(lengths[i][j]);
			} else {
			  if ( si.find(j) != si.end() ) {
			  	t += lc(lengths[i][j])+lc(lengths[j][i]);
			  } else {
			  	t += max(lc(lengths[i][j]),lc(lengths[j][i]));
			  }
			}
		}
				
	}
	
			
	return rt+t;
}


double test0() {
	string t0[] = { "abc",
  "def",
  "ghi" };
	vector <string> p0(t0, t0+sizeof(t0)/sizeof(string));
	CableDonation * obj = new CableDonation();
	clock_t start = clock();
	int my_answer = obj->cable(p0);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	int p1 = 40;
	cout <<"Desired answer: " <<endl;
	cout <<"\t" << p1 <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t" << my_answer <<endl;
	if (p1 != my_answer) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test1() {
	string t0[] = { "a0",
  "0b" };
	vector <string> p0(t0, t0+sizeof(t0)/sizeof(string));
	CableDonation * obj = new CableDonation();
	clock_t start = clock();
	int my_answer = obj->cable(p0);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	int p1 = -1;
	cout <<"Desired answer: " <<endl;
	cout <<"\t" << p1 <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t" << my_answer <<endl;
	if (p1 != my_answer) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test2() {
	string t0[] = { "0X00",
  "00Y0",
  "0000",
  "00Z0" };
	vector <string> p0(t0, t0+sizeof(t0)/sizeof(string));
	CableDonation * obj = new CableDonation();
	clock_t start = clock();
	int my_answer = obj->cable(p0);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	int p1 = 0;
	cout <<"Desired answer: " <<endl;
	cout <<"\t" << p1 <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t" << my_answer <<endl;
	if (p1 != my_answer) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test3() {
	string t0[] = { "Az",
  "aZ" };
	vector <string> p0(t0, t0+sizeof(t0)/sizeof(string));
	CableDonation * obj = new CableDonation();
	clock_t start = clock();
	int my_answer = obj->cable(p0);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	int p1 = 105;
	cout <<"Desired answer: " <<endl;
	cout <<"\t" << p1 <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t" << my_answer <<endl;
	if (p1 != my_answer) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test4() {
	string t0[] = { "0top",
  "c0od",
  "er0o",
  "pen0" };
	vector <string> p0(t0, t0+sizeof(t0)/sizeof(string));
	CableDonation * obj = new CableDonation();
	clock_t start = clock();
	int my_answer = obj->cable(p0);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	int p1 = 134;
	cout <<"Desired answer: " <<endl;
	cout <<"\t" << p1 <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t" << my_answer <<endl;
	if (p1 != my_answer) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}

int main() {
	int time;
	bool errors = false;
	
	time = test0();
	if (time < 0)
		errors = true;
	
	time = test1();
	if (time < 0)
		errors = true;
	
	time = test2();
	if (time < 0)
		errors = true;
	
	time = test3();
	if (time < 0)
		errors = true;
	
	time = test4();
	if (time < 0)
		errors = true;
	
	if (!errors)
		cout <<"You're a stud (at least on the example cases)!" <<endl;
	else
		cout <<"Some of the test cases had errors." <<endl;
}

//Powered by [KawigiEdit] 2.0!
