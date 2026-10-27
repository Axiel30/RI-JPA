package uo.ri.cws.domain;

import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;

public class Associations {

    public static class Owns {

        public static void link(Client client, Vehicle vehicle) {
            ArgumentChecks.isNotNull(client);
            ArgumentChecks.isNotNull(vehicle);
            StateChecks.isTrue(vehicle.getClient() == null, "Vehicle already has a client");
            StateChecks.isFalse(client._getVehicles().contains(vehicle), "Client already owns this vehicle");
            vehicle._setClient(client);
            client._getVehicles().add(vehicle);
        }

        public static void unlink(Vehicle vehicle) {
            ArgumentChecks.isNotNull(vehicle);
            Client client = vehicle.getClient();
            StateChecks.isTrue(client != null, "Vehicle has no client");
            StateChecks.isTrue(client._getVehicles().contains(vehicle), "Client does not have this vehicle");
            client._getVehicles().remove(vehicle);
            vehicle._setClient(null);
        }
    }

    public static class Classifies {

        public static void link(VehicleType vehicleType, Vehicle vehicle) {
            ArgumentChecks.isNotNull(vehicleType);
            ArgumentChecks.isNotNull(vehicle);
            StateChecks.isTrue(vehicle.getVehicleType() == null, "Vehicle already has a vehicle type");
            StateChecks.isFalse(vehicleType._getVehicles().contains(vehicle), "VehicleType already has this vehicle");

            vehicle._setVehicleType(vehicleType);
            vehicleType._getVehicles().add(vehicle);
        }

        public static void unlink(Vehicle vehicle) {
            ArgumentChecks.isNotNull(vehicle);
            VehicleType vehicleType = vehicle.getVehicleType();
            StateChecks.isTrue(vehicleType != null, "Vehicle has no vehicle type");
            StateChecks.isTrue(vehicleType._getVehicles().contains(vehicle), "VehicleType does not have this vehicle");

            vehicleType._getVehicles().remove(vehicle);
            vehicle._setVehicleType(null);
        }
    }

    public static class Holds {

        public static void link(Client client, PaymentMean paymentMean) {
            ArgumentChecks.isNotNull(client);
            ArgumentChecks.isNotNull(paymentMean);
            StateChecks.isTrue(paymentMean.getClient() == null, "Payment mean already has a client");
            StateChecks.isFalse(client._getPaymentMeans().contains(paymentMean),
                "Client already has this payment mean");

            paymentMean._setClient(client);
            client._getPaymentMeans().add(paymentMean);
        }

        public static void unlink(PaymentMean paymentMean) {
            ArgumentChecks.isNotNull(paymentMean);
            Client client = paymentMean.getClient();
            StateChecks.isTrue(client != null, "Payment mean has no client");
            StateChecks.isTrue(client._getPaymentMeans().contains(paymentMean),
                "Client does not have this payment mean");

            client._getPaymentMeans().remove(paymentMean);
            paymentMean._setClient(null);
        }
    }

    public static class Fixes {

        /* package */ static void link(Vehicle vehicle, WorkOrder workOrder) {
            ArgumentChecks.isNotNull(vehicle);
            ArgumentChecks.isNotNull(workOrder);
            StateChecks.isTrue(workOrder.getVehicle() == null, "Work order already has a vehicle");
            StateChecks.isFalse(vehicle._getWorkOrders().contains(workOrder), "Vehicle already has this work order");

            workOrder._setVehicle(vehicle);
            vehicle._getWorkOrders().add(workOrder);
        }

        public static void unlink(WorkOrder workOrder) {
            ArgumentChecks.isNotNull(workOrder);
            Vehicle vehicle = workOrder.getVehicle();
            StateChecks.isTrue(vehicle != null, "Work order has no vehicle");
            StateChecks.isTrue(vehicle._getWorkOrders().contains(workOrder), "Vehicle does not have this work order");

            vehicle._getWorkOrders().remove(workOrder);
            workOrder._setVehicle(null);
        }
    }

    public static class Bills {

        /* package */ static void link(Invoice invoice, WorkOrder workOrder) {
            ArgumentChecks.isNotNull(invoice);
            ArgumentChecks.isNotNull(workOrder);
            StateChecks.isTrue(workOrder.getInvoice() == null, "Work order already has an invoice");
            StateChecks.isFalse(invoice._getWorkOrders().contains(workOrder), "Invoice already has this work order");

            workOrder._setInvoice(invoice);
            invoice._getWorkOrders().add(workOrder);
        }

        /* package */ static void unlink(WorkOrder workOrder) {
            ArgumentChecks.isNotNull(workOrder);
            Invoice invoice = workOrder.getInvoice();
            StateChecks.isTrue(invoice != null, "Work order has no invoice");
            StateChecks.isTrue(invoice._getWorkOrders().contains(workOrder), "Invoice does not have this work order");

            invoice._getWorkOrders().remove(workOrder);
            workOrder._setInvoice(null);
        }
    }

    /**
     * Atomically manages the settles and withdraws associations of a Charge. All
     * identity ends are set before adding to, and cleared after removing from, the
     * inverse collection.
     */
    public static class SettlesWithdraws {

        /* package */ static void atomicLink(Invoice invoice, Charge charge, PaymentMean paymentMean) {

            ArgumentChecks.isNotNull(invoice);
            ArgumentChecks.isNotNull(charge);
            ArgumentChecks.isNotNull(paymentMean);
            StateChecks.isTrue(charge.getInvoice() == null, "Charge already has an invoice");
            StateChecks.isTrue(charge.getPaymentMean() == null, "Charge already has a payment mean");
            StateChecks.isFalse(invoice._getCharges().contains(charge), "Invoice already has this charge");

            charge._setInvoice(invoice);
            charge._setPaymentMean(paymentMean);
            invoice._getCharges().add(charge);
        }

        /* package */ static void atomicUnlink(Charge charge) {
            ArgumentChecks.isNotNull(charge);
            Invoice invoice = charge.getInvoice();
            StateChecks.isTrue(invoice != null, "Charge has no invoice");
            StateChecks.isTrue(charge.getPaymentMean() != null, "Charge has no payment mean");
            StateChecks.isTrue(invoice._getCharges().contains(charge), "Invoice does not have this charge");

            invoice._getCharges().remove(charge);
            charge._setInvoice(null);
            charge._setPaymentMean(null);
        }
    }

    public static class Assigns {

        /* package */ static void link(Mechanic mechanic, WorkOrder workOrder) {
            ArgumentChecks.isNotNull(mechanic);
            ArgumentChecks.isNotNull(workOrder);
            StateChecks.isTrue(workOrder.getMechanic() == null, "Work order already has a mechanic");
            StateChecks.isFalse(mechanic._getAssigned().contains(workOrder), "Mechanic already has this work order");

            workOrder._setMechanic(mechanic);
            mechanic._getAssigned().add(workOrder);
        }

        /* package */ static void unlink(WorkOrder workOrder) {
            ArgumentChecks.isNotNull(workOrder);
            Mechanic mechanic = workOrder.getMechanic();
            StateChecks.isTrue(mechanic != null, "Work order has no mechanic");
            StateChecks.isTrue(mechanic._getAssigned().contains(workOrder), "Mechanic does not have this work order");

            mechanic._getAssigned().remove(workOrder);
            workOrder._setMechanic(null);
        }
    }

    /**
     * Atomically manages the records and intervenes associations of an
     * Intervention. All identity ends are set before adding to, and cleared after
     * removing from, the inverse collection.
     */
    public static class RecordsIntervenes {

        /* package */ static void atomicLink(WorkOrder workOrder, Intervention intervention, Mechanic mechanic) {
            ArgumentChecks.isNotNull(workOrder);
            ArgumentChecks.isNotNull(intervention);
            ArgumentChecks.isNotNull(mechanic);
            StateChecks.isTrue(intervention.getWorkOrder() == null, "Intervention already has a work order");
            StateChecks.isTrue(intervention.getMechanic() == null, "Intervention already has a mechanic");
            StateChecks.isFalse(workOrder._getInterventions().contains(intervention),
                "Work order already has this intervention");

            intervention._setWorkOrder(workOrder);
            intervention._setMechanic(mechanic);
            workOrder._getInterventions().add(intervention);
        }

        /* package */ static void atomicUnlink(Intervention intervention) {
            ArgumentChecks.isNotNull(intervention);
            WorkOrder workOrder = intervention.getWorkOrder();
            StateChecks.isTrue(workOrder != null, "Intervention has no work order");
            StateChecks.isTrue(intervention.getMechanic() != null, "Intervention has no mechanic");
            StateChecks.isTrue(workOrder._getInterventions().contains(intervention),
                "Work order does not have this intervention");

            workOrder._getInterventions().remove(intervention);
            intervention._setWorkOrder(null);
            intervention._setMechanic(null);
        }
    }

    /**
     * Atomically manages the performs and substitutes associations of a
     * Substitution. All identity ends are set before adding to, and cleared after
     * removing from, the inverse collection.
     */
    public static class PerformsSubstitutes {

        /* package */ static void atomicLink(Intervention intervention, Substitution substitution,
            SparePart sparePart) {
            ArgumentChecks.isNotNull(intervention);
            ArgumentChecks.isNotNull(substitution);
            ArgumentChecks.isNotNull(sparePart);
            StateChecks.isTrue(substitution.getIntervention() == null, "Substitution already has an intervention");
            StateChecks.isTrue(substitution.getSparePart() == null, "Substitution already has a spare part");
            StateChecks.isFalse(intervention._getSubstitutions().contains(substitution),
                "Intervention already has this substitution");

            substitution._setIntervention(intervention);
            substitution._setSparePart(sparePart);
            intervention._getSubstitutions().add(substitution);
        }

        /* package */ static void atomicUnlink(Substitution substitution) {
            ArgumentChecks.isNotNull(substitution);
            Intervention intervention = substitution.getIntervention();
            StateChecks.isTrue(intervention != null, "Substitution has no intervention");
            StateChecks.isTrue(substitution.getSparePart() != null, "Substitution has no spare part");
            StateChecks.isTrue(intervention._getSubstitutions().contains(substitution),
                "Intervention does not have this substitution");

            intervention._getSubstitutions().remove(substitution);
            substitution._setIntervention(null);
            substitution._setSparePart(null);
        }
    }

    public static class Recommends {

        public static void link(Client recommender, Recommendation recommendation, Client recommended) {
            ArgumentChecks.isNotNull(recommender);
            ArgumentChecks.isNotNull(recommendation);
            ArgumentChecks.isNotNull(recommended);
            StateChecks.isTrue(recommendation.getRecommender() == null, "Recommendation already has a recommender");
            StateChecks.isTrue(recommendation.getRecommended() == null,
                "Recommendation already has a recommended client");
            StateChecks.isFalse(recommender._getRecommendations().contains(recommendation),
                "Recommender already has this recommendation");
            StateChecks.isTrue(recommended.getRecommendation() == null,
                "Recommended client already has a recommendation");

            recommendation._setRecommender(recommender);
            recommendation._setRecommended(recommended);
            recommender._getRecommendations().add(recommendation);
            recommended._setRecommendation(recommendation);
        }

        public static void unlink(Recommendation recommendation) {
            ArgumentChecks.isNotNull(recommendation);
            Client recommender = recommendation.getRecommender();
            Client recommended = recommendation.getRecommended();
            StateChecks.isTrue(recommender != null, "Recommendation has no recommender");
            StateChecks.isTrue(recommended != null, "Recommendation has no recommended client");
            StateChecks.isTrue(recommender._getRecommendations().contains(recommendation),
                "Recommender does not contain recommendation");
            StateChecks.isTrue(recommended.getRecommendation() == recommendation,
                "Recommended client does not refer to recommendation");

            recommender._getRecommendations().remove(recommendation);
            recommended._setRecommendation(null);
            recommendation._setRecommender(null);
            recommendation._setRecommended(null);
        }
    }
}
