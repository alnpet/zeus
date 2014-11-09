package com.alnpet.category;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.unidal.dal.jdbc.DalException;
import org.unidal.helper.Files;
import org.unidal.lookup.annotation.Inject;

import com.alnpet.dal.core.CategoryDao;
import com.alnpet.dal.core.CategoryDo;
import com.site.helper.Splitters;

public class CategoryManager {
	@Inject
	private CategoryDao m_dao;

	private List<CategoryDo> parseCategories() throws IOException {
		InputStream in = getClass().getResourceAsStream("category.csv");
		String csv = Files.forIO().readFrom(in, "utf-8");
		List<String> lines = Splitters.by('\n').trim().noEmptyItem().split(csv);
		List<CategoryDo> categories = new ArrayList<CategoryDo>();

		for (String line : lines) {
			List<String> fields = Splitters.by(',').trim().split(line);
			String first = fields.get(0);

			if (first.length() == 0) {
				continue;
			} else if ("No.".equals(first)) {
				continue;
			}

			if (first.length() > 0) {
				CategoryDo c = new CategoryDo();
				int index = 0;

				c.setId(Integer.parseInt(fields.get(index++)));
				c.setName(fields.get(index++));
				c.setType(PetType.getByName(fields.get(index++), PetType.UNKNOWN).getId());

				c.setMaleWeightMin(Double.parseDouble(fields.get(index++)));
				c.setMaleWeightMax(Double.parseDouble(fields.get(index++)));
				c.setMaleWeightAvg(Double.parseDouble(fields.get(index++)));
				c.setFemaleWeightMin(Double.parseDouble(fields.get(index++)));
				c.setFemaleWeightMax(Double.parseDouble(fields.get(index++)));
				c.setFemaleWeightAvg(Double.parseDouble(fields.get(index++)));

				c.setMonthToGrowup(Integer.parseInt(fields.get(index++)));

				c.setMaleKidWeight(Double.parseDouble(fields.get(index++)));
				c.setFemaleKidWeight(Double.parseDouble(fields.get(index++)));

				c.setMaleHeight(Double.parseDouble(fields.get(index++)));
				c.setFemaleHeight(Double.parseDouble(fields.get(index++)));

				c.setMaleEnergy(Double.parseDouble(fields.get(index++)));
				c.setFemaleEnergy(Double.parseDouble(fields.get(index++)));

				c.setMaleFeed(Double.parseDouble(fields.get(index++)));
				c.setFemaleFeed(Double.parseDouble(fields.get(index++)));

				c.setStatus(1);

				categories.add(c);
			}
		}

		return categories;
	}

	public void setup() throws IOException, DalException {
		List<CategoryDo> categories = parseCategories();

		for (CategoryDo category : categories) {
			m_dao.insert(category);
		}
	}
}
