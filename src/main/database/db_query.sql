\c breedog
-- Print data about all dogs in the system
SELECT *
	FROM dog;


-- Print data about all adoptable dogs
SELECT * 
	FROM dog
	WHERE status = 'Adottabile';


-- Find the predecessors of a dog
SELECT *
	FROM dog INNER JOIN ( 
	SELECT predecessor
	FROM succeed
	WHERE successor = '380260000000000') AS parents ON dog.microchip = parents.predecessor;


/* Print all dogs of specific breeder*/
SELECT I.breederfc, I.name, I.surname, dog.microchip, dog.name
	FROM dog
	INNER JOIN
	(SELECT * FROM breeder WHERE breederfc='PRCDDR72R18A170D') AS I
	ON I.breederfc = dog.breederfc;


/* Print all basic info of dogs of specific breeder */
SELECT I.breederfc, dog.microchip, dog.name AS dog_name, dog.sex, dog.birth,
	dog.kennel, dog.height, dog.weight, dog.fci, L.name AS breed_name
	FROM dog
	INNER JOIN
	(SELECT * FROM breeder WHERE breederfc='PRCDDR72R18A170D') AS I
	ON I.breederfc = dog.breederfc
	INNER JOIN
	(SELECT breed.fci, breed.name FROM breed) AS L
	ON L.fci = dog.fci;


/* Get specific dog basic + advance informations */
SELECT dog.microchip, dog.tattoo, dog.name, dog.birth, dog.sex, dog.height, dog.weight, dog.coat,
	dog.character, dog.dna, dog.teeth, dog.signs, dog.photo, dog.status, dog.kennel,
	breed.fci, breed.breedgroup, breed.name as breed_name
	FROM dog
	INNER JOIN
	breed ON dog.fci = breed.fci
	WHERE dog.microchip = '380260000000000';


/* Get specific dog pathologies informations */
SELECT dog.microchip, whichas.pathology, pathology.name as pathology_name, pathology.severity
	FROM dog
	INNER JOIN
	whichas ON dog.microchip = whichas.dog
	INNER JOIN
	pathology ON pathology.pathologycode = whichas.pathology
	WHERE dog.microchip = '380260543289992';


/* get specific dog awards informations */
SELECT dog.microchip, takepartin.competition AS competition_id, takepartin.win, competition.type,
        competition.compgroup, competition.class, competition.breederfc AS judge_fc, competition.eventid,
        event.location, event.begin, event.finish, event.origincode
    FROM dog 
    INNER JOIN
    takepartin ON dog.microchip = takepartin.dog
    INNER JOIN
    competition ON takepartin.competition = competition.competitionid
    INNER JOIN
    event ON competition.eventid = event.eventid
    WHERE dog.microchip = '380260012004329';


/* Select all the sons of a requested dog */
SELECT *
    FROM dog
    INNER JOIN
    (SELECT * FROM succeed WHERE succeed.predecessor = '380266549297328') AS a 
    ON dog.microchip = a.successor;
