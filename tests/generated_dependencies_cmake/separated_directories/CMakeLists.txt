project(test_generated_dependecies_cmake_several_directories)
add_subdirectory( user_dir )
add_subdirectory( src )
add_dependencies( hello dummy_lib )



