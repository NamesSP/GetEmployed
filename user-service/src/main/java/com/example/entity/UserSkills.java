package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "User_Skills")
@Data
@IdClass(UserSkillsId.class)
public class UserSkills implements Serializable {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "skill_id")
    private Long skillId;

    // ---- Relationships ----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", insertable = false, updatable = false)
    private Skills skill;
}

// Composite key class
class UserSkillsId implements Serializable {
    private Long userId;
    private Long skillId;

    public UserSkillsId() {
    }

    public UserSkillsId(Long userId, Long skillId) {
        this.userId = userId;
        this.skillId = skillId;
    }

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserSkillsId that = (UserSkillsId) o;
        return userId.equals(that.userId) && skillId.equals(that.skillId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode() + skillId.hashCode();
    }
}
