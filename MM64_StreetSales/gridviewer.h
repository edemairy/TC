#ifndef GRIDVIEWER_H
#define GRIDVIEWER_H

#include <QGraphicsView>
#include <QMainWindow>
#include <vector>
#include <string>
using namespace std;

class GridViewer : public QMainWindow
{
    Q_OBJECT
public:
    GridViewer( vector<string> map );
public slots:
    void sendPath( vector<string> map );
private:
    void drawMap();

    QGraphicsScene* scene;
    QGraphicsView* view;
    vector<string> map;

};

#endif // GRIDVIEWER_H
