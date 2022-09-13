package edu.school21.restful.controllers;

import edu.school21.restful.models.User;
import edu.school21.restful.models.dto.UserDto;
import edu.school21.restful.services.UsersService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<?> findUsers() {
        CollectionModel<User> users = CollectionModel.of(usersService.findAll());
        users.add(linkTo(methodOn(UsersController.class).findUsers()).withSelfRel());
//        for (User user : users) {
//            user.add(linkTo(UsersController.class).slash(String.valueOf(user.getId())).withSelfRel());
//        }
        return ResponseEntity.ok(users);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public User addNewUser(@RequestBody UserDto userDto) {
        return usersService.addNewUser(userDto);
    }

    @PutMapping("/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody UserDto userDto, @PathVariable("user-id") String id) {
        User user = usersService.findById(Long.valueOf(id));
        usersService.updateUser(user, userDto);
        return user;
    }

    @DeleteMapping("/{user-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("user-id") String id) {
        usersService.deleteById(Long.valueOf(id));
    }
}
