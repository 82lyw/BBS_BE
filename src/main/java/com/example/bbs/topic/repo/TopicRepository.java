package com.example.bbs.topic.repo;

import com.example.bbs.topic.model.Topic;
import com.example.bbs.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends CrudRepository<Topic, Long>, JpaSpecificationExecutor<Topic> {
    Optional<Topic> findById(long id);

    List<Topic> findAll();

    List<Topic> findAllByCreateUser(User user);

    List<Topic> findAllByBoutiqueIsTrue();

    @Query("select topic from Topic topic " +
            "inner join Demand demand on demand.topic = topic " +
            "where demand.winner = null " +
            "order by topic.id")
    List<Topic> findAllByDemandExists();
}
