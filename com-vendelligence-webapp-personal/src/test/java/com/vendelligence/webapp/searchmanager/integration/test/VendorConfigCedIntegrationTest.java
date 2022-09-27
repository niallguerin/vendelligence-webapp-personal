package com.vendelligence.webapp.searchmanager.integration.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.core.env.Environment;

import com.vendelligence.webapp.WebApplication;
import com.vendelligence.webapp.searchmanager.config.VendorConfig;

/*
 * This is an integration test class using 1.3.8* Spring Boot test guidelines.
 * We use the main classes of the system to obtain the application context in
 * conjunction with the out-of-the-box spring test support. Hence, these are
 * flagged as integration tests as we are autowiring dependencies here for these
 * test cases.
 * 
 * The purpose here is to ensure the vendor string matches the custom search engine
 * id in our main cse specifications every time we add more vendors to the vendor
 * repository as I previously created mismatches or got 0 results back due to
 * incorrect vendor configuration data being pulled back after additons or deletions.
 * You must add a new test case every time a new vendor and cseid is created.
 * 
 * The spring profile *.properties controls the vendor source configurations and
 * is used to load the source values. Our test case values are run from the specs
 * so it is intended to add a second control check in case of a faulty value.
 * 
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WebApplication.class)
@WebIntegrationTest
public class VendorConfigCedIntegrationTest
{
    @Autowired
    private Environment env;
    
	@Autowired
	private VendorConfig vc;
	
	// local variables
	
	// aws
    private String vendorAmazonWebServices = "aws";
    // aws cseid
    private String awsCseid = "011807575984263020329:8bqearrdzya";
    
    // docker
    private String vendorDocker = "docker";
    // docker cseid
    private String dockerCseid = "011807575984263020329:5creoupuieo";
    
    // eclipse
    private String vendorEclipse = "eclipse";
    // eclipse cseid
    private String eclipseCseid = "011807575984263020329:g0kjcb-6r7m";
    
    // google-cloud-platform
    private String vendorGoogleCloudPlatform = "google-cloud-platform";
    // google-cloud-platform cseid
    private String googleCloudPlatformCseid = "011807575984263020329:ubd30bseen8";
    
    // hadoop
    private String vendorHadoop = "hadoop";
    // hadoop cseid
    private String hadoopCseid = "011807575984263020329:maswivr0q8k";

    // ibm-cloud-platform
    private String vendorIbmCloudPlatform = "ibm-cloud-platform";
    // ibmc-cloud-platform cseid
    private String ibmCloudPlatformCseid = "011807575984263020329:0vycahsm1ky";
    
    // jetbrains-intellij
    private String vendorJetbrainsIntellij = "jetbrains-intellij";
    // jetbrains-intellij cseid
    private String jetbrainsIntellijCseid = "011807575984263020329:7t-bot71toq";
    
    // jquery
    private String vendorJquery = "jquery";
    // jquery cseid
    private String jqueryCseid = "011807575984263020329:tw_xo5e1o64";
    
    // mariadb
    private String vendorMariadb = "mariadb";
    // mariadb cseid
    private String mariadbCseid = "011807575984263020329:lgli0_xbvns";
    
    // microsoft-cloud-platform
    private String vendorMicrosoftCloudPlatform = "microsoft-cloud-platform";
    // microsoft-cloud-platform cseid
    private String microsoftCloudPlatformCseid = "011807575984263020329:zbbn5zxdg3a";
    
    // npm
    private String vendorNpm = "npm";
    // npm cseid
    private String npmCseid = "011807575984263020329:s6etx3bgsku";

    // openstack
    private String vendorOpenstack = "openstack";
    // openstack cseid
    private String openstackCseid = "011807575984263020329:gaet5bnctg4";
    
    // oracle
    private String vendorOracle = "oracle";
    // oracle cseid
    private String oracleCseid = "011807575984263020329:tfs4ay5moyq";
    
    // spring
    private String vendorPivotalSpring = "pivotal-spring";
    // spring cseid
    private String pivotalSpringCseid = "011807575984263020329:cgiza3-2dye";
    
    // sap
    private String vendorSap = "sap";
    // sap cseid
    private String sapCseid = "011807575984263020329:imwe9_rvz54";
    
    // apache-spark
    private String vendorApacheSpark = "apache-spark";
    // apache-spark cseid
    private String apacheSparkCseid = "011807575984263020329:ihtpddgrrdm";
    
    // wireshark
    private String vendorWireshark = "wireshark";
    // wireshark cseid
    private String wiresharkCseid = "011807575984263020329:3nx7vry5ops";
    
    // test cases
    
    // test amazon web services
	@Test
	public void whenVendorFilterIsAmazonWebServices_thenVendorCseIdentifier_MustMatch()
	{	
		assertTrue(vc.getVendor().get(vendorAmazonWebServices).equals(awsCseid));
	}
    
	// test docker
	@Test
	public void whenVendorFilterIsDocker_thenVendorCseIdentifier_MustMatch()
	{	
		assertTrue(vc.getVendor().get(vendorDocker).equals(dockerCseid));
	}
	
	// test eclipse
	@Test
	public void whenVendorFilterIsEclipse_thenVendorCseIdentifier_MustMatch()
	{	
		assertTrue(vc.getVendor().get(vendorEclipse).equals(eclipseCseid));
	}
	
	// test google-cloud-platform
	@Test
	public void whenVendorFilterIsGoogleCloudPlatform_thenVendorCseIdentifier_MustMatch()
	{	
		assertTrue(vc.getVendor().get(vendorGoogleCloudPlatform).equals(googleCloudPlatformCseid));
	}
	
	// test hadoop
	@Test
	public void whenVendorFilterIsHadoop_thenVendorCseIdentifier_MustMatch()
	{	
		assertTrue(vc.getVendor().get(vendorHadoop).equals(hadoopCseid));
	}
	
	// test ibm-cloud-platform
	@Test
	public void whenVendorFilterIsIbmCloudPlatform_thenVendorCseIdentifier_MustMatch()
	{	
		assertTrue(vc.getVendor().get(vendorIbmCloudPlatform).equals(ibmCloudPlatformCseid));
	}
	
	// test jetbrains-intellij
	@Test
	public void whenVendorFilterIsIntellij_thenVendorCseIdentifier_MustMatch()
	{	
		assertTrue(vc.getVendor().get(vendorJetbrainsIntellij).equals(jetbrainsIntellijCseid));
	}
	
	// test jquery
	@Test
	public void whenVendorFilterIsJquery_thenVendorCseIdentifier_MustMatch()
	{	
		assertTrue(vc.getVendor().get(vendorJquery).equals(jqueryCseid));
	}
	
	// test mariadb
	@Test
	public void whenVendorFilterIsMariadb_thenVendorCseIdentifier_MustMatch()
	{	
		assertTrue(vc.getVendor().get(vendorMariadb).equals(mariadbCseid));
	}
	
	// test microsoft-cloud-platform
	@Test
	public void whenVendorFilterIsMicrosoftCloudPlatform_thenVendorCseIdentifier_MustMatch()
	{	
		assertTrue(vc.getVendor().get(vendorMicrosoftCloudPlatform).equals(microsoftCloudPlatformCseid));
	}
	
	// test npm
	@Test
	public void whenVendorFilterIsNpm_thenVendorCseIdentifier_MustMatch()
	{	
		assertTrue(vc.getVendor().get(vendorNpm).equals(npmCseid));
	}
	
	// openstack
	@Test
	public void whenVendorFilterIsOpenstack_thenVendorCseIdentifier_MustMatch()
	{	
		assertTrue(vc.getVendor().get(vendorOpenstack).equals(openstackCseid));
	}
	
	// test oracle
	@Test
	public void whenVendorFilterIsOracle_thenVendorCseIdentifier_MustMatch()
	{	
		assertTrue(vc.getVendor().get(vendorOracle).equals(oracleCseid));
	}
	
	// test pivotal-spring
	@Test
	public void whenVendorFilterIsSpring_thenVendorCseIdentifier_MustMatch()
	{	
		assertTrue(vc.getVendor().get(vendorPivotalSpring).equals(pivotalSpringCseid));
	}
	
	// test sap
	@Test
	public void whenVendorFilterIsSap_thenVendorCseIdentifier_MustMatch()
	{	
		assertTrue(vc.getVendor().get(vendorSap).equals(sapCseid));
	}
	
	// test apache spark
	@Test
	public void whenVendorFilterIsApacheSpark_thenVendorCseIdentifier_MustMatch()
	{	
		assertTrue(vc.getVendor().get(vendorApacheSpark).equals(apacheSparkCseid));
	}
	
	// test wireshark
	@Test
	public void whenVendorFilterIsWireshark_thenVendorCseIdentifier_MustMatch()
	{	
		assertTrue(vc.getVendor().get(vendorWireshark).equals(wiresharkCseid));
	}
}
