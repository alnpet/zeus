package com.alnpet.build;

import java.util.ArrayList;
import java.util.List;

import org.unidal.dal.jdbc.datasource.JdbcDataSourceDescriptorManager;
import org.unidal.lookup.configuration.AbstractResourceConfigurator;
import org.unidal.lookup.configuration.Component;
import org.unidal.web.mvc.view.model.ModelBuilder;

import com.alnpet.api.XmlViewer;
import com.alnpet.dal.pet.ActivityInDayDao;
import com.alnpet.dal.pet.ActivityInHourDao;
import com.alnpet.dal.pet.CategoryDao;
import com.alnpet.dal.pet.PetDao;
import com.alnpet.dal.pet.SettingDao;
import com.alnpet.dal.trx.AddressDao;
import com.alnpet.dal.trx.CouponDao;
import com.alnpet.dal.trx.OrderDao;
import com.alnpet.service.ActivityService;
import com.alnpet.service.CategoryService;
import com.alnpet.service.DefaultActivityService;
import com.alnpet.service.DefaultCategoryService;
import com.alnpet.service.DefaultOrderService;
import com.alnpet.service.DefaultPetService;
import com.alnpet.service.DefaultSettingService;
import com.alnpet.service.OrderService;
import com.alnpet.service.PetService;
import com.alnpet.service.SettingService;

public class ComponentsConfigurator extends AbstractResourceConfigurator {
	@Override
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();

		all.add(C(XmlViewer.class)//
		      .req(ModelBuilder.class, "xml", "m_xmlBuilder"));

		all.add(C(PetService.class, DefaultPetService.class) //
		      .req(PetDao.class));
		all.add(C(CategoryService.class, DefaultCategoryService.class) //
		      .req(CategoryDao.class));
		all.add(C(ActivityService.class, DefaultActivityService.class) //
		      .req(ActivityInHourDao.class, ActivityInDayDao.class));
		all.add(C(SettingService.class, DefaultSettingService.class) //
		      .req(SettingDao.class));
		all.add(C(OrderService.class, DefaultOrderService.class) //
				.req(AddressDao.class, CouponDao.class, OrderDao.class));

		// move following line to top-level project as possible
		all.add(C(JdbcDataSourceDescriptorManager.class) //
		      .config(E("datasourceFile").value("config/datasources.xml"), //
		            E("baseDirRef").value("PET_HOME")));

		all.addAll(new PetDatabaseConfigurator().defineComponents());

		// Please keep it as last
		all.addAll(new WebComponentConfigurator().defineComponents());

		return all;
	}

	public static void main(String[] args) {
		generatePlexusComponentsXmlFile(new ComponentsConfigurator());
	}
}
