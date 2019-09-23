package org.molgenis.beacon.config;

import org.junit.jupiter.api.Test;
import org.molgenis.data.config.EntityBaseTestConfig;
import org.molgenis.data.meta.AbstractEntityFactoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(
    classes = {
      EntityBaseTestConfig.class,
      BeaconOrganizationMetadata.class,
      BeaconOrganizationFactory.class,
      BeaconPackage.class,
      BeaconTestConfig.class
    })
class BeaconOrganizationFactoryTest extends AbstractEntityFactoryTest {

  @Autowired BeaconOrganizationFactory factory;

  @Override
  @Test
  public void testCreate() {
    super.testCreate(factory, BeaconOrganization.class);
  }

  @Override
  @Test
  public void testCreateWithId() {
    super.testCreateWithId(factory, BeaconOrganization.class);
  }

  @Override
  @Test
  public void testCreateWithEntity() {
    super.testCreateWithEntity(factory, BeaconOrganization.class);
  }
}
