-- v0
-- Initial schema

CREATE TABLE ONTRACK_VERSION (
  VALUE   INTEGER   NOT NULL,
  UPDATED TIMESTAMP NOT NULL
);

CREATE TABLE ACCOUNTS (
  ID       INTEGER     NOT NULL AUTO_INCREMENT,
  NAME     VARCHAR(40) NOT NULL,
  MODE     VARCHAR(20) NOT NULL,
  PASSWORD VARCHAR(80) NULL,
  ROLE     VARCHAR(16) NOT NULL,
  CONSTRAINT ACCOUNTS_PK PRIMARY KEY (ID),
  CONSTRAINT ACCOUNTS_UQ_NAME UNIQUE (NAME)
);

CREATE TABLE PROJECTS (
  ID          INTEGER      NOT NULL AUTO_INCREMENT,
  NAME        VARCHAR(40)  NOT NULL,
  DESCRIPTION VARCHAR(500) NOT NULL,
  CONSTRAINT PROJECTS_PK PRIMARY KEY (ID),
  CONSTRAINT PROJECTS_UQ UNIQUE (NAME)
);

CREATE TABLE BRANCHES (
  ID          INTEGER      NOT NULL AUTO_INCREMENT,
  PROJECTID   INTEGER      NOT NULL,
  NAME        VARCHAR(40)  NOT NULL,
  DESCRIPTION VARCHAR(500) NOT NULL,
  CONSTRAINT BRANCHES_PK PRIMARY KEY (ID),
  CONSTRAINT BRANCHES_UQ UNIQUE (PROJECTID, NAME),
  CONSTRAINT BRANCHES_FK_PROJECTS FOREIGN KEY (PROJECTID) REFERENCES PROJECTS (ID)
    ON DELETE CASCADE
);

CREATE TABLE BUILDS (
  ID          INTEGER      NOT NULL AUTO_INCREMENT,
  BRANCHID    INTEGER      NOT NULL,
  NAME        VARCHAR(40)  NOT NULL,
  DESCRIPTION VARCHAR(500) NOT NULL,
  CREATION    TIMESTAMP    NOT NULL,
  CREATOR     VARCHAR(40)  NOT NULL,
  CONSTRAINT BUILDS_PK PRIMARY KEY (ID),
  CONSTRAINT BUILDS_UQ UNIQUE (BRANCHID, NAME),
  CONSTRAINT BUILDS_FK_BRANCH FOREIGN KEY (BRANCHID) REFERENCES BRANCHES (ID)
    ON DELETE CASCADE
);

