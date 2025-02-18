package org.pjug.react.service;

import java.util.UUID;
import org.pjug.react.domain.UserGroupMember;
import org.pjug.react.model.UserGroupMemberDTO;
import org.pjug.react.repos.UserGroupInfoRepository;
import org.pjug.react.repos.UserGroupMemberRepository;
import org.pjug.react.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class UserGroupMemberServiceImpl implements UserGroupMemberService {

    private final UserGroupMemberRepository userGroupMemberRepository;
    private final UserGroupInfoRepository userGroupInfoRepository;
    private final UserGroupMemberMapper userGroupMemberMapper;

    public UserGroupMemberServiceImpl(final UserGroupMemberRepository userGroupMemberRepository,
            final UserGroupInfoRepository userGroupInfoRepository,
            final UserGroupMemberMapper userGroupMemberMapper) {
        this.userGroupMemberRepository = userGroupMemberRepository;
        this.userGroupInfoRepository = userGroupInfoRepository;
        this.userGroupMemberMapper = userGroupMemberMapper;
    }

    @Override
    public Page<UserGroupMemberDTO> findAll(final String filter, final Pageable pageable) {
        Page<UserGroupMember> page;
        if (filter != null) {
            UUID uuidFilter = null;
            try {
                uuidFilter = UUID.fromString(filter);
            } catch (final IllegalArgumentException illegalArgumentException) {
                // keep null - no parseable input
            }
            page = userGroupMemberRepository.findAllById(uuidFilter, pageable);
        } else {
            page = userGroupMemberRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(userGroupMember -> userGroupMemberMapper.updateUserGroupMemberDTO(userGroupMember, new UserGroupMemberDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public UserGroupMemberDTO get(final UUID id) {
        return userGroupMemberRepository.findById(id)
                .map(userGroupMember -> userGroupMemberMapper.updateUserGroupMemberDTO(userGroupMember, new UserGroupMemberDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public UUID create(final UserGroupMemberDTO userGroupMemberDTO) {
        final UserGroupMember userGroupMember = new UserGroupMember();
        userGroupMemberMapper.updateUserGroupMember(userGroupMemberDTO, userGroupMember, userGroupInfoRepository);
        return userGroupMemberRepository.save(userGroupMember).getId();
    }

    @Override
    public void update(final UUID id, final UserGroupMemberDTO userGroupMemberDTO) {
        final UserGroupMember userGroupMember = userGroupMemberRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        userGroupMemberMapper.updateUserGroupMember(userGroupMemberDTO, userGroupMember, userGroupInfoRepository);
        userGroupMemberRepository.save(userGroupMember);
    }

    @Override
    public void delete(final UUID id) {
        userGroupMemberRepository.deleteById(id);
    }

    @Override
    public boolean emailExists(final String email) {
        return userGroupMemberRepository.existsByEmailIgnoreCase(email);
    }

}
