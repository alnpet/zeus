package com.alnpet.service;

import org.junit.Test;
import org.unidal.lookup.ComponentTestCase;

import com.alnpet.service.CategoryService;

public class CategoryServiceTest extends ComponentTestCase {
	@Test
	public void testSetup() throws Exception {
		CategoryService service = lookup(CategoryService.class);

		service.setup();
	}
}
