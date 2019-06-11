DROP TABLE IF EXISTS history_map CASCADE;
DROP TABLE IF EXISTS history_location CASCADE;
DROP TABLE IF EXISTS history_map_location CASCADE;

CREATE TABLE IF NOT EXISTS history_map (
	id serial8 PRIMARY KEY,
	url varchar,
	img varchar NOT NULL,
	copyright boolean DEFAULT false,
	anno int,
	scale float4,
	mapcoord_nw_lon float4,
	mapcoord_nw_lat float4,
	mapcoord_ne_lon float4,
	mapcoord_ne_lat float4,
	mapcoord_sw_lon float4,
	mapcoord_sw_lat float4,
	mapcoord_se_lon float4,
	mapcoord_se_lat float4,
	description varchar DEFAULT ' '
);

CREATE TABLE IF NOT EXISTS history_location (
	id serial8 PRIMARY KEY,
	name varchar NOT NULL,
	lon float4 NOT NULL,
	lat float4 NOT NULL,
	description varchar DEFAULT ' '
);

CREATE TABLE IF NOT EXISTS history_map_location (
	map_id int NOT NULL,
	location_id int NOT NULL,
	CONSTRAINT map_location_id PRIMARY KEY(map_id, location_id),
	FOREIGN KEY (map_id) REFERENCES history_map(id) ON DELETE CASCADE,
	FOREIGN KEY (location_id) REFERENCES history_location(id) ON DELETE RESTRICT
);