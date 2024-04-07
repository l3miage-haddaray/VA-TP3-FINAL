# Trinôme: Rayane HADDAD, Manyl TIDJANI, Nour BOUGUESSA

(c'est les questions/remarques que je me suis posé tout au long du tp) -> Manyl

## Question 1:
### Tester les fonctions nommées du repository CandidateRepository.

### Mes questions/remarques
- utiliser l'annotation @SpringBootTest() afin de nous permettre de faire de l'injection.
- @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
  - Dans notre cas nous ne voulons pas faire d'appel depuis l'extérieur, mais nous avons besoin de l'injection de dépendance pour le repository, donc nous allons utiliser MOCK
  - Dans notre cas, nous ne voulons pas nous embêter à avoir une base de données postgres sur un docker, surtout pour les tests. Nous allons donc utiliser les bases de données H2, qui est une base de données embarquée directement par un système d'écriture dans un fichier → Dans notre cas, nous allons changer la propriété spring.jpa.database-platform par org.hibernate.dialect.H2Dialect
- De plus, puisque nous utilisons une base données particulière, nous devons ajouter quelques configurations. SpringBoot met à notre disposition une annotation qui nous permet de configurer automatiquement la base, en utilisant l'annotation @AutoConfigureTestDatabase
- Pour nos tests nous allons avoir besoin du repository, il nous faut donc l'injecter. Les tests n'ayant pas de constructeurs, nous allons faire de l'injection par attribut via l'annotation @Autowired

#### Maintenant, les différents tests à faire:
- nous pouvons créer nos tests [insérer test] en utilisant l'annotation @Test au début de chacun
  - Nous allons appliquer le "Given - When - Then"
- Les test se feront sur:
  - Set<CandidateEntity> findAllByTestCenterEntityCode(TestCenterCode code);
  - Set<CandidateEntity> findAllByCandidateEvaluationGridEntitiesGradeLessThan(double grade);
  - Set<CandidateEntity> findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate localDate);

- IMPORTANT ! Faire attention à la nature des relations, plus précisemment si elle est unidirectionnelle ou bidirectionnelle
  - c'est dans l'élément/attribut contenant @ManytoOne que l'on stocke notre "entité" (est-ce bien formulé ?)
    - exemple : .candidateEntity(candidateEntity) (voir 'testFindAllByCandidateEvaluationGridEntitiesGradeLessThan()' CandidateRepositoryTest.java )
  - aussi, toujours VIDER ses tables de la BD avant chaque test avec un repository.deleteAll():
    - ex: 
      @BeforeEach
      @Transactional
      public void cleanBDsetUp() {
        repository.deleteAll();
      }
    - tq: 
      - @BeforeEach est une annotation de JUnit 5 qui indique que la méthode annotée doit être exécutée avant chaque test dans la classe de test. Cela permet d'initialiser l'état nécessaire pour chaque test, par exemple en configurant des objets ou en effectuant des opérations de nettoyage dans la base de données avant chaque test.
      - @Transactional est une annotation de Spring qui indique que la méthode annotée doit être exécutée dans le contexte d'une transaction. Dans le cas des tests, cela signifie que toutes les opérations effectuées dans cette méthode seront regroupées dans une seule transaction, et cette transaction sera annulée à la fin de la méthode (ce qui signifie que toutes les modifications apportées à la base de données seront annulées). Cela garantit que les tests ne modifient pas réellement la base de données, préservant ainsi l'intégrité des données et assurant que chaque test démarre avec un état de base de données propre.

## Question 2: 

### Testez les endpoints complètement (Component, Service, Controller) :

- déjà, il faut implémenter:
  - CandidateServiceTest
  - SessionServiceTest

#### Récupération de la moyenne d'un candidat.
- tester la fonction 'public Double getCandidateAverage(Long candidateId)' dans CandidateService.java
- INUTILE -> Ensuite pour le mapper, nous allons en faire un Spy (le comportement de base n'est pas alteré tant qu'il n'est pas redéfini, et cela nous permet de suivre le nombre d'appels fait sur le spy) avec l'annotation @SpyBean

#### Création d'une session d'examen.
 - tester la fonction 'public SessionResponse createSession(SessionCreationRequest sessionCreationRequest)' dans SessionService.java
   -  la méthode 'createSession' prend les informations fournies dans la requête de création de session, les utilise pour créer une nouvelle session d'examen, associe les examens, la programmation de la session et les étapes de programmation à cette session, puis retourne une réponse indiquant que la session a été créée avec succès.
 - Ici, nous avons besoin de quelles dépendances ? : 
   - 

#### Remarques: 
- Les services ont souvent beaucoup de dépendances. Puisque nous voulons isoler le service testé de ses dépendants, ces dernières doivent toutes devenir des mock ou encore des spy
  - et donc, de quel(s) Component aurais-je besoin dans chaque cas ?
    - CandidateServiceTest:
      - CandidateComponent 
      - ExamComponent (besoin de tester ce component si on l'utilise ?)
      - Simuler le comportement de la méthode du service : J'ai utilisé Mockito pour simuler le comportement de la méthode getCandidateAverage du service candidateService. Cette simulation est faite avec when(candidateService.getCandidateAverage(anyLong())).thenReturn(expectedAverage). Cela signifie que lorsque getCandidateAverage est appelée avec n'importe quel argument (anyLong()), Mockito retournera la valeur expectedAverage, qui est la moyenne attendue.
      - mettre à jour les associations bidirectionnelles entre le candidat, l'examen et la grille d'évaluation: en ajoutant candidateEvaluationGridEntity à l'ensemble des grilles d'évaluation de l'examen et du candidat, je m'assure que ces entités sont correctement liées entre elles.
      - En résumé, l'utilisation de .examEntity(examEntity) lors de la construction de CandidateEvaluationGridEntity établit l'association directe entre l'examen et la grille d'évaluation, tandis que l'utilisation de setCandidateEvaluationGridEntities met à jour les associations bidirectionnelles entre le candidat, l'examen et la grille d'évaluation pour garantir la cohérence des données.

    - SessionServiceTest:
  - @MockBean ou @SpyBean ?
    - utilisation de @SpyBean sur 'sessionMapper' conserve la logique réelle de SessionMapper tout en autorisant des modifications ou des substitutions spécifiques pour les besoins du test. Cela peut être utile lorsque je souhaite tester le comportement réel de la méthode toEntity, par exemple, tout en simulant le comportement d'autres dépendances ou en manipulant les retours de cette méthode pour des scénarios de test particuliers
    - Le rôle de la méthode `toResponse` dans `ExamMapper` est de convertir une entité `ExamEntity` en un objet de transfert de données (DTO) `ExamResponse`. En d'autres termes, cette méthode prend en entrée un objet `ExamEntity` et crée un objet `ExamResponse` correspondant, qui est ensuite renvoyé à l'utilisateur ou utilisé dans d'autres parties de l'application.

Plus précisément, la méthode `toResponse` effectue les tâches suivantes :

1. Prendre un objet `ExamEntity` en entrée.
2. Extraire les données pertinentes de l'objet `ExamEntity`, telles que la date de début, la date de fin, le nom, le poids, etc.
3. Créer un nouvel objet `ExamResponse`.
4. Définir les propriétés de l'objet `ExamResponse` avec les données extraites de l'objet `ExamEntity`.
5. Renvoyer l'objet `ExamResponse` ainsi créé.

En résumé, la méthode `toResponse` facilite la conversion des données d'un objet `ExamEntity` en un format plus adapté à la communication avec d'autres parties de l'application, telles que les couches de présentation ou les services REST. Cela permet de séparer les préoccupations et de rendre le code plus modulaire et plus maintenable.

# Remarques générales
- Mes tests sont relativement pauvres, les plus simples possibles.C'est surtout histoire de m'attarder plus sur les concepts fondamentaux abordées dans ce cours,et je ne sais pas si des test plus "fournis"/"complexes" seraient meilleurs dans ce cas précis
