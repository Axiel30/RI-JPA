package uo.ri.cws.application.service.vehicletype;

import java.math.BigDecimal;

/**
 * This service is intended to be used by the Cashier
 * It follows the ISP principle (@see SOLID principles from RC Martin)
 */
public interface VehicleTypeCrudService {

	// ...

	public static class VehicleTypeDto {
		public String id;
		public long version;

		public String name;
		public BigDecimal pricePerHour;
		public int minTrainigHours;
	}
}
