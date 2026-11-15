package uo.ri.cws.application.service;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

import io.cucumber.junit.platform.engine.Constants;

/**
 * Executes the acceptance and robustness tests for the service layer.
 * Cucumber engine for acceptance tests
 * JUnit 5 engine for robustness tests 
 */
@Suite

/*
 * Cucumber engine for acceptance tests
 * JUnit 5 engine for robustness tests 
 */
@IncludeEngines({ "cucumber", "junit-jupiter" })

// --- Acceptance/Cucumber tests ---
//		common basic tests 
@SelectClasspathResource("uo/ri/cws/application/service/acceptance/mechanic")
@SelectClasspathResource("uo/ri/cws/application/service/acceptance/invoice/CreateInvoiceFor.feature")
@SelectClasspathResource("uo/ri/cws/application/service/acceptance/workorder")

//		specific tests for UO % 3 == 2
@SelectClasspathResource("uo/ri/cws/application/service/acceptance/voucher/GenerateForInvoicesOver500.feature")
@SelectClasspathResource("uo/ri/cws/application/service/acceptance/voucher/FindByClientNif.feature")
@SelectClasspathResource("uo/ri/cws/application/service/acceptance/voucher/Summary.feature")
@SelectClasspathResource("uo/ri/cws/application/service/acceptance/invoice/SettleInvoice.feature")
@SelectClasspathResource("uo/ri/cws/application/service/acceptance/invoice/FindInvoiceByNumber.feature")
@SelectClasspathResource("uo/ri/cws/application/service/acceptance/invoice/FindPayMeansByClientNif.feature")

// --- Robustness/JUnit tests ---
@SelectPackages({
		"uo.ri.cws.application.service.robustness.mechanic",
		"uo.ri.cws.application.service.robustness.invoice",
		"uo.ri.cws.application.service.robustness.voucher"
})

@ConfigurationParameter(
		key = Constants.PLUGIN_PROPERTY_NAME,
		value = "pretty, html:target/cucumber-results.html"
)
@ConfigurationParameter(
		key = Constants.SNIPPET_TYPE_PROPERTY_NAME,
		value = "camelcase"
)
@ConfigurationParameter(
		key = Constants.GLUE_PROPERTY_NAME,
		value = "uo.ri.cws.application.service.acceptance"
)
public class RunServiceTests_2 {}
