package com.example.bbs.topic;


import com.alibaba.fastjson2.JSONObject;
import com.example.bbs.topic.model.Topic;
import com.example.bbs.user.UserService;
import com.example.bbs.user.model.User;
import com.example.bbs.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/topic")
public class TopicController {
    private final TopicService topicService;

    private final UserService userService;


    public TopicController(TopicService topicService, UserService userService) {
        this.topicService = topicService;
        this.userService = userService;
    }

    @PutMapping
    public JSONObject create(@RequestBody JSONObject request)
            throws MissingServletRequestParameterException {
        JSONObject response = new JSONObject();

        String title = Optional.ofNullable(
                request.getString("title")
        ).orElseThrow(() -> new MissingServletRequestParameterException(
                "title", "String"
        ));
        String content = Optional.ofNullable(
                request.getString("content")
        ).orElseThrow(() -> new MissingServletRequestParameterException(
                "content", "String"
        ));

        Topic topic = new Topic();
        User user = userService.loadById(SecurityUtil.getUserId());

        topic.setTitle(title);
        topic.setContent(content);
        topic.setCreateUser(user);
        topic.setCreateTime(new Timestamp(System.currentTimeMillis()));

        response.put("data", topicService.addOrUpdate(topic));
        response.put("status", 1);
        response.put("message", "Create topic success.");

        return  response;
    }

    @PostMapping
    @Transactional
    public JSONObject update(@RequestBody JSONObject request)
            throws MissingServletRequestParameterException {
        JSONObject response = new JSONObject();

        Topic topic = topicService.loadById(
                Optional.ofNullable(request.getLong("id")
                ).orElseThrow(() -> new MissingServletRequestParameterException("id","Long")));

        if (topic.getCreateUser().getId().equals(SecurityUtil.getUserId())) {
            Optional.ofNullable(
                    request.getString("title")
            ).ifPresent(topic::setTitle);

            Optional.ofNullable(
                    request.getString("content")
            ).ifPresent(topic::setContent);

            response.put("data", topicService.addOrUpdate(topic));
            response.put("status", 1);
            response.put("message", "Update topic success.");
        } else {
            response.put("status", 0);
            response.put("message", "You can't update this topic.");
        }

        return response;
    }

    @DeleteMapping
    public JSONObject delete(@RequestParam Long id){
        JSONObject response=new JSONObject();
        try {
            Topic topic = topicService.loadById(id);

            if (topic.getCreateUser().getId().equals(SecurityUtil.getUserId())) {
                topicService.deleteById(id);
                response.put("status", 1);
                response.put("message", "Delete topic success");
            } else {
                response.put("status", 0);
                response.put("message", "You can't delete this topic.");
            }
        } catch (NoSuchElementException e) {

            response.put("status", 0);
            response.put("message", "The topic isn't exist.");
        }
        return response;
    }

    @GetMapping("/{*id}")
    public JSONObject getTopic(@PathVariable("*id") Long id) {
        JSONObject response = new JSONObject();
        try {
            Topic topic = topicService.loadById(id);

            JSONObject data = new JSONObject();

            data.put("id", topic.getId());
            data.put("title", topic.getTitle());
            data.put("content", topic.getContent());
            data.put("createTime", topic.getCreateTime());

            data.putAll(topic.getInfo());

            response.put("status", 1);
            response.put("message", "Get topic success.");
            response.put("data", data);
        } catch (NoSuchElementException e) {
            response.put("status", 0);
            response.put("message", "This topic isn't exist.");
        }

        return response;
    }

    @GetMapping
    public JSONObject getAll() {
        JSONObject response = new JSONObject();
        JSONObject data = new JSONObject();
        List<Topic> topics = topicService.loadAll();
        data.put("total", topics);
        data.put("topics", topics);
        response.put("status", 1);
        response.put("message", "Get topics success.");
        response.put("data", data);

        return response;
    }

    @GetMapping("/boutique")
    public JSONObject getBoutique(@RequestParam(defaultValue = "0") Integer pageNumber) {
        JSONObject response = new JSONObject();
        JSONObject data = new JSONObject();

        List<Topic> topics = topicService.loadAllBoutique();
//        data.put("total", topics.getTotalPages());
//        data.put("topics", topics.getContent());
        data.put("topics", topics);
        response.put("status", 1);
        response.put("message", "Get all boutique topics success.");
        response.put("data", data);

        return response;
    }

//    @GetMapping("/hot")
//    public JSONObject getHot(@RequestParam(defaultValue = "0") Integer pageNumber) {
//        JSONObject response = new JSONObject();
//        JSONObject data = new JSONObject();
//
//        Page<Topic> topics = topicService.loadAllOrderByCountComment(pageNumber);
//        data.put("total", topics.getTotalPages());
//        data.put("topics", topics.getContent());
//
//        response.put("status", 1);
//        response.put("message", "Get all hot topics success.");
//        response.put("data", data);
//
//        return response;
//    }

    @GetMapping("/demand")
    public JSONObject getHaveDemand(@RequestParam(defaultValue = "0") Integer pageNumber) {
        JSONObject response = new JSONObject();
        JSONObject data = new JSONObject();

        // Page<Topic> topics = topicService.loadAllByDemandExists(pageNumber);

//        data.put("total", topics.getTotalPages());
//        data.put("topics", topics.getContent());
        List<Topic> topics = topicService.loadAllByDemandExists();
        data.put("topics", topics);

        response.put("status", 1);
        response.put("message", "Get all have demand topics success.");
        response.put("data", data);

        return response;
    }
}
