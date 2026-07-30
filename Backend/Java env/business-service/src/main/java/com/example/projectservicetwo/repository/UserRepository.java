package com.example.projectservicetwo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.projectservicetwo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}