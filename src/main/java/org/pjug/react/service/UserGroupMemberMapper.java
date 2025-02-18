package org.pjug.react.service;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.pjug.react.domain.UserGroupInfo;
import org.pjug.react.domain.UserGroupMember;
import org.pjug.react.model.UserGroupMemberDTO;
import org.pjug.react.repos.UserGroupInfoRepository;
import org.pjug.react.util.NotFoundException;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserGroupMemberMapper {

    @Mapping(target = "usergroupId", ignore = true)
    UserGroupMemberDTO updateUserGroupMemberDTO(UserGroupMember userGroupMember,
            @MappingTarget UserGroupMemberDTO userGroupMemberDTO);

    @AfterMapping
    default void afterUpdateUserGroupMemberDTO(UserGroupMember userGroupMember,
            @MappingTarget UserGroupMemberDTO userGroupMemberDTO) {
        userGroupMemberDTO.setUsergroupId(userGroupMember.getUsergroupId() == null ? null : userGroupMember.getUsergroupId().getId());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usergroupId", ignore = true)
    UserGroupMember updateUserGroupMember(UserGroupMemberDTO userGroupMemberDTO,
            @MappingTarget UserGroupMember userGroupMember,
            @Context UserGroupInfoRepository userGroupInfoRepository);

    @AfterMapping
    default void afterUpdateUserGroupMember(UserGroupMemberDTO userGroupMemberDTO,
            @MappingTarget UserGroupMember userGroupMember,
            @Context UserGroupInfoRepository userGroupInfoRepository) {
        final UserGroupInfo usergroupId = userGroupMemberDTO.getUsergroupId() == null ? null : userGroupInfoRepository.findById(userGroupMemberDTO.getUsergroupId())
                .orElseThrow(() -> new NotFoundException("usergroupId not found"));
        userGroupMember.setUsergroupId(usergroupId);
    }

}
