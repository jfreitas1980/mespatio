  CREATE TABLE "MESPATIO"."IP_ANALOGDEF"
   ( "IP_TREND_TIME" DATE,
 "IP_TREND_VALUE" NUMBER(22,0),
 "NAME" VARCHAR2(20 BYTE)
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "USERS" ;

--------------------

REM INSERTING into IP_ANALOGDEF

------------

Insert into IP_ANALOGDEF (IP_TREND_TIME,IP_TREND_VALUE,NAME) values (to_timestamp('16/10/09','DD/MM/RR HH24:MI:SSXFF'),2000,'WIT_798A-OE');
/
Insert into IP_ANALOGDEF (IP_TREND_TIME,IP_TREND_VALUE,NAME) values (to_timestamp('16/10/09','DD/MM/RR HH24:MI:SSXFF'),10000,'WIT_798A-OE');
/
Insert into IP_ANALOGDEF (IP_TREND_TIME,IP_TREND_VALUE,NAME) values (to_timestamp('16/10/09','DD/MM/RR HH24:MI:SSXFF'),12000,'WIT_798A-OE');
/
Insert into IP_ANALOGDEF (IP_TREND_TIME,IP_TREND_VALUE,NAME) values (to_timestamp('16/10/09','DD/MM/RR HH24:MI:SSXFF'),13000,'WIT_798A-OE');
/
Insert into IP_ANALOGDEF (IP_TREND_TIME,IP_TREND_VALUE,NAME) values (to_timestamp('16/10/09','DD/MM/RR HH24:MI:SSXFF'),0,'WIT_798A-OE');
/
Insert into IP_ANALOGDEF (IP_TREND_TIME,IP_TREND_VALUE,NAME) values (to_timestamp('16/10/09','DD/MM/RR HH24:MI:SSXFF'),1000,'WIT_798A-OE');


