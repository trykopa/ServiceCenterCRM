package com.googe.ssadm.sc.service;

import com.googe.ssadm.sc.entity.News;
import com.googe.ssadm.sc.repository.NewsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NewsService {
    private final NewsRepo newsRepo;

    @Autowired
    public NewsService(NewsRepo newsRepo) {
        this.newsRepo = newsRepo;
    }

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(NewsService.class);

    public Page<News> findAllPaginated(Pageable pageable){
        log.info("NewsService: findAllPaginated");
        return newsRepo.findAll(pageable);
    }

    public Optional<News> findById(Long id){
        log.info("NewsService: findById " + id);
        return newsRepo.findById(id);
    }

    public News save(News news){
        log.info("NewsService: save ");
        return newsRepo.save(news);
    }

    public void deleteById(Long id){
        log.info("NewsService: deleteById " + id);
        newsRepo.deleteById(id);
    }

}
