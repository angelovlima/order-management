package com.product_catalog.api.controller;

import com.product_catalog.api.exception.ResourceNotFoundException;
import com.product_catalog.api.model.dto.ProductDTO;
import com.product_catalog.api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(
            summary = "Registrar Produto",
            description = "Registra um novo produto no sistema com suas informações.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Exemplo de Registro de Produto",
                                            summary = "Exemplo de dados para registrar um produto",
                                            value = "{ \"name\": \"Notebook Dell Inspiron 15\", \"price\": 3500.50, \"stockQuantity\": 25 }"
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<ProductDTO> saveProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO newProduct = productService.save(productDTO);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar Produto",
            description = "Atualiza as informações de um produto existente no sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Exemplo de Atualização de Produto",
                                            summary = "Exemplo de dados para atualizar um produto",
                                            value = "{ \"name\": \"Monitor LG Ultrawide\", \"price\": 2000.00, \"stockQuantity\": 15 }"
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDTO productDTO
    ) {
        ProductDTO updatedProduct = productService.update(id, productDTO);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletar Produto",
            description = "Remove um produto existente do sistema com base no ID fornecido."
    )
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar Produto por ID",
            description = "Retorna as informações de um produto com base no ID fornecido."
    )
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    @Operation(
            summary = "Listar Todos os Produtos",
            description = "Retorna uma lista com todos os produtos cadastrados no sistema."
    )
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}/validate")
    public ResponseEntity<Void> validateProduct(
            @PathVariable("id") Long id,
            @RequestParam("quantity") Integer quantity) {
        try {
            productService.validateProductStock(id, quantity);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

