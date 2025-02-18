package org.pjug.react.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.UUID;
import org.pjug.react.model.GroupMeetingDTO;
import org.pjug.react.model.SimpleValue;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class GroupMeetingAssembler implements SimpleRepresentationModelAssembler<GroupMeetingDTO> {

    @Override
    public void addLinks(final EntityModel<GroupMeetingDTO> entityModel) {
        entityModel.add(linkTo(methodOn(GroupMeetingResource.class).getGroupMeeting(entityModel.getContent().getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(GroupMeetingResource.class).getAllGroupMeetings(null, null)).withRel(IanaLinkRelations.COLLECTION));
        if (entityModel.getContent().getUserGroupId() != null) {
            entityModel.add(linkTo(methodOn(UserGroupInfoResource.class).getUserGroupInfo(entityModel.getContent().getUserGroupId())).withRel("userGroupId"));
        }
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<GroupMeetingDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(GroupMeetingResource.class).getAllGroupMeetings(null, null)).withSelfRel());
    }

    public EntityModel<SimpleValue<UUID>> toSimpleModel(final UUID id) {
        final EntityModel<SimpleValue<UUID>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(GroupMeetingResource.class).getGroupMeeting(id)).withSelfRel());
        return simpleModel;
    }

}
