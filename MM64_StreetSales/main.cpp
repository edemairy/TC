#include <QObject>
#include <iostream>
#include <sstream>
#include <cassert>
using namespace std;
#include <QApplication>
#include "gridviewer.h"
#include <QSemaphore>
#include "mainProcessingThread.h"

QSemaphore semaphore;
extern vector<string> districtMap;
GridViewer* grid;


int main( int argc, char** argv ) {
   qRegisterMetaType<vector<string> >("vector<string>");

   QThread* thread = new MainProcessingThread( );
   thread->start();
  // thread->exec();
   QApplication* app = new QApplication( argc, argv );
   semaphore.acquire();
   grid = new GridViewer(districtMap);
   QObject::connect( thread, SIGNAL(sendPath(vector<string>)), grid, SLOT(sendPath(vector<string>))); //, Qt::QueuedConnection );
   grid->show();
   app->exec();
}
