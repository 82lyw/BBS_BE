package com.example.bbs.topic;

import com.example.bbs.topic.model.Demand;
import com.example.bbs.topic.model.Topic;
import com.example.bbs.topic.repo.DemandRepository;
import com.example.bbs.topic.repo.TopicRepository;
import com.example.bbs.user.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TopicService {
    private final TopicRepository topicRepository;

    private final DemandRepository demandRepository;


    public TopicService(TopicRepository topicRepository, DemandRepository demandRepository) {
        this.topicRepository = topicRepository;
        this.demandRepository = demandRepository;
    }

    public Topic addOrUpdate(Topic topic) {
        Demand demand = null;
        if(topic.getDemand() != null) {
            demand = topic.getDemand();
            // topic.setDemand(null);
        }
        topic = topicRepository.save(topic);

        if (demand != null) {
            topic.setDemand(demand);
            demand.setTopicId(topic.getId());
            demandRepository.save(demand);
        }

        return topic;
    }

    public void deleteById(Long id) throws EmptyResultDataAccessException {
        topicRepository.deleteById(id);
    }

    public boolean checkById(Long id) {
        return topicRepository.existsById(id);
    }

    public Topic loadById(Long id) throws NoSuchElementException {
        return topicRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<Topic> loadAll() {
        return topicRepository.findAll();
    }

    public List<Topic> loadAllByCreateUser(User user) {
        return topicRepository.findAllByCreateUser(user);
    }

    public List<Topic> loadAllBoutique() {
        return topicRepository.findAllByBoutiqueIsTrue();
    }

    public List<Topic> loadAllByDemandExists() {
        return topicRepository.findAllByDemandExists();
    }
}
