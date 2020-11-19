CREATE TABLE users (
  userID varchar(32) NOT NULL,
  print1 LONGBLOB,
  PRIMARY KEY (userID),
  INDEX	(vcustomer_id)
)TYPE=InnoDB;
