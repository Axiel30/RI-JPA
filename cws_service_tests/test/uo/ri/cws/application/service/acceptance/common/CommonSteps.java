package uo.ri.cws.application.service.acceptance.common;


import io.cucumber.java.After;
import io.cucumber.java.Before;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;

public class CommonSteps {

	@Before
	public void setUp() {
		Db.clearTables();
	}

	@After
	public void tearDown() {
		Factories.close();
	}

}