INSERT INTO styles(id, name)	VALUES (1, 'POP');
INSERT INTO styles(id, name)	VALUES (2, 'ROCK');
INSERT INTO styles(id, name)	VALUES (3, 'JAZZ');
INSERT INTO styles(id, name)	VALUES (4, 'TRAP');
INSERT INTO styles(id, name)	VALUES (5, 'RANCHERAS');
INSERT INTO styles(id, name)	VALUES (6, 'CLÁSICA');
INSERT INTO styles(id, name)	VALUES (7, 'BLUES');
INSERT INTO styles(id, name)	VALUES (8, 'ELECTRÓNICA');

INSERT INTO artists(id, name, year) VALUES (1, 'SHAKIRA', 2013);
INSERT INTO artists(id, name, year) VALUES (2, 'MALUMA', 2016);
INSERT INTO artists(id, name, year) VALUES (3, 'FARRUKO', 2010);
INSERT INTO artists(id, name, year) VALUES (4, 'ALEJANDRO FERNANDEZ', 2010);
INSERT INTO artists(id, name, year) VALUES (5, 'THALIA', 2013);
INSERT INTO artists(id, name, year) VALUES (6, 'NACHO', 2015);
INSERT INTO artists(id, name, year) VALUES (7, 'ALEJANDRO SANZ', 2010);
INSERT INTO artists(id, name, year) VALUES (8, 'FREDDIE MERCURY', 2018);

INSERT INTO REL_ARTIST_STYLE(FK_ARTIST, FK_STYLE) VALUES (1,1);
INSERT INTO rel_artist_style(FK_ARTIST, FK_STYLE) VALUES (1,3);
INSERT INTO rel_artist_style(FK_ARTIST, FK_STYLE) VALUES (2,1);
INSERT INTO rel_artist_style(FK_ARTIST, FK_STYLE) VALUES (3,4);
INSERT INTO rel_artist_style(FK_ARTIST, FK_STYLE) VALUES (3,1);
INSERT INTO rel_artist_style(FK_ARTIST, FK_STYLE) VALUES (4,5);
INSERT INTO rel_artist_style(FK_ARTIST, FK_STYLE) VALUES (5,6);
INSERT INTO rel_artist_style(FK_ARTIST, FK_STYLE) VALUES (6,1);
INSERT INTO rel_artist_style(FK_ARTIST, FK_STYLE) VALUES (7,7);
INSERT INTO rel_artist_style(FK_ARTIST, FK_STYLE) VALUES (8,8);


INSERT INTO rel_artist_related(FK_ARTIST, FK_ARTIST_RELATED) VALUES (1,2);
INSERT INTO rel_artist_related(FK_ARTIST, FK_ARTIST_RELATED) VALUES (1,3);
INSERT INTO rel_artist_related(FK_ARTIST, FK_ARTIST_RELATED) VALUES (4,2);

INSERT INTO peoples(id, name, years,artist_id) VALUES (1, 'YASMIRA OMANA', 32,1);
INSERT INTO peoples(id, name, years,artist_id) VALUES (2, 'PEDRO PEREZ', 22,2);
INSERT INTO peoples(id, name, years,artist_id) VALUES (3, 'CARLOS GUZMAN', 12,2);
INSERT INTO peoples(id, name, years,artist_id) VALUES (4, 'ELSA MURILLO', 62,NULL);
INSERT INTO peoples(id, name, years,artist_id) VALUES (5, 'MARIA PEÑA', 15,NULL);
INSERT INTO peoples(id, name, years,artist_id) VALUES (6, 'ELIANA SOLIS', 32,NULL);
INSERT INTO peoples(id, name, years,artist_id) VALUES (7, 'MICHAEL AGUILAR', 32,NULL);
INSERT INTO peoples(id, name, years,artist_id) VALUES (8, 'ISABEL LA CATOLICA', 20,NULL);

