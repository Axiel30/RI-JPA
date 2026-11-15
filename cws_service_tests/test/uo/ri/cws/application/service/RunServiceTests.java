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
@SelectClasspathResource("uo/ri/cws/application/service/acceptance")

// --- Robustness/JUnit tests ---
@SelectPackages("uo.ri.cws.application.service.robustness")

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
public class RunServiceTests {}
