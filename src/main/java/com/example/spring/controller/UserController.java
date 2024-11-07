package com.example.spring.controller;

import com.example.spring.dto.UserDTO;
import com.example.spring.helper.CSVHelper;
import com.example.spring.model.User;
import com.example.spring.service.UserService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Controller
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = {"", "/"})
    public String showUsers(
            Model model,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10000") Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "ASC") String sort,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy
    ) {
        Sort sortable = sort.equals("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, size, sortable);

        Page<User> userPage = userService.getUsers(username, pageable);
        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.range(1, totalPages + 1)
                    .boxed()
                    .toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("userPage", userPage);
        model.addAttribute("searchKey", username);
        return "users-list";
    }

    @GetMapping("/add-user")
    public String showAddUserForm(
            Model model
    ) {
        model.addAttribute("userForm", new UserDTO());
        return "add-user";
    }

    @PostMapping("/add-user")
    public String addUser(
            @ModelAttribute("userForm") @Valid UserDTO userForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "add-user";
        }
        userService.addUser(userForm);
        return "redirect:/users";
    }

    @GetMapping("/edit-user/{id}")
    public String getUserDetails(
            Model model,
            @PathVariable("id") long id
    ) {

        User userDetails = userService.getUserDetails(id);
        if (userDetails == null) {
            return "user-not-found";
        }
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("result", null);
        return "edit-user";
    }

    @PostMapping("/edit-user/{id}")
    public String editUser(
            Model model,
            @ModelAttribute("userDetails") User updatedUser
    ) {
        model.addAttribute("result", updateUser(updatedUser));
        return "edit-user";
    }

    @PostMapping("/update-user")
    @ResponseBody
    public ResponseEntity<?> editUserForAjaxCall(
            @RequestBody User updatedUser
    ) {
        return ResponseEntity.ok().body(updateUser(updatedUser));
    }

    @PostMapping(path = "/delete-user")
    @ResponseBody
    public ResponseEntity<?> deleteUser(
            @RequestBody int id
    ) {
        boolean result = userService.deleteUser(id);
        return ResponseEntity.ok().body(result);
    }

    private Map<String, Object> updateUser(User updatedUser) {
        boolean isSuccessful = userService.editUser(updatedUser);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("isSuccessful", isSuccessful);
        resultMap.put("message", isSuccessful ? "Update Successfully!" : "Update Failed!");
        return resultMap;
    }

    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        List<String> files = userService.getUploadFilePaths().map(this::getUriByPath).toList();
        model.addAttribute("files", files);
        return "upload-form";
    }

    private String getUriByPath(Path path) {
        String fileName = path.getFileName().toString();
        return MvcUriComponentsBuilder.fromMethodName(
                        UserController.class,
                        "serveFile",
                        fileName)
                .build()
                .toUri()
                .toString();
    }

    /// When a user accesses /files/document.pdf, the filename will be "document.pdf".
    ///
    /// If a user accesses /files/report.final.v1.docx, the filename will be "report.final.v1.docx".
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = userService.loadFileAsResource(filename);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        // HttpHeaders.CONTENT_DISPOSITION: This is the header name used to specify
        // how the browser should handle the content in the response.

        // attachment; filename="...": Sets the value of the Content-Disposition header to "attachment".
        // When the browser sees the content as "attachment",
        // it prompts the user to download the file instead of displaying it directly
        // (commonly used for downloading files such as PDFs, images, etc.).

        // file.getFilename(): Returns the filename and inserts it into filename="..." so the browser uses this name when saving the file.
        return ResponseEntity.ok().header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\""
        ).body(file);
    }

    @PostMapping("/upload")
    public String upload(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes
    ) {
        if (!CSVHelper.hasCSVFormat(file)) {
            redirectAttributes.addFlashAttribute(
                    "error_message",
                    "Only support uploaded .csv files!"
            );
        } else {
            try {
                userService.storeFileAndImportUsers(file);
                redirectAttributes.addFlashAttribute(
                        "message",
                        "You successfully uploaded " + file.getOriginalFilename() + "!"
                );
            } catch (ConstraintViolationException e) {
                redirectAttributes.addFlashAttribute(
                        "error_message",
                        "Some records invalidate or wrong format!"
                );
            }
        }
        return "redirect:/users/upload";
    }
}
