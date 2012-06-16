-- Generado por Oracle SQL Developer Data Modeler 3.1.0.699
--   en:        2012-06-11 18:40:01 CLT
--   sitio:      Oracle Database 11g
--   tipo:      Oracle Database 11g



CREATE TABLE Categoria 
    ( 
     idCategoria INTEGER , 
     nombre VARCHAR2 (30 CHAR)  NOT NULL , 
     Usuario_nombre VARCHAR2 (30 CHAR)  NOT NULL 
    ) 
;



ALTER TABLE Categoria 
    ADD CONSTRAINT Categoria_PK PRIMARY KEY ( idCategoria ) ;



CREATE TABLE Cuenta 
    ( 
     idCuenta INTEGER , 
     nombre VARCHAR2 (30 CHAR)  NOT NULL , 
     comentario CLOB , 
     saldo INTEGER DEFAULT 0  NOT NULL , 
     Usuario_nombre VARCHAR2 (30 CHAR)  NOT NULL 
    ) 
;



ALTER TABLE Cuenta 
    ADD CONSTRAINT Cuenta_PK PRIMARY KEY ( idCuenta ) ;



CREATE TABLE Prestamo 
    ( 
     fecha_limite DATE  NOT NULL , 
     Transaccion_idTransaccion INTEGER  NOT NULL 
    ) 
;



ALTER TABLE Prestamo 
    ADD CONSTRAINT Prestamo_PK PRIMARY KEY ( Transaccion_idTransaccion ) ;



CREATE TABLE Transaccion 
    ( 
     idTransaccion INTEGER , 
     monto INTEGER  NOT NULL , 
     fecha DATE  NOT NULL , 
     tipo VARCHAR2 (8)  NOT NULL CHECK ( tipo IN ('GASTO', 'INGRESO', 'PRESTAMO')) , 
     Categoria_idCategoria INTEGER  NOT NULL , 
     Cuenta_idCuenta INTEGER  NOT NULL 
    ) 
;



ALTER TABLE Transaccion 
    ADD CONSTRAINT Transaccion_PK PRIMARY KEY ( idTransaccion ) ;



CREATE TABLE Transaccion_Interna 
    ( 
     Transaccion_idTransaccion INTEGER  NOT NULL , 
     Cuenta_idCuenta INTEGER  NOT NULL 
    ) 
;



ALTER TABLE Transaccion_Interna 
    ADD CONSTRAINT Transaccion_Interna_PK PRIMARY KEY ( Transaccion_idTransaccion, Cuenta_idCuenta ) ;



CREATE TABLE Usuario 
    ( 
     nombre VARCHAR2 (30 CHAR)  NOT NULL , 
     nombre_completo VARCHAR2 (100 CHAR)  NOT NULL , 
     contraseña VARCHAR2 (30 CHAR)  NOT NULL , 
     correo VARCHAR2 (50 CHAR)  NOT NULL , 
     admin CHAR (1)  NOT NULL 
    ) 
;



ALTER TABLE Usuario 
    ADD CONSTRAINT Usuario_PK PRIMARY KEY ( nombre ) ;




ALTER TABLE Categoria 
    ADD CONSTRAINT Categoria_Usuario_FK FOREIGN KEY 
    ( 
     Usuario_nombre
    ) 
    REFERENCES Usuario 
    ( 
     nombre
    ) 
    ON DELETE CASCADE 
;


ALTER TABLE Cuenta 
    ADD CONSTRAINT Cuenta_Usuario_FK FOREIGN KEY 
    ( 
     Usuario_nombre
    ) 
    REFERENCES Usuario 
    ( 
     nombre
    ) 
    ON DELETE CASCADE 
;


ALTER TABLE Prestamo 
    ADD CONSTRAINT Prestamo_Transaccion_FK FOREIGN KEY 
    ( 
     Transaccion_idTransaccion
    ) 
    REFERENCES Transaccion 
    ( 
     idTransaccion
    ) 
;


ALTER TABLE Transaccion 
    ADD CONSTRAINT Transaccion_Categoria_FK FOREIGN KEY 
    ( 
     Categoria_idCategoria
    ) 
    REFERENCES Categoria 
    ( 
     idCategoria
    ) 
    ON DELETE CASCADE 
;


ALTER TABLE Transaccion 
    ADD CONSTRAINT Transaccion_Cuenta_FK FOREIGN KEY 
    ( 
     Cuenta_idCuenta
    ) 
    REFERENCES Cuenta 
    ( 
     idCuenta
    ) 
    ON DELETE CASCADE 
;


ALTER TABLE Transaccion_Interna 
    ADD CONSTRAINT fk_Cuenta FOREIGN KEY 
    ( 
     Cuenta_idCuenta
    ) 
    REFERENCES Cuenta 
    ( 
     idCuenta
    ) 
;


ALTER TABLE Transaccion_Interna 
    ADD CONSTRAINT fk_Transaccion FOREIGN KEY 
    ( 
     Transaccion_idTransaccion
    ) 
    REFERENCES Transaccion 
    ( 
     idTransaccion
    ) 
;




ALTER SESSION SET PLSCOPE_SETTINGS = 'IDENTIFIERS:NONE';




CREATE SEQUENCE Categoria_idCategoria_SEQ 
    NOCACHE 
    ORDER ;

CREATE OR REPLACE TRIGGER Categoria_idCategoria_TRG 
BEFORE INSERT ON Categoria 
FOR EACH ROW 
WHEN (NEW.idCategoria IS NULL) 
BEGIN 
    SELECT Categoria_idCategoria_SEQ.NEXTVAL INTO :NEW.idCategoria FROM DUAL; 
END;
/

CREATE SEQUENCE Cuenta_idCuenta_SEQ 
    NOCACHE 
    ORDER ;

CREATE OR REPLACE TRIGGER Cuenta_idCuenta_TRG 
BEFORE INSERT ON Cuenta 
FOR EACH ROW 
WHEN (NEW.idCuenta IS NULL) 
BEGIN 
    SELECT Cuenta_idCuenta_SEQ.NEXTVAL INTO :NEW.idCuenta FROM DUAL; 
END;
/

CREATE SEQUENCE Transaccion_idTransaccion_SEQ 
    NOCACHE 
    ORDER ;

CREATE OR REPLACE TRIGGER Transaccion_idTransaccion_TRG 
BEFORE INSERT ON Transaccion 
FOR EACH ROW 
WHEN (NEW.idTransaccion IS NULL) 
BEGIN 
    SELECT Transaccion_idTransaccion_SEQ.NEXTVAL INTO :NEW.idTransaccion FROM DUAL; 
END;
/




ALTER SESSION SET PLSCOPE_SETTINGS = 'IDENTIFIERS:ALL'; 



-- Informe de Resumen de Oracle SQL Developer Data Modeler: 
-- 
-- CREATE TABLE                             6
-- CREATE INDEX                             0
-- ALTER TABLE                             13
-- CREATE VIEW                              0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           3
-- ALTER TRIGGER                            0
-- CREATE STRUCTURED TYPE                   0
-- CREATE COLLECTION TYPE                   0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          3
-- CREATE MATERIALIZED VIEW                 0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0
