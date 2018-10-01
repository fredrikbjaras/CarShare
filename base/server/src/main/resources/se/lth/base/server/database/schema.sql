-- This is the schema file that the database is initialized with. It is specific to the H2 SQL dialect.
-- Author: Jonathan Jakobsson

--TODO: Session table 
--TODO: DATATYPES of User(password), User(userNAME) och Flag_Reports(flags) see comments
--TODO: Routes(passengers) make foreign key if possible

--Table one made for Userlogin, user information and role specification
CREATE TABLE User(userID INT AUTO_INCREMENT NOT NULL,
                  userName VARCHAR_IGNORECASE NOT NULL UNIQUE, --Vet inte om username ska vara casesesitive?
                  password VARCHAR NOT NULL, --Kommer förmodligen ändras till något annat om vi kör ett hashat lösen typ UUID 
				  phoneNr INT NOT NULL,
				  profilePicture BLOB,
				  description VARCHAR,
				  isAdmin BOOLEAN NOT NULL DEFAULT FALSE,
				  
                  PRIMARY KEY (userID),
                  CHECK (LENGTH(userName) >= 8),
				  CHECK (LENGTH(userName) <= 16),
				  CHECK (LENGTH(password) >= 8)
				
); 

	--Lägger till en admin med Användarnamn: Admin och lösen: password
	INSERT INTO User (userID,userName,password,phoneNr,isAdmin) 
	VALUES (1,'AdminAdmin','password',123456789,TRUE);

--Tagen mestadels från labben. Gör en chansning tills vi har har specificerat hur vi ska hantera våra sessions.
CREATE TABLE Session(session_uuid UUID DEFAULT RANDOM_UUID(),
                     userID INT NOT NULL,
                     last_seen TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
					 isAdmin BOOLEAN NOT NULL,
					 
                     PRIMARY KEY(session_uuid),
                     FOREIGN KEY(userID) REFERENCES User(userID) ON DELETE CASCADE);

-- Table used to store active routes
CREATE TABLE Routes(routeId INT AUTO_INCREMENT NOT NULL,
					driverID INT NOT NULL,
					freeSeats INT NOT NULL,
					--location ARRAY<DOUBLE> NOT NULL,
					--destination ARRAY<DOUBLE> NOT NULL,
					timeOfDeparture TIMESTAMP NOT NULL,
					timeOfArrival TIMESTAMP NOT NULL,
					--passengers ARRAY<INT> NOT NULL, 
					recurring ENUM ('no', 'daily', 'weekly', 'monthly') DEFAULT ('no'),
					finished BOOLEAN NOT NULL,

					PRIMARY KEY(routeId),
					FOREIGN KEY(driverID) REFERENCES User(userID)
					);

--Table used to store reports/flags 
CREATE TABLE FlagReports(flagReportID INT AUTO_INCREMENT NOT NULL,
							routeID INT NOT NULL,
							fromUserID INT NOT NULL,
							toUserID INT NOT NULL,
							reason VARCHAR DEFAULT '', --skulle vara comment men går inte i h2 syntax
							--flags ARRAY<BOOLEAN> NOT NULL, --Inte säker vad denna är till?
							
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