package com.tcc.CadeMeuBichinho.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import com.tcc.CadeMeuBichinho.model.Comment;
import com.tcc.CadeMeuBichinho.model.Pet;
import com.tcc.CadeMeuBichinho.model.User;
import com.tcc.CadeMeuBichinho.repository.CommentRepository;
import com.tcc.CadeMeuBichinho.repository.PetRepository;
import com.tcc.CadeMeuBichinho.repository.UserRepository;

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
			    || commentPet.get("idReceived") == null) {
				return new ResponseEntity<String>("Preencha os ids do usuário e do pet", HttpStatus.BAD_REQUEST); 
			} 
				
			Optional<User> optionalUserReceived = userRepository.
					findById(Long.parseLong(commentPet.get("idReceived"))); 
			if(!optionalUserReceived.isPresent()) {
				return new ResponseEntity<String>("User não existe", HttpStatus.BAD_REQUEST); 
			}
			User userReceived = optionalUserReceived.get();

			Optional<Pet> getPet = petRepository.
					findById(Long.parseLong(commentPet.get("idPet"))); 
			if(!getPet.isPresent()) {
				return new ResponseEntity<String>("Pet não existe", HttpStatus.BAD_REQUEST); 
			}
			Pet pet = getPet.get();
			
			User userSend = null;
			if(!commentPet.get("idSend").equals("")){
				Optional<User> optionalUserSend = userRepository.
						findById(Long.parseLong(commentPet.get("idSend")));			
				if(!optionalUserSend.isPresent()) {
							return new ResponseEntity<String>("User que enviou não existe", HttpStatus.BAD_REQUEST); 
				}
				userSend = optionalUserSend.get();
			}
			
			SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			Date date = simpleFormat.parse(commentPet.get("date"));
			
			Comment comment = new Comment();
			comment.setDate(date);
			comment.setNotificationActive(true);
			comment.setPet(pet);
			comment.setUserReceived(userReceived);  //Usuário logado
			comment.setUserSend(userSend);          //Usuário que comentou no pet..
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
			Comment comment = getComment.get();
			return new ResponseEntity<Comment>(comment, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}

	@PostMapping("/notification/desactive/{id}")
	public ResponseEntity<?> desactiveNotifications(@PathVariable Long id){
		try {	
			
			Optional<Comment> comment = commentRepository.findById(id);
			if(!comment.isPresent()) {
				return new ResponseEntity<String>("Comentário não existe", HttpStatus.BAD_REQUEST); 
			}
			Comment desactiveComment = comment.get();
			desactiveComment.setNotificationActive(false);
			
			commentRepository.save(desactiveComment);
			return new ResponseEntity<Object>("Notificação removida com sucesso", HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}
	
	@Transactional 
	@GetMapping("/notification/user/{userId}/active/asc")
	public ResponseEntity<?> getActiveNotificationByUserAndOrderAsc(@PathVariable Long userId){ 
		try {
			
			Optional<User> optionalUserReceived = userRepository.
					findById(userId);			
			if(!optionalUserReceived.isPresent()) {
						return new ResponseEntity<String>("User logado não existe", HttpStatus.BAD_REQUEST); 
			}
			User userReceived = optionalUserReceived.get();
			
			List<Comment> comments = commentRepository
					.findByNotificationActiveAndUserReceivedOrderByIdAsc(true,userReceived);
			
			List<Map<String, String>> commentsMap = new ArrayList<Map<String,String>>();
			for(Comment comment: comments) {
				Map<String, String>  map = new HashMap<String, String>();
                                map.put("id",comment.getId());
				map.put("nameUser", comment.getUserSend().getName());
				map.put("phone", comment.getUserSend().getPhone().toString());
				map.put("phoneWithWhats", comment.getUserSend().getPhoneWithWhats().toString());
				
				map.put("name", comment.getPet().getName());
				map.put("specie", comment.getPet().getSpecie().toString());
				map.put("sex", comment.getPet().getSex().toString());
				map.put("lostPet", comment.getPet().getLostPet().toString());
				
				map.put("date", comment.getDate().toString());
				map.put("comment", comment.getComment());
				
				commentsMap.add(map);
			}
			
			return new ResponseEntity<>(commentsMap, HttpStatus.OK); 
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

			return new ResponseEntity<Object>("Comentário removido com sucesso", HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}

}
