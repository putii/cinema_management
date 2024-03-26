DROP DATABASE IF EXISTS cinema_db;
CREATE DATABASE cinema_db;

-- CREATING USER AND GRANTING HIM PRIVILAGES
-- CREATE USER 'cinema_db_user'@'localhost' IDENTIFIED BY '1234';
-- GRANT SELECT, INSERT, UPDATE, DELETE ON cinema_db.* TO 'cinema_db_user'@'localhost';
-- FLUSH PRIVILEGES;



CREATE TABLE cinema_db.client
(
    client_id  INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL
);

CREATE TABLE cinema_db.movie
(
    movie_id INT PRIMARY KEY AUTO_INCREMENT,
    title    VARCHAR(255) NOT NULL,
    length   INT          NOT NULL CHECK (length > 0),
    genre    VARCHAR(255) NOT NULL,
    price    DOUBLE       NOT NULL CHECK (price >= 0)
);


CREATE TABLE cinema_db.hall
(
    hall_id         INT PRIMARY KEY AUTO_INCREMENT,
    num_of_rows     INT NOT NULL CHECK (num_of_rows > 0),
    seats_per_row   INT NOT NULL CHECK (seats_per_row > 0),
    name            VARCHAR(255) NOT NULL
);

CREATE TABLE cinema_db.seat
(
    seat_id  INT PRIMARY KEY AUTO_INCREMENT,
    number   INT NOT NULL CHECK (number >= 1),
    row      INT NOT NULL CHECK (number >= 1),
    hall_id  INT NOT NULL,
    FOREIGN KEY (hall_id) REFERENCES cinema_db.hall (hall_id) ON DELETE CASCADE
);

CREATE TABLE cinema_db.showing
(
    showing_id  INT PRIMARY KEY AUTO_INCREMENT,
    movie_id    INT NOT NULL,
    hall_id     INT NOT NULL,
    date        TIMESTAMP NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES cinema_db.movie (movie_id) ON DELETE CASCADE,
    FOREIGN KEY (hall_id) REFERENCES cinema_db.hall (hall_id) ON DELETE CASCADE
);

CREATE TABLE cinema_db.booking
(
    booking_id  INT PRIMARY KEY AUTO_INCREMENT,
    client_id   INT NOT NULL,
    showing_id  INT NOT NULL,
    seat_id     INT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES cinema_db.client (client_id) ON DELETE CASCADE,
    FOREIGN KEY (seat_id) REFERENCES cinema_db.seat (seat_id) ON DELETE CASCADE,
    FOREIGN KEY (showing_id) REFERENCES cinema_db.showing (showing_id) ON DELETE CASCADE
);

-- TRIGGERS
-- Create a trigger to check if booking isn't already made and to verify hall_id consistency
DELIMITER //

CREATE TRIGGER before_booking_insert
BEFORE INSERT ON booking
FOR EACH ROW
BEGIN
    DECLARE seat_count INT;
    DECLARE showing_hall_id INT;
    DECLARE seat_hall_id INT;

    -- Check if the seat is already taken for the specified showing
    SELECT COUNT(*) INTO seat_count
    FROM booking
    WHERE showing_id = NEW.showing_id AND seat_id = NEW.seat_id;

    -- If the seat is already taken, raise an error
    IF seat_count > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Seat is already taken for this showing';
    END IF;

    -- Check if the seat is available for the specified showing
    SELECT hall_id INTO showing_hall_id FROM showing WHERE showing_id = NEW.showing_id;
    SELECT hall_id INTO seat_hall_id FROM seat WHERE seat_id = NEW.seat_id;

    -- Check if the hall_id from the showing and seat match
    IF showing_hall_id != seat_hall_id THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Hall mismatch between showing and seat';
    END IF;

    -- Check if the seat is available
    SELECT COUNT(*) INTO seat_count
    FROM seat
    WHERE hall_id = showing_hall_id AND seat_id = NEW.seat_id;

    -- If the seat is not available, raise an error
    IF seat_count = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Seat is not available for this showing';
    END IF;
END //
DELIMITER ;

-- Trigger for creating new seats for each newly created Hall
DELIMITER //
CREATE TRIGGER after_insert_hall
AFTER INSERT
ON cinema_db.hall FOR EACH ROW
BEGIN
    DECLARE row_index INT DEFAULT 1;
    DECLARE seat_index INT;
    
    WHILE row_index <= NEW.num_of_rows DO
        SET seat_index = 1;
        WHILE seat_index <= NEW.seats_per_row DO
            INSERT INTO cinema_db.seat (number, row, hall_id)
            VALUES (seat_index, row_index, NEW.hall_id);
            SET seat_index = seat_index + 1;
        END WHILE;
        SET row_index = row_index + 1;
    END WHILE;
END //
DELIMITER ;


-- INSERTS
INSERT INTO cinema_db.hall (num_of_rows, seats_per_row, name)
VALUES (3, 4, 'Hall 1'),
       (4, 5, 'Hall 2'),
       (2, 6, 'Hall 3');

-- seats should be created using trigger

INSERT INTO cinema_db.movie (title, length, genre, price)
VALUES 
    ('The Matrix', 150, 'Sci-Fi', 10.99),
    ('Inception', 148, 'Sci-Fi', 12.99),
    ('The Shawshank Redemption', 142, 'Drama', 9.99),
    ('The Dark Knight', 152, 'Action', 11.99),
    ('Pulp Fiction', 154, 'Crime', 10.49);

INSERT INTO cinema_db.client (first_name, last_name)
VALUES 
    ('John', 'Doe'),
    ('Alice', 'Smith'),
    ('Bob', 'Johnson');

INSERT INTO cinema_db.showing (movie_id, hall_id, date)
VALUES 
    (1, 1, '2023-01-01 18:00:00'),
    (2, 2, '2023-01-02 15:30:00'),
    (3, 3, '2023-01-03 20:15:00');

-- Inserting 16 new bookings with seat availability checks
INSERT INTO cinema_db.booking (client_id, showing_id, seat_id)
VALUES
    (1, 1, 1), 
	(2, 1, 2), 
	(3, 1, 4), 
	(1, 1, 5),
    (2, 1, 12), 
	(3, 2, 16), 
	(1, 2, 18), 
	(2, 2, 29),
	(1, 2, 14), 
	(2, 2, 23), 
	(3, 2, 30), 
	(1, 3, 37),
    (2, 3, 35), 
	(3, 3, 41), 
	(1, 3, 40), 
	(2, 3, 43);
	
	
