INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) 
VALUES ('FAILURE_SEQUENCE', 0, 10, 0);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) 
VALUES ('FAILURE_EXHAUST_1', 9223372036854775807, 1, 1);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) 
VALUES ('FAILURE_EXHAUST_2', 9223372036854775807, 20, 0);

; it's for oracle sequence
CREATE SEQUENCE FAILURE_SEQUENCE_SEQ
    MINVALUE 1
    MAXVALUE 10000000
    START WITH  1
    INCREMENT BY 20
    CACHE 20;
    
CREATE SEQUENCE FAILURE_EXHAUST_1_SEQ
    MINVALUE 1
    MAXVALUE 1000
    START WITH  1000
    INCREMENT BY 20
    CACHE 20;    

CREATE SEQUENCE FAILURE_EXHAUST_2_SEQ
    MINVALUE 1
    MAXVALUE 1000
    START WITH  990
    INCREMENT BY 20
    CACHE 20;   
