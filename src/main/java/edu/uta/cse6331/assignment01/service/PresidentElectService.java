package edu.uta.cse6331.assignment01.service;

import edu.uta.cse6331.assignment01.model.PresidentElect;
import edu.uta.cse6331.assignment01.repository.PresidentElectRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class PresidentElectService {
    @Autowired @Setter private PresidentElectRepository presidentElectRepository;

    public Page<PresidentElect> findAll(Pageable pageable){
        return presidentElectRepository.findAll(pageable);
    }
    public Page<PresidentElect> findByYearEqualsAndStatePoEquals(Pageable pageable, BigInteger year, String statePo){
        return presidentElectRepository.findByYearEqualsAndStatePoEquals(pageable, year, statePo);
    }
    public Page<PresidentElect> findByCandidateVotesBetweenAndYearBetween(Pageable pageable, BigInteger startVotes, BigInteger endVotes, BigInteger startYear, BigInteger endYears){
        return presidentElectRepository.findByCandidateVotesBetweenAndYearBetween(pageable, startVotes, endVotes, startYear, endYears);
    }
    public Page<PresidentElect> findByCandidateContaining(Pageable pageable, String name){
        return presidentElectRepository.findByCandidateContaining(pageable, name);
    }
    @Cacheable("PresidentElect_findAllCacheable")
    public Page<PresidentElect> findAllCacheable(Pageable pageable){
        return presidentElectRepository.findAll(pageable);
    }
    @Cacheable("PresidentElect_findByYearEqualsAndStatePoEquals")
    public Page<PresidentElect> findByYearEqualsAndStatePoEqualsCacheable(Pageable pageable, BigInteger year, String statePo){
        return presidentElectRepository.findByYearEqualsAndStatePoEquals(pageable, year, statePo);
    }
    @Cacheable("PresidentElect_findByCandidateVotesBetweenAndYearBetween")
    public Page<PresidentElect> findByCandidateVotesBetweenAndYearBetweenCacheable(Pageable pageable, BigInteger startVotes, BigInteger endVotes, BigInteger startYear, BigInteger endYears){
        return presidentElectRepository.findByCandidateVotesBetweenAndYearBetween(pageable, startVotes, endVotes, startYear, endYears);
    }
    @Cacheable("PresidentElect_findByCandidateContaining")
    public Page<PresidentElect> findByCandidateContainingCacheable(Pageable pageable, String name){
        return presidentElectRepository.findByCandidateContaining(pageable, name);
    }

}
