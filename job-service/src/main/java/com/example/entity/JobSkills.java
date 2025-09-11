package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "job_skills")
@Data
@IdClass(JobSkillsId.class)
public class JobSkills implements Serializable {

    @Id
    @Column(name = "job_id")
    private Long jobId;

    @Id
    @Column(name = "skill_id")
    private Long skillId;

}

// Composite key class
class JobSkillsId implements Serializable {
    private Long jobId;
    private Long skillId;

    public JobSkillsId() {
    }

    public JobSkillsId(Long jobId, Long skillId) {
        this.jobId = jobId;
        this.skillId = skillId;
    }

    // Getters and setters
    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
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
        JobSkillsId that = (JobSkillsId) o;
        return jobId.equals(that.jobId) && skillId.equals(that.skillId);
    }

    @Override
    public int hashCode() {
        return jobId.hashCode() + skillId.hashCode();
    }
}
