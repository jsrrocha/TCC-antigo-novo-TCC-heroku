package com.tcc.CadeMeuBichinho.controller;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tcc.CadeMeuBichinho.Repository.CommentRepository;
import com.tcc.CadeMeuBichinho.Repository.PetRepository;
import com.tcc.CadeMeuBichinho.Repository.UserRepository;
import com.tcc.CadeMeuBichinho.model.Comment;
import com.tcc.CadeMeuBichinho.model.Pet;
import com.tcc.CadeMeuBichinho.model.User;


@RestController
@RequestMapping("comment")
public class CommentController {
	@Autowired
	CommentRepository commentRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PetRepository petRepository;

	@PostMapping("/add")
	public ResponseEntity<?> addComment(@RequestBody Map<String, String>  commentPet){
		try {
			
			if(commentPet.get("idPet") == null
					|| commentPet.get("idSend") == null
					|| commentPet.get("idReceived") == null) {
				
				return new ResponseEntity<String>("Preencha os ids dos usuários e do pet", HttpStatus.BAD_REQUEST); 
			} 
			
			Optional<User> optionalUserReceived = userRepository.
					findById(Long.parseLong(commentPet.get("idReceived"))); 
			if(!optionalUserReceived.isPresent()) {
				return new ResponseEntity<String>("User não existe", HttpStatus.BAD_REQUEST); 
			}
			User userReceived = optionalUserReceived.get();
			
			
			Optional<User> optionalUserSend = userRepository.
					findById(Long.parseLong(commentPet.get("idSend")));			
			if(!optionalUserSend.isPresent()) {
						return new ResponseEntity<String>("User não existe", HttpStatus.BAD_REQUEST); 
			}
			User userSend = optionalUserSend.get();
			

			Optional<Pet> getPet = petRepository.
					findById(Long.parseLong(commentPet.get("idPet"))); 
			if(!getPet.isPresent()) {
				return new ResponseEntity<String>("Pet não existe", HttpStatus.BAD_REQUEST); 
			}
			Pet pet = getPet.get();
			
			Comment comment = new Comment();
			
			Date date = new Date(Long.parseLong(commentPet.get("date")));
			comment.setDate(date);
			comment.setNotificationActive(true);
			comment.setPet(pet);
			comment.setUserReceived(userReceived);
			comment.setUserSend(userSend);
			comment.setComment(commentPet.get("comment"));
			
			commentRepository.save(comment);
			
			return new ResponseEntity<Comment>(comment, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}

	@Transactional
	@PostMapping("/edit")
	public ResponseEntity<?> editComment(@RequestBody Map<String, String>  commentPet){
		try {
			if(commentPet.get("id") ==null) {
				return new ResponseEntity<String>("Preencha o id do comentário", HttpStatus.BAD_REQUEST); 
			}
			
			if(commentPet.get("comment") == null) {
				return new ResponseEntity<String>("Preencha o novo comentário", HttpStatus.BAD_REQUEST); 
			}
			
			Optional<Comment> optionalComment= commentRepository.findById(Long.parseLong(commentPet.get("id")));
			if(!optionalComment.isPresent()) {
				return new ResponseEntity<String>("Comentário não existe", HttpStatus.BAD_REQUEST); 
			}
			
			Comment editComment = optionalComment.get();
			editComment.setComment(commentPet.get("comment"));

			return new ResponseEntity<Comment>(editComment, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}

	@Transactional 
	@GetMapping("/{id}")
	public ResponseEntity<?> getComment(@PathVariable Long id){
		try {
			Optional<Comment> getComment= commentRepository.findById(id);
			if(!getComment.isPresent()) {
				return new ResponseEntity<String>("Comentário não existe", HttpStatus.BAD_REQUEST); 
			}
			return new ResponseEntity<Optional<Comment>>(getComment, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}

	@PostMapping("/notification/desactive/{id}")
	public ResponseEntity<?> getActiveByOrderAsc(@PathVariable Long id){
		try {	
			
			Optional<Comment> comment = commentRepository.findById(id);
			if(!comment.isPresent()) {
				return new ResponseEntity<String>("Comentário não existe", HttpStatus.BAD_REQUEST); 
			}
			Comment desactiveComment = comment.get();
			desactiveComment.setNotificationActive(false);
			
			commentRepository.save(desactiveComment);
			return new ResponseEntity<>("Notificação removida com sucesso", HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}
	
	@Transactional 
	@GetMapping("/notification/active/asc")
	public ResponseEntity<?> getActiveByOrderAsc(){
		try {
			Iterable<Comment> list = commentRepository.findByNotificationActiveOrderByIdAsc(true);
			return new ResponseEntity<>(list, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteComment (@PathVariable Long id){
		try {
			Optional<Comment> comment = commentRepository.findById(id);
			if(!comment.isPresent()) {
				return new ResponseEntity<String>("Comentário não existe", HttpStatus.BAD_REQUEST); 
			}
			Comment removeComment = comment.get();
			commentRepository.delete(removeComment);

			return new ResponseEntity<String>("Comentário removido com sucesso", HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}

}
