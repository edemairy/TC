#include <QtGui/QApplication>
#include "mainwindow.h"
#include "CuttingFigures.h"
#include <QThread>

class CuttingFiguresThread: public QThread {
public:
    void run();
private:
    CuttingFigures m_cuttingFigures;
};

int main(int argc, char *argv[])
{
    CuttingFiguresThread thread;
    thread.start();
    QApplication a(argc, argv);
    MainWindow w;
    //w.show();
    return a.exec();
}
