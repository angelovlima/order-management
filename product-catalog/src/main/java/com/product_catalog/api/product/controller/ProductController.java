package com.product_catalog.api.product.controller;

import com.product_catalog.api.product.exception.ResourceNotFoundException;
import com.product_catalog.api.product.domain.Product;
import com.product_catalog.api.product.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final FindProductByIdUseCase findProductByIdUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final ValidateProductStockUseCase validateProductStockUseCase;
    private final FindAllProductsUseCase findAllProductsUseCase;

    @PostMapping
    @Operation(
            summary = "Registrar Produto",
            description = "Registra um novo produto no sistema com suas informações.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Product.class),
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
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        Product newProduct = createProductUseCase.execute(product);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar Produto",
            description = "Atualiza as informações de um produto existente no sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Product.class),
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
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product
    ) {
        Product updatedProduct = updateProductUseCase.execute(id, product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletar Produto",
            description = "Remove um produto existente do sistema com base no ID fornecido."
    )
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        deleteProductUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar Produto por ID",
            description = "Retorna as informações de um produto com base no ID fornecido."
    )
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = findProductByIdUseCase.execute(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    @Operation(
            summary = "Listar Todos os Produtos",
            description = "Retorna uma lista com todos os produtos cadastrados no sistema."
    )
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = findAllProductsUseCase.execute();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}/validate")
    public ResponseEntity<Void> validateProduct(
            @PathVariable Long id,
            @RequestParam Integer stockQuantity) {
        try {
            validateProductStockUseCase.execute(id, stockQuantity);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

