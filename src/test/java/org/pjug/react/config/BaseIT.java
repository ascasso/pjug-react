package org.pjug.react.config;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.pjug.react.ReactApplication;
import org.pjug.react.repos.GroupMeetingRepository;
import org.pjug.react.repos.UserGroupInfoRepository;
import org.pjug.react.repos.UserGroupMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.util.StreamUtils;
import org.testcontainers.containers.PostgreSQLContainer;


/**
 * Abstract base class to be extended by every IT test. Starts the Spring Boot context with a
 * Datasource connected to the Testcontainers Docker instance. The instance is reused for all tests,
 * with all data wiped out before each test.
 */
@SpringBootTest(
        classes = ReactApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("it")
@Sql("/data/clearAll.sql")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public abstract class BaseIT {

    @ServiceConnection
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:17.2");
    private static final KeycloakContainer keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:26.0.4");
    public static final String ROLE_USER = "roleUser@invalid.bootify.io";
    public static final String ROLE_ADMIN = "roleAdmin@invalid.bootify.io";
    public static final String PASSWORD = "Bootify!";
    private static final HashMap<String, String> keycloakSecurityTokens = new HashMap<>();

    static {
        postgreSQLContainer.withReuse(true)
                .start();
        keycloakContainer.withRealmImportFile("keycloak-realm.json")
                .withReuse(true)
                .start();
    }

    @LocalServerPort
    public int serverPort;

    @Autowired
    public UserGroupMemberRepository userGroupMemberRepository;

    @Autowired
    public UserGroupInfoRepository userGroupInfoRepository;

    @Autowired
    public GroupMeetingRepository groupMeetingRepository;

    @PostConstruct
    public void initRestAssured() {
        RestAssured.port = serverPort;
        RestAssured.urlEncodingEnabled = false;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @DynamicPropertySource
    public static void setDynamicProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri",
                () -> keycloakContainer.getAuthServerUrl() + "/realms/realmId123/protocol/openid-connect/certs");
    }

    public String readResource(final String resourceName) {
        try {
            return StreamUtils.copyToString(getClass().getResourceAsStream(resourceName), StandardCharsets.UTF_8);
        } catch (final IOException io) {
            throw new UncheckedIOException(io);
        }
    }

    public String keycloakSecurityToken(final String username) {
        String keycloakSecurityToken = keycloakSecurityTokens.get(username);
        if (keycloakSecurityToken == null) {
            // get a fresh token
            final String tokenUrl = keycloakContainer.getAuthServerUrl() + "/realms/realmId123/protocol/openid-connect/token";
            final Map<String, Object> keycloakTokenResponse = RestAssured
                    .given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.URLENC)
                        .formParam("grant_type", "password")
                        .formParam("client_id", "clientId123")
                        .formParam("client_secret", "A76D6010A0E5F76DD1BD85C55B9C4E06")
                        .formParam("username", username)
                        .formParam("password", PASSWORD)
                    .when()
                        .post(tokenUrl)
                    .body().as(new TypeRef<>() {
                    });
            keycloakSecurityToken = "Bearer " + keycloakTokenResponse.get("access_token");
            keycloakSecurityTokens.put(username, keycloakSecurityToken);
        }
        return keycloakSecurityToken;
    }

}
