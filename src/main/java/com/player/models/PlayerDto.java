package com.player.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerDto {

    private Long id;
    private Integer age;
    private Gender gender;
    private String login;
    private String password;
    private Role role;
    private String screenName;

    public PlayerDto() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerDto that = (PlayerDto) o;
        return Objects.equals(age, that.age)
                && Objects.equals(gender, that.gender)
                && Objects.equals(login, that.login)
                && Objects.equals(screenName, that.screenName)
                && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, gender, login, screenName, role);
    }

    @Override
    public String toString() {
        return "PlayerDto{" +
                "id=" + id +
                ", age=" + age +
                ", gender=" + gender +
                ", login='" + login + '\'' +
                ", screenName='" + screenName + '\'' +
                ", role=" + role +
                '}';
    }

    public static class Builder {
        private final PlayerDto dto = new PlayerDto();

        public Builder age(Integer age) { dto.setAge(age); return this; }
        public Builder gender(Gender gender) { dto.setGender(gender); return this; }
        public Builder login(String login) { dto.setLogin(login); return this; }
        public Builder password(String password) { dto.setPassword(password); return this; }
        public Builder role(Role role) { dto.setRole(role); return this; }
        public Builder screenName(String screenName) { dto.setScreenName(screenName); return this; }

        public PlayerDto build() {
            return dto;
        }
    }
}