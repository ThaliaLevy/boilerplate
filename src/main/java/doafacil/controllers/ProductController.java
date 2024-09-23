package doafacil.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import doafacil.entities.Teste;
import doafacil.services.ProductService;

@RestController
@RequestMapping("v1/product")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping("teste")
	public ResponseEntity<Teste> testandoClasses() {
		Teste valorDoTeste = productService.testandoService();
		return  ResponseEntity.ok(valorDoTeste);
	}
}
