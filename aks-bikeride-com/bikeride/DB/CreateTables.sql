create schema z_com_bikeride;

CREATE TABLE z_com_bikeride.users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    created_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE z_com_bikeride.Ride (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ride_name VARCHAR(255) NOT NULL,
    ride_type VARCHAR(50) NOT NULL,
    ride_location VARCHAR(50) NOT NULL,
    ride_date VARCHAR(50) NOT NULL,
    ride_time VARCHAR(50) NOT NULL,
    userId BIGINT NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    users VARCHAR(255) DEFAULT NULL,
    created_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES z_com_bikeride.users(id)
);

CREATE TABLE z_com_bikeride.User_Ride (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ride_id BIGINT,
    users_id BIGINT,
    ride_name VARCHAR(255) NOT NULL,
    ride_type VARCHAR(50) NOT NULL,
    created_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ride_id) REFERENCES z_com_bikeride.Ride(id),
    FOREIGN KEY (users_id) REFERENCES z_com_bikeride.users(id)
);