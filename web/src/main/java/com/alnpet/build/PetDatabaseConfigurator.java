package com.alnpet.build;

import java.util.ArrayList;
import java.util.List;

import org.unidal.dal.jdbc.configuration.AbstractJdbcResourceConfigurator;
import org.unidal.lookup.configuration.Component;

final class PetDatabaseConfigurator extends AbstractJdbcResourceConfigurator {
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();


      defineSimpleTableProviderComponents(all, "pet", com.alnpet.dal.core._INDEX.getEntityClasses());
      defineDaoComponents(all, com.alnpet.dal.core._INDEX.getDaoClasses());

      defineSimpleTableProviderComponents(all, "pet", com.alnpet.dal.biz._INDEX.getEntityClasses());
      defineDaoComponents(all, com.alnpet.dal.biz._INDEX.getDaoClasses());

      return all;
   }
}
