project(test_generated_dependecies_cmake)

add_custom_command(
  OUTPUT ${CMAKE_CURRENT_SOURCE_DIR}/main.cpp
  COMMAND cp ${CMAKE_CURRENT_SOURCE_DIR}/main.txt ${CMAKE_CURRENT_SOURCE_DIR}/main.cpp
)

add_executable(hello main.cpp)

add_custom_command(
  TARGET hello
  COMMAND rm ${CMAKE_CURRENT_SOURCE_DIR}/main.cpp
)
