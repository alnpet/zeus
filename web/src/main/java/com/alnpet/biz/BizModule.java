package com.alnpet.biz;

import org.unidal.web.mvc.AbstractModule;
import org.unidal.web.mvc.annotation.ModuleMeta;
import org.unidal.web.mvc.annotation.ModulePagesMeta;

@ModuleMeta(name = "biz", defaultInboundAction = "order", defaultTransition = "default", defaultErrorAction = "default")
@ModulePagesMeta({

com.alnpet.biz.order.Handler.class,

com.alnpet.biz.payment.Handler.class
})
public class BizModule extends AbstractModule {

}
