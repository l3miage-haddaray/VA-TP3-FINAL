package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateEvaluationGridRepository;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CandidateComponent {
    private final CandidateEvaluationGridRepository candidateEvaluationGridRepository;
    private final CandidateRepository candidateRepository;

    public Set<CandidateEntity> getAllEliminatedCandidate(){
        return candidateEvaluationGridRepository.findAllByGradeIsLessThanEqual(5)
                .stream()
                .map(CandidateEvaluationGridEntity::getCandidateEntity)
                .collect(Collectors.toSet());
    }


    public CandidateEntity getCandidatById(Long id) throws CandidateNotFoundException {
       return candidateRepository.findById(id).orElseThrow(()-> new CandidateNotFoundException(String.format("Le candidat [%s] n'a pas été trouvé",id),id));
    }

    public void addCandidates(HashSet<CandidateEntity> candidates) {

        LocalDate currentDate = LocalDate.now();

        for (CandidateEntity candidate : candidates) {
            LocalDate birthDate = candidate.getBirthDate();
            if (birthDate.plusYears(18).isAfter(currentDate)) {
                throw new IllegalArgumentException("L'âge d'un candidat est inférieur à 18 ans.");
            }
        }
        candidateRepository.saveAll(candidates);
    }
}
