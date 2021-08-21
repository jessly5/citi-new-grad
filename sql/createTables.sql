DROP DATABASE IF EXISTS conygre;
CREATE DATABASE conygre;

USE conygre;

-- Table 1: Accounts
CREATE TABLE `conygre`.`accounts` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `amount` DECIMAL(13,2) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`));



-- Table 2: History
CREATE TABLE `conygre`.`history` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `account_type` VARCHAR(45) NOT NULL,
  `transaction_date` DATE NOT NULL,
  `amount` DECIMAL(13,2) NOT NULL,
  `account_id` INT NOT NULL,
  PRIMARY KEY (`id`));
  
  
  
-- Table 3: Securities
CREATE TABLE `conygre`.`securities` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) NOT NULL,
  `symbol` VARCHAR(45) NOT NULL,
  `holdings` INT NOT NULL,
  `closing_cost` DECIMAL(13,2) NOT NULL,
  `current_cost` DECIMAL(13,2) NOT NULL,
  `cash_account` VARCHAR(45),
  `account_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_accounts_securities`
    FOREIGN KEY (`account_id`)
    REFERENCES `conygre`.`accounts` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);




USE conygre;

INSERT INTO accounts(amount, name, type) values
(10000, "RBC", "cash"),
(10000, "TD", "cash"),
(50000, "CitiBank Savings", "cash");

INSERT INTO accounts(amount, name, type) values
(2894.38, "Fort Trade", "investment"),
(3293.32, "Wealth Simple", "investment"),
(19470.88, "Citi Ventures", "investment");

-- select * from accounts;

-- If you need to reset the table ID generation and wipe the current table execute below code 
-- DELETE FROM accounts where id<100;
-- SET  @num := 0;
-- UPDATE accounts SET id = @num := (@num+1);
-- ALTER TABLE accounts AUTO_INCREMENT =1;

INSERT INTO securities(type, symbol, holdings, closing_cost, current_cost, account_id) values
("stock","TSLA",2, 719.13, 722.25, 4),
("stock","APPL",4, 148.89, 148.45, 4),
("stock","SPOT",4, 215.04, 214.02, 4),
("stock","DIS", 2, 179.29, 182.79, 5),
("stock","FB", 6, 362.65, 363.29, 5),
("stock","BABA", 4, 191.66, 187.00, 5),
("stock","AMZN", 4, 3303.50, 3293.12, 6),
("ETF","SPY", 10, 445.11, 445.56, 6),
("ETF","QQQ", 5, 367.53, 368.56, 6);

-- select * from securities;

INSERT INTO history(account_type, transaction_date, amount, account_id) values
("cash", "2021-06-15", -1000, 1),
("cash", "2021-07-15", -2000, 2),
("cash", "2021-08-12", 3000, 3);
