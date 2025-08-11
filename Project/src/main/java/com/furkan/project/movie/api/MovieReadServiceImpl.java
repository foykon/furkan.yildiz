package com.furkan.project.movie.api;


import com.furkan.project.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieReadServiceImpl implements MovieReadService {
    private final MovieRepository movieRepository;

    @Override
    public boolean existsById(Long movieId) {
        return movieRepository.existsById(movieId);
    }
}
