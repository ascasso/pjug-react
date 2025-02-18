package org.pjug.react.repos;

import java.util.UUID;
import org.pjug.react.domain.UserGroupInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserGroupInfoRepository extends JpaRepository<UserGroupInfo, UUID> {

    Page<UserGroupInfo> findAllById(UUID id, Pageable pageable);

    boolean existsByGroupIDIgnoreCase(String groupID);

}
