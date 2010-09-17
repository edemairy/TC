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

        int H;
        stream >> H;
        stream.readLine();
        vector<string> plate(H);

        for ( int i = 0; i < H; i++) {
            plate[i] = stream.readLine().toStdString();
        }
        int K;
        stream >> K;
        int orderCnt;
        stream >> orderCnt;

        m_cuttingFigures.init(plate, K, orderCnt);

        for ( int orderId = 0; orderId < orderCnt; orderId++) {
            stream >> H;
            stream.readLine();
            vector<string> figure(H);
            for ( int i = 0; i < H; i++) {
                figure[i] = stream.readLine().toStdString();
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

        exec();
    }

#endif // CUTTINGFIGURESTHREAD_H
