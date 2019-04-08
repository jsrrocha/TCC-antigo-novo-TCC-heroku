package com.tcc.CadeMeuBichinho.Repository;


import org.springframework.data.repository.CrudRepository;
import com.tcc.CadeMeuBichinho.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
	public User findByEmailAndPassword(String email,String password);
	public User findByEmail(String email);
}
