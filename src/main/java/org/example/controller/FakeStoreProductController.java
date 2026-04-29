package org.example.controller;

import org.example.dto.FakeStoreProductResponseDTO;
import org.example.service.FakeStoreProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class FakeStoreProductController {

    private final FakeStoreProductService fakeStoreProductService;

    public FakeStoreProductController(FakeStoreProductService  fakeStoreProductService){
        this.fakeStoreProductService=fakeStoreProductService;

    }


    @GetMapping
    public List<FakeStoreProductResponseDTO> getAllProducts(){
        return fakeStoreProductService.getAllProducts();
    }

    @GetMapping("/{id}")
    public FakeStoreProductResponseDTO getProductById(@PathVariable Long id){
        return fakeStoreProductService.getProductById(id);
    }


    // get all
    // findbyId
    //post
    //update
    //delete
    //

}
