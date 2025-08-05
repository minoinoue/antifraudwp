package com.tuandat.antifraudwp.controller;

import com.tuandat.antifraudwp.model.MyAppUser;
import com.tuandat.antifraudwp.repository.MyAppUserRepository;
import com.tuandat.antifraudwp.service.MyAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class UserAdminController {

    @Autowired
    private MyAppUserRepository userRepository;

    @Autowired
    private MyAppUserService userService;

    @GetMapping
    public String listUsers(Model model) {
        List<MyAppUser> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/user_list";
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new MyAppUser());
        return "admin/user_form";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute MyAppUser user, RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra username đã tồn tại chưa
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Username đã tồn tại!");
                return "redirect:/admin/users/create";
            }

            // Kiểm tra email đã tồn tại chưa
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Email đã tồn tại!");
                return "redirect:/admin/users/create";
            }

            // Mã hóa mật khẩu
            user.setPassword(userService.encodePassword(user.getPassword()));
            
            // Mặc định là chưa xác thực email
            user.setVerified(false);
            
            // Mặc định role là USER nếu không được set
            if (user.getRole() == null) {
                user.setRole("USER");
            }

            userRepository.save(user);
            redirectAttributes.addFlashAttribute("success", "Tạo người dùng thành công!");
            return "redirect:/admin/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/admin/users/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<MyAppUser> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            return "admin/user_form";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng!");
            return "redirect:/admin/users";
        }
    }

    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, @ModelAttribute MyAppUser user, RedirectAttributes redirectAttributes) {
        try {
            Optional<MyAppUser> existingUserOpt = userRepository.findById(id);
            if (!existingUserOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng!");
                return "redirect:/admin/users";
            }

            MyAppUser existingUser = existingUserOpt.get();
            
            // Kiểm tra username đã tồn tại chưa (trừ user hiện tại)
            Optional<MyAppUser> userWithSameUsername = userRepository.findByUsername(user.getUsername());
            if (userWithSameUsername.isPresent() && !userWithSameUsername.get().getId().equals(id)) {
                redirectAttributes.addFlashAttribute("error", "Username đã tồn tại!");
                return "redirect:/admin/users/edit/" + id;
            }

            // Kiểm tra email đã tồn tại chưa (trừ user hiện tại)
            Optional<MyAppUser> userWithSameEmail = userRepository.findByEmail(user.getEmail());
            if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(id)) {
                redirectAttributes.addFlashAttribute("error", "Email đã tồn tại!");
                return "redirect:/admin/users/edit/" + id;
            }

            // Cập nhật thông tin
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setRole(user.getRole());
            existingUser.setVerified(user.isVerified());

            // Chỉ cập nhật mật khẩu nếu có thay đổi
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(userService.encodePassword(user.getPassword()));
            }

            userRepository.save(existingUser);
            redirectAttributes.addFlashAttribute("success", "Cập nhật người dùng thành công!");
            return "redirect:/admin/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/admin/users/edit/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<MyAppUser> userOpt = userRepository.findById(id);
            if (userOpt.isPresent()) {
                MyAppUser user = userOpt.get();
                
                // Không cho phép xóa admin
                if ("ADMIN".equals(user.getRole())) {
                    redirectAttributes.addFlashAttribute("error", "Không thể xóa tài khoản ADMIN!");
                    return "redirect:/admin/users";
                }
                
                userRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Xóa người dùng thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/toggle-verification/{id}")
    public String toggleVerification(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<MyAppUser> userOpt = userRepository.findById(id);
            if (userOpt.isPresent()) {
                MyAppUser user = userOpt.get();
                user.setVerified(!user.isVerified());
                userRepository.save(user);
                
                String status = user.isVerified() ? "xác thực" : "hủy xác thực";
                redirectAttributes.addFlashAttribute("success", "Đã " + status + " email cho người dùng!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
} 