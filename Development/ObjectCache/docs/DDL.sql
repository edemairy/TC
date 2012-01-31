CREATE TABLE ALERT.CACHED_OBJECT ( 
	ID VARCHAR(45)  NOT NULL , 
	CACHE_SET_NAME VARCHAR(45)  NOT NULL , 
	CONTENT BLOB(1048576) NOT NULL , 
	CREATE_TS TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , 
	EXPIRE_TS TIMESTAMP,
	PRIMARY KEY( ID , CACHE_SET_NAME ) ) ;
	
COMMENT ON TABLE ALERT.CACHED_OBJECT 
	IS 'Serializable Java Object Database Cache table' ; 
  
COMMENT ON COLUMN ALERT.CACHED_OBJECT 
( ID IS 'Unique Id for Serializable Java Object cached' , 
CACHE_SET_NAME IS 'Unique cache name.', 
CONTENT IS 'Binary storage of Serializable Java Object',
CREATE_TS IS 'Timestamp when cached entry was created', 
EXPIRE_TS IS 'Timestamp when cached entry is to expire.  Null value mean the entry never expires Default value is Null) ;
