package org.pjug.react.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;


public class UserGroupMemberDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    private String firstName;

    @NotNull
    @Size(max = 255)
    private String lastName;

    @NotNull
    @Size(max = 255)
    @UserGroupMemberEmailUnique
    private String email;

    private JDKVersion preferredJDK;

    @NotNull
    private UUID usergroupId;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public JDKVersion getPreferredJDK() {
        return preferredJDK;
    }

    public void setPreferredJDK(final JDKVersion preferredJDK) {
        this.preferredJDK = preferredJDK;
    }

    public UUID getUsergroupId() {
        return usergroupId;
    }

    public void setUsergroupId(final UUID usergroupId) {
        this.usergroupId = usergroupId;
    }

}
