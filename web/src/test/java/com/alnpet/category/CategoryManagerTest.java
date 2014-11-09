package com.alnpet.category;

import java.io.IOException;

import org.junit.Test;
import org.unidal.dal.jdbc.DalException;
import org.unidal.lookup.ComponentTestCase;

public class CategoryManagerTest extends ComponentTestCase {
	@Test
	public void testSetup() throws IOException, DalException {
		CategoryManager manager = lookup(CategoryManager.class);

		manager.setup();
	}
}
