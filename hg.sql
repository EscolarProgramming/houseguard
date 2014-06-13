CREATE TABLE hg_players (
id int unsigned auto_increment,
name varchar(100) NOT NULL UNIQUE,
PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS hg.hg_regions ( 
	id INT unsigned AUTO_INCREMENT, 
	name VARCHAR(100) NOT NULL, 
	full_name VARCHAR(100) NOT NULL UNIQUE, 
	owner_id INT NOT NULL, 
	tenant_id INT , 
	world VARCHAR(100) NOT NULL, 
	main_region_id INT, 

	initial_position_x INT NOT NULL, 
	final_position_x INT NOT NULL, 
	initial_position_y INT NOT NULL, 
	final_position_y INT NOT NULL, 
	initial_position_z INT NOT NULL, 
	final_position_z INT NOT NULL, 

	blocked BOOLEAN, 
	start_date_tenancy TIMESTAMP, 
	tenancy_period INT, 
	tenancy_tax INT, 
	PRIMARY KEY (id), 
	FOREIGN KEY (main_region_id)  REFERENCES hg_regions(id), 
	FOREIGN KEY (owner_id)  REFERENCES hg_players(id), 
	FOREIGN KEY (tenant_id) REFERENCES hg_players(id) 
) 

CREATE TABLE hg_flags (
id int unsigned auto_increment,
name varchar(100) NOT NULL,
value varchar(100) NOT NULL,
region_id int unsigned NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (region_id) REFERENCES hg_regions(id)
);