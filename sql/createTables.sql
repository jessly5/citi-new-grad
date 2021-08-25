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
  `account_id` INT,
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
  `account_id` INT,
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
("stock","AAPL",4, 148.89, 148.45, 4),
("stock","SPOT",4, 215.04, 214.02, 4),
("stock","DIS", 2, 179.29, 182.79, 5),
("stock","FB", 6, 362.65, 363.29, 5),
("stock","BABA", 4, 191.66, 187.00, 5),
("stock","AMZN", 4, 3303.50, 3293.12, 6),
("ETF","SPY", 10, 445.11, 445.56, 6),
("ETF","QQQ", 5, 367.53, 368.56, 6);

-- select * from securities;

INSERT INTO history(account_type, transaction_date, amount) values
("cash", "2020-11-15", 3000),
("cash", "2021-06-15", -1000),
("investment", "2021-06-15", 1000),
("investment", "2021-07-15", 2000),
("cash", "2021-07-15", -2000),
("cash", "2021-08-12", 5000),
("investment",	"2020-08-29" , 489.21),
("investment",	"2020-09-04" ,  -307.03),
("investment",	"2020-09-09" ,  -376.38),
("investment",	"2020-09-14" ,  -661.98),
("investment",	"2020-09-24" ,  -787.26),
("investment",	"2020-09-29" ,  880.83),
("investment",	"2020-10-09" ,  938.62),
("investment",	"2020-10-14" ,  505.39),
("investment",	"2020-10-19" , -844.95),
("investment",	"2020-10-29" ,  -21.06),
("investment",	"2020-11-03" ,  -909.52),
("investment",	"2020-11-13" ,  695.72),
("investment",	"2020-11-18" ,  -5.10),
("investment",	"2020-11-23" , 142.01),
("investment",	"2020-12-03" ,  967.05),
("investment",	"2020-12-08" , 208.55),
("investment",	"2020-12-18" , 180.68),
("investment",	"2020-12-23" ,  -256.69),
("investment",	"2020-12-28" , 396.17),
("investment",	"2021-01-07" ,  -100.46),
("investment",	"2021-01-12" , -169.27),
("investment",	"2021-01-22" , 1053.59),
("investment",	"2021-01-27" , -368.46),
("investment",	"2021-02-01" , 376.66),
("investment",	"2021-02-11" , -65.62),
("investment",	"2021-02-16" , 116.10),
("investment",	"2021-02-26" , -1629.52),
("investment",	"2021-03-03" , -448.75),
("investment",	"2021-03-08" , -645.76),
("investment",	"2021-03-18" , 872.42),
("investment",	"2021-03-23" ,  522.37),
("investment",	"2021-04-07" , 984.33),
("investment",	"2021-04-12" , 601.09),
("investment",	"2021-04-22" , -447.44),
("investment",	"2021-04-27" , 674.08),
("investment",	"2021-05-07" , -731.5),
("investment",	"2021-05-12" , -1197.0),
("investment",	"2021-05-17" ,617.81),
("investment",	"2021-05-27" , 219.15),
("investment",	"2021-06-01" , -49.52),
("investment",	"2021-06-11" , 571.7),
("investment",	"2021-06-16" , 232.36),
("investment",	"2021-06-21" , 223.72),
("investment",	"2021-07-01" , 471.39),
("investment",	"2021-07-06" , 928.18),
("investment",	"2021-07-16" , -588.13),
("investment",	"2021-07-21" , 156.54),
("investment",	"2021-07-26" , 631.37),
("investment",	"2021-08-05" , -1284.47),
("investment",	"2021-08-10" , -288.43),
("investment",	"2021-08-22" , -681.67);
