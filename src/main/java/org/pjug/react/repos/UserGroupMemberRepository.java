package org.pjug.react.repos;

import java.util.UUID;
import org.pjug.react.domain.UserGroupInfo;
import org.pjug.react.domain.UserGroupMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserGroupMemberRepository extends JpaRepository<UserGroupMember, UUID> {

    Page<UserGroupMember> findAllById(UUID id, Pageable pageable);

    UserGroupMember findFirstByUsergroupId(UserGroupInfo userGroupInfo);

    boolean existsByEmailIgnoreCase(String email);

}
