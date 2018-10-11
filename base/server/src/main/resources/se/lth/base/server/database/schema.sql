-- This is the schema file that the database is initialized with. It is specific to the H2 SQL dialect.
-- Author: Jonathan Jakobsson

--TODO: Session table 
--TODO: DATATYPES of User(password), User(userNAME) och Flag_Reports(flags) see comments
--TODO: Routes(passengers) make foreign key if possible

--Table one made for Userlogin, user information and role specification
CREATE TABLE User(userID INT AUTO_INCREMENT NOT NULL,
                  userName VARCHAR_IGNORECASE NOT NULL UNIQUE, --Vet inte om username ska vara casesesitive?
                  salt BIGINT NOT NULL,
                  password_hash UUID NOT NULL,
				  phoneNr VARCHAR NOT NULL,
				  profilePicture VARCHAR,
				  description VARCHAR,
				  isAdmin BOOLEAN NOT NULL DEFAULT FALSE,
				  
                  PRIMARY KEY (userID),
                  CHECK (LENGTH(userName) >= 8),
				  CHECK (LENGTH(userName) <= 16)
				
); 

	--Lägger till en admin med Användarnamn: Admin och lösen: password
	INSERT INTO User (userID,userName,salt, password_hash,phoneNr,isAdmin) 
	VALUES (1, 'AdminUser', -2883142073796788660, '8dc0e2ab-4bf1-7671-c0c4-d22ffb55ee59','0',TRUE),
           (2, 'TestUser', 5336889820313124494, '144141f3-c868-85e8-0243-805ca28cdabd','0',FALSE);

--Tagen mestadels från labben. Gör en chansning tills vi har har specificerat hur vi ska hantera våra sessions.
CREATE TABLE Session(session_uuid UUID DEFAULT RANDOM_UUID(),
                     userID INT NOT NULL,
                     last_seen TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
					 isAdmin BOOLEAN NOT NULL,
					 
                     PRIMARY KEY(session_uuid),
                     FOREIGN KEY(userID) REFERENCES User(userID) ON DELETE CASCADE);

-- Table used to store active routes
CREATE TABLE Routes(routeID INT AUTO_INCREMENT NOT NULL,
					driverID INT NOT NULL,
					freeSeats INT NOT NULL,
					location VARCHAR NOT NULL,
					destination VARCHAR NOT NULL,
					timeOfDeparture TIMESTAMP NOT NULL,
					timeOfArrival TIMESTAMP NOT NULL,
					passengers VARCHAR NOT NULL, 
					description VARCHAR NOT NULL,
					bookingEndTime TIMESTAMP NOT NULL,
					recurring ENUM ('no', 'daily', 'weekly', 'monthly') DEFAULT ('no'),
					finished BOOLEAN NOT NULL,

					PRIMARY KEY(routeId),
					FOREIGN KEY(driverID) REFERENCES User(userID)
					);

INSERT INTO Routes(routeID,driverID,freeSeats, location,destination,timeOfDeparture,timeOfArrival, passengers,description,bookingEndTime,recurring,finished) 
	VALUES (1, '2', 2, 'Här','Där', '0', '8','','','4','0',FALSE);

--Table used to store reports/flags 
CREATE TABLE FlagReports(flagReportID INT AUTO_INCREMENT NOT NULL,
							routeID INT NOT NULL,
							fromUserID INT NOT NULL,
							toUserID INT NOT NULL,
							reason VARCHAR DEFAULT '', --skulle vara comment men går inte i h2 syntax
							flags VARCHAR NOT NULL, 
							
							PRIMARY KEY(flagReportID),
							FOREIGN KEY(fromUserID) REFERENCES User(userID),
							FOREIGN KEY(toUserID) REFERENCES User(userID) ON DELETE CASCADE,
							FOREIGN KEY(routeID) REFERENCES Routes(routeID) ON DELETE CASCADE

);

--Table used to store requests to join an active route
CREATE TABLE BookingRequests(bookingReqID INT AUTO_INCREMENT,
								routeID INT NOT NULL,
								fromUserID INT Not NULL,
								toUserID INT Not NULL,
								accepted BOOLEAN NOT NULL,
								
								PRIMARY KEY(bookingReqID),
								FOREIGN KEY(routeID) REFERENCES Routes(routeID) ON DELETE CASCADE,
								FOREIGN KEY(fromUserID) REFERENCES User(userID) ON DELETE CASCADE,
								FOREIGN KEY(toUserID) REFERENCES User(userID) ON DELETE CASCADE
								
);
