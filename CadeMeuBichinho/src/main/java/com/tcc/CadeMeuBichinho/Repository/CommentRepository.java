package com.tcc.CadeMeuBichinho.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tcc.CadeMeuBichinho.model.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long>{ 
	public List<Comment> findByNotificationActiveOrderByIdAsc(Boolean notificationActive);
}
