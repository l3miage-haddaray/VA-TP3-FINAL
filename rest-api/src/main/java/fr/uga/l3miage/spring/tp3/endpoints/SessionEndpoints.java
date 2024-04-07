package fr.uga.l3miage.spring.tp3.endpoints;

import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Gestion des session")
@RestController
@RequestMapping("/api/sessions")
public interface SessionEndpoints {

    @Operation(description = "Créer une session")
    @ApiResponse(responseCode = "201",description = "La session à bien été créer")
    @ApiResponse(responseCode = "400" ,description = "La session n'a pas être créer", content = @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    SessionResponse createSession(@RequestBody SessionCreationRequest request);


    @Operation(description = "Changer l'état d'une session de EVAL_STARTED à EVAL_ENDED")
    @ApiResponse(responseCode = "200",description = "Le changement de status de la session a bien été crée")
    @ApiResponse(responseCode = "409" ,description = "Le changement de status de la session a échoué", content = @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{idSession}/change-state-to-end")
    SessionResponse changeSessionStateToEvalEnded(@PathVariable(name = "idSession") Long sessionId);


}
