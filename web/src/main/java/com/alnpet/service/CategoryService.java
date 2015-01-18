package com.alnpet.service;

import java.util.List;

import com.alnpet.model.entity.Category;

public interface CategoryService {

	public void setup() throws Exception;

	public List<Category> findActiveCategories() throws Exception;

}