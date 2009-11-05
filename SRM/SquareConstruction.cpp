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

class SquareConstruction {
public:
	vector <string> construct(int, int, int, int, int);
};

int t[10][10];
vector <string> SquareConstruction::construct(int N, int a, int b, int c, int d) {
	fill(&t[0][0], &t[10][10], -1);
	bool cont = true;
	int curv = 1;
	t[0][0] = curv;
	int cx = 0;
	int cy = 0;
	while (cont) {
	  if (t[(cx+a)%N][(cy+b)%N] == -1) {
	    cx = (cx +a)%N;
	    cy = (cy+b)%N;
	    curv++;
	    t[cx][cy] = curv;
	  } else if  (t[(cx+c+a)%N][(cy+d+b)%N] == -1) {
 	    cx = (cx +c+a)%N;
	    cy = (cy+d+b)%N;
	    curv++;
	    t[cx][cy] = curv;

	  } else {
	    cont = false;
	  }
	}
	vector<string> r;
	for (int i=0; i<N; ++i) {
	  ostringstream  s;
	  for (int j=0; j<N; ++j) {
	    s << t[i][j];
	    if (j != (N-1) ) {
	      s << " ";
	    }
	  }
	  r.push_back(s.str());
	}
	return r;
}


double test0() {
	int p0 = 5;
	int p1 = 0;
	int p2 = 0;
	int p3 = 0;
	int p4 = 0;
	SquareConstruction * obj = new SquareConstruction();
	clock_t start = clock();
	vector <string> my_answer = obj->construct(p0, p1, p2, p3, p4);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	string t5[] = {"1 -1 -1 -1 -1", "-1 -1 -1 -1 -1", "-1 -1 -1 -1 -1", "-1 -1 -1 -1 -1", "-1 -1 -1 -1 -1" };
	vector <string> p5(t5, t5+sizeof(t5)/sizeof(string));
	cout <<"Desired answer: " <<endl;
	cout <<"\t{ ";
	if (p5.size() > 0) {
		cout <<"\""<<p5[0]<<"\"";
		for (int i=1; i<p5.size(); i++)
			cout <<", \"" <<p5[i]<<"\"";
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	cout <<endl <<"Your answer: " <<endl;
	cout <<"\t{ ";
	if (my_answer.size() > 0) {
		cout <<"\""<<my_answer[0]<<"\"";
		for (int i=1; i<my_answer.size(); i++)
			cout <<", \"" <<my_answer[i]<<"\"";
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	if (my_answer != p5) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test1() {
	int p0 = 5;
	int p1 = 1;
	int p2 = 1;
	int p3 = 2;
	int p4 = 2;
	SquareConstruction * obj = new SquareConstruction();
	clock_t start = clock();
	vector <string> my_answer = obj->construct(p0, p1, p2, p3, p4);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	string t5[] = {"1 -1 -1 -1 -1", "-1 2 -1 -1 -1", "-1 -1 3 -1 -1", "-1 -1 -1 4 -1", "-1 -1 -1 -1 5" };
	vector <string> p5(t5, t5+sizeof(t5)/sizeof(string));
	cout <<"Desired answer: " <<endl;
	cout <<"\t{ ";
	if (p5.size() > 0) {
		cout <<"\""<<p5[0]<<"\"";
		for (int i=1; i<p5.size(); i++)
			cout <<", \"" <<p5[i]<<"\"";
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	cout <<endl <<"Your answer: " <<endl;
	cout <<"\t{ ";
	if (my_answer.size() > 0) {
		cout <<"\""<<my_answer[0]<<"\"";
		for (int i=1; i<my_answer.size(); i++)
			cout <<", \"" <<my_answer[i]<<"\"";
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	if (my_answer != p5) {
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
	int p1 = 1;
	int p2 = 1;
	int p3 = 1;
	int p4 = 0;
	SquareConstruction * obj = new SquareConstruction();
	clock_t start = clock();
	vector <string> my_answer = obj->construct(p0, p1, p2, p3, p4);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	string t5[] = {"1 22 18 14 10", "6 2 23 19 15", "11 7 3 24 20", "16 12 8 4 25", "21 17 13 9 5" };
	vector <string> p5(t5, t5+sizeof(t5)/sizeof(string));
	cout <<"Desired answer: " <<endl;
	cout <<"\t{ ";
	if (p5.size() > 0) {
		cout <<"\""<<p5[0]<<"\"";
		for (int i=1; i<p5.size(); i++)
			cout <<", \"" <<p5[i]<<"\"";
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	cout <<endl <<"Your answer: " <<endl;
	cout <<"\t{ ";
	if (my_answer.size() > 0) {
		cout <<"\""<<my_answer[0]<<"\"";
		for (int i=1; i<my_answer.size(); i++)
			cout <<", \"" <<my_answer[i]<<"\"";
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	if (my_answer != p5) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test3() {
	int p0 = 5;
	int p1 = 0;
	int p2 = 1;
	int p3 = 2;
	int p4 = 3;
	SquareConstruction * obj = new SquareConstruction();
	clock_t start = clock();
	vector <string> my_answer = obj->construct(p0, p1, p2, p3, p4);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	string t5[] = {"1 2 3 4 5", "17 18 19 20 16", "8 9 10 6 7", "24 25 21 22 23", "15 11 12 13 14" };
	vector <string> p5(t5, t5+sizeof(t5)/sizeof(string));
	cout <<"Desired answer: " <<endl;
	cout <<"\t{ ";
	if (p5.size() > 0) {
		cout <<"\""<<p5[0]<<"\"";
		for (int i=1; i<p5.size(); i++)
			cout <<", \"" <<p5[i]<<"\"";
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	cout <<endl <<"Your answer: " <<endl;
	cout <<"\t{ ";
	if (my_answer.size() > 0) {
		cout <<"\""<<my_answer[0]<<"\"";
		for (int i=1; i<my_answer.size(); i++)
			cout <<", \"" <<my_answer[i]<<"\"";
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	if (my_answer != p5) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test4() {
	int p0 = 6;
	int p1 = 1;
	int p2 = 2;
	int p3 = 2;
	int p4 = 2;
	SquareConstruction * obj = new SquareConstruction();
	clock_t start = clock();
	vector <string> my_answer = obj->construct(p0, p1, p2, p3, p4);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	string t5[] = {"1 -1 15 -1 11 -1", "12 -1 2 -1 16 -1", "17 -1 7 -1 3 -1", "4 -1 18 -1 8 -1", "9 -1 5 -1 13 -1", "14 -1 10 -1 6 -1" };
	vector <string> p5(t5, t5+sizeof(t5)/sizeof(string));
	cout <<"Desired answer: " <<endl;
	cout <<"\t{ ";
	if (p5.size() > 0) {
		cout <<"\""<<p5[0]<<"\"";
		for (int i=1; i<p5.size(); i++)
			cout <<", \"" <<p5[i]<<"\"";
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	cout <<endl <<"Your answer: " <<endl;
	cout <<"\t{ ";
	if (my_answer.size() > 0) {
		cout <<"\""<<my_answer[0]<<"\"";
		for (int i=1; i<my_answer.size(); i++)
			cout <<", \"" <<my_answer[i]<<"\"";
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	if (my_answer != p5) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test5() {
	int p0 = 1;
	int p1 = 0;
	int p2 = 0;
	int p3 = 0;
	int p4 = 0;
	SquareConstruction * obj = new SquareConstruction();
	clock_t start = clock();
	vector <string> my_answer = obj->construct(p0, p1, p2, p3, p4);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	string t5[] = {"1" };
	vector <string> p5(t5, t5+sizeof(t5)/sizeof(string));
	cout <<"Desired answer: " <<endl;
	cout <<"\t{ ";
	if (p5.size() > 0) {
		cout <<"\""<<p5[0]<<"\"";
		for (int i=1; i<p5.size(); i++)
			cout <<", \"" <<p5[i]<<"\"";
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	cout <<endl <<"Your answer: " <<endl;
	cout <<"\t{ ";
	if (my_answer.size() > 0) {
		cout <<"\""<<my_answer[0]<<"\"";
		for (int i=1; i<my_answer.size(); i++)
			cout <<", \"" <<my_answer[i]<<"\"";
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	if (my_answer != p5) {
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
	
	time = test5();
	if (time < 0)
		errors = true;
	
	if (!errors)
		cout <<"You're a stud (at least on the example cases)!" <<endl;
	else
		cout <<"Some of the test cases had errors." <<endl;
}

//Powered by [KawigiEdit] 2.0!
