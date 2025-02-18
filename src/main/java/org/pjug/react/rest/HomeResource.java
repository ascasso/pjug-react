package org.pjug.react.rest;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeResource {

    @GetMapping("/home")
    public RepresentationModel<?> index() {
        return RepresentationModel.of(null)
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserGroupInfoResource.class).getAllUserGroupInfos(null, null)).withRel("userGroupInfoes"))
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserGroupMemberResource.class).getAllUserGroupMembers(null, null)).withRel("userGroupMembers"))
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GroupMeetingResource.class).getAllGroupMeetings(null, null)).withRel("groupMeetings"));
    }

}
