package com.lyj.portfolio.profile;

import com.lyj.portfolio.VO.Movie;
import com.lyj.portfolio.mapper.MovieMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {
    private final MovieMapper movieMapper;

    public List<Movie> getMyComment(String userId) {
        return movieMapper.getMyComment(userId);
    }
}
