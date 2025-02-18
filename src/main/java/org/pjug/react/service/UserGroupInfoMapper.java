package org.pjug.react.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.pjug.react.domain.UserGroupInfo;
import org.pjug.react.model.UserGroupInfoDTO;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserGroupInfoMapper {

    UserGroupInfoDTO updateUserGroupInfoDTO(UserGroupInfo userGroupInfo,
            @MappingTarget UserGroupInfoDTO userGroupInfoDTO);

    @Mapping(target = "id", ignore = true)
    UserGroupInfo updateUserGroupInfo(UserGroupInfoDTO userGroupInfoDTO,
            @MappingTarget UserGroupInfo userGroupInfo);

}
