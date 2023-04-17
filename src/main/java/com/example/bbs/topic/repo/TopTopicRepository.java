package com.example.bbs.topic.repo;

import com.example.bbs.topic.model.TopTopic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopTopicRepository extends CrudRepository<TopTopic, Long> {
    void deleteTopTopicByTopicId(Long topicId);

    List<TopTopic> findAll();

    Optional<TopTopic> findByTopicId(Long id);
}
