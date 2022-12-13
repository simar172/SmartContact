package com.example.demo.Repositries;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Enitities.User;

@Repository
public interface Urepo extends MongoRepository<User, Integer> {
	public User findByEmail(String email);
}
