package edu.uta.cse6331.assignment01.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;

@Entity
@Table(name = "PRESIDENTIAL_ELECT")
@Data
public class PresidentElect {
    @Id
    private BigInteger id;
    private BigInteger year;
    private String state;
    private String statePo;
    private String candidate;
    private String partyDetailed;
    private BigInteger candidateVotes;
    private BigInteger totalVotes;
    private String partySimplified;
}
