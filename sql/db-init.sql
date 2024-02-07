CREATE TABLE "suppliers"
(
    "id"             varchar PRIMARY KEY,
    "name"           varchar,
    "url"            varchar,
    "mapping_schema" varchar
);

CREATE TABLE "data_warehouse"
(
    "id"          varchar PRIMARY KEY,
    "supplier_id" varchar,
    "data"        varchar
);

CREATE TABLE "hotels"
(
    "id"             varchar PRIMARY KEY,
    "destination_id" varchar,
    "location_id"    varchar,
    "name"           varchar,
    "description"    varchar
);

CREATE TABLE "locations"
(
    "id"        varchar PRIMARY KEY,
    "latitude"  float,
    "longitude" float,
    "country"   varchar,
    "city"      varchar,
    "address"   varchar
);

CREATE TABLE "hotel_amenities"
(
    "amenity_id" varchar,
    "hotel_id"   varchar,
    PRIMARY KEY ("amenity_id", "hotel_id")
);

CREATE TABLE "amenities"
(
    "id"          varchar PRIMARY KEY,
    "description" varchar,
    "category"    varchar
);

CREATE TABLE "images"
(
    "id"          varchar PRIMARY KEY,
    "hotel_id"    varchar,
    "url"         varchar,
    "description" varchar,
    "category"    varchar
);

ALTER TABLE "hotels"
    ADD FOREIGN KEY ("location_id") REFERENCES "locations" ("id");

ALTER TABLE "hotel_amenities"
    ADD FOREIGN KEY ("amenity_id") REFERENCES "amenities" ("id");

ALTER TABLE "hotel_amenities"
    ADD FOREIGN KEY ("hotel_id") REFERENCES "hotels" ("id");

ALTER TABLE "images"
    ADD FOREIGN KEY ("hotel_id") REFERENCES "hotels" ("id");

INSERT INTO "suppliers"(name, url, mapping_schema) VALUES