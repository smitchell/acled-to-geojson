INSERT INTO CODE_CATEGORY (CODE_CATEGORY_PK, CATEGORY) VALUES 
  (1, 'EVENT_TYPE'),
  (2, 'ACTOR_TYPE'),
  (3, 'GEO_PRECIS'),
  (4, 'TIME_PRECISION'),
  (5, 'LOCATION'),
  (6, 'COUNTRY'),
  (8, 'PATH');
INSERT INTO CODE_DEFINITION (CODE_DEFINITION_PK, CODE_CATEGORY_PK, CODE, DEFINITION) VALUES
  (1, 2, 1, 'Government/Military/Police'),
  (2, 2, 2, 'Rebel group'),
  (3, 2, 3, 'Political Militia'),
  (4, 2, 4, 'Communal Militia'),
  (5, 2, 5, 'Rioters'),
  (6, 2, 6, 'Protestors'),
  (7, 2, 7, 'Civilians'),
  (8, 2, 8, 'Other (e.g. Regional groups such as AFICOM; or UN'),
  (9, 3, 1, 'Town or immediate surroundings'),
  (10, 3, 2, 'Part of ADM 2 region'),
  (11, 3, 3, 'Regional capital'),
  (12, 4, 1, 'Day'),
  (13, 4, 2, 'Week'),
  (14, 4, 3, 'Monthly');