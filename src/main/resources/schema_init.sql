CREATE SCHEMA IF NOT EXISTS poc;

/*==============================================================*/
/* Table: connectivity_history */
/*==============================================================*/
CREATE TABLE IF NOT EXISTS poc.connectivity_history (
ID INT4 not null,
HOST VARCHAR(100) null,
PORT VARCHAR(5) null,
STATUS VARCHAR(15) null,
TRIGGERED_BY INT4 null,
TRIGGERED_AT TIMESTAMP null,
PING_RESULT TEXT null,
SOCKET_RESULT TEXT null,
CURL_RESULT TEXT null,
constraint PK_CONNECTIVITY_HISTORY primary key (ID)
);