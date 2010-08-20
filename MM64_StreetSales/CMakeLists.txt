project(StreetSales)

set( ${PROJECT_NAME}_SOURCES ${PROJECT_NAME}.cpp main.cpp )
set( ${PROJECT_NAME}_HEADERS ${PROJECT_NAME}.h )

add_executable( ${PROJECT_NAME} ${${PROJECT_NAME}_SOURCES} ${${PROJECT_NAME}_HEADERS} )
