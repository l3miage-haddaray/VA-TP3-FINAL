package fr.uga.l3miage.spring.tp3.controllers;

import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationStepCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;


import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureTestDatabase
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class SessionControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private EcosSessionRepository ecosSessionRepository;

    @Test
    void testCreateSessionSuccess(){
        //GIVEN
        final HttpHeaders headers = new HttpHeaders();

        SessionProgrammationStepCreationRequest sessionProgrammationStepCreationRequest = SessionProgrammationStepCreationRequest
                .builder()
                .code("test")
                .build();
        SessionProgrammationCreationRequest sessionProgrammationCreationRequest = SessionProgrammationCreationRequest
                .builder()
                .steps(Set.of(sessionProgrammationStepCreationRequest))
                .build();

        final SessionCreationRequest request = SessionCreationRequest
                .builder()
                .name("Test")
                .examsId(Set.of())
                .ecosSessionProgrammation(sessionProgrammationCreationRequest)
                .build();

        ResponseEntity<SessionResponse> response = testRestTemplate.exchange("/api/sessions/create", HttpMethod.POST, new HttpEntity<>(request, headers), SessionResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(ecosSessionRepository.count()).isEqualTo(1);
    }
}