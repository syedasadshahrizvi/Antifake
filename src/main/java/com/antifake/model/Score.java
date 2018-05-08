package com.antifake.model;

public class Score {
    private Integer scoreId;

    private String userId;

    private Long score;

    private Byte status;

    public Integer getScoreId() {
        return scoreId;
    }

    public void setScoreId(Integer scoreId) {
        this.scoreId = scoreId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

	@Override
	public String toString() {
		return "Score [scoreId=" + scoreId + ", userId=" + userId + ", score=" + score + ", status=" + status + "]";
	}
    
}