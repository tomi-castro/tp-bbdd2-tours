Un ORM es una técnica que se usa para poder persistir datos de un modelo orientado a objetos a un modelo de datos relacional. Suelve el problema de "Diferencia de Impedancia" que son las diferencias que hay entre estos modelos. Estas diferencias son:
- Tipos de datos
- Identidad e igualdad: En POO, los objetos se identifican por su referencia y basan la igualdad en su estado interno, en bases de datos relacionales solo se identifican por el ID. 
- Herencia y polimorfismo: Las bases de datos relacionales no tienen soporte nativo para herencia, por lo que usan estrategias como usar una única tabla con una columna discriminadora.

En los sistemas orientados a objetos, los objetos pueden hacerse persistentes de diferentes 
maneras. La elección del método de persistencia, es una parte importante del diseño de una 
aplicación. Tenemos diferentes alternativas:
• JDBC & SQL: JDBC es una interface de programación o API estándar de Java para acceder a 
una base de datos. Incluye manejo de conexiones a base de datos y ejecución de sentencias 
SQL, independientemente del SGBD. Usando JDBC se realizan las conexiones y manejo desde 
Java a una base de datos "a mano". Es flexible pero de bajo nivel.
• ORM (object/relational mapping): es la persistencia automatizada y transparente de objetos 
pertenecientes a una aplicación en tablas en una base de datos relacional, usando metadata que 
describen el mapeo entre los objetos y la base. ORM trabaja transformando datos desde una 
representación a otra. Se necesita usar motores de persistencia compatibles con JPA como 
Hibernate, Apache JPA, TopLink.
• Spring Data: es un módulo, parte del framework Spring cuyo objetivo es facilitar el acceso y uso 
de datos en aplicaciones basadas en Spring. Usa convenciones de nombres de métodos para 
deducir consultas automáticamente.

# Ventajas y desventajas de los ORM
## Ventajas
● Permite concentrarnos en el diseño de objetos sin pensar 
demasiado en la forma en la que lo persistiremos
● Facilidad y velocidad de desarrollo, ya que la mayoría provee creación automática del esquema de base de datos y  operaciones de alto nivel para CRUD 
● Evita el “coding” repetitivo de sentencias DDL y DML
● Detección automática de cambios y persistencia por alcance
● Abstracción del motor de base de datos
● Portabilidad, es posible cambiar a otra base de datos, incluso 
durante el desarrollo de un proyecto

## Desventajas
● Menor rendimiento, por ejemplo para una consulta el 
sistema tendrá que convertir la consulta al SQL del 
proveedor de BD utilizado
● Curva de aprendizaje, los frameworks ORM suelen 
tener mucha funcionalidad así que llegar a explotar su 
máximo rendimiento costará cierto tiempo

# JPA (Java Persistence API)
Es una API que provee una interfaz común de ORM para plataformas JSE y JEE. Se define
como un contrato de persistencia estándar que establece un conjunto de reglas y definiciones
para gestionar cómo los objetos de una aplicación Java se almacenan y recuperan de una base
de datos.

JPA necesita de un proveedor para funcionar, es decir, alguna implementación concreta que cree el código ejecutable que se conectará a la base de datos y ejecutará el SQL. Por ej, Hibernate es una de esas implementaciones.

La persistencia estándar de Java contempla tres áreas:
● La API llamada JPA, definida en el paquete jakarta.persistence– Un lenguaje de consultas JPQL
● Java Persistence Query 
● Language– Metadatos objeto/relacional

Estados de entidades

- New: recien creado.
- Managed: La entidad ya está siendo gestionada por el entity manager.Puede ser que este en BD como no, ya que sino se hizo un flush todavía puede estar solo en RAM. Se puede transicionar desde New (persist()), desde Detached (merge()) o de un nuevo objeto ( con un find() desde la base de datos)
- Detached: Sucede cuando un objeto que previamente estaba en estado managed, se desconecta del contexto de persistencia, por alguna de las siguientes acciones:
    - El contexto de persistencia se cierra o se limpia.
    - Se llama a detach o evict.
    - La entidad pasa por un proceso de serialización / deserialización
- Removed: Esta entidad esta marcada para ser removida una vez que se haga el flush. Para llegar aca se debe hacer un delete() desde un objeto managed definido por JPA aunque Hibernate permite remover entidades en estado detached. 

Cosas necesarias para poder mapear entidades en JPA
- Anotaciones @Entity y @Id
- Constructor sin argumentos
- Clase y metodos no finales

Estrategia de creación de IDs
- Identity: Hacer la columna autoincremental.
- Sequence: Usa unos tipos de datos especiales del motor de base de datos. No tan portable pero el más eficiente en inserción por lotes.
- Table: Se crea una tabla para guardar los IDs insertados. Es el más lento pero más portable. 
- Auto: Se le deja la tarea de elegir al ORM

Segun la documentacion de Hibernate siempre que se pueda elegir Sequence debería elegirse por encima de Identity.
Y si eligo siempre Auto? 

Persistencia por alcance es un principio que sigue JPA que dice todo objeto al que se pueda llegar a través de un objeto persistido debe ser persistido a su vez. Si está la operación de cascada persist declarada se persistirá automáticamente y sino, tirará error.

FetchTypes 
Lazy
Eager
Segun la documentación de Hibernate se debería usar casi siempre eager, ya que este no se puede anular, y si se quiere traer los datos de forma ansiosa debería hacerse con consultas dinámicas con join fetch.
Si se necesita en 



Asociaciones 

Siempre hay un lado propietario o dueño y un lado cliente, el dueño es el que ;aneja la relación y tiene la foreign key en su tabla
Unidireccionales
- @ManyToOne -->  lleva directamente la clave foránea en la base de datos del lado ese
- @OneToMany --> Se crea una talba intermedia con id del hijo pk (de la clase del Many) 
- @OneToOne --> Se pone la fk del hijo en el dueño de la relación. Supuestamente está mal hacer así la relación porque si por ej, una clase persona tiene un teléfono, clase persona sería el cliente de la relación porque tiene la fk, pero en el modelo de negocio no tiene sentido que persona sea la clase cliente. Por eso coviene hacerla bidireccional, hacer que la fk esté del lado del teléfono y que persona sea la clase dueña. Aparte así el padre es el que define las operaciones de cascade y no el hijo. 
- @ManyToMany --> El dueño de la relación es el que tiene la anotación y se crea una tabla intermedia que tiene como pk combinada los dos ids de las clases de la relación.
Por alguna razón que desconozco y no entendí no se debería usar lazy fetching en relaciones OneToOne bidireccionales.

Bidireccionales
- @ManyToOne -->


Los esquemas en la bd del @ManyToOne y el @OnetoOne son idénticos


Herencia
Tres estrategias
- Single Table: Se hace una sola clase con todos los atributos y un discriminador (que sino se declara se pone uno por defecto llamado DTYPE) con una anotación @DiscriminatorColumnn y con parametro discriminatortype, que solo tiene los tipos string, char y int.
- Joined (o table_per_subclass): Hay una tabla para cada clase y los datos se juntan a través de joins. Las entidades hijas deben tener pk en sus tablas, y estas pk también son fk a la tabla "padre". Estas pk se pueden crear automáticamente por Hibernate, asumiendo que tienen el mismo nombre que la pk de la clase padre.
