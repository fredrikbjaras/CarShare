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
	VALUES (1000000000, 'AdminUser', -2883142073796788660, '8dc0e2ab-4bf1-7671-c0c4-d22ffb55ee59','0',TRUE),
           (1000000001, 'TestUser', 5336889820313124494, '144141f3-c868-85e8-0243-805ca28cdabd','0',FALSE),
           (1000000002, 'TestUser2', '-2139185227452823400', '79ce2b6d-e279-5a0b-426a-a33d659a4934', '123', FALSE);

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
					passengers VARCHAR, 
					description VARCHAR NOT NULL,
					bookingEndTime TIMESTAMP NOT NULL,
					recurring ENUM ('no', 'daily', 'weekly', 'monthly') DEFAULT ('no'),
					finished BOOLEAN NOT NULL,

					PRIMARY KEY(routeId),
					FOREIGN KEY(driverID) REFERENCES User(userID)
					);

INSERT INTO Routes(routeID,driverID,freeSeats, location,destination,timeOfDeparture,timeOfArrival, passengers,description,bookingEndTime,recurring,finished) 
	VALUES (1, 1000000001, 4, 'Här','Där', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(),'1000000000;','',CURRENT_TIMESTAMP(),'no',FALSE),
		   (2, 1000000001, 3, 'Lund','Malmö', PARSEDATETIME('2020-01-02 09:00:00', 'yyyy-MM-dd HH:mm:ss'), PARSEDATETIME('2020-01-02 10:00:00', 'yyyy-MM-dd HH:mm:ss'),'','',PARSEDATETIME('2020-01-01 22:00:00', 'yyyy-MM-dd HH:mm:ss'),'no',FALSE);

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

INSERT INTO BookingRequests(bookingReqID, routeID, fromUserID, toUserID, accepted)
VALUES (1, 1, 1000000002, 1000000001, FALSE);