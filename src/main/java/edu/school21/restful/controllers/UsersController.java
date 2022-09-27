package edu.school21.restful.controllers;

import edu.school21.restful.models.User;
import edu.school21.restful.models.dto.UserDto;
import edu.school21.restful.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name="My Users controller", description="My Users controller description")
@AllArgsConstructor
public class UsersController {
    private final UsersService usersService;
    private final PagedResourcesAssembler<User> pagedResourcesAssembler;

    @GetMapping(produces = { "application/hal+json" })
    @Operation(description = "Возвращает всех имеющихся пользователей [есть пагинация, сортировка по ID]")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<EntityModel<User>> getAllUsers(@PageableDefault(sort = "id", size = 10) Pageable pageable) {
        return pagedResourcesAssembler.toModel(usersService.findAll(pageable));
    }

    @PostMapping()
    @Operation(description = "Добавление нового пользователя")
    @ResponseStatus(HttpStatus.CREATED)
    public User addNewUser(@RequestBody UserDto userDto) {
        return usersService.addNewUser(userDto);
    }

    @GetMapping("/{user-id}")
    @Operation(description = "Возвращает пользователя по id")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable("user-id") Long id) {
        return usersService.findById(id);
    }

    @PutMapping("/{user-id}")
    @Operation(description = "Изменение пользователя")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody UserDto userDto, @PathVariable("user-id") Long id) {
        User user = usersService.findById(id);
        usersService.updateUser(user, userDto);
        return user;
    }

    @DeleteMapping("/{user-id}")
    @Operation(description = "Удаление пользователя")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("user-id") String id) {
        usersService.deleteById(Long.valueOf(id));
    }
}
