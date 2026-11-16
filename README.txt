DATOS DEL ALUMNO
Nombre completo: Axiel Almeda Tomás
Código UO:       UO306090

MODELO DE EJERCICIO ASIGNADO
Variante: UO % 3 == 0 (306090 % 3 = 0)
Nota adicional: Se han implementado todos los casos de uso del proyecto completo
(Variantes 0, 1 y 2).

LISTA DE AMPLIACIONES REALIZADAS
1. Generación y gestión de bonos (VoucherGenerationService - Completo):
   - generateVouchers(): Implementa las tres políticas de generación:
     * 20 € por cada 3 averías / órdenes de trabajo pagadas ("Por tres órdenes de trabajo").
     * 25 € por recomendaciones elegibles ("Por recomendación").
     * 30 € por facturas pagadas superiores a 500 € ("Por factura de más de 500").
     Devuelve el array de contadores int[] { count20, count25, count30 }.
   - findVouchersByClientNif(String nif): Búsqueda de bonos por NIF de cliente.
   - getVoucherSummary(): Resumen agregado de bonos por cliente ordenado por
     saldo disponible descendente.

2. Gestión CRUD de medios de pago (PaymentMeanCrudService - Completo):
   - addCard(CardDto): Alta de tarjetas de crédito con validaciones de fecha y unicidad.
   - addVoucher(VoucherDto): Alta de bonos con validación de código único y saldo no negativo.
   - deletePaymentMean(String id): Baja de medios de pago no usados, protegiendo efectivo
     y medios con cargos (Charge).
   - findById(String id), findPaymentMeansByClientNif(String nif),
     findCreditCardByNumber(String cardNumber) y findVoucherByCode(String code).

3. Gestión CRUD de Clientes (ClientCrudService - Completo):
   - addClient(ClientDto, recommenderNif): Alta de cliente generando automáticamente medio
     de pago Cash e interconectando recomendación recibida si se especifica patrocinador.
   - deleteClientByNif(String nif): Baja de cliente verificando que no tenga vehículos
     asociados ni facturas pagadas (vía averías o cargos en medios de pago), con desvinculación
     en cascada manual de medios de pago y recomendaciones hechas/recibidas.
   - updateClient(ClientDto): Actualización de datos personales con control de versión optimista
     y verificación de unicidad de NIF.
   - findAllClients(): Listado de todos los clientes registrados.
   - findClientByNif(String nif): Búsqueda de cliente por NIF.
   - findClientsRecommendedBy(String recommenderNif): Listado de clientes recomendados.

4. Liquidación y cobro de facturas (InvoicingService - Completo):
   - settleInvoice(String invoiceId, Map<String, BigDecimal> charges): Liquidación y cobro de
     facturas mediante cargos contra uno o varios medios de pago (efectivo, tarjeta no caducada,
     bono con saldo disponible suficiente), asegurando que la suma coincida al céntimo con el
     total de la factura y delegando la transición a PAID y creación de Charge en el dominio.
   - findInvoiceByNumber(Long number): Búsqueda de factura por su número secuencial correlativo.
   - findPayMeansByClientNif(String nif): Consulta polimórfica de los medios de pago de un cliente
     (CashDto, CardDto, VoucherDto).
   - create(List<String> workOrderIds): Creación y facturación de órdenes de trabajo aprobadas.
   - findNotInvoicedWorkOrdersByClientNif(String nif): Consulta de averías terminadas y aprobadas.

5. Mapeo y Persistencia JPA (cws_services_jpa):
   - Mapeo bidireccional completo de la entidad Recommendation con Client:
     @OneToMany(mappedBy = "recommender") y @OneToOne(mappedBy = "recommended").
   - Mapeo de columnas de estado de bonos @Column(name = "USEDFORVOUCHER") en entidades
     Recommendation, WorkOrder e Invoice para su persistencia en el esquema extendido de la BD.
   - Implementación de los repositorios JPA extendiendo BaseJpaRepository:
     PaymentMeanJpaRepository, ClientJpaRepository, RecommendationJpaRepository,
     InvoiceJpaRepository, WorkOrderJpaRepository, MechanicJpaRepository,
     VehicleJpaRepository, SparePartJpaRepository y VehicleTypeJpaRepository.
   - Consultas JPA nombradas externalizadas en META-INF/orm.xml.

6. Funcionalidades base de la práctica:
   - CRUD completo de mecánicos (MechanicCrudService): AddMechanic, DeleteMechanic,
     UpdateMechanic, FindAllMechanics, FindMechanicById y FindMechanicByNif.

7. Externalización completa del mapeo objeto-relacional a XML (orm.xml):
   - Se han migrado todas las anotaciones JPA de las clases de dominio (@Entity, @Table,
     @Id, @Version, @Column, @Embedded, @AttributeOverride, @Enumerated, @ManyToOne,
     @OneToMany, @OneToOne, @JoinColumn, @DiscriminatorValue, @MappedSuperclass,
     @Embeddable, @PreUpdate) al archivo META-INF/orm.xml.
   - Las clases de dominio (entidades, tipos de valor/embeddables y superclases mapeadas)
     quedan como POJOs puros desacoplados por completo de jakarta.persistence,
     cumpliendo los principios de diseño y arquitectura limpia.

TIEMPO DE TRABAJO AUTÓNOMO DEDICADO
Aproximadamente 15 horas.