CREATE TABLE vk_gallery.album (
  id BIGINT NOT NULL PRIMARY KEY,
  name VARCHAR(1024) NOT NULL,
  created_by BIGINT NOT NULL REFERENCES vk_gallery.vk_user(id),
  created_date TIMESTAMP WITH TIME ZONE NOT NULL,
  CONSTRAINT unique_name_created_by UNIQUE (name, created_by)
);

CREATE SEQUENCE vk_gallery.s_album START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE vk_gallery.s_album_image START WITH 1 INCREMENT BY 1;

CREATE TABLE vk_gallery.album_image (
  id BIGINT NOT NULL PRIMARY KEY,
  urls VARCHAR(10000) NOT NULL,
  post_id BIGINT NOT NULL,
  thumbnail VARCHAR(2048) NOT NULL,
  album_id BIGINT REFERENCES vk_gallery.album(id),
  created_by BIGINT NOT NULL REFERENCES vk_gallery.vk_user(id),
  created_date TIMESTAMP WITH TIME ZONE NOT NULL,
  CONSTRAINT unique_thumbnail_album_id UNIQUE (album_id, thumbnail)
);

ALTER TABLE vk_gallery.album ADD COLUMN cover_id BIGINT REFERENCES vk_gallery.album_image(id);