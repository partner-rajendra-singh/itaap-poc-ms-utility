CREATE SCHEMA IF NOT EXISTS poc;

/*==============================================================*/
/* Table: azureVariables */
/*==============================================================*/
CREATE TABLE IF NOT EXISTS poc.AZURE_VARIABLES_REQUEST(
ID INT not null,
REQUESTER VARCHAR not null,
APPROVER VARCHAR not null,
REQUEST_DATE DATE,
AZURE_GROUP_NAME VARCHAR not null,
UPDATED_ALL_VARIABLES VARCHAR not null,
MODIFIED_VARIABLES VARCHAR not null,
STATUS VARCHAR not null,

constraint PK_AZURE primary key (ID)
);
