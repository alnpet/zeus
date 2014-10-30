package com.alnpet.api;

import org.unidal.web.mvc.AbstractModule;
import org.unidal.web.mvc.annotation.ModuleMeta;
import org.unidal.web.mvc.annotation.ModulePagesMeta;

@ModuleMeta(name = "api", defaultInboundAction = "home", defaultTransition = "default", defaultErrorAction = "default")
@ModulePagesMeta({

com.alnpet.api.home.Handler.class,

com.alnpet.api.pet.Handler.class,

com.alnpet.api.activity.Handler.class,

com.alnpet.api.category.Handler.class
})
public class ApiModule extends AbstractModule {

}
