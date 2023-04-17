package com.example.bbs.topic.repo;

import com.example.bbs.topic.model.Demand;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandRepository extends CrudRepository<Demand, Long> {
}
