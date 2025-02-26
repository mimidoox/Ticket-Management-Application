/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ticketManager.controllersTest;

/**
 *
 * @author macbookmimid
 */
import com.example.ticketManager.controllers.CategoryController;
import com.example.ticketManager.entities.Category;
import com.example.ticketManager.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setTitle("Bug");
    }

    @Test
    public void testCreateCategory() {
        when(categoryService.createCategory(any(Category.class))).thenReturn(category);

        Category createdCategory = categoryController.createCategory(category);

        assertNotNull(createdCategory);
        assertEquals("Bug", createdCategory.getTitle());
        verify(categoryService, times(1)).createCategory(category);
    }

    @Test
    public void testGetAllCategories() {
        when(categoryService.getAllCategories()).thenReturn(Arrays.asList(category));

        List<Category> categories = categoryController.getAllCategories();

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals("Bug", categories.get(0).getTitle());
        verify(categoryService, times(1)).getAllCategories();
    }
}
