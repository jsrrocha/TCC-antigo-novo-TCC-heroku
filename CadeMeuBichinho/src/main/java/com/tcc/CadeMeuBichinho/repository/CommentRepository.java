package com.tcc.CadeMeuBichinho.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tcc.CadeMeuBichinho.model.Comment;
import com.tcc.CadeMeuBichinho.model.User;

public interface CommentRepository extends CrudRepository<Comment, Long>{ 
	public List<Comment> findByNotificationActiveAndUserReceivedOrderByIdAsc(Boolean notificationActive,User user);
}
