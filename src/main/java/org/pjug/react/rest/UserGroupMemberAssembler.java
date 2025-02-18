package org.pjug.react.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.UUID;
import org.pjug.react.model.SimpleValue;
import org.pjug.react.model.UserGroupMemberDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class UserGroupMemberAssembler implements SimpleRepresentationModelAssembler<UserGroupMemberDTO> {

    @Override
    public void addLinks(final EntityModel<UserGroupMemberDTO> entityModel) {
        entityModel.add(linkTo(methodOn(UserGroupMemberResource.class).getUserGroupMember(entityModel.getContent().getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(UserGroupMemberResource.class).getAllUserGroupMembers(null, null)).withRel(IanaLinkRelations.COLLECTION));
        entityModel.add(linkTo(methodOn(UserGroupInfoResource.class).getUserGroupInfo(entityModel.getContent().getUsergroupId())).withRel("usergroupId"));
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<UserGroupMemberDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(UserGroupMemberResource.class).getAllUserGroupMembers(null, null)).withSelfRel());
    }

    public EntityModel<SimpleValue<UUID>> toSimpleModel(final UUID id) {
        final EntityModel<SimpleValue<UUID>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(UserGroupMemberResource.class).getUserGroupMember(id)).withSelfRel());
        return simpleModel;
    }

}
