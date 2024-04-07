package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.*;
import fr.uga.l3miage.spring.tp3.models.*;
import fr.uga.l3miage.spring.tp3.repositories.*;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateComponentTest {
    @Autowired
    private CandidateComponent candidateComponent;

    @MockBean
    private CandidateRepository candidateRepository;

    @MockBean
    CandidateEvaluationGridRepository candidateEvaluationGridRepository;

    @Test
    void getAllEliminatedCandidateIsEmpty() {
        //Given
        when(candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(anyDouble())).thenReturn(new HashSet<>());

        //when
        Set<CandidateEntity> reponse = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(4.0);

        //then
        assertThat(reponse).isEmpty();
    }
    @Test
    void getAllEliminatedCandidateIsNotEmpty() {

        //Given
        CandidateEntity candidate1= CandidateEntity
                .builder()
                .lastname("candidat1")
                .email("candidat1@gmail.com")
                .build();


        Set<CandidateEntity> Setcandidate1 = Set.of(candidate1);

        //when
        when(candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(anyDouble())).thenReturn(Setcandidate1);
        Set<CandidateEntity> response = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(5.0);

        //then
        assertThat(response).isNotEmpty();
    }
    @Test
    void getCandidateByIdNotFound() {
        //given
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Then - When
        assertThrows(CandidateNotFoundException.class,()->candidateComponent.getCandidatById(anyLong()));
    }


    @Test
    void getCandidateByIdFound() {
        //given
        CandidateEntity candidate1= CandidateEntity
                .builder()
                .lastname("candidat1")
                .email("candidat1@gmail.com")
                .build();

        //when
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.of(candidate1));

        //Then
        assertDoesNotThrow(()->candidateComponent.getCandidatById(anyLong()));
    }

}