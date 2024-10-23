delete
from user
where id between 1 and 4;

select *
from user;

ALTER TABLE user
    ADD birthday datetime NOT NULL;

ALTER TABLE user
    ADD CONSTRAINT uc_user_birthday UNIQUE (birthday);
ALTER TABLE user
    DROP COLUMN birthday;

ALTER TABLE user
    ADD birthday date NOT NULL;

ALTER TABLE user
    ADD CONSTRAINT uc_user_birthday UNIQUE (birthday);