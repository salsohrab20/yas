package com.yas.product.controller;

import com.yas.product.ProductApplication;
import com.yas.product.service.ProductService;
import com.yas.product.viewmodel.product.ProductPostVm;
import com.yas.product.viewmodel.product.ProductPutVm;
import com.yas.product.viewmodel.product.ProductQuantityPutVm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProductController.class)
@ContextConfiguration(classes = ProductApplication.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testListProductsEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backoffice/products"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testExportProductsEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backoffice/export/products"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testCreateProductEndpoint() throws Exception {
        ProductPostVm productPostVm = new ProductPostVm(
                "Laptop","laptop-1",1L,
                List.of(1L),"short-description","description",
                "specification","laptop-sku", "laptop-gtin",50000D,
                true, true,true, true, true,
                "laptop-meta", "laptop-keywords", "laptop--meta-description",
                1L,null,null,null, null, 1L);
        String jsonBody = objectMapper.writeValueAsString(productPostVm);

        mockMvc.perform(MockMvcRequestBuilders.post("/backoffice/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void testUpdateProductEndpoint() throws Exception {
        ProductPutVm productPutVm = new ProductPutVm(
                "Laptop","laptop-1",50000D,
                true,true, true, true, true,
                1L, List.of(1L), "laptop-short-description",
                "laptop-description",null,null,null, "laptop-meta-title", "laptop-meta-key",
                "laptop--meta-description", 1L, null, null, null,
                null,  null

        );
        String jsonBody = objectMapper.writeValueAsString(productPutVm);

        mockMvc.perform(MockMvcRequestBuilders.put("/backoffice/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testGetFeaturedProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/storefront/products/featured"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetProductsByBrand() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/storefront/brand/{brandSlug}/products", "sampleBrand"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetProductsByCategory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/storefront/category/{categorySlug}/products", "sampleCategory"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetProductById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backoffice/products/{productId}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetFeaturedProductsById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/storefront/products/list-featured")
                        .param("productId", "1", "2", "3"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetProductDetail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/storefront/product/{slug}", "sample-slug"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/backoffice/products/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testGetProductsByMultiQuery() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/storefront/products")
                        .param("pageNo", "0")
                        .param("pageSize", "5")
                        .param("productName", "sampleProduct")
                        .param("categorySlug", "sampleCategory")
                        .param("startPrice", "10.0")
                        .param("endPrice", "100.0"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetProductVariationsByParentId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/storefront/product-variations/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetProductSlug() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/storefront/productions/1/slug"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetProductEsDetailById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/storefront/products-es/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetRelatedProductsBackoffice() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backoffice/products/related-products/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetRelatedProductsStorefront() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/storefront/products/related-products/1")
                        .param("pageNo", "0")
                        .param("pageSize", "5"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetProductsForWarehouse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backoffice/products/for-warehouse")
                        .param("name", "sampleName")
                        .param("sku", "sampleSku"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testUpdateProductQuantity() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/backoffice/products/update-quantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testSubtractProductQuantity() throws Exception {
        List<ProductQuantityPutVm> productQuantityPutVmList = List.of(new ProductQuantityPutVm(1L ,10L));

        String jsonBody = objectMapper.writeValueAsString(productQuantityPutVmList);

        mockMvc.perform(MockMvcRequestBuilders.put("/backoffice/products/subtract-quantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}