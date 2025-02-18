package org.pjug.react.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.UUID;
import org.pjug.react.domain.UserGroupInfo;
import org.pjug.react.model.GroupMeetingDTO;
import org.pjug.react.model.SimpleValue;
import org.pjug.react.repos.UserGroupInfoRepository;
import org.pjug.react.service.GroupMeetingService;
import org.pjug.react.util.CustomCollectors;
import org.pjug.react.util.UserRoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/groupMeetings", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('" + UserRoles.ROLE_USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class GroupMeetingResource {

    private final GroupMeetingService groupMeetingService;
    private final GroupMeetingAssembler groupMeetingAssembler;
    private final PagedResourcesAssembler<GroupMeetingDTO> pagedResourcesAssembler;
    private final UserGroupInfoRepository userGroupInfoRepository;

    public GroupMeetingResource(final GroupMeetingService groupMeetingService,
            final GroupMeetingAssembler groupMeetingAssembler,
            final PagedResourcesAssembler<GroupMeetingDTO> pagedResourcesAssembler,
            final UserGroupInfoRepository userGroupInfoRepository) {
        this.groupMeetingService = groupMeetingService;
        this.groupMeetingAssembler = groupMeetingAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.userGroupInfoRepository = userGroupInfoRepository;
    }

    @Operation(
            parameters = {
                    @Parameter(
                            name = "page",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "size",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "sort",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = String.class)
                    )
            }
    )
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<GroupMeetingDTO>>> getAllGroupMeetings(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        final Page<GroupMeetingDTO> groupMeetingDTOs = groupMeetingService.findAll(filter, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(groupMeetingDTOs, groupMeetingAssembler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<GroupMeetingDTO>> getGroupMeeting(
            @PathVariable(name = "id") final UUID id) {
        final GroupMeetingDTO groupMeetingDTO = groupMeetingService.get(id);
        return ResponseEntity.ok(groupMeetingAssembler.toModel(groupMeetingDTO));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<EntityModel<SimpleValue<UUID>>> createGroupMeeting(
            @RequestBody @Valid final GroupMeetingDTO groupMeetingDTO) {
        final UUID createdId = groupMeetingService.create(groupMeetingDTO);
        return new ResponseEntity<>(groupMeetingAssembler.toSimpleModel(createdId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SimpleValue<UUID>>> updateGroupMeeting(
            @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final GroupMeetingDTO groupMeetingDTO) {
        groupMeetingService.update(id, groupMeetingDTO);
        return ResponseEntity.ok(groupMeetingAssembler.toSimpleModel(id));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteGroupMeeting(@PathVariable(name = "id") final UUID id) {
        groupMeetingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/userGroupIdValues")
    public ResponseEntity<Map<UUID, String>> getUserGroupIdValues() {
        return ResponseEntity.ok(userGroupInfoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(UserGroupInfo::getId, UserGroupInfo::getGroupID)));
    }

}
