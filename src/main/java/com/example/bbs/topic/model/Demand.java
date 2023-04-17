package com.example.bbs.topic.model;

import com.example.bbs.exception.NoEnoughScoreException;
import com.example.bbs.exception.RewardInvalidException;
import com.example.bbs.user.model.User;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class Demand implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(updatable = false)
    @JsonIgnore
    private Long topicId;

    @OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH, CascadeType.MERGE })
    @PrimaryKeyJoinColumn(name = "topic_id", referencedColumnName = "id")
    @JsonIgnore
    private Topic topic;

    @Column(nullable = false)
    //@Type(type = "Text")
    private String content;

    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer reward = 0;

    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH, CascadeType.MERGE })
    @JsonIgnore
    private User winner;

    public Demand() {
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getReward() {
        return reward;
    }

    public void setReward(Integer reward) throws NoEnoughScoreException {
        if (reward == null || reward < 0 || winner != null) {
            throw new RewardInvalidException();
        }

        User createUser = topic.getCreateUser();
        Integer diff = reward - this.reward;

        if (createUser.getScore() < diff) {
            throw new NoEnoughScoreException();
        }

        createUser.setScore(createUser.getScore() - diff);
        this.reward += diff;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        if (this.winner == null && !topic.getCreateUser().equals(winner)) {
            winner.setScore(winner.getScore() + reward);
            this.winner = winner;
        }
    }

    @JsonGetter
    public String getWinnerUsername() {
        if (winner != null) {
            return winner.getUsername();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Demand)) return false;
        Demand demand = (Demand) o;
        return topicId.equals(demand.topicId) &&
                content.equals(demand.content) &&
                reward.equals(demand.reward) &&
                Objects.equals(winner, demand.winner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topicId, content, reward, winner);
    }

    @Override
    public String toString() {
        return "Demand{" +
                "topicId=" + topicId +
                ", content='" + content + '\'' +
                ", reward=" + reward +
                ", winner=" + winner +
                '}';
    }
}
