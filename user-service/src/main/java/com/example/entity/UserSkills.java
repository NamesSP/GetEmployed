package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_skills")
@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@IdClass(UserSkills.UserSkillsId.class)
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
    @JsonIgnore
    private UserEntity user;

    @Data
    public static class UserSkillsId implements Serializable {
        private Long userId;
        private Long skillId;

        public UserSkillsId() {
        }

        public UserSkillsId(Long userId, Long skillId) {
            this.userId = userId;
            this.skillId = skillId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            UserSkillsId that = (UserSkillsId) o;
            return Objects.equals(userId, that.userId) && Objects.equals(skillId, that.skillId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, skillId);
        }
    }
}
