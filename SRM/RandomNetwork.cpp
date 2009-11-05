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

class RandomNetwork {
public:
	vector <double> probableLocation(vector <string>, int);
};

long double mat[51][51];
long double rpow[51][51];
long double temp[51][51];
long double lbr[51][51];
void  pown(int sn,int steps){
   while (steps >1) {
	if ((steps %2) == 1) {
	     for (int i=0; i<sn; ++i) {
	       for (int j=0; j<sn; ++j) {
	         temp[i][j] = 0;
	         for (int k=0; k<sn; ++k) {
	           temp[i][j] += rpow[i][k]*lbr[k][j];
	         }
	       }
     }
    	 for (int i=0; i<sn; ++i) {
	       for (int j=0; j<sn; ++j) {
	          lbr[i][j] = temp[i][j];
			}
		}     	
	   }
//	   if ((steps % 2) == 0) {
	     for (int i=0; i<sn; ++i) {
	       for (int j=0; j<sn; ++j) {
	         temp[i][j] = 0;
	         for (int k=0; k<sn; ++k) {
	           temp[i][j] += rpow[i][k]*rpow[k][j];
	         }
	       }
	     }
    	 for (int i=0; i<sn; ++i) {
	       for (int j=0; j<sn; ++j) {
	          rpow[i][j] = temp[i][j];
			}
		}     
//	   } else {
	   steps /=2;
	}
	
	
	     for (int i=0; i<sn; ++i) {
	       for (int j=0; j<sn; ++j) {
	         temp[i][j] = 0;
	         for (int k=0; k<sn; ++k) {
	           temp[i][j] += rpow[i][k]*lbr[k][j];
	         }
	       }
	     }
    	 for (int i=0; i<sn; ++i) {
	       for (int j=0; j<sn; ++j) {
	          rpow[i][j] = temp[i][j];
			}
		}     	
}


vector <double> RandomNetwork::probableLocation(vector <string> network, int steps) {
    int sn = network.size();
	fill(&mat[0][0],&mat[51][51],0);
	fill(&rpow[0][0],&rpow[51][51],0);
    fill(&temp[0][0],&temp[51][51],0);
	for (int i=0; i<sn; ++i) {
	    int nt = 0;

		for (int j=0; j<sn; ++j) {
		  nt += (network[i][j] == 'Y');
		  lbr[i][j] = 0;
		}
	    lbr[i][i] = 1;
		long double v = 1;
		if (nt >0) {
		  v /= nt;
		}
	 	for (int j=0; j<sn; ++j) {
	 	  rpow[i][j] = mat[i][j] = (network[i][j] == 'Y') ? v : 0;	 	  
	 	}
	}
	pown(sn, steps);
	vector<double> r(sn);
	for (int i=0; i<sn; ++i){
	  r[i] = rpow[0][i];
	}
	return r;
}


double test0() {
	string t0[] = {"NYYY", "YNYY", "YYNY", "YYYN"};
	vector <string> p0(t0, t0+sizeof(t0)/sizeof(string));
	int p1 = 1;
	RandomNetwork * obj = new RandomNetwork();
	clock_t start = clock();
	vector <double> my_answer = obj->probableLocation(p0, p1);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	double t2[] = {0.0, 0.3333333333333333, 0.3333333333333333, 0.3333333333333333 };
	vector <double> p2(t2, t2+sizeof(t2)/sizeof(double));
	cout <<"Desired answer: " <<endl;
	cout <<"\t{ ";
	if (p2.size() > 0) {
		cout <<p2[0];
		for (int i=1; i<p2.size(); i++)
			cout <<", " <<p2[i];
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	cout <<endl <<"Your answer: " <<endl;
	cout <<"\t{ ";
	if (my_answer.size() > 0) {
		cout <<my_answer[0];
		for (int i=1; i<my_answer.size(); i++)
			cout <<", " <<my_answer[i];
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	if (my_answer != p2) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test1() {
	string t0[] = {"NYYY", "YNYY", "YYNY", "YYYN"};
	vector <string> p0(t0, t0+sizeof(t0)/sizeof(string));
	int p1 = 2;
	RandomNetwork * obj = new RandomNetwork();
	clock_t start = clock();
	vector <double> my_answer = obj->probableLocation(p0, p1);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	double t2[] = {0.3333333333333333, 0.2222222222222222, 0.2222222222222222, 0.2222222222222222 };
	vector <double> p2(t2, t2+sizeof(t2)/sizeof(double));
	cout <<"Desired answer: " <<endl;
	cout <<"\t{ ";
	if (p2.size() > 0) {
		cout <<p2[0];
		for (int i=1; i<p2.size(); i++)
			cout <<", " <<p2[i];
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	cout <<endl <<"Your answer: " <<endl;
	cout <<"\t{ ";
	if (my_answer.size() > 0) {
		cout <<my_answer[0];
		for (int i=1; i<my_answer.size(); i++)
			cout <<", " <<my_answer[i];
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	if (my_answer != p2) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test2() {
	string t0[] = {"NYY", "NNY", "NYN"};
	vector <string> p0(t0, t0+sizeof(t0)/sizeof(string));
	int p1 = 5;
	RandomNetwork * obj = new RandomNetwork();
	clock_t start = clock();
	vector <double> my_answer = obj->probableLocation(p0, p1);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	double t2[] = {0.0, 0.5, 0.5 };
	vector <double> p2(t2, t2+sizeof(t2)/sizeof(double));
	cout <<"Desired answer: " <<endl;
	cout <<"\t{ ";
	if (p2.size() > 0) {
		cout <<p2[0];
		for (int i=1; i<p2.size(); i++)
			cout <<", " <<p2[i];
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	cout <<endl <<"Your answer: " <<endl;
	cout <<"\t{ ";
	if (my_answer.size() > 0) {
		cout <<my_answer[0];
		for (int i=1; i<my_answer.size(); i++)
			cout <<", " <<my_answer[i];
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	if (my_answer != p2) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test3() {
	string t0[] = {"NYYNN", "NNYYN", "NNNYY", "YNNNY", "YYNNN"};
	vector <string> p0(t0, t0+sizeof(t0)/sizeof(string));
	int p1 = 3;
	RandomNetwork * obj = new RandomNetwork();
	clock_t start = clock();
	vector <double> my_answer = obj->probableLocation(p0, p1);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	double t2[] = {0.375, 0.125, 0.0, 0.125, 0.375 };
	vector <double> p2(t2, t2+sizeof(t2)/sizeof(double));
	cout <<"Desired answer: " <<endl;
	cout <<"\t{ ";
	if (p2.size() > 0) {
		cout <<p2[0];
		for (int i=1; i<p2.size(); i++)
			cout <<", " <<p2[i];
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	cout <<endl <<"Your answer: " <<endl;
	cout <<"\t{ ";
	if (my_answer.size() > 0) {
		cout <<my_answer[0];
		for (int i=1; i<my_answer.size(); i++)
			cout <<", " <<my_answer[i];
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	if (my_answer != p2) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test4() {
	string t0[] = {"NYYY", "NNYY", "NNNY", "YNNN"};
	vector <string> p0(t0, t0+sizeof(t0)/sizeof(string));
	int p1 = 3;
	RandomNetwork * obj = new RandomNetwork();
	clock_t start = clock();
	vector <double> my_answer = obj->probableLocation(p0, p1);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	double t2[] = {0.5, 0.1111111111111111, 0.1111111111111111, 0.2777777777777778 };
	vector <double> p2(t2, t2+sizeof(t2)/sizeof(double));
	cout <<"Desired answer: " <<endl;
	cout <<"\t{ ";
	if (p2.size() > 0) {
		cout <<p2[0];
		for (int i=1; i<p2.size(); i++)
			cout <<", " <<p2[i];
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	cout <<endl <<"Your answer: " <<endl;
	cout <<"\t{ ";
	if (my_answer.size() > 0) {
		cout <<my_answer[0];
		for (int i=1; i<my_answer.size(); i++)
			cout <<", " <<my_answer[i];
		cout <<" }" <<endl;
	}
	else
		cout <<"}" <<endl;
	if (my_answer != p2) {
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
