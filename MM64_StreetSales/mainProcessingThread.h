#ifndef MAINPROCESSINGTHREAD_H
#define MAINPROCESSINGTHREAD_H
#include <QThread>
#include <vector>
#include <QSemaphore>
using namespace std;
extern QSemaphore semaphore;

class MainProcessingThread: public QThread  {
  Q_OBJECT
 signals:
        void sendPath( vector<string> map );
        void test();

public:
        MainProcessingThread():QThread(),stepByStep(true) {}
  void run();
private:
  bool stepByStep;
};
#endif // MAINPROCESSINGTHREAD_H
