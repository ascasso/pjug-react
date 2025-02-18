package org.pjug.react.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.pjug.react.config.BaseIT;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;


public class UserGroupInfoResourceTest extends BaseIT {

    @Test
    @Sql("/data/userGroupInfoData.sql")
    void getAllUserGroupInfos_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/userGroupInfos")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(2))
                    .body("content.get(0).id", Matchers.equalTo("a9b7ba70-783b-317e-9998-dc4dd82eb3c5"));
    }

    @Test
    @Sql("/data/userGroupInfoData.sql")
    void getAllUserGroupInfos_filtered() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/userGroupInfos?filter=b8c37e33-defd-351c-b91e-1e03e51657da")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(1))
                    .body("content.get(0).id", Matchers.equalTo("b8c37e33-defd-351c-b91e-1e03e51657da"));
    }

    @Test
    @Sql("/data/userGroupInfoData.sql")
    void getUserGroupInfo_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/userGroupInfos/a9b7ba70-783b-317e-9998-dc4dd82eb3c5")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("groupID", Matchers.equalTo("Sed diam nonumy."));
    }

    @Test
    void getUserGroupInfo_notFound() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/userGroupInfos/23d7c8a0-8b4a-3a1b-87c5-99473f5dddda")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("code", Matchers.equalTo("NOT_FOUND"));
    }

    @Test
    void createUserGroupInfo_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/userGroupInfoDTORequest.json"))
                .when()
                    .post("/api/userGroupInfos")
                .then()
                    .statusCode(HttpStatus.CREATED.value());
        assertEquals(1, userGroupInfoRepository.count());
    }

    @Test
    void createUserGroupInfo_missingField() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/userGroupInfoDTORequest_missingField.json"))
                .when()
                    .post("/api/userGroupInfos")
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("code", Matchers.equalTo("VALIDATION_FAILED"))
                    .body("fieldErrors.get(0).property", Matchers.equalTo("groupID"))
                    .body("fieldErrors.get(0).code", Matchers.equalTo("REQUIRED_NOT_NULL"));
    }

    @Test
    @Sql("/data/userGroupInfoData.sql")
    void updateUserGroupInfo_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/userGroupInfoDTORequest.json"))
                .when()
                    .put("/api/userGroupInfos/a9b7ba70-783b-317e-9998-dc4dd82eb3c5")
                .then()
                    .statusCode(HttpStatus.OK.value());
        assertEquals("Lorem ipsum dolor.", userGroupInfoRepository.findById(UUID.fromString("a9b7ba70-783b-317e-9998-dc4dd82eb3c5")).orElseThrow().getGroupID());
        assertEquals(2, userGroupInfoRepository.count());
    }

    @Test
    @Sql("/data/userGroupInfoData.sql")
    void deleteUserGroupInfo_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .delete("/api/userGroupInfos/a9b7ba70-783b-317e-9998-dc4dd82eb3c5")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        assertEquals(1, userGroupInfoRepository.count());
    }

}
