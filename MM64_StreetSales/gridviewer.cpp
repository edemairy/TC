#include "gridviewer.h"
#include <QPen>
#include <QBrush>
#include <iostream>
#include <sstream>
using namespace std;




GridViewer::GridViewer( vector<string> i_map ):
     scene( new QGraphicsScene ),
        view(new QGraphicsView(scene) )
{
    setCentralWidget( view );
    map = i_map;
}

void GridViewer::drawMap(){
    for (unsigned int i = 0; i < map.size(); i++ ) {
        for (unsigned int j=0; j<map[0].length(); j++ ){
            if ( map[i][j] == '.' ) {
              scene->addRect(i*10,j*10,10,10,
                           QPen(Qt::green,1),
                           QBrush(Qt::white)
                          );
            } else if ( map[i][j] == 'X' ) {
                scene->addRect(i*10,j*10,10,10,
                             QPen(Qt::green,1),
                             QBrush(Qt::black)
                            );
            }
        }
    }
}

void GridViewer::sendPath( vector<string> path ){
    scene->clear();
    drawMap();
    for (unsigned int i = 0; i<path.size(); ++i ) {
        istringstream is(path[i]);
        int i,j;
        is >> i >> j;
        cerr << "i="<<i<<" ,j="<<j<<endl;
        scene->addRect(i*10,j*10,10,10,
                     QPen(Qt::red,1),
                     QBrush(Qt::yellow)
                    );

    }
}
