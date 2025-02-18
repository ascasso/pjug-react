package org.pjug.react.service;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.pjug.react.domain.GroupMeeting;
import org.pjug.react.domain.UserGroupInfo;
import org.pjug.react.model.GroupMeetingDTO;
import org.pjug.react.repos.UserGroupInfoRepository;
import org.pjug.react.util.NotFoundException;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface GroupMeetingMapper {

    @Mapping(target = "userGroupId", ignore = true)
    GroupMeetingDTO updateGroupMeetingDTO(GroupMeeting groupMeeting,
            @MappingTarget GroupMeetingDTO groupMeetingDTO);

    @AfterMapping
    default void afterUpdateGroupMeetingDTO(GroupMeeting groupMeeting,
            @MappingTarget GroupMeetingDTO groupMeetingDTO) {
        groupMeetingDTO.setUserGroupId(groupMeeting.getUserGroupId() == null ? null : groupMeeting.getUserGroupId().getId());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userGroupId", ignore = true)
    GroupMeeting updateGroupMeeting(GroupMeetingDTO groupMeetingDTO,
            @MappingTarget GroupMeeting groupMeeting,
            @Context UserGroupInfoRepository userGroupInfoRepository);

    @AfterMapping
    default void afterUpdateGroupMeeting(GroupMeetingDTO groupMeetingDTO,
            @MappingTarget GroupMeeting groupMeeting,
            @Context UserGroupInfoRepository userGroupInfoRepository) {
        final UserGroupInfo userGroupId = groupMeetingDTO.getUserGroupId() == null ? null : userGroupInfoRepository.findById(groupMeetingDTO.getUserGroupId())
                .orElseThrow(() -> new NotFoundException("userGroupId not found"));
        groupMeeting.setUserGroupId(userGroupId);
    }

}
