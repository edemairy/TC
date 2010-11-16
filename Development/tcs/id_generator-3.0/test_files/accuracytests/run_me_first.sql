; for mysql

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted)
VALUES ('accuracytests_impl', 0, 10, 0);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted)
VALUES ('accuracytests_factory', 0, 10, 0);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted)
VALUES ('accuracytests_gen_ex1', 0, 10, 0);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted)
VALUES ('accuracytests_gen_ex2', 0, 10, 0);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted)
VALUES ('accuracytests_exh_ex1', 9223372036854775805, 3, 0);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted)
VALUES ('accuracytests_exh_ex2', 9223372036854775806, 4, 1);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted)
VALUES ('accuracytests_exh_ex3', 9223372036854775805, 3, 0);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted)
VALUES ('accuracytests_exh_ex4', 9223372036854775806, 4, 1);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted)
VALUES ('accuracytests_exh_ex5', 9223372036854775805, 3, 0);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted)
VALUES ('accuracytests_exh_ex6', 9223372036854775806, 4, 1);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted)
VALUES ('accuracytests_ejb', 400, 50, 0);



INSERT INTO id_sequences (name, next_block_start, block_size, exhausted)
VALUES ('accuracytests_TestIDGeneratorImpl_normal', 0, 10, 0);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted)
VALUES ('accuracytests_TestIDGeneratorImpl_big', 0, 10, 0);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted)
VALUES ('accuracytests_TestOracleSequanceGenerator_normal', 0, 10, 0);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted)
VALUES ('accuracytests_TestOracleSequanceGenerator_big', 0, 10, 0);




; for oracle

CREATE SEQUENCE accuracytests_oracle_test_noraml
    MINVALUE 10
    MAXVALUE 10000
    START WITH 10
    INCREMENT BY 1
    CACHE 4;


CREATE SEQUENCE accuracytests_oracle_test_big
    MINVALUE 100
    MAXVALUE 500
    START WITH 100
    INCREMENT BY 1
    CACHE 4;
