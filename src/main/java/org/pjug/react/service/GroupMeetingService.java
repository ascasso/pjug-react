package org.pjug.react.service;

import java.util.UUID;
import org.pjug.react.model.GroupMeetingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface GroupMeetingService {

    Page<GroupMeetingDTO> findAll(String filter, Pageable pageable);

    GroupMeetingDTO get(UUID id);

    UUID create(GroupMeetingDTO groupMeetingDTO);

    void update(UUID id, GroupMeetingDTO groupMeetingDTO);

    void delete(UUID id);

}
