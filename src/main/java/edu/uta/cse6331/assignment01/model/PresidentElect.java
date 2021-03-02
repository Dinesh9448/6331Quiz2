package edu.uta.cse6331.assignment01.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;

@Entity
@Table(name = "PRESIDENTIALELECT")
@Data
public class PresidentElect {
    @Id
    private BigInteger id;
    private BigInteger year;
    private String state;
    private String statePo;
    private String candidate;
    private String partyDetailed;
    @Column(name = "candidatevotes") private BigInteger candidateVotes;
    @Column(name = "totalvotes") private BigInteger totalVotes;
    private String partySimplified;
}
