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
const int FOMAX=30;
const int TMAX = (FOMAX*(FOMAX+1))/2;
//long long  n[TMAX+1][FOMAX+1];
long long  nbm[1000][31];
class FIELDDiagrams {
public:
	long long countDiagrams(int);
};

long long nb(int t, int n) {
  long long r = 0;
  if (t<=0) {
    return 1;
  }
  for (int i=min(n,t); i>0; --i) {
    r += (nb(t-i, min(i, n-1) ));
    
  };
  nbm[t][n] = r;
  return r;
}

long long FIELDDiagrams::countDiagrams(int fo) {
	for (int i=0; i<=TMAX; ++i) {
	  for (int j=0; j<=(FOMAX); ++j) {
	    nbm[i][j] = -1;
	  }
	}
	long long r = 0;
	for (int i=(fo*(fo+1))/2; i>=fo; i--) {
	  r += nb((fo*(fo+1))/2, fo);
	  cout << i << " " << r << endl;
	}
	for (int i=0; i<=10; ++i) {
	    cout << i << " ";
	  for (int j=0; j<=(10); ++j) {
	    cout <<  nbm[i][j] << " ";
	  }
	  cout << endl;
	}
	
	return r;
}


double test0() {
	int p0 = 2;
	FIELDDiagrams * obj = new FIELDDiagrams();
	clock_t start = clock();
	long long my_answer = obj->countDiagrams(p0);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	long long p1 = 4LL;
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
	int p0 = 3;
	FIELDDiagrams * obj = new FIELDDiagrams();
	clock_t start = clock();
	long long my_answer = obj->countDiagrams(p0);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	long long p1 = 13LL;
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
	int p0 = 5;
	FIELDDiagrams * obj = new FIELDDiagrams();
	clock_t start = clock();
	long long my_answer = obj->countDiagrams(p0);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	long long p1 = 131LL;
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
	
	if (!errors)
		cout <<"You're a stud (at least on the example cases)!" <<endl;
	else
		cout <<"Some of the test cases had errors." <<endl;
}

//Powered by [KawigiEdit] 2.0!
