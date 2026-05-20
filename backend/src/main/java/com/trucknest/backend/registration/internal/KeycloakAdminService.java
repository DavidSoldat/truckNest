package com.trucknest.backend.registration.internal;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class KeycloakAdminService {

    @Value("${keycloak.admin.server-url}")
    private String serverUrl;

    @Value("${keycloak.admin.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    @Value("${keycloak.admin.client-secret}")
    private String clientSecret;

    @Value("${keycloak.admin.user-realm}")
    private String userRealm;

    private Keycloak buildAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType("client_credentials")
                .build();
    }

    public String createUser(String firstName, String lastName,
                             String email, String password, UUID companyId) {
        try (Keycloak keycloak = buildAdminClient()) {
            var usersResource = keycloak.realm(userRealm).users();

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(false);

            UserRepresentation user = new UserRepresentation();
            user.setUsername(email);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEnabled(true);
            user.setCredentials(List.of(credential));
            user.setAttributes(Map.of("company_id", List.of(companyId.toString())));

            Response response = usersResource.create(user);

            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed to create Keycloak user, status: " + response.getStatus());
            }

            String location = response.getHeaderString("Location");
            String keycloakUserId = location.substring(location.lastIndexOf("/") + 1);

            RoleRepresentation ownerRole = keycloak.realm(userRealm)
                    .roles()
                    .get("OWNER")
                    .toRepresentation();

            usersResource.get(keycloakUserId)
                    .roles()
                    .realmLevel()
                    .add(List.of(ownerRole));

            return keycloakUserId;
        }
    }

    public void deleteUser(String keycloakUserId) {
        try (Keycloak keycloak = buildAdminClient()) {
            keycloak.realm(userRealm).users().get(keycloakUserId).remove();
        }
    }
}