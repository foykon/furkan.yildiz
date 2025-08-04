package com.example.July4SefLearning.controller;

import com.example.July4SefLearning.dto.UserDTO;
import com.example.July4SefLearning.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/add1")
    public ResponseEntity<String> addRequestParam(@RequestParam int id, @RequestParam String name){
        userService.add(UserDTO.builder()
                .id(id)
                .name(name)
                .build());
        return ResponseEntity.ok("Kullanıcı eklendi.");

    }

    @PostMapping("/add2")
    public ResponseEntity<String> addRequestBody(@RequestBody UserDTO userDTO){
        userService.add(userDTO);
        return ResponseEntity.ok("Kullanıcı eklendi.");

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserDTO> getByIdFromPath(@PathVariable int id){
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("/getById")
    public ResponseEntity<UserDTO> getByIdFromRequest(@RequestParam int id){
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserDTO>> getAllUser(){
        return ResponseEntity.ok(userService.getAll());
    }

}
