package com.example.bbs.topic;

import com.example.bbs.topic.model.Demand;
import com.example.bbs.topic.repo.DemandRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class DemandService {
    private final DemandRepository demandRepository;

    public DemandService(DemandRepository demandRepository) {
        this.demandRepository = demandRepository;
    }

    public Demand addOrUpdate(Demand demand) {
        return demandRepository.save(demand);
    }

    public void deleteById(Long topicId) {
        demandRepository.deleteById(topicId);
    }

    public Demand loadById(Long topicId) {
        return demandRepository.findById(topicId)
                .orElseThrow(NoSuchElementException::new);
    }

}
