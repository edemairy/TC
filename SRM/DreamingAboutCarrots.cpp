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

class DreamingAboutCarrots {
public:
	int carrotsBetweenCarrots(int, int, int, int);
};

int DreamingAboutCarrots::carrotsBetweenCarrots(int x1, int y1, int x2, int y2) {
	if (x2==x1) {
	  if (y2==y1) {
	    return 0;
	  } else {
	    return (abs(y2-y1));
	  }
	}
	if (y2==y1) {
	  if (x2==x1) {
	    return 0;
	  } else {
	    return (abs(x2-x1));
	  }
	}
	if (x2<x1) {
	  int tmp = x1;
	  x1 = x2;
	  x2 = tmp;
	  tmp = y1;
	  y1 = y2;
	  y2 = tmp;
	}
	int r=0;
	for (int i=1; i<(x2-x1); ++i) {
	  if ((( (y2-y1)*i+y1*(x2-x1)) % (x2-x1)) == 0) {
	    ++r;
	  }
	}
	return r;
}


double test0() {
	int p0 = 1;
	int p1 = 1;
	int p2 = 5;
	int p3 = 5;
	DreamingAboutCarrots * obj = new DreamingAboutCarrots();
	clock_t start = clock();
	int my_answer = obj->carrotsBetweenCarrots(p0, p1, p2, p3);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	int p4 = 3;
	cout <<"Desired answer: " <<endl;
	cout <<"\t" << p4 <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t" << my_answer <<endl;
	if (p4 != my_answer) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test1() {
	int p0 = 0;
	int p1 = 0;
	int p2 = 1;
	int p3 = 1;
	DreamingAboutCarrots * obj = new DreamingAboutCarrots();
	clock_t start = clock();
	int my_answer = obj->carrotsBetweenCarrots(p0, p1, p2, p3);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	int p4 = 0;
	cout <<"Desired answer: " <<endl;
	cout <<"\t" << p4 <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t" << my_answer <<endl;
	if (p4 != my_answer) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test2() {
	int p0 = 50;
	int p1 = 48;
	int p2 = 0;
	int p3 = 0;
	DreamingAboutCarrots * obj = new DreamingAboutCarrots();
	clock_t start = clock();
	int my_answer = obj->carrotsBetweenCarrots(p0, p1, p2, p3);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	int p4 = 1;
	cout <<"Desired answer: " <<endl;
	cout <<"\t" << p4 <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t" << my_answer <<endl;
	if (p4 != my_answer) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test3() {
	int p0 = 0;
	int p1 = 0;
	int p2 = 42;
	int p3 = 36;
	DreamingAboutCarrots * obj = new DreamingAboutCarrots();
	clock_t start = clock();
	int my_answer = obj->carrotsBetweenCarrots(p0, p1, p2, p3);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	int p4 = 5;
	cout <<"Desired answer: " <<endl;
	cout <<"\t" << p4 <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t" << my_answer <<endl;
	if (p4 != my_answer) {
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
