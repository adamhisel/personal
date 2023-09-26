CREATE TABLE Users (
    UserID INT PRIMARY Key,
    Username VARCHAR(255) NOT NULL,
    UserType VARCHAR(50) NOT NULL,
    CanViewPersonalAndTeamStats BOOLEAN,
    CanModifyPersonalInfo BOOLEAN,
    CanViewTeamRosters BOOLEAN,
    CanStepThroughGameAnalysis BOOLEAN,
    CanViewOtherTeamStats BOOLEAN,
    CanCreateAndModifyIndividualWorkoutStats BOOLEAN,
    CanViewTeamWorsys_configkoutStats BOOLEAN,
    CanModifyGameStats BOOLEAN,
    CanModifyTeamWorkoutStats BOOLEAN,
    CanCreateAndModifyTeamRosters BOOLEAN,
    CanSetAndModifyLineups BOOLEAN,
    CanDrawVisualsAndPlays BOOLEAN
    
);

create table Team(
teamName varchar(20),
teamID int Primary Key not null,
teamAdmin varchar(20) not null
);



create table shot(
ShotZone int null,
shotID int PRIMARY KEY,
shotPoints int null,
shotUser varchar (20) Not null
);

Create TABLE Game(
ShotID int PRIMARY KEY,
ShotBy VARCHAR(255) NOT NULL,
shotZone int  null,
shotBy varchar (20)  not null
)


