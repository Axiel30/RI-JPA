DATOS DEL ALUMNO
Nombre completo: Axiel Almeda Tomás
Código UO:       UO306090

MODELO DE EJERCICIO ASIGNADO
Variante: UO % 3 == 0 (306090 % 3 = 0)

LISTA DE AMPLIACIONES REALIZADAS
1. Generación y gestión de bonos (VoucherGenerationService):
   - generateVouchers(): Generación de bonos de 25 € con la descripción "Por recomendación"
     para clientes elegibles (patrocinadores con facturas pagadas y que acumulan al menos
     3 recomendaciones válidas cuyos clientes recomendados también tienen facturas pagadas).
     Las recomendaciones empleadas se marcan como consumidas (usedForVoucher = true).
   - findVouchersByClientNif(String nif): Búsqueda y listado de todos los bonos
     registrados asociados al NIF de un cliente.
   - getVoucherSummary(): Informe agregado de bonos por cliente (NIF, nombre, apellidos,
     cantidad de bonos emitidos, importe total, importe disponible e importe consumido),
     ordenado de forma descendente por el importe disponible.

2. Gestión CRUD de medios de pago (PaymentMeanCrudService):
   - addCard(CardDto): Registro de tarjetas de crédito para un cliente existente, validando
     la unicidad del número de tarjeta y que la fecha de caducidad no sea anterior al mes en curso.
   - addVoucher(VoucherDto): Registro de bonos para un cliente existente, comprobando la
     unicidad del código y que el saldo disponible no sea negativo.
   - deletePaymentMean(String id): Eliminación de medios de pago no utilizados, impidiendo
     borrar medios de tipo efectivo (Cash) o medios que ya tengan cargos asociados (Charge),
     desvinculando correctamente mediante Associations.Holds.unlink().
   - findById(String id): Consulta de cualquier medio de pago por su identificador.
   - findPaymentMeansByClientNif(String nif): Listado de todos los medios de pago de un cliente.
   - findCreditCardByNumber(String cardNumber): Búsqueda de tarjeta de crédito por número.
   - findVoucherByCode(String code): Búsqueda de bono por su código alfanumérico.

3. Mapeo y Persistencia JPA (cws_services_jpa):
   - Mapeo bidireccional completo de la entidad Recommendation con Client:
     @OneToMany(mappedBy = "recommender") y @OneToOne(mappedBy = "recommended").
   - Mapeo de columnas de estado de bonos @Column(name = "USEDFORVOUCHER") en entidades
     Recommendation, WorkOrder e Invoice para su persistencia en el esquema extendido de la base de datos.
   - Implementación de los repositorios JPA extendiendo BaseJpaRepository:
     PaymentMeanJpaRepository, ClientJpaRepository, RecommendationJpaRepository,
     InvoiceJpaRepository, WorkOrderJpaRepository, MechanicJpaRepository,
     VehicleJpaRepository, SparePartJpaRepository y VehicleTypeJpaRepository.
   - Implementación de las clases de consulta (Queries):
     PaymentMeanJpaQueries (comprobación de cargos y generación del resumen de bonos),
     ClientJpaQueries, MechanicJpaQueries e InvoiceJpaQueries.
   - Definición de todas las consultas nombradas JPQL en META-INF/orm.xml.

4. Funcionalidades base de la práctica:
   - CRUD completo de mecánicos (MechanicCrudService): AddMechanic, DeleteMechanic,
     UpdateMechanic, FindAllMechanics, FindMechanicById y FindMechanicByNif.
   - Creación de facturas (InvoicingService): CreateInvoiceFor y búsqueda de averías
     terminadas y aprobadas no facturadas (FindNotInvoicedWorkOrdersByClientNif).

TIEMPO DE TRABAJO AUTÓNOMO DEDICADO
Aproximadamente 12 horas.