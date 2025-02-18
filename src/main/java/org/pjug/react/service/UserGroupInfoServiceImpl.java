package org.pjug.react.service;

import java.util.UUID;
import org.pjug.react.domain.GroupMeeting;
import org.pjug.react.domain.UserGroupInfo;
import org.pjug.react.domain.UserGroupMember;
import org.pjug.react.model.UserGroupInfoDTO;
import org.pjug.react.repos.GroupMeetingRepository;
import org.pjug.react.repos.UserGroupInfoRepository;
import org.pjug.react.repos.UserGroupMemberRepository;
import org.pjug.react.util.NotFoundException;
import org.pjug.react.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class UserGroupInfoServiceImpl implements UserGroupInfoService {

    private final UserGroupInfoRepository userGroupInfoRepository;
    private final UserGroupInfoMapper userGroupInfoMapper;
    private final UserGroupMemberRepository userGroupMemberRepository;
    private final GroupMeetingRepository groupMeetingRepository;

    public UserGroupInfoServiceImpl(final UserGroupInfoRepository userGroupInfoRepository,
            final UserGroupInfoMapper userGroupInfoMapper,
            final UserGroupMemberRepository userGroupMemberRepository,
            final GroupMeetingRepository groupMeetingRepository) {
        this.userGroupInfoRepository = userGroupInfoRepository;
        this.userGroupInfoMapper = userGroupInfoMapper;
        this.userGroupMemberRepository = userGroupMemberRepository;
        this.groupMeetingRepository = groupMeetingRepository;
    }

    @Override
    public Page<UserGroupInfoDTO> findAll(final String filter, final Pageable pageable) {
        Page<UserGroupInfo> page;
        if (filter != null) {
            UUID uuidFilter = null;
            try {
                uuidFilter = UUID.fromString(filter);
            } catch (final IllegalArgumentException illegalArgumentException) {
                // keep null - no parseable input
            }
            page = userGroupInfoRepository.findAllById(uuidFilter, pageable);
        } else {
            page = userGroupInfoRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(userGroupInfo -> userGroupInfoMapper.updateUserGroupInfoDTO(userGroupInfo, new UserGroupInfoDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public UserGroupInfoDTO get(final UUID id) {
        return userGroupInfoRepository.findById(id)
                .map(userGroupInfo -> userGroupInfoMapper.updateUserGroupInfoDTO(userGroupInfo, new UserGroupInfoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public UUID create(final UserGroupInfoDTO userGroupInfoDTO) {
        final UserGroupInfo userGroupInfo = new UserGroupInfo();
        userGroupInfoMapper.updateUserGroupInfo(userGroupInfoDTO, userGroupInfo);
        return userGroupInfoRepository.save(userGroupInfo).getId();
    }

    @Override
    public void update(final UUID id, final UserGroupInfoDTO userGroupInfoDTO) {
        final UserGroupInfo userGroupInfo = userGroupInfoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        userGroupInfoMapper.updateUserGroupInfo(userGroupInfoDTO, userGroupInfo);
        userGroupInfoRepository.save(userGroupInfo);
    }

    @Override
    public void delete(final UUID id) {
        userGroupInfoRepository.deleteById(id);
    }

    @Override
    public boolean groupIDExists(final String groupID) {
        return userGroupInfoRepository.existsByGroupIDIgnoreCase(groupID);
    }

    @Override
    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final UserGroupInfo userGroupInfo = userGroupInfoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final UserGroupMember usergroupIdUserGroupMember = userGroupMemberRepository.findFirstByUsergroupId(userGroupInfo);
        if (usergroupIdUserGroupMember != null) {
            referencedWarning.setKey("userGroupInfo.userGroupMember.usergroupId.referenced");
            referencedWarning.addParam(usergroupIdUserGroupMember.getId());
            return referencedWarning;
        }
        final GroupMeeting userGroupIdGroupMeeting = groupMeetingRepository.findFirstByUserGroupId(userGroupInfo);
        if (userGroupIdGroupMeeting != null) {
            referencedWarning.setKey("userGroupInfo.groupMeeting.userGroupId.referenced");
            referencedWarning.addParam(userGroupIdGroupMeeting.getId());
            return referencedWarning;
        }
        return null;
    }

}
