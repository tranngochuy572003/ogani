package com.example.repository;

import com.example.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository  extends JpaRepository<News,String> {
}
