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

enum EV {E=2, G=1, U=0};
class PhoneNumbers {
public:
	string bestNumber(string);
};
int e2[50];
int e3[50];
int evE[50];
int evG[50];
int evU[50];
int evn[50];
int evnext[50];
void maj(int n) {
  evn[n] = 2*evE[n]*2+evG[n];
}

int eval(int e, int g, int u, int n) {
  return evn[n]+2*e+g;
}

string PhoneNumbers::bestNumber(string number) {
    fill(&evnext[0], &evnext[50],0);
        fill(&e2[0], &e2[50],0);
            fill(&e3[0], &e3[50],0);
                fill(&evE[0], &evE[50],0);
                fill(&evG[0], &evG[50],0);                
                fill(&evU[0], &evU[50],0);                
       fill(&evn[0], &evn[50],0);                
    int ln = number.size(); 
	for (int i=0; i<(ln-1); ++i) {
	  if (number[i]==number[i+1]) {
	    e2[i] = E;
	    if (i<(ln-2)) {
	      if (number[i]==number[i+2]){
	        e3[i] = E;
	      } else {
	        e3[i]=G;
	      }
	    }
	  } else {
	    e2[i] = U;
	    if (i<(ln-2)) {
	      if ((number[i] == number[i+2]) || (number[i]==number[i+1])|| (number[i+1]==number[i+2]) ) {
	        e3[i]=G;
	      } else {
	        e3[i]=U;
	      }
	    }
	  }
	}
	(e2[ln-2] == E) ? evE[ln-2] = 1 : 0;
	(e2[ln-2] == G) ? evG[ln-2] = 1 : 0;
	(e2[ln-2] == U) ? evU[ln-2] = 1 : 0;
	(e3[ln-3] == E) ? evE[ln-3] = 1 : 0;
	(e3[ln-3] == G) ? evG[ln-3] = 1 : 0;
	(e3[ln-3] == U) ? evU[ln-3] = 1 : 0;
	maj(ln-2);
	maj(ln-3);
	for (int i=ln-4; i>=0; --i) {
	  evE[i] = evU[i] = evG[i] = 0;
	  if (eval( (e2[i]==E), (e2[i]=G), (e2[i]=U), i+2) >= eval( (e3[i]==E), (e3[i]=G), (e3[i]=U), i+3)) {
	    evnext[i]=2;
	  } else {
	    evnext[i]=3;
	  }; 
	}
	ostringstream os;
	int i=0;
	do {
	  if (evnext[i]==2) {
	    os << number.substr(i,2);
	    i+=2;
	    if (i<=ln-2)
	    os << '-';
	  } else if (evnext[i]==3) {
	    os << number.substr(i,3);
	    i+=3;
	    if (i<=ln-2)	    
	    os << '-';
	  };
	}while (i<ln-3);
	os << number.substr(i, ln-i);
	return os.str();;
}


double test0() {
	string p0 = "5088638";
	PhoneNumbers * obj = new PhoneNumbers();
	clock_t start = clock();
	string my_answer = obj->bestNumber(p0);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	string p1 = "50-88-638";
	cout <<"Desired answer: " <<endl;
	cout <<"\t\"" << p1 <<"\"" <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t\"" << my_answer<<"\"" <<endl;
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
	string p0 = "0123456789";
	PhoneNumbers * obj = new PhoneNumbers();
	clock_t start = clock();
	string my_answer = obj->bestNumber(p0);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	string p1 = "01-23-45-67-89";
	cout <<"Desired answer: " <<endl;
	cout <<"\t\"" << p1 <<"\"" <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t\"" << my_answer<<"\"" <<endl;
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
	string p0 = "09";
	PhoneNumbers * obj = new PhoneNumbers();
	clock_t start = clock();
	string my_answer = obj->bestNumber(p0);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	string p1 = "09";
	cout <<"Desired answer: " <<endl;
	cout <<"\t\"" << p1 <<"\"" <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t\"" << my_answer<<"\"" <<endl;
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
	string p0 = "54545454545454545454";
	PhoneNumbers * obj = new PhoneNumbers();
	clock_t start = clock();
	string my_answer = obj->bestNumber(p0);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	string p1 = "54-545-454-545-454-545-454";
	cout <<"Desired answer: " <<endl;
	cout <<"\t\"" << p1 <<"\"" <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t\"" << my_answer<<"\"" <<endl;
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
	string p0 = "00110001011100010111";
	PhoneNumbers * obj = new PhoneNumbers();
	clock_t start = clock();
	string my_answer = obj->bestNumber(p0);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	string p1 = "00-11-00-010-11-10-00-101-11";
	cout <<"Desired answer: " <<endl;
	cout <<"\t\"" << p1 <<"\"" <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t\"" << my_answer<<"\"" <<endl;
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
