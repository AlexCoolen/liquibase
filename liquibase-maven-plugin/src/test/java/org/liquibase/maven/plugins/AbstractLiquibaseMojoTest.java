// Version:   $Id: $
// Copyright: Copyright(c) 2008 Trace Financial Limited
package org.liquibase.maven.plugins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import liquibase.resource.ResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.codehaus.plexus.configuration.PlexusConfiguration;

/**
 * A base class for writing tests that validate the properties passed in to the plugins.
 * @author Peter Murray
 */
public abstract class AbstractLiquibaseMojoTest extends AbstractMojoTestCase {

  protected void loadPropertiesFileIfPresent(AbstractLiquibaseMojo mojo)
          throws MojoExecutionException, MojoFailureException {

    File rootDir = new File(getBasedir(), "target/test-classes");
    ResourceAccessor fo = new FileSystemResourceAccessor(rootDir.getAbsolutePath());
    mojo.configureFieldsAndValues(fo);
  }

  protected PlexusConfiguration loadConfiguration(String configFile) throws Exception {
    File testPom = new File(getBasedir(), "target/test-classes/" + configFile);
    assertTrue("The configuration pom could not be found, " + testPom.getAbsolutePath(),
               testPom.exists());

    PlexusConfiguration config = extractPluginConfiguration("dbmanul-plugin",
                                                            testPom);
    assertNotNull("There should be a configuration for the plugin in the pom", config);
    return config;
  }

  protected void checkValues(Map expected, Map values) {
    for (Object key : expected.keySet()) {
      Object expectedValue = expected.get(key);
      Object actualValue = values.get(key);
      assertEquals("The values do not match for property '" + key + "'",
                   expectedValue,
                   actualValue);
    }
  }

  protected void checkValues(Properties expected, Map values) {
    for (Object key : expected.keySet()) {
      Object expectedValue = expected.get(key);
      Object actualValue = values.get(key);
      assertEquals("The values do not match for property '" + key + "'",
                   expectedValue,
                   actualValue);
    }
  }

  protected void createPropertiesFile(String filename, Properties p) throws IOException {
    File output = new File(getBasedir(), "/target/test-classes/" + filename);
    if (!output.exists()) {
      System.out.println("Creating file: " + output.getPath());
      if (!output.createNewFile()) {
        throw new IOException("Unable to create the properties file, "
                              + output.getAbsolutePath());
      }
    }

    FileOutputStream fw = null;
    try {
      fw = new FileOutputStream(output);
      p.store(fw, "generated by unit test");
    }
    finally {
      if (fw != null) {
        fw.close();
      }
    }
  }

  protected void dumpValues(Map values) {
    for (Object key : values.keySet()) {
      System.out.println(key + " :: " + values.get(key));
    }
  }
}
