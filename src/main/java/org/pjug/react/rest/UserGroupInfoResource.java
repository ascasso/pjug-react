package org.pjug.react.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.pjug.react.model.UserGroupInfoDTO;
import org.pjug.react.service.UserGroupInfoService;
import org.pjug.react.util.ReferencedException;
import org.pjug.react.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(value = "/api/userGroupInfos", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserGroupInfoResource {

    private final UserGroupInfoService userGroupInfoService;

    public UserGroupInfoResource(final UserGroupInfoService userGroupInfoService) {
        this.userGroupInfoService = userGroupInfoService;
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
    public ResponseEntity<Page<UserGroupInfoDTO>> getAllUserGroupInfos(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(userGroupInfoService.findAll(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGroupInfoDTO> getUserGroupInfo(
            @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(userGroupInfoService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createUserGroupInfo(
            @RequestBody @Valid final UserGroupInfoDTO userGroupInfoDTO) {
        final UUID createdId = userGroupInfoService.create(userGroupInfoDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateUserGroupInfo(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final UserGroupInfoDTO userGroupInfoDTO) {
        userGroupInfoService.update(id, userGroupInfoDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUserGroupInfo(@PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = userGroupInfoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        userGroupInfoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
