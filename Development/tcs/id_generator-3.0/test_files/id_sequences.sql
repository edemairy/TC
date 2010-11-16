; it's for IDGeneratorImpl
CREATE TABLE id_sequences (
    name                VARCHAR(255) NOT NULL  PRIMARY KEY,
    next_block_start    BIGINT NOT NULL,
    block_size          INT NOT NULL,
    exhausted		int NOT NULL default 0
);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) 
VALUES ('unit_test_id_sequence', 0, 10, 0);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) 
VALUES ('unit_test_id_sequence_2', 0, 20, 0);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) 
VALUES ('unit_test_bean', 0, 10, 0);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) 
VALUES ('unit_test_bean_2', 0, 10, 0);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) 
VALUES ('id_exhaust_1', 9223372036854775807, 1, 1);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) 
VALUES ('id_exhaust_2', 9223372036854775807, 20, 0);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) 
VALUES ('test', 0, 10, 0);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) 
VALUES ('test100', 0, 100, 0);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) 
VALUES ('test250', 0, 250, 0);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) 
VALUES ('test500', 0, 500, 0);

; it's for oracle sequence
CREATE SEQUENCE ID_SEQUENCE_SEQ
    MINVALUE 1
    MAXVALUE 10000000
    START WITH  1
    INCREMENT BY 20
    CACHE 20;
    
CREATE SEQUENCE ORACLE_EXHAUST_SEQ
    MINVALUE 1
    MAXVALUE 1000
    START WITH  1000
    INCREMENT BY 20
    CACHE 20;    

CREATE SEQUENCE ORACLE_EXHAUST_LESS
    MINVALUE 1
    MAXVALUE 1000
    START WITH  990
    INCREMENT BY 20
    CACHE 20;   

CREATE SEQUENCE ORACLE_EXHAUST_LESS_SEQ
    MINVALUE 1
    MAXVALUE 1000
    START WITH  990
    INCREMENT BY 20
    CACHE 20;
    