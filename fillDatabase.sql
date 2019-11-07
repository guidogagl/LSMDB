INSERT INTO `esercizio1`.`azienda` (`nomeAzienda`, `urlLogo`, `urlSito`, `indirizzo`, `cap`,`password`) VALUES ('Tesla', 'https://upload.wikimedia.org/wikipedia/commons/thumb/b/bd/Tesla_Motors.svg/793px-Tesla_Motors.svg.png', 'https://www.tesla.com/it_IT', 'Via Portogallo, 11 Padova', '35127','Tesla');
INSERT INTO `esercizio1`.`azienda` (`nomeAzienda`, `urlLogo`, `urlSito`, `indirizzo`, `cap`,`password`) VALUES ('Volkswagen', 'https://www.stickpng.com/assets/images/580b585b2edbce24c47b2cf2.png', 'https://www.volkswagen.it/it.html', 'Viale G.R. Gumpert 1, Verona', '37137','Volkswagen');
INSERT INTO `esercizio1`.`azienda` (`nomeAzienda`, `urlLogo`, `urlSito`, `indirizzo`, `cap`,`password`) VALUES ('Ecord', 'https://www.marum.de/Binaries/Binary12812/ECORD-logo-solo.jpg', 'https://www.ecord.org', 'Palazzo Berlaymont,Bruxelles', '1049','Ecord');

INSERT INTO `esercizio1`.`progetto` (`nome`, `budget`, `descrizione`, `azienda_id`) VALUES ('Viaggio-Luna', '6000000', 'Sono Elon Musk, e mi servono soldi per andare sulla luna', 'Tesla');
INSERT INTO `esercizio1`.`progetto` (`nome`, `budget`, `descrizione`, `azienda_id`) VALUES ('Nuova Macchina', '4000000', 'Qui Volkswagen,ci servono soldi per creare una nuova macchina che consumi meno', 'Volkswagen');
INSERT INTO `esercizio1`.`progetto` (`nome`, `budget`, `descrizione`, `azienda_id`) VALUES ('Viaggio al centro della Terra', '435000', 'Se ci sono riusciti nel libro,perchè non anche nella realtà?', 'Ecord');

INSERT INTO `esercizio1`.`finanziamento` (`budget`, `azienda_id`, `progetto_id`) VALUES ('1000', 'Tesla', '1');
INSERT INTO `esercizio1`.`finanziamento` (`budget`, `azienda_id`, `progetto_id`) VALUES ('4000', 'Volkswagen', '2');
INSERT INTO `esercizio1`.`finanziamento` (`budget`, `azienda_id`, `progetto_id`) VALUES ('3000', 'Ecord', '3');
INSERT INTO `esercizio1`.`finanziamento` (`budget`, `azienda_id`, `progetto_id`) VALUES ('4000', 'Ecord', '1');

INSERT INTO `esercizio1`.`messaggio` (`mittente_id`, `destinatario_id`, `progetto_id`, `testo`, `stake`, `data`) 
VALUES ('Tesla', 'Ecord', '1', 'La nostra azienda avrebbe bisogno di denaro per finanziare il viaggio sulla Luna', '1000', '2019-10-18');
INSERT INTO `esercizio1`.`messaggio` (`mittente_id`, `destinatario_id`, `progetto_id`, `testo`, `stake`, `data`) 
VALUES ('Volkswagen', 'Ecord', '2', 'Vorremmo un finanziamento per pubblicizzare la nuova auto', '2000', '2019-10-18');
