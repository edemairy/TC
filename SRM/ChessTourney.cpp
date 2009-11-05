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

class ChessTourney {
public:
	double expectedWinning(vector <int>, vector <int>);
};

double ChessTourney::expectedWinning(vector <int> winnings, vector <int> odds) {
  double s;
  double p = 1;
  for (int i=0; i<odds.size(); ++i) {
  	s += p*winnings[i];
  	p *= (odds[i]/100.0);
  }
  s += p*(*winnings.rbegin());
//  s /= odds.size();
  return s;
}


double test0() {
	int t0[] = {10, 100};
	vector <int> p0(t0, t0+sizeof(t0)/sizeof(int));
	int t1[] = {100};
	vector <int> p1(t1, t1+sizeof(t1)/sizeof(int));
	ChessTourney * obj = new ChessTourney();
	clock_t start = clock();
	double my_answer = obj->expectedWinning(p0, p1);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	double p2 = 110.0;
	cout <<"Desired answer: " <<endl;
	cout <<"\t" << p2 <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t" << my_answer <<endl;
	if (p2 != my_answer) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test1() {
	int t0[] = {10, 10, 10, 10};
	vector <int> p0(t0, t0+sizeof(t0)/sizeof(int));
	int t1[] = {50, 50, 50};
	vector <int> p1(t1, t1+sizeof(t1)/sizeof(int));
	ChessTourney * obj = new ChessTourney();
	clock_t start = clock();
	double my_answer = obj->expectedWinning(p0, p1);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	double p2 = 18.75;
	cout <<"Desired answer: " <<endl;
	cout <<"\t" << p2 <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t" << my_answer <<endl;
	if (p2 != my_answer) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test2() {
	int t0[] = {100, 100, 200, 200};
	vector <int> p0(t0, t0+sizeof(t0)/sizeof(int));
	int t1[] = {100, 0, 100};
	vector <int> p1(t1, t1+sizeof(t1)/sizeof(int));
	ChessTourney * obj = new ChessTourney();
	clock_t start = clock();
	double my_answer = obj->expectedWinning(p0, p1);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	double p2 = 200.0;
	cout <<"Desired answer: " <<endl;
	cout <<"\t" << p2 <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t" << my_answer <<endl;
	if (p2 != my_answer) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test3() {
	int t0[] = {100, 100, 200, 400, 800, 1000};
	vector <int> p0(t0, t0+sizeof(t0)/sizeof(int));
	int t1[] = {100, 50, 25, 10, 5};
	vector <int> p1(t1, t1+sizeof(t1)/sizeof(int));
	ChessTourney * obj = new ChessTourney();
	clock_t start = clock();
	double my_answer = obj->expectedWinning(p0, p1);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	double p2 = 360.625;
	cout <<"Desired answer: " <<endl;
	cout <<"\t" << p2 <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t" << my_answer <<endl;
	if (p2 != my_answer) {
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
	
	if (!errors)
		cout <<"You're a stud (at least on the example cases)!" <<endl;
	else
		cout <<"Some of the test cases had errors." <<endl;
}

//Powered by [KawigiEdit] 2.0!
