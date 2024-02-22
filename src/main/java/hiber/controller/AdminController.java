package hiber.controller;

import hiber.model.Role;
import hiber.model.User;
import hiber.service.RoleService;
import hiber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
//@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.roleService = roleService;
        this.userService = userService;
    }


    @GetMapping ("/users")
    public String getAllUsers(ModelMap model, Principal principal) {
        List<User> users = userService.findAllUsers();
        Long userId = userService.getUserIdByEmail(principal.getName());
        User user = userService.findUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/new")
    public String createUserForm(ModelMap model) {
        User user = new User();
        model.addAttribute("users", user);
        Set<Role> roles = roleService.getRoles();
        model.addAttribute("roles", roles);
        return "userCreate";
    }

    @PostMapping("/userCreate")
    public String addUser(@ModelAttribute("user") User user, @RequestParam(value = "role", required = false) String[] roles) {

        userService.getUserAndRoles(user, roles);
        userService.getNotNullRole(user);
        userService.saveUser(user);

        return "redirect:/users";
    }

    @PostMapping("/removeUser")
    public String removeUser(@RequestParam("id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/users";
    }

    @GetMapping("/updateUser/{id}")
    public String getEditUserForm(Model model, @RequestParam("id") Long id) {
        model.addAttribute("users", userService.findUserById(id));
        Set<Role> roles = roleService.getRoles();
        model.addAttribute("roles", roles);
        return "userUpdate";
    }

    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam(value = "role", required = false) String[] roles) {
        userService.getUserAndRoles(user, roles);
        userService.updateUser(user);
        return "redirect:/users";
    }
}


