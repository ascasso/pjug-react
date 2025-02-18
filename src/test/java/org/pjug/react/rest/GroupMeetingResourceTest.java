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


public class GroupMeetingResourceTest extends BaseIT {

    @Test
    @Sql("/data/groupMeetingData.sql")
    void getAllGroupMeetings_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/groupMeetings")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(2))
                    .body("content.get(0).id", Matchers.equalTo("a92d0103-08a6-3379-9a3d-9c728ee74244"));
    }

    @Test
    @Sql("/data/groupMeetingData.sql")
    void getAllGroupMeetings_filtered() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/groupMeetings?filter=b801e5d4-da87-3c39-9782-741cd794002d")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(1))
                    .body("content.get(0).id", Matchers.equalTo("b801e5d4-da87-3c39-9782-741cd794002d"));
    }

    @Test
    @Sql("/data/groupMeetingData.sql")
    void getGroupMeeting_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/groupMeetings/a92d0103-08a6-3379-9a3d-9c728ee74244")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("location", Matchers.equalTo("Eget est lorem."));
    }

    @Test
    void getGroupMeeting_notFound() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/groupMeetings/23de10ad-baa1-32ee-93f7-7f679fa1483a")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("code", Matchers.equalTo("NOT_FOUND"));
    }

    @Test
    void createGroupMeeting_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/groupMeetingDTORequest.json"))
                .when()
                    .post("/api/groupMeetings")
                .then()
                    .statusCode(HttpStatus.CREATED.value());
        assertEquals(1, groupMeetingRepository.count());
    }

    @Test
    void createGroupMeeting_missingField() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/groupMeetingDTORequest_missingField.json"))
                .when()
                    .post("/api/groupMeetings")
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("code", Matchers.equalTo("VALIDATION_FAILED"))
                    .body("fieldErrors.get(0).property", Matchers.equalTo("meetingStartTime"))
                    .body("fieldErrors.get(0).code", Matchers.equalTo("REQUIRED_NOT_NULL"));
    }

    @Test
    @Sql("/data/groupMeetingData.sql")
    void updateGroupMeeting_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/groupMeetingDTORequest.json"))
                .when()
                    .put("/api/groupMeetings/a92d0103-08a6-3379-9a3d-9c728ee74244")
                .then()
                    .statusCode(HttpStatus.OK.value());
        assertEquals("Consectetuer adipiscing.", groupMeetingRepository.findById(UUID.fromString("a92d0103-08a6-3379-9a3d-9c728ee74244")).orElseThrow().getLocation());
        assertEquals(2, groupMeetingRepository.count());
    }

    @Test
    @Sql("/data/groupMeetingData.sql")
    void deleteGroupMeeting_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .delete("/api/groupMeetings/a92d0103-08a6-3379-9a3d-9c728ee74244")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        assertEquals(1, groupMeetingRepository.count());
    }

}
