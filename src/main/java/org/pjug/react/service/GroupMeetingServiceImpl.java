package org.pjug.react.service;

import java.util.UUID;
import org.pjug.react.domain.GroupMeeting;
import org.pjug.react.model.GroupMeetingDTO;
import org.pjug.react.repos.GroupMeetingRepository;
import org.pjug.react.repos.UserGroupInfoRepository;
import org.pjug.react.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class GroupMeetingServiceImpl implements GroupMeetingService {

    private final GroupMeetingRepository groupMeetingRepository;
    private final UserGroupInfoRepository userGroupInfoRepository;
    private final GroupMeetingMapper groupMeetingMapper;

    public GroupMeetingServiceImpl(final GroupMeetingRepository groupMeetingRepository,
            final UserGroupInfoRepository userGroupInfoRepository,
            final GroupMeetingMapper groupMeetingMapper) {
        this.groupMeetingRepository = groupMeetingRepository;
        this.userGroupInfoRepository = userGroupInfoRepository;
        this.groupMeetingMapper = groupMeetingMapper;
    }

    @Override
    public Page<GroupMeetingDTO> findAll(final String filter, final Pageable pageable) {
        Page<GroupMeeting> page;
        if (filter != null) {
            UUID uuidFilter = null;
            try {
                uuidFilter = UUID.fromString(filter);
            } catch (final IllegalArgumentException illegalArgumentException) {
                // keep null - no parseable input
            }
            page = groupMeetingRepository.findAllById(uuidFilter, pageable);
        } else {
            page = groupMeetingRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(groupMeeting -> groupMeetingMapper.updateGroupMeetingDTO(groupMeeting, new GroupMeetingDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public GroupMeetingDTO get(final UUID id) {
        return groupMeetingRepository.findById(id)
                .map(groupMeeting -> groupMeetingMapper.updateGroupMeetingDTO(groupMeeting, new GroupMeetingDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public UUID create(final GroupMeetingDTO groupMeetingDTO) {
        final GroupMeeting groupMeeting = new GroupMeeting();
        groupMeetingMapper.updateGroupMeeting(groupMeetingDTO, groupMeeting, userGroupInfoRepository);
        return groupMeetingRepository.save(groupMeeting).getId();
    }

    @Override
    public void update(final UUID id, final GroupMeetingDTO groupMeetingDTO) {
        final GroupMeeting groupMeeting = groupMeetingRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        groupMeetingMapper.updateGroupMeeting(groupMeetingDTO, groupMeeting, userGroupInfoRepository);
        groupMeetingRepository.save(groupMeeting);
    }

    @Override
    public void delete(final UUID id) {
        groupMeetingRepository.deleteById(id);
    }

}
