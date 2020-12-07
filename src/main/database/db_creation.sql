-- DB creation
CREATE DATABASE breedog OWNER POSTGRES ENCODING = 'UTF-8';

\c breedog

-- create extension for generating uuids
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create new data type
CREATE TYPE gender AS ENUM (
    'Male',
    'Female'
);

CREATE TYPE dogstatus AS ENUM (
	'Attivo',
	'Maternità',
	'Pensione',
	'Adottabile'
);

CREATE TYPE competitionclass AS ENUM (

	'Campioni',
	'Libera',
	'IPO-1',
	'IPO-2',
	'IPO-3',
	'CAL',
	'CAE',
    'Esordienti',
	'Sheapdog',
	'Intermedia',
	'Giovani',
	'Juniores',
	'Veterani',
	'Fuori concorso',
	'RSR'
);

CREATE TYPE competitiontype AS ENUM (
	'Show',
	'Lavoro'
);

-- Creation Tables

-- Breeder
CREATE TABLE Breeder (
    BreederFC VARCHAR NOT NULL,
    Name VARCHAR(32) NOT NULL,
    Surname VARCHAR(32) NOT NULL,
    Birth VARCHAR(10) NOT NULL,
    Address VARCHAR NOT NULL,
    Telephone VARCHAR NOT NULL,
    Email VARCHAR NOT NULL,
    Vat VARCHAR,
    Photo VARCHAR,
    Description VARCHAR,
    PRIMARY KEY (BreederFC)
);

-- Event
-- origin code corresponds to italian postal code
CREATE TABLE Event (
    EventID VARCHAR NOT NULL,
    Location VARCHAR NOT NULL,
    Begin VARCHAR(10) NOT NULL,
    Finish VARCHAR(10) NOT NULL,
    OriginCode VARCHAR(5) NOT NULL,
    PRIMARY KEY (EventID)
);

-- Competition
CREATE TABLE Competition (
    CompetitionID VARCHAR NOT NULL,
    Type COMPETITIONTYPE NOT NULL,
    CompGroup SMALLINT NOT NULL CHECK (CompGroup BETWEEN 1 AND 10),
    Class COMPETITIONCLASS NOT NULL,
    EventID VARCHAR NOT NULL,
    PRIMARY KEY (CompetitionID),
    FOREIGN KEY (EventID) REFERENCES Event(EventID)
);


-- Pathology
CREATE TABLE Pathology (
    PathologyCode VARCHAR NOT NULL,
    Name VARCHAR NOT NULL,
    PRIMARY KEY (PathologyCode)
);

-- Breed
CREATE TABLE Breed (
    FCI INT NOT NULL,
    BreedGroup SMALLINT NOT NULL,
    Name VARCHAR NOT NULL,
    PRIMARY KEY (FCI)
);

-- Dog
CREATE TABLE Dog (
    Microchip VARCHAR NOT NULL,
    Tattoo VARCHAR,
    Name VARCHAR NOT NULL,
    Birth VARCHAR(10) NOT NULL,
    Sex GENDER NOT NULL,
    Height NUMERIC NOT NULL,
    Weight NUMERIC NOT NULL,
    Coat VARCHAR NOT NULL,
    Character VARCHAR,
    Dna VARCHAR,
    Teeth VARCHAR,
    Signs VARCHAR,
    Photo VARCHAR NOT NULL,
    Owner VARCHAR NOT NULL,
    Status DOGSTATUS NOT NULL,
    FCI INT NOT NULL,
    BreederFC VARCHAR NOT NULL,
    Kennel VARCHAR,
    PRIMARY KEY (Microchip),
    FOREIGN KEY (FCI) REFERENCES Breed(FCI),
    FOREIGN KEY (BreederFC) REFERENCES Breeder(BreederFC)
);

-- User
CREATE TABLE Users (
    Username VARCHAR NOT NULL,
    Password VARCHAR NOT NULL,
    BreederFC VARCHAR NOT NULL,
    PRIMARY KEY (Username),
    FOREIGN KEY (BreederFC) REFERENCES Breeder(BreederFC)
);

-- Succeed
CREATE TABLE Succeed (
    Predecessor VARCHAR NOT NULL,
    Successor VARCHAR NOT NULL,
    DateBirth VARCHAR(10) NOT NULL,
    PRIMARY KEY (Predecessor, Successor),
    FOREIGN KEY (Predecessor) REFERENCES Dog(Microchip),
    FOREIGN KEY (Successor) REFERENCES Dog(Microchip)
);

-- Which has
CREATE TABLE WhichAs (
    Dog VARCHAR NOT NULL,
    Pathology VARCHAR NOT NULL,
    Severity VARCHAR NOT NULL,
    PRIMARY KEY (Dog, Pathology),
     FOREIGN KEY (Dog) REFERENCES Dog(Microchip),
     FOREIGN KEY (Pathology) REFERENCES Pathology(PathologyCode)
);

-- Take part in
CREATE TABLE TakePartIn (
    Dog VARCHAR NOT NULL,
    Competition VARCHAR NOT NULL,
    Win BOOLEAN NOT NULL,
    PRIMARY KEY (Dog, Competition),
    FOREIGN KEY (Dog) REFERENCES Dog(Microchip),
    FOREIGN KEY (Competition) REFERENCES Competition(CompetitionID)
);


-- Judge
CREATE TABLE Judge (
    CompetitionID VARCHAR NOT NULL,
    BreederFC VARCHAR NOT NULL,
    PRIMARY KEY (CompetitionID),
    FOREIGN KEY (CompetitionID) REFERENCES Competition(CompetitionID),
    FOREIGN KEY (BreederFC) REFERENCES Breeder(BreederFC)
);
