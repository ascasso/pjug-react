package org.pjug.react.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class GroupMeeting {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column
    private String location;

    @Column
    private String meetingTopic;

    @Column(nullable = false)
    private OffsetDateTime meetingStartTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_group_id_id")
    private UserGroupInfo userGroupId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

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

    public UserGroupInfo getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(final UserGroupInfo userGroupId) {
        this.userGroupId = userGroupId;
    }

    public OffsetDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(final OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(final OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
