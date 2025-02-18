package org.pjug.react.service;

import java.util.UUID;
import org.pjug.react.model.UserGroupMemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserGroupMemberService {

    Page<UserGroupMemberDTO> findAll(String filter, Pageable pageable);

    UserGroupMemberDTO get(UUID id);

    UUID create(UserGroupMemberDTO userGroupMemberDTO);

    void update(UUID id, UserGroupMemberDTO userGroupMemberDTO);

    void delete(UUID id);

    boolean emailExists(String email);

}
