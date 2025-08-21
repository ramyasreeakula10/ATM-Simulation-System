# ATM-Simulation-System
This project is a console-based ATM Simulation System built using Java, JDBC, and MySQL.
It mimics the basic functionalities of an ATM machine where users can perform banking operations such as checking balance, depositing money, withdrawing money, and viewing transaction history.

#1.Import the project into your IDE (Eclipse / IntelliJ / VS Code).

#2.Configure MySQL:
CREATE DATABASE ATM_DB;
USE ATM_DB;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50),
    pin VARCHAR(10) UNIQUE,
    balance DOUBLE
);

#Insert one user for testing:

INSERT INTO users (name, pin, balance) 
VALUES ('john', '123123', 100000);

#3.Update database credentials (url, username, password) in Atm_Project.java.

#4.Run the project.
