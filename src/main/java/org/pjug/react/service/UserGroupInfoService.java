package org.pjug.react.service;

import java.util.UUID;
import org.pjug.react.model.UserGroupInfoDTO;
import org.pjug.react.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserGroupInfoService {

    Page<UserGroupInfoDTO> findAll(String filter, Pageable pageable);

    UserGroupInfoDTO get(UUID id);

    UUID create(UserGroupInfoDTO userGroupInfoDTO);

    void update(UUID id, UserGroupInfoDTO userGroupInfoDTO);

    void delete(UUID id);

    boolean groupIDExists(String groupID);

    ReferencedWarning getReferencedWarning(UUID id);

}
