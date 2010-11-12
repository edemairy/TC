a.
run the test cases

1. set up the database, executes test_files/sqls/all.sql(updated in this version)

2. change the configuration in test_files/config/DB_Factory.xml and user_project_data_store.xml,
And they still on test_files/accuracy, test_files/failure, test_files/stress,
DO NOT FORGET test_files/failure/config.xml
Please NOTE: make sure you have update all database connection in test_files.

3. setup the email-engine setting too. dev null smtp server should start first.

4. add test_files into classpath


d. since the test cases are mostly db related, so it takes long time to execute, please be patient.
