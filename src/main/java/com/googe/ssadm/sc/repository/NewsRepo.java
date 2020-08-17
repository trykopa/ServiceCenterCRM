package com.googe.ssadm.sc.repository;

import com.googe.ssadm.sc.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepo extends JpaRepository<News, Long> {

}
