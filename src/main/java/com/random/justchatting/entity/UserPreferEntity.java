package com.random.justchatting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="users_prefer")
@Getter
@NoArgsConstructor
public class UserPreferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String preferLevel;
    private String preferRole;
    private String preferTime;

    @Builder
    public UserPreferEntity(Long id, Long userId, String preferLevel, String preferRole, String preferTime) {
        this.id = id;
        this.userId = userId;
        this.preferLevel = preferLevel;
        this.preferRole = preferRole;
        this.preferTime = preferTime;
    }
}
