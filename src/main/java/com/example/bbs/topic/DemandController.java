package com.example.bbs.topic;

import com.alibaba.fastjson2.JSONObject;
import com.example.bbs.exception.NoEnoughScoreException;
import com.example.bbs.exception.RewardInvalidException;
import com.example.bbs.topic.model.Demand;
import com.example.bbs.topic.model.Topic;
import com.example.bbs.user.UserService;
import com.example.bbs.user.model.User;
import com.example.bbs.util.SecurityUtil;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/demand")
public class DemandController {
    private final DemandService demandService;

    private final TopicService topicService;

    private final UserService userService;

    private final static JSONObject permission;

    static {
        permission = new JSONObject();
        permission.put("status", 0);
        permission.put("message", "No permission to update.");
    }

    public DemandController(DemandService demandService, TopicService topicService, UserService userService) {
        this.demandService = demandService;
        this.topicService = topicService;
        this.userService = userService;
    }

    @PutMapping
    @Transactional
    public JSONObject add(@RequestBody JSONObject request)
            throws MissingServletRequestParameterException {
        JSONObject response = new JSONObject();

        Demand demand = new Demand();

        Long topicId = Optional.of(request.getLong("topicId"))
                .orElseThrow(() -> new MissingServletRequestParameterException("topicId", "Long"));
        String content =Optional.of(request.getString("content"))
                .orElseThrow(() -> new MissingServletRequestParameterException("content", "String"));
        Integer reward = request.getInteger("reward");

        try {
            Topic topic = topicService.loadById(topicId);

            if (judge(topic)) return permission;

            if (topic.getDemand() == null) {
                demand.setTopicId(topicId);
                demand.setTopic(topic);
                demand.setContent(content);
                demand.setReward(reward);

                response.put("data", demandService.addOrUpdate(demand));
                response.put("status", 1);
                response.put("message", "Add Success");
            } else {
                response.put("status", 0);
                response.put("message", "This topic have demand, can't add demand.");
            }
        } catch (NoSuchElementException e) {
            response.put("status", 0);
            response.put("message", "The Id is not exist.");
        }

        return response;
    }

    @PostMapping
    @Transactional
    public JSONObject update(@RequestBody JSONObject request)
            throws MissingServletRequestParameterException{
        JSONObject response = new JSONObject();

        Long id = Optional.ofNullable(
                request.getLong("topicId")
        ).orElseThrow(() -> new MissingServletRequestParameterException(
                "topicId", "Long"
        ));

        try {
            Demand demand = demandService.loadById(id);

            if (judge(demand.getTopic())) return permission;

            Optional.ofNullable(
                    request.getString("content")
            ).ifPresent(demand::setContent);

            Optional.ofNullable(
                    request.getInteger("reward")
            ).ifPresent(demand::setReward);

            Optional.ofNullable(
                    request.getLong("winnerId")
            ).ifPresent(winnerId -> demand.setWinner(userService.loadById(winnerId)));

            Demand finalDemand = demandService.addOrUpdate(demand);

            response.put("status", 1);
            response.put("message", "Update demand success.");
            response.put("data", finalDemand);
        } catch (NoEnoughScoreException e) {
            response.put("status", 0);
            response.put("message", "Lack of score");
        } catch (RewardInvalidException e) {
            response.put("status", 0);
            response.put("message", "Reward invalid.");
        } catch (NoSuchElementException e) {
            response.put("status", 0);
            response.put("message", "The Id is not exist.");
        }

        return response;
    }

    @DeleteMapping
    @Transactional
    public JSONObject delete(@RequestParam Long topicId){
        JSONObject response = new JSONObject();

        try {
            Demand demand = demandService.loadById(topicId);

            if (judge(demand.getTopic())) return permission;

            if (demand.getWinner() == null) {
                User user = demand.getTopic().getCreateUser();

                user.setScore(user.getScore() + demand.getReward());

                userService.update(user);
            }
            demandService.deleteById(topicId);

            response.put("status", 1);
            response.put("message", "The demand has been delete");

        } catch (NoSuchElementException e) {
            response.put("status", 0);
            response.put("message", "The Id is not exist.");
        }

        return response;
    }

    private boolean judge(Topic topic) {
        return !topic.getCreateUser().getId().equals(SecurityUtil.getUserId());
    }

}
