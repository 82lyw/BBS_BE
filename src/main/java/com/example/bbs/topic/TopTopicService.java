package com.example.bbs.topic;

import com.example.bbs.topic.model.TopTopic;
import com.example.bbs.topic.repo.TopTopicRepository;
import com.example.bbs.topic.repo.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TopTopicService {
    private final TopTopicRepository topTopicRepository;

    private final TopicRepository topicRepository;

    public TopTopicService(TopTopicRepository topTopicRepository, TopicRepository topicRepository) {
        this.topTopicRepository = topTopicRepository;
        this.topicRepository = topicRepository;
    }

    public TopTopic add(TopTopic topTopic){
        topicRepository.findById(topTopic.getTopicId())
                .orElseThrow(NoSuchElementException::new);
        return topTopicRepository.save(topTopic);
    }

    public void deleteById(Long topicId){
        topTopicRepository.deleteTopTopicByTopicId(topicId);
    }

    public boolean checkById(Long topicId) {
        return topTopicRepository.existsById(topicId);
    }

    public List<TopTopic> loadAll(){
        return  topTopicRepository.findAll();
    }
}
