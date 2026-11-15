package uo.ri.cws.persistence.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uo.ri.cws.domain.Cash;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - A client recovers its payment means after being persisted and reloaded
 * - A payment mean recovers its client after being persisted and reloaded
 */
class HoldsMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private Client client;
	private Cash cash;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over(factory);

		client = new Client(new Nif("12345678Z"));
		cash = new Cash(client);

		unitOfWork.persist(client, cash);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove(cash, client);
		factory.close();
	}

	@Test
	void testClientRecoversPaymentMeans() {
		Client restored = unitOfWork.findById(Client.class, client.getId());

		assertFalse(restored.getPaymentMeans().isEmpty());
		assertEquals(1, restored.getPaymentMeans().size());
	}

	@Test
	void testPaymentMeanRecoversClient() {
		Cash restored = unitOfWork.findById(Cash.class, cash.getId());

		assertEquals(client, restored.getClient());
	}

}
