package com.uni.vendas.item.controller;

import com.uni.vendas.item.dto.DefaultItemDTO;
import com.uni.vendas.item.dto.RegisterItemDTO;
import com.uni.vendas.item.model.Item;
import com.uni.vendas.item.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Object> findById(@PathVariable("id") String id) {

        var item = itemService.findById(id);
        if (item.isPresent()) {
            return ResponseEntity.ok().body(item);
        }
        return ResponseEntity.notFound().build();

    }

    @GetMapping(
            value = "/all",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Page<DefaultItemDTO>> findAll(
            @RequestParam(value = "page", defaultValue = "0")
            Integer page,
            @RequestParam(value = "size", defaultValue = "10")
            Integer size
    ) {
        Page<DefaultItemDTO> itens = itemService.findAll(page, size);
        return ResponseEntity.ok(itens);
    }


    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<Object> createItem(@Valid @ModelAttribute RegisterItemDTO registerItemDTO) {
        Item item = itemService.createItem(registerItemDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(item.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body("Author created successfully with ID: " + item.getId());
    }

    @PutMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<Object> updateItem(@PathVariable("id") String id, @ModelAttribute @Valid RegisterItemDTO registerItemDTO) {
        Optional<RegisterItemDTO> itemOptional = itemService.updateItem(id, registerItemDTO);;
        if (itemOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable("id") String id) {
        Optional<RegisterItemDTO> authorOptional = itemService.findById(id);
        if (authorOptional.isPresent()) {
            itemService.deleteItem(id);
            return ResponseEntity.ok("Item deleted successfully.");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(
            value = "/search",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Page<DefaultItemDTO>> searchItems(
            @RequestParam(value = "name", required = false)
            String name,
            @RequestParam(value = "description", required = false)
            String description,
            @RequestParam(value = "priceLess", required = false)
            Double priceLess,
            @RequestParam(value = "priceGreater", required = false)
            Double priceGreater,
            @RequestParam(value = "page", defaultValue = "0")
            Integer page,
            @RequestParam(value = "size", defaultValue = "10")
            Integer size,
            @RequestParam(value = "category", required = false)
            String category
    ) {
        Page<DefaultItemDTO> pageResult = itemService.searchItem(name, description, priceLess, priceGreater, page, size, category);

        return ResponseEntity.ok(pageResult);
    }


}
