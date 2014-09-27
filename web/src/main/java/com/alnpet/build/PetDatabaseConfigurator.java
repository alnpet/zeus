package com.alnpet.build;

import java.util.ArrayList;
import java.util.List;

import org.unidal.dal.jdbc.configuration.AbstractJdbcResourceConfigurator;
import org.unidal.lookup.configuration.Component;

final class PetDatabaseConfigurator extends AbstractJdbcResourceConfigurator {
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      // all.add(defineJdbcDataSourceComponent("pet", "com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/pet", "pet", "***", "<![CDATA[useUnicode=true&characterEncoding=UTF-8&autoReconnect=true]]>"));

      defineSimpleTableProviderComponents(all, "pet", com.alnpet.dal.core._INDEX.getEntityClasses());
      defineDaoComponents(all, com.alnpet.dal.core._INDEX.getDaoClasses());

      return all;
   }
}
