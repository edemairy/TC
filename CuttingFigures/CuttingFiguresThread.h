#ifndef CUTTINGFIGURESTHREAD_H
#define CUTTINGFIGURESTHREAD_H
#include <QThread>
#include <QTextStream>
#include <string>
#include <vector>
#include <string>
#include <map>
#include <bitset>
#include <set>
using namespace std;
#include "CuttingFigures.h"

class CuttingFiguresThread: public QThread {
public:
    Q_OBJECT
public:
    void run();
private:
    CuttingFigures m_cuttingFigures;
};

void CuttingFiguresThread::run(){
        QTextStream stream(stdin);
        QTextStream out(stdout);
        QTextStream err(stderr);
        err << __PRETTY_FUNCTION__ << "\n";
        err.flush();
        int H;
        stream >> H;
        stream.readLine();
        vector<string> plate(H);

        for ( int i = 0; i < H; i++) {
            plate[i] = stream.readLine().toStdString();
            err << QString::fromStdString(plate[i]) << "\n";
            err.flush();
        }
        int K;
        stream >> K;
        int orderCnt;
        stream >> orderCnt;
        err << __LINE__ << " K=" << K << " orderCnt=" << orderCnt << "\n";
        err.flush();

        m_cuttingFigures.init(plate, K, orderCnt);
        err << __LINE__ << "\n";
        err.flush();

        for ( int orderId = 0; orderId < orderCnt; orderId++) {
            err << __LINE__ << " orderId = " << orderId << "\n";
            err.flush();
            stream >> H;
            stream.readLine();
            vector<string> figure(H);
            for ( int i = 0; i < H; i++) {
                figure[i] = stream.readLine().toStdString();
                err << "i=" << i << " " << QString::fromStdString( figure[i] ) << "\n";
                err.flush();
            }
            int income;
            stream >> income;
            stream.readLine();
            vector<int> ret = m_cuttingFigures.processOrder(figure, income);

            out << ret.size() << "\n";

            for ( int i = 0; i < ret.size(); i++) {
              out << ret[i] << "\n";
            }

            out.flush();
        }
        err << __LINE__ << "\n";
        err.flush();

        exec();
    }

#endif // CUTTINGFIGURESTHREAD_H
