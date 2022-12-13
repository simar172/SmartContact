package com.example.demo.Repositries;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Enitities.Contact;

@Repository
public interface ContactRepo extends MongoRepository<Contact, Integer> {

	public Page<Contact> findByU(int uid, Pageable p);
}
