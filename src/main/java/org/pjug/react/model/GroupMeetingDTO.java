package org.pjug.react.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.UUID;


public class GroupMeetingDTO {

    private UUID id;

    @Size(max = 255)
    private String location;

    @Size(max = 255)
    private String meetingTopic;

    @NotNull
    private OffsetDateTime meetingStartTime;

    private UUID userGroupId;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public String getMeetingTopic() {
        return meetingTopic;
    }

    public void setMeetingTopic(final String meetingTopic) {
        this.meetingTopic = meetingTopic;
    }

    public OffsetDateTime getMeetingStartTime() {
        return meetingStartTime;
    }

    public void setMeetingStartTime(final OffsetDateTime meetingStartTime) {
        this.meetingStartTime = meetingStartTime;
    }

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(final UUID userGroupId) {
        this.userGroupId = userGroupId;
    }

}
