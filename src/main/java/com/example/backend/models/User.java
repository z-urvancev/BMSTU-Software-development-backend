package com.example.backend.models;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;


@Entity
@Table(name="users")
@Access(AccessType.FIELD)
public class User {

    public User() { }
    public User(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    public long id;

    @Column(name = "login", nullable = false, unique = true)
    public String login;

    @JsonIgnore
    @Column(name = "password")
    public String password;

    @Column(name = "email", nullable = false, unique = true)
    public String email;

    @JsonIgnore
    @Column(name = "salt")
    public String salt;

    @JsonIgnore
    @Column(name = "token")
    public String token;

    @JsonIgnore
    @Column(name = "activity")
    public LocalDateTime activity;

    @ManyToMany(mappedBy = "users")
    public Set<Museum> museums = new HashSet<>();

    public String getLogin(){
        return this.login;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }

    public void addMuseum(Museum m) {
        this.museums.add(m);
        m.users.add(this);
    }

    public void removeMuseum(Museum m) {
        this.museums.remove(m);
        m.users.remove(this);
    }
}
