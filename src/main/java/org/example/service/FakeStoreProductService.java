package org.example.service;

import org.example.client.FakeStoreProductClient;
import org.example.dto.FakeStoreProductResponseDTO;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FakeStoreProductService {

    private final FakeStoreProductClient fakeStoreProductClient;

    public FakeStoreProductService(FakeStoreProductClient fakeStoreProductClient){
        this.fakeStoreProductClient=fakeStoreProductClient;

    }

    public List<FakeStoreProductResponseDTO> getAllProducts(){
        return fakeStoreProductClient.getAllProducts();
    }

    public FakeStoreProductResponseDTO getProductById(Long id){
        return fakeStoreProductClient.getProductById(id);
    }


}
