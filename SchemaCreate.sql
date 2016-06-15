DROP TABLE IF EXISTS user;
CREATE TABLE user 
(
	id INT NOT NULL AUTO_INCREMENT Primary Key,
	username VARCHAR(30) NOT NULL,
	password VARCHAR(30) NOT NULL,
	email VARCHAR(30),
	firstName VARCHAR(30), 
	lastName VARCHAR(30), 
	phoneNumber VARCHAR(12),
	isSeller BIT NOT NULL,
	sellerRegistrationPOSTerminalNumber VARCHAR(20),
	buyerCreditCardNumber VARCHAR(20)
);

-- apartment must have at least one room
DROP TABLE IF EXISTS apartment;
CREATE TABLE apartment 
(
	id INT NOT NULL AUTO_INCREMENT Primary Key,
	name VARCHAR(30) NOT NULL,
	state VARCHAR(30) NOT NULL,
	city VARCHAR(30),
	street VARCHAR(30),
	numberInStreet SMALLINT,
	isLocked BIT NULL,
	sellerId INT NOT NULL,
    CONSTRAINT apartment_userId_fk FOREIGN KEY (sellerId) REFERENCES user(id)
);

DROP TABLE IF EXISTS room;
CREATE TABLE room
(
	id INT NOT NULL AUTO_INCREMENT Primary Key,
	numberInApartment TINYINT NOT NULL,
	maxNumberOfOccupants TINYINT NOT NULL,
	description VARCHAR(30),
	isLocked BIT NULL,
	apartmentId INT NOT NULL,
    CONSTRAINT room_apartmentId_fk FOREIGN KEY (apartmentId) REFERENCES apartment(id)
);

DROP TABLE IF EXISTS reservation;
CREATE TABLE reservation
(
	id INT NOT NULL AUTO_INCREMENT Primary Key,
	checkInDate DATE NOT NULL,
	checkOutDate DATE NOT NULL,
	roomId INT NOT NULL,
	buyerId INT NOT NULL,
    CONSTRAINT reservation_roomId_fk FOREIGN KEY (roomId) REFERENCES room(id),
    CONSTRAINT reservation_userId_fk FOREIGN KEY (buyerId) REFERENCES user(id)
);


INSERT INTO user (username, password, isSeller)
VALUES ("prodavac", "test", 1), ("kupac", "test", 0);

INSERT INTO apartment(name, state, city, street, numberinstreet, isLocked, sellerId)
VALUES ("Lux 1.0", "Srbija", "Beograd", "Raljska", 12, 0, 1),
("Lux 2.0", "Srbija", "Beograd", "BeogradNaVodi", 11, 0, 1),
("Std 2.0", "Srbija", "Novi Sad", "Bulevar", 4, 0, 1),
("Std 1.0", "Srbija", null, "Jon Does street", null, 1, 1);

INSERT INTO room(numberInApartment, maxNumberOfOccupants, isLocked, apartmentId)
VALUES (1, 2, 0, 1), (1, 4, 0, 2), (2, 4, 0, 2), (1, 2, 0, 3), (2, 2, 0, 3), (5, 3, 0, 4);

INSERT INTO reservation (checkInDate, checkOutDate, roomId, buyerId)
VALUES (STR_TO_DATE('15-07-2016', '%d-%m-%Y'), STR_TO_DATE('21-07-2016', '%d-%m-%Y'), 2, 2);