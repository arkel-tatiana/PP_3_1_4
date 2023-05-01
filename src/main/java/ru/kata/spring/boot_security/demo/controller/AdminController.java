package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.validator.UserValidator;
import java.security.Principal;




@Controller
@RequestMapping("/admin")
public class AdminController {


    private final UserService userService;
    private final RoleService roleService;
     private final UserValidator userValidator;

    public AdminController(UserService userService, RoleService roleService, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

    @GetMapping(value = "/")
    public String showAllUsers(ModelMap modelUser, Principal principal, Authentication authentication) {
        String str1 = String.join(" ", authentication.getAuthorities().toString()).replaceAll("ROLE_","").replaceAll("\\[","").replaceAll("\\]","").replaceAll("\\,","");
        String title = authentication.getName() + " with roles: " + str1;
        modelUser.addAttribute("authUser", title);
        modelUser.addAttribute("userList", userService.getUsers());
        modelUser.addAttribute("roleList", roleService.getAllRole());
        return "admin";
    }
    @GetMapping(value = "/addNewUser")
    public String addNewUser(ModelMap modelUser, Authentication authentication) {
        String str1 = String.join(" ", authentication.getAuthorities().toString()).replaceAll("ROLE_","").replaceAll("\\[","").replaceAll("\\]","").replaceAll("\\,","");
        String title = authentication.getName() + " with roles: " + str1;
        modelUser.addAttribute("authUser", title);
        User addUser = new User();
        modelUser.addAttribute("userList", addUser);
        modelUser.addAttribute("roleList", roleService.getAllRole());
        return "addUser";
    }
    @PostMapping(value = "/saveUser")
    public String saveUser(@ModelAttribute("userList") @Validated User userSave,
                           BindingResult bindingResult,
                           @ModelAttribute("roleList") Role roleSave, ModelMap modelUser, Authentication authentication) {
        userValidator.validate(userSave, bindingResult);

        if (bindingResult.hasErrors()) {
            String str1 = String.join(" ", authentication.getAuthorities().toString()).replaceAll("ROLE_","").replaceAll("\\[","").replaceAll("\\]","").replaceAll("\\,","");
            String title = authentication.getName() + " with roles: " + str1;
            modelUser.addAttribute("authUser", title);
            modelUser.addAttribute("roleList", roleService.getAllRole());
            modelUser.addAttribute("userList", userSave);
            return "addUser";
        }
          userSave.addRolesUsers(roleSave);
          userService.saveUser(userSave);
          return "redirect:/admin/";
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable("id") Long idDelete) {
        userService.deleteUser(idDelete);
        return "redirect:/admin/";
    }
    @RequestMapping(value = "/edit/{id}")

    public String updateUser(@ModelAttribute("user") User updateUser) {
        userService.updateUser(updateUser);
        return "redirect:/admin/";
    }

}
