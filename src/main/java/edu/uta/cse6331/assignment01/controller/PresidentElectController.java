package edu.uta.cse6331.assignment01.controller;

import edu.uta.cse6331.assignment01.model.PresidentElectBody;
import edu.uta.cse6331.assignment01.model.PresidentElect;
import edu.uta.cse6331.assignment01.model.QueryStatistic;
import edu.uta.cse6331.assignment01.repository.PresidentElectRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.stat.QueryStatistics;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*@BasePathAwareController
@RequestMapping("people")*/
@RepositoryRestController
@RequestMapping("president-elect")
@CrossOrigin
@Slf4j
public class PresidentElectController {

    private static final String COMMA_DELIMITER = ",";
    @Autowired
    @Setter
    private PresidentElectRepository presidentElectRepository;

    @Autowired @Setter private EntityManager entityManager;

    @GetMapping
    @Transactional
    public @ResponseBody
    ResponseEntity<?> findAll(@RequestParam("page") int page, @RequestParam("sort") String sort){
        clearStatistics();
        Pageable pageable = getPageable(page, sort);
        Page<PresidentElect> earthQuakePage = presidentElectRepository.findAll(pageable);
        return getResponseEntity(earthQuakePage);
    }

    @GetMapping("/findByYearEqualsAndStatePoEquals")
    @Transactional
    public @ResponseBody
    ResponseEntity<?> findByYearEqualsAndStatePoEquals(@RequestParam("page") int page, @RequestParam("sort") String sort,
                                        @RequestParam("year") BigInteger year, @RequestParam("statePo") String statePo, @RequestParam(value = "times", defaultValue = "1", required = false) int times){
        clearStatistics();
        Pageable pageable = getPageable(page, sort);
        log.info("Year: " + year);
        Page<PresidentElect> earthQuakePage = null;
        for(int i=1; i <= times; i++) {
            earthQuakePage =
                    presidentElectRepository.findByYearEqualsAndStatePoEquals(pageable, year, statePo);
        };
        return getResponseEntity(earthQuakePage);
    }

    @GetMapping("/findByCandidateLike")
    @Transactional
    public @ResponseBody
    ResponseEntity<?> findByCandidateLike(@RequestParam("page") int page, @RequestParam("sort") String sort,
                                                       @RequestParam("name") String name, @RequestParam(value = "times", defaultValue = "1", required = false) int times){
        clearStatistics();
        Pageable pageable = getPageable(page, sort);
        Page<PresidentElect> earthQuakePage = null;
        for(int i=1; i <= times; i++) {
            earthQuakePage =
                    presidentElectRepository.findByCandidateContaining(pageable, name);
        };
        return getResponseEntity(earthQuakePage);
    }

    @GetMapping("/findByCandidateVotesBetweenAndYearBetween")
    @Transactional
    public @ResponseBody
    ResponseEntity<?> findByCandidateVotesBetweenAndYearBetween(@RequestParam("page") int page, @RequestParam("sort") String sort,
                                                                @RequestParam("startVotes") BigInteger startVotes, @RequestParam("endVotes") BigInteger endVotes, @RequestParam("startYear") BigInteger startYear, @RequestParam("endYears") BigInteger endYears,
                                                                @RequestParam(value = "times", defaultValue = "1", required = false) int times){
        clearStatistics();
        Pageable pageable = getPageable(page, sort);
        Page<PresidentElect> earthQuakePage = null;
        for(int i=1; i <= times; i++) {
            earthQuakePage =
                    presidentElectRepository.findByCandidateVotesBetweenAndYearBetween(pageable, startVotes, endVotes, startYear, endYears);
        };
        return getResponseEntity(earthQuakePage);
    }

    private void clearStatistics() {
        Session session = entityManager.unwrap(Session.class);
        Statistics statistics = session.getSessionFactory().getStatistics();
        statistics.clear();
    }

    public ResponseEntity<?> getResponseEntity(Page<PresidentElect> earthQuakePage) {
        PresidentElectBody earthQuakeBody = new PresidentElectBody();
        earthQuakeBody.setPresidentElects(earthQuakePage);
        earthQuakeBody.setQueryStatistics(getQueryStatistics());
        return ResponseEntity.ok(earthQuakeBody);
    }

    public Pageable getPageable(@RequestParam("page") int page, @RequestParam("sort") String sort) {
        return PageRequest.of(page, 20, Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]);
    }

    private List<QueryStatistic> getQueryStatistics() {
        Session session = entityManager.unwrap(Session.class);
        Statistics statistics = session.getSessionFactory().getStatistics();
        String[] queries = statistics.getQueries();
        return Arrays.stream(queries).map(qu -> {
            QueryStatistics queryStatistics = statistics.getQueryStatistics(qu);
            QueryStatistic queryStatistic = new QueryStatistic();
            queryStatistic.setQuery(qu);
            queryStatistic.setExecutionTime(queryStatistics.getExecutionTotalTime());
            return queryStatistic;
        }).collect(Collectors.toList());
    }



}
