package com.toolmanagement.api.repositories;

import com.toolmanagement.api.entities.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {
    List<Tool> findByNameContainingIgnoreCase(String name);
    List<Tool> findByBarcode(String barcode);
    Optional<Tool> findByBarcodeOrName(String barcode, String name);
    List<Tool> findByStatus(String status);
}