package com.nosql.poc.channel.controller;

import com.nosql.poc.channel.dto.PartnerDTO;
import com.nosql.poc.channel.service.PartnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing partners in the channel service.
 * Renamed from BusinessController to better reflect the microservice architecture.
 */
@RestController
@RequestMapping("/partners")
@RequiredArgsConstructor
public class PartnerController {
    private final PartnerService partnerService;

    @PostMapping
    public ResponseEntity<PartnerDTO> createPartner(@Valid @RequestBody PartnerDTO partnerDTO) {
        PartnerDTO createdPartner = partnerService.createPartner(partnerDTO);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdPartner.getId())
            .toUri();
        return ResponseEntity.created(location).body(createdPartner);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartnerDTO> updatePartner(
            @PathVariable String id,
            @Valid @RequestBody PartnerDTO partnerDTO) {
        return ResponseEntity.ok(partnerService.updatePartner(id, partnerDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartnerDTO> getPartner(@PathVariable String id) {
        return ResponseEntity.ok(partnerService.getPartner(id));
    }

    @GetMapping
    public ResponseEntity<List<PartnerDTO>> getAllPartners() {
        return ResponseEntity.ok(partnerService.getAllPartners());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartner(@PathVariable String id) {
        partnerService.deletePartner(id);
        return ResponseEntity.noContent().build();
    }
}