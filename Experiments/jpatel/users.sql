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
    FOREIGN KEY (PlayerID) REFERENCES (UserID),
    
);

Create TABLE GAME(
ShotID int PRIMARY KEY,
ShotBy VARCHAR(255) NOT NULL,
shotZone int  null,



