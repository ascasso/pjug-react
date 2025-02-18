package org.pjug.react.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;


public class UserGroupInfoDTO {

    private UUID id;

    @NotNull
    @Size(max = 30)
    @UserGroupInfoGroupIDUnique
    private String groupID;

    @NotNull
    @Size(max = 60)
    private String groupName;

    @JsonProperty("isActive")
    private Boolean isActive;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(final String groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(final String groupName) {
        this.groupName = groupName;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(final Boolean isActive) {
        this.isActive = isActive;
    }

}
