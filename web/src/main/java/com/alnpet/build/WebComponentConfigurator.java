package com.alnpet.build;

import java.util.ArrayList;
import java.util.List;

import com.alnpet.api.ApiModule;
import com.alnpet.ajax.AjaxModule;
import com.alnpet.biz.BizModule;

import org.unidal.lookup.configuration.Component;
import org.unidal.web.configuration.AbstractWebComponentsConfigurator;

class WebComponentConfigurator extends AbstractWebComponentsConfigurator {
	@SuppressWarnings("unchecked")
	@Override
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();

		defineModuleRegistry(all, ApiModule.class, ApiModule.class, AjaxModule.class, BizModule.class);

		return all;
	}
}
