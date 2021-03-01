package edu.uta.cse6331.assignment01.model;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PresidentElectBody {

    Page<PresidentElect> presidentElects;
    List<QueryStatistic> queryStatistics;

}

