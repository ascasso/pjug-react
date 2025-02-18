package org.pjug.react.repos;

import java.util.UUID;
import org.pjug.react.domain.GroupMeeting;
import org.pjug.react.domain.UserGroupInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GroupMeetingRepository extends JpaRepository<GroupMeeting, UUID> {

    Page<GroupMeeting> findAllById(UUID id, Pageable pageable);

    GroupMeeting findFirstByUserGroupId(UserGroupInfo userGroupInfo);

}
