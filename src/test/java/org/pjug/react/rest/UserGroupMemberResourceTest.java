package org.pjug.react.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.pjug.react.config.BaseIT;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;


public class UserGroupMemberResourceTest extends BaseIT {

    @Test
    @Sql({"/data/userGroupInfoData.sql", "/data/userGroupMemberData.sql"})
    void getAllUserGroupMembers_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, keycloakSecurityToken(ROLE_USER))
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/userGroupMembers")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(2))
                    .body("_embedded.userGroupMemberDTOList.get(0).id", Matchers.equalTo("a96e0a04-d20f-3096-bc64-dac2d639a577"))
                    .body("_links.self.href", Matchers.endsWith("/api/userGroupMembers?page=0&size=20&sort=id,asc"));
    }

    @Test
    @Sql({"/data/userGroupInfoData.sql", "/data/userGroupMemberData.sql"})
    void getAllUserGroupMembers_filtered() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, keycloakSecurityToken(ROLE_USER))
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/userGroupMembers?filter=b8bff625-bdb0-3939-92c9-d4db0c6bbe45")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(1))
                    .body("_embedded.userGroupMemberDTOList.get(0).id", Matchers.equalTo("b8bff625-bdb0-3939-92c9-d4db0c6bbe45"));
    }

    @Test
    @Sql({"/data/userGroupInfoData.sql", "/data/userGroupMemberData.sql"})
    void getUserGroupMember_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, keycloakSecurityToken(ROLE_USER))
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/userGroupMembers/a96e0a04-d20f-3096-bc64-dac2d639a577")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("firstName", Matchers.equalTo("Nulla facilisis."))
                    .body("_links.self.href", Matchers.endsWith("/api/userGroupMembers/a96e0a04-d20f-3096-bc64-dac2d639a577"));
    }

    @Test
    void getUserGroupMember_notFound() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, keycloakSecurityToken(ROLE_USER))
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/userGroupMembers/23a93ba8-9a5b-3c6c-a26e-49b88973f46e")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("code", Matchers.equalTo("NOT_FOUND"));
    }

    @Test
    @Sql("/data/userGroupInfoData.sql")
    void createUserGroupMember_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, keycloakSecurityToken(ROLE_USER))
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/userGroupMemberDTORequest.json"))
                .when()
                    .post("/api/userGroupMembers")
                .then()
                    .statusCode(HttpStatus.CREATED.value());
        assertEquals(1, userGroupMemberRepository.count());
    }

    @Test
    void createUserGroupMember_missingField() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, keycloakSecurityToken(ROLE_USER))
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/userGroupMemberDTORequest_missingField.json"))
                .when()
                    .post("/api/userGroupMembers")
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("code", Matchers.equalTo("VALIDATION_FAILED"))
                    .body("fieldErrors.get(0).property", Matchers.equalTo("firstName"))
                    .body("fieldErrors.get(0).code", Matchers.equalTo("REQUIRED_NOT_NULL"));
    }

    @Test
    @Sql({"/data/userGroupInfoData.sql", "/data/userGroupMemberData.sql"})
    void updateUserGroupMember_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, keycloakSecurityToken(ROLE_USER))
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/userGroupMemberDTORequest.json"))
                .when()
                    .put("/api/userGroupMembers/a96e0a04-d20f-3096-bc64-dac2d639a577")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("_links.self.href", Matchers.endsWith("/api/userGroupMembers/a96e0a04-d20f-3096-bc64-dac2d639a577"));
        assertEquals("No sea takimata.", userGroupMemberRepository.findById(UUID.fromString("a96e0a04-d20f-3096-bc64-dac2d639a577")).orElseThrow().getFirstName());
        assertEquals(2, userGroupMemberRepository.count());
    }

    @Test
    @Sql({"/data/userGroupInfoData.sql", "/data/userGroupMemberData.sql"})
    void deleteUserGroupMember_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, keycloakSecurityToken(ROLE_USER))
                    .accept(ContentType.JSON)
                .when()
                    .delete("/api/userGroupMembers/a96e0a04-d20f-3096-bc64-dac2d639a577")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        assertEquals(1, userGroupMemberRepository.count());
    }

}
