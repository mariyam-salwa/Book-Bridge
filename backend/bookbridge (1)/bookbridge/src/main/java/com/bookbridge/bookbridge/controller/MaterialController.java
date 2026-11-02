package com.bookbridge.bookbridge.controller;

import com.bookbridge.bookbridge.Material;
import com.bookbridge.bookbridge.repository.MaterialRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/materials")
@CrossOrigin(origins = "*")
public class MaterialController {

    private final MaterialRepository materialRepository;

    private final Path uploadDirectory =
            Paths.get("uploads").toAbsolutePath().normalize();

    public MaterialController(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @GetMapping
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    @PostMapping
    public Material addMaterial(@RequestBody Material material) {
        return materialRepository.save(material);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadMaterial(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("materialType") String materialType,
            @RequestParam("uploaderName") String uploaderName) {

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Please select a file"));
            }

            Files.createDirectories(uploadDirectory);

            String originalName = Paths.get(
                file.getOriginalFilename() == null
                    ? "document"
                    : file.getOriginalFilename()
            ).getFileName().toString();

            String extension = originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf("."))
                    .toLowerCase()
                : "";

            if (!List.of(".pdf", ".doc", ".docx", ".ppt",
                    ".pptx", ".txt").contains(extension)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Unsupported file type"));
            }

            String fileName = UUID.randomUUID() + extension;

            Path target = uploadDirectory.resolve(fileName).normalize();

            if (!target.startsWith(uploadDirectory)) {
                return ResponseEntity.badRequest().build();
            }

            Files.copy(file.getInputStream(), target);

            Material material = new Material();
            material.setTitle(title);
            material.setDescription(description);
            material.setCategory(category);
            material.setMaterialType(materialType);
            material.setUploaderName(uploaderName);
            material.setFileUrl("/uploads/" + fileName);

            return ResponseEntity.ok(
                materialRepository.save(material)
            );

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "File upload failed"));
        }
    }

    @GetMapping("/{id}")
    public Material getMaterialById(@PathVariable Long id) {
        return materialRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteMaterial(@PathVariable Long id) {
        materialRepository.deleteById(id);
    }
}