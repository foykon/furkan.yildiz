package com.furkan.project.movie.api;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface MovieApiService {
    boolean existsById(Long movieId);
    MovieSummary getSummary(Long movieId);
    Map<Long, MovieSummary> getSummaries(Collection<Long> ids);
    Set<Long> filterIdsByTitleWithin(Collection<Long> candidateIds, String q);
     MovieSummaryForAi getSummaryForAi(Long movieId) ;

}