project(StreetSales)
set(CMAKE_BUILD_TYPE Debug)
find_package(Qt4)
include(${QT_USE_FILE})
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}  -Wall " )
qt4_wrap_cpp( ${PROJECT_NAME}_SOURCES_MOC gridviewer.h mainProcessingThread.h )

set( ${PROJECT_NAME}_SOURCES main.cpp gridviewer.cpp StreetSales.h mainProcessingThread.cpp ${${PROJECT_NAME}_SOURCES_MOC})
#set( ${PROJECT_NAME}_HEADERS ${PROJECT_NAME}.h gridviewer.h)

add_executable( ${PROJECT_NAME} ${${PROJECT_NAME}_SOURCES} ${${PROJECT_NAME}_HEADERS} )

target_link_libraries( ${PROJECT_NAME} ${QT_LIBRARIES} )
