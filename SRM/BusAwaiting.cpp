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

#define all(c) c.begin(), c.end()
#define forall(c, it) for( typeof((c).begin()) it = (c).begin(); it != (c).end(); ++it )
// Containers with find (set, map)
#define present(c, v) ((c).find(v) != (c).end())
// Containers without embedded find (vector, list)
#define cpresent(c, v) ((c).count(v))

class BusAwaiting {
public:
	int waitingTime(vector <string>, int);
};

int BusAwaiting::waitingTime(vector <string> buses, int arrivalTime) {
    set<long long> lb;
	forall(buses,it) {
		istringstream is( *it );
		long long s;
		long long i;
		int c;
		is >> s >> i >> c;
		for (int cpt=0; cpt<c; ++cpt) {
			lb.insert(s+(cpt*i));
		}
	}
	forall (lb, it) {
	  if (*it >= arrivalTime) {
	    return *it -arrivalTime;
	  }
	}
	return -1;
}


double test0() {
	string t0[] = {"150 50 10"};
	vector <string> p0(t0, t0+sizeof(t0)/sizeof(string));
	int p1 = 285;
	BusAwaiting * obj = new BusAwaiting();
	clock_t start = clock();
	int my_answer = obj->waitingTime(p0, p1);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	int p2 = 15;
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
	string t0[] = {"123456 10000 1"};
	vector <string> p0(t0, t0+sizeof(t0)/sizeof(string));
	int p1 = 123456;
	BusAwaiting * obj = new BusAwaiting();
	clock_t start = clock();
	int my_answer = obj->waitingTime(p0, p1);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	int p2 = 0;
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
	string t0[] = {"270758 196 67",
 "904526 8930 66",
 "121164 3160 56"};
	vector <string> p0(t0, t0+sizeof(t0)/sizeof(string));
	int p1 = 1;
	BusAwaiting * obj = new BusAwaiting();
	clock_t start = clock();
	int my_answer = obj->waitingTime(p0, p1);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	int p2 = 121163;
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
	string t0[] = {"718571 2557 74",
 "480573 9706 54",
 "16511 6660 90"};
	vector <string> p0(t0, t0+sizeof(t0)/sizeof(string));
	int p1 = 1000000;
	BusAwaiting * obj = new BusAwaiting();
	clock_t start = clock();
	int my_answer = obj->waitingTime(p0, p1);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	int p2 = -1;
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
double test4() {
	string t0[] = {"407917 8774 24",
 "331425 4386 58",
 "502205 9420 32",
 "591461 1548 79",
 "504695 8047 53"};
	vector <string> p0(t0, t0+sizeof(t0)/sizeof(string));
	int p1 = 395439;
	BusAwaiting * obj = new BusAwaiting();
	clock_t start = clock();
	int my_answer = obj->waitingTime(p0, p1);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	int p2 = 1776;
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
	
	time = test4();
	if (time < 0)
		errors = true;
	
	if (!errors)
		cout <<"You're a stud (at least on the example cases)!" <<endl;
	else
		cout <<"Some of the test cases had errors." <<endl;
}

//Powered by [KawigiEdit] 2.0!
