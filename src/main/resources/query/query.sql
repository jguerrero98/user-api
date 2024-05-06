CREATE TABLE USERS ( 
   ID VARCHAR(255) PRIMARY KEY NOT NULL, 
   CREATED TIMESTAMP NOT NULL, 
   EMAIL VARCHAR(255) UNIQUE NOT NULL, 
   IS_ACTIVE BOOLEAN NOT NULL,
   LAST_LOGIN TIMESTAMP NOT NULL,
   MODIFIED TIMESTAMP NOT NULL,
   NAME VARCHAR(255) NOT NULL, 
   PASSWORD VARCHAR(255) NOT NULL
);


CREATE TABLE REFRESH_TOKEN ( 
   ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
   EXPIRY_DATE TIMESTAMP NOT NULL, 
   TOKEN VARCHAR(255) NOT NULL, 
   USER_ID VARCHAR(255) NOT NULL
);


ALTER TABLE REFRESH_TOKEN
    ADD FOREIGN KEY (USER_ID) 
    REFERENCES USERS(ID);

CREATE TABLE PHONES ( 
   ID BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT, 
   CITY_CODE VARCHAR(255) NOT NULL, 
   COUNTRY_CODE VARCHAR(255) NOT NULL,
   IS_ACTIVE BOOLEAN NOT NULL,
   NUMBER VARCHAR(255) NOT NULL,
   USER_ID VARCHAR(255) NOT NULL
);

ALTER TABLE PHONES
    ADD FOREIGN KEY (USER_ID) 
    REFERENCES USERS(ID);