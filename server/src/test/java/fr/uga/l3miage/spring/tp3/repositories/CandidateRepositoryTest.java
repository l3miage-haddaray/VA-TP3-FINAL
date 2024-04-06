package fr.uga.l3miage.spring.tp3.repositories;

import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class CandidateRepositoryTest {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private TestCenterRepository testCenterRepository ;

    @Autowired
    private CandidateEvaluationGridRepository candidateEvaluationGridRepository ;

    @Test
    public void testFindAllByTestCenterEntityCode() {

        // Given
        TestCenterEntity centerEntity = TestCenterEntity.builder()
                .code(TestCenterCode.GRE)
                .university(null)
                .city(null)
                .build();

        testCenterRepository.save(centerEntity) ;
        CandidateEntity candidate1 = CandidateEntity.builder()
                .testCenterEntity(centerEntity)
                .email("candidate1@example.com")
                .birthDate(LocalDate.of(1990, 1, 1))
                .hasExtraTime(true)
                // Configurez les autres champs comme nécessaire
                .build();

        CandidateEntity candidate2 = CandidateEntity.builder()
                .testCenterEntity(centerEntity)
                .email("candidate2@example.com")
                .birthDate(LocalDate.of(1992, 2, 2))
                .hasExtraTime(false)
                // Configurez les autres champs comme nécessaire
                .build();

        Set<CandidateEntity> candidateEntities = Set.of(candidate1, candidate2);

        candidateRepository.saveAll(candidateEntities);


        // When
        Set<CandidateEntity> reponse = candidateRepository.findAllByTestCenterEntityCode(centerEntity.getCode()) ;


        // Then

        assertThat(reponse).hasSize(2) ;
        assertThat(reponse.stream().findFirst().get().getTestCenterEntity().getCode()).isEqualTo(TestCenterCode.GRE) ;

    }

    @Test
    public void testfindAllByCandidateEvaluationGridEntitiesGradeLessThan() {
        double grade = 75.0;
        //Given
        CandidateEvaluationGridEntity cEGE = CandidateEvaluationGridEntity.builder()
                .grade(71.5)
                .submissionDate(null)
                .build();

        CandidateEvaluationGridEntity cEGE1 = CandidateEvaluationGridEntity.builder()
                .grade(77.5)
                .submissionDate(null)
                .build();

        candidateEvaluationGridRepository.save(cEGE);
        candidateEvaluationGridRepository.save(cEGE1);

        CandidateEntity candidate3 = CandidateEntity.builder()
                .candidateEvaluationGridEntities(Set.of(cEGE))
                .email("candidate3@example.com")
                .birthDate(LocalDate.of(1993, 3, 3))
                .hasExtraTime(true)
                // Configurez les autres champs comme nécessaire
                .build();

        CandidateEntity candidate4 = CandidateEntity.builder()
                .candidateEvaluationGridEntities(Set.of(cEGE1))
                .email("candidate4@example.com")
                .birthDate(LocalDate.of(1994, 4, 4))
                .hasExtraTime(false)
                // Configurez les autres champs comme nécessaire
                .build();


        Set<CandidateEntity> candidateEntities = Set.of(candidate3, candidate4);
        candidateRepository.saveAll(candidateEntities);

        cEGE.setCandidateEntity(candidate3);
        cEGE1.setCandidateEntity(candidate4);

        candidateEvaluationGridRepository.save(cEGE);
        candidateEvaluationGridRepository.save(cEGE1);

        //When
        Set<CandidateEntity> reponse = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(grade);
        //Then
        assertThat(reponse).hasSize(1);
        assertThat(reponse.stream().findFirst().get().getBirthDate()).isEqualTo(LocalDate.of(1993, 3, 3));

    }

    @Test
    public void testfindAllByHasExtraTimeFalseAndBirthDateBefore(){
        //Given
        LocalDate date = LocalDate.of(1990, 1, 1);

        CandidateEntity candidate1 = CandidateEntity.builder()
                .email("candidate1@example.com")
                .birthDate(LocalDate.of(1985, 12, 12)) // Before dateThreshold
                .hasExtraTime(false)
                // Configure other fields as necessary
                .build();

        CandidateEntity candidate2 = CandidateEntity.builder()
                .email("candidate2@example.com")
                .birthDate(LocalDate.of(1992, 2, 2)) // After dateThreshold
                .hasExtraTime(true) // Has extra time
                .build();

        CandidateEntity candidate3 = CandidateEntity.builder()
                .email("candidate3@example.com")
                .birthDate(LocalDate.of(1983, 3, 3)) // After dateThreshold
                .hasExtraTime(false)
                .build();

        candidateRepository.saveAll(List.of(candidate1, candidate2, candidate3));



        //When
        Set<CandidateEntity> result = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(date);

        //Then
        assertThat(result).hasSize(2);
        assertThat(result.iterator().next().getBirthDate()).isEqualTo(LocalDate.of(1985, 12, 12));

    }

}
