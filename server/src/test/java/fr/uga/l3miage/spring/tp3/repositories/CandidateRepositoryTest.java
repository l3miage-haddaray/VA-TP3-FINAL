package fr.uga.l3miage.spring.tp3.repositories;

import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class CandidateRepositoryTest {

    @Autowired
    private CandidateRepository candidateRepository;


    @Test
    public void testFindAllByTestCenterEntityCode() {
        // Given
        TestCenterEntity centerEntity = TestCenterEntity.builder()
                .code(TestCenterCode.GRE)
                .university(null)
                .city(null)
                .build();

        CandidateEntity candidate1 = CandidateEntity.builder()
                .birthDate(LocalDate.of(1990, 1, 1))
                .hasExtraTime(true)
                // Configurez les autres champs comme nécessaire
                .build();

        CandidateEntity candidate2 = CandidateEntity.builder()
                .birthDate(LocalDate.of(1992, 2, 2))
                .hasExtraTime(false)
                // Configurez les autres champs comme nécessaire
                .build();

        Set<CandidateEntity> candidateEntities = Set.of(candidate1, candidate2);

        candidateRepository.save(candidateEntities);


        // When
        Set<CandidateEntity> reponse = candidateRepository.findAllByTestCenterEntityCode(centerEntity.getCode()) ;


        // Then

        assertThat(reponse).hasSize(1) ;




    }

    @Test
    public void testfindAllByCandidateEvaluationGridEntitiesGradeLessThan() {

        //Given

        //When

        //Then

    }

    @Test
    public void testfindAllByHasExtraTimeFalseAndBirthDateBefore(){
        //Given

        //When

        //Then

    }

}
