package org.pjug.react.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.UUID;
import org.pjug.react.model.SimpleValue;
import org.pjug.react.model.UserGroupInfoDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class UserGroupInfoAssembler implements SimpleRepresentationModelAssembler<UserGroupInfoDTO> {

    @Override
    public void addLinks(final EntityModel<UserGroupInfoDTO> entityModel) {
        entityModel.add(linkTo(methodOn(UserGroupInfoResource.class).getUserGroupInfo(entityModel.getContent().getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(UserGroupInfoResource.class).getAllUserGroupInfos(null, null)).withRel(IanaLinkRelations.COLLECTION));
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<UserGroupInfoDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(UserGroupInfoResource.class).getAllUserGroupInfos(null, null)).withSelfRel());
    }

    public EntityModel<SimpleValue<UUID>> toSimpleModel(final UUID id) {
        final EntityModel<SimpleValue<UUID>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(UserGroupInfoResource.class).getUserGroupInfo(id)).withSelfRel());
        return simpleModel;
    }

}
