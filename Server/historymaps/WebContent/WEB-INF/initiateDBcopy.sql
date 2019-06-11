DROP TABLE IF EXISTS history_map CASCADE;
DROP TABLE IF EXISTS history_location CASCADE;
DROP TABLE IF EXISTS history_map_location CASCADE;

CREATE TABLE IF NOT EXISTS history_map (
	id serial8 PRIMARY KEY,
	url char(4096) NOT NULL,
	anno int,
	scale float4,
	description varchar DEFAULT ' '
);

CREATE TABLE IF NOT EXISTS history_location (
	id serial8 PRIMARY KEY,
	name varchar NOT NULL,
	lon float4 NOT NULL,
	lat float4 NOT NULL,
	description varchar DEFAULT ' '
	
	id serial8 REFERENCES slice(id) ON DELETE CASCADE
	PRIMARY KEY (product_no, order_id)
	
);

CREATE TABLE IF NOT EXISTS history_map_location (
	map_id int NOT NULL,
	location_id int NOT NULL,
	CONSTRAINT map_location_id PRIMARY KEY(map_id, location_id),
	FOREIGN KEY (map_id) REFERENCES history_map(id),
	FOREIGN KEY (location_id) REFERENCES history_location(id)
);