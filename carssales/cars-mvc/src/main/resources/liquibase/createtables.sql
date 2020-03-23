CREATE TABLE IF NOT EXISTS bodytype
(
    id integer NOT NULL,
    name character varying(255),
    CONSTRAINT bodytype_pkey PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS city
(
    id integer NOT NULL,
    name character varying(255),
    CONSTRAINT city_pkey PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS driveunit
(
    id integer NOT NULL,
    name character varying(255),
    CONSTRAINT driveunit_pkey PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS enginestype
(
    id integer NOT NULL,
    name character varying(255),
    CONSTRAINT enginestype_pkey PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS mark
(
    id integer NOT NULL,
    name character varying(255),
     CONSTRAINT mark_pkey PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS transmission
(
    id integer NOT NULL,
    name character varying(255),
    CONSTRAINT transmission_pkey PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS wheel
(
    id integer NOT NULL,
    name character varying(255),
     CONSTRAINT wheel_pkey PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS model
(
    id integer NOT NULL,
    name character varying(255),
    mark_id integer,
    CONSTRAINT model_pkey PRIMARY KEY (id),
    CONSTRAINT fktj5rabfi6sypr43683jbhs78g FOREIGN KEY (mark_id)
        REFERENCES public.mark (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
CREATE TABLE IF NOT EXISTS users
(
    id integer NOT NULL,
    name character varying(255),
    email character varying(255),
    password character varying(255),
    city_id integer,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT fkkpqrx37esphstf2tqxbt89avn FOREIGN KEY (city_id)
        REFERENCES city (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);



