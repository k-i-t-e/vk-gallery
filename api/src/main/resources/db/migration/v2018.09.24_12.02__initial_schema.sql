CREATE SCHEMA IF NOT EXISTS vk_gallery;

CREATE TABLE vk_gallery.vk_user (
  id BIGINT NOT NULL PRIMARY KEY,
  first_name VARCHAR(512) NOT NULL,
  last_name VARCHAR(512) NOT NULL
);

CREATE TABLE vk_gallery.favourite_group (
  id BIGINT NOT NULL PRIMARY KEY,
  domain VARCHAR(256) NOT NULL,
  name VARCHAR(1024) NOT NULL,
  group_alias VARCHAR(1024) NOT NULL,
  created_by BIGINT NOT NULL REFERENCES vk_gallery.vk_user(id),
  image_url VARCHAR(2018)
);