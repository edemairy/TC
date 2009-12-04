#project(test_generated_dependecies_cmake)

add_custom_command(
  OUTPUT ${CMAKE_BINARY_DIR}/main.cpp
  COMMAND cp ${CMAKE_CURRENT_SOURCE_DIR}/main.txt ${CMAKE_BINARY_DIR}/main.cpp
)

add_library( dummy_lib
  ${CMAKE_BINARY_DIR}/main.cpp
)
