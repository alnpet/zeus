package com.alnpet.ajax;

import org.unidal.web.mvc.AbstractModule;
import org.unidal.web.mvc.annotation.ModuleMeta;
import org.unidal.web.mvc.annotation.ModulePagesMeta;

@ModuleMeta(name = "ajax", defaultInboundAction = "newsletter", defaultTransition = "default", defaultErrorAction = "default")
@ModulePagesMeta({

com.alnpet.ajax.newsletter.Handler.class
})
public class AjaxModule extends AbstractModule {

}
