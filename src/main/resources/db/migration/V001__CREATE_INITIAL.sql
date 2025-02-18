CREATE TABLE user_group_info (
    id UUID NOT NULL,
    groupid VARCHAR(30) NOT NULL,
    group_name VARCHAR(60) NOT NULL,
    is_active BOOLEAN,
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT user_group_info_pkey PRIMARY KEY (id)
);

CREATE TABLE user_group_member (
    id UUID NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    preferredjdk VARCHAR(100),
    usergroup_id_id UUID NOT NULL,
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT user_group_member_pkey PRIMARY KEY (id)
);

CREATE TABLE group_meeting (
    id UUID NOT NULL,
    location VARCHAR(255),
    meeting_topic VARCHAR(255),
    meeting_start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_group_id_id UUID,
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT group_meeting_pkey PRIMARY KEY (id)
);

ALTER TABLE user_group_info ADD CONSTRAINT unique_user_group_info_groupid UNIQUE (groupid);

ALTER TABLE user_group_member ADD CONSTRAINT fk_user_group_member_usergroup_id_id FOREIGN KEY (usergroup_id_id) REFERENCES user_group_info (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE user_group_member ADD CONSTRAINT unique_user_group_member_email UNIQUE (email);

ALTER TABLE group_meeting ADD CONSTRAINT fk_group_meeting_user_group_id_id FOREIGN KEY (user_group_id_id) REFERENCES user_group_info (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
