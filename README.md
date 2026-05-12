### Ejercicio 1. ¿Qué es Spring Data JPA? ¿Qué problema resuelve respecto de usar Hibernate  directamente? Describir dos situaciones del proyecto donde Spring Data JPA simplifica código que en la Práctica 1 requería implementación manual. 

Spring Boot Data JPA is a Spring framework module that simplifies working with the Java Persistence API (JPA) by reducing boilerplate code for database operations. It acts as an abstraction layer over JPA providers like Hibernate, enabling developers to focus on business logic rather than repetitive CRUD code.
Es un modulo de Spring Framework que simplifica trabajar con JPA reduciendo el código repetido al implementar los repositorios, con métodos CRUD ya hechos, soporte de consultas custom con anotacion @Query.
En la práctica 1 hubiera sido útil al hacer los repositorios y al simplificar el código de las querys custom

### Ejercicio 2. Spring Data JPA no es un ORM sino una capa de abstracción sobre el ORM. Explicar la diferencia: ¿qué hace Spring Data JPA? ¿Qué sigue haciendo Hibernate internamente?

Spring Data es una capa intermedia entre el desarrollador e Hibernate, Mientras Spring Data JPA orquesta la lógica de alto nivel, delega en Hibernate el trabajo real contra la base de datos mediante el EntityManager. Hibernate sigue controlando el ciclo de vida de las entidades en memoria. En las queries customizadas, Spring Data genera el lenguaje JPQL que después Hibernate es el que pasará al lenguaje SQL correcto.

### Ejercicio 3. Cual hace Spring Data, cual Hibernate y cual JDBC

Abrir y cerrar la conexión a la base de datos                   JDBC  
Implementar save(), findById() y deleteById()                   Spring Data
Gestionar el ciclo de vida de las entidades (@Entity)           Hibernate 
Derivar una consulta a partir del nombre del método             Spring Data
Manejar el pool de conexiones                                   JDBC
Propagar transacciones con @Transactional                       Hibernate
Generar la implementación del repositorio en runtime            Spring Data
Mapear ResultSet a objetos Java                                 Hibernate 
Proveer soporte nativo de paginación (Pageable)                 Spring Data

### Ejercicio 6.
Se debe configurar la DataSource, la conexión con la BD a nivel JDBC
- la url, servidor y nombre de la bd,
- credenciales del usuario
- driver que se usa

Se debe configurar el dialecto de Hibernate, que base de datos usas básicamente

Se debe configurar como Hibernate controla la estructura de las tablas al arranque de la aplicación
- none: no hace nada
- create: borra el esquema y crea uno nuevo cada vez que se arranca la aplicación
- create-drop: lo mismo que create + borra el esquema cuando se detiene la aplicación
- update: va actualizando las tablas a medida que se van haciendo cambios en la estructura
- validate: Solo verifica que las tablas y columnas existan y coincidan con tus entidades. Si hay diferencias, la aplicación no arranca.

###Ejercicio 7.
Es una interfaz para operaciones CRUD genéricas. Implementa la interfaz repositorio que simplemente tiene el tipo de la clase y el tipo del ID de la misma.
Provee operaciones para buscar, para persistir y para borrar.

### Ejercicio 8.

1. CrudRepository (El nivel básico) Es la interfaz fundamental que proporciona las operaciones estándar para crear, leer, actualizar y borrar (CRUD) registros en la base de datos.
Qué agrega: Te libera de escribir consultas básicas. Incorpora métodos como save(...) para guardar o actualizar entidades
, delete(...) para borrar
, findById(...), findAll(...), y count().
Limitación principal: Sus métodos suelen devolver colecciones genéricas de tipo Iterable y no está preparado para procesar consultas masivas de forma controlada.

2. PagingAndSortingRepository (El nivel intermedio) Esta interfaz hereda de CrudRepository y soluciona el problema de consultar tablas con miles o millones de registros sin saturar la memoria.
Qué agrega respecto al anterior: Incorpora capacidades para recuperar los datos paginados (en bloques o páginas) y ordenados.
Operaciones que incorpora: Añade los métodos findAll(Sort sort) para obtener registros ordenados según ciertos criterios, y findAll(Pageable pageable) para obtener una "página" específica de resultados (por ejemplo, los primeros 20 registros).

3. JpaRepository (El nivel más avanzado y específico) Esta interfaz hereda de PagingAndSortingRepository (así como de otras interfaces como QueryByExampleExecutor
), y es la que más se suele utilizar al trabajar con bases de datos relacionales en Spring Boot. A diferencia de las anteriores, que son agnósticas a la tecnología subyacente, esta está diseñada específicamente para interactuar con JPA y el contexto de persistencia (Hibernate).
Qué agrega respecto a los anteriores:
Mejora de tipos: Modifica los métodos de las interfaces padre para devolver colecciones List en lugar de Iterable, lo cual es mucho más amigable de procesar en Java.
Control del contexto de persistencia (Flushing): Incorpora operaciones para interactuar directamente con el EntityManager de JPA, como flush() o saveAndFlush(...), lo que permite forzar la sincronización inmediata de los cambios en memoria hacia la base de datos.
Operaciones en lote (Batch): Añade métodos de eliminación optimizados a nivel de base de datos, como deleteAllInBatch(), que borra registros directamente con una sola sentencia SQL en lugar de cargarlos en memoria primero y borrarlos uno por uno.
Query by Example: Incorpora métodos nativos para realizar búsquedas dinámicas pasando una entidad de ejemplo (findAll(Example))



19. HQL es un lenguaje de consultas similar a SQL pero que opera a nivel de objetos persistentes e independientemente del motor de BD
JPQL también trabaja a nivel de objetos persistentes y es independiente del motor de BD.
La diferencia radica en la portabilidad, JPQL está definido por JPA, por lo que funciona en cualquier implementación de esta, HQL solo funciona con Hibernate. Hibernate prioriza rendimiento sobre portabilidad, y además tiene features más avanzados(?)
@Query usa JPQL por defecto. Son intercambiables si se usa Hibernate.

20.
Al establecer nativeQuery = true, le indicas a Spring Data JPA que la consulta que estás declarando está escrita en SQL puro. Se puede optimizar más, pero pierde la portabilidad de JPA.
Conviene usar nativeQuery = true únicamente cuando tienes consultas altamente complejas o necesitas utilizar funciones específicas de tu motor de base de datos que no son soportadas por el estándar JPA
. El costo de usar SQL nativo es que pierdes la independencia de la base de datos
, no podrás usar el ordenamiento dinámico proporcionado por Spring Data, y si necesitas paginar los resultados, estarás obligado a declarar explícitamente una consulta de conteo adicional

21. 
- Parámetros posicionales (?1, ?2) Es el mecanismo que utiliza Spring Data JPA por defecto
. Consiste en utilizar un signo de interrogación seguido de un número (?1, ?2, etc.) dentro de la consulta, donde el número representa la posición exacta del argumento en la firma del método de Java

Parámetros nombrados (@Param) En lugar de depender del orden, este enfoque te permite asignar un nombre explícito a cada parámetro utilizando la anotación @Param("nombre") en los argumentos del método. Luego, en la consulta, haces referencia a ese valor anteponiendo dos puntos (:nombre)

Son mejores los nombrados ya que los posicionales pueden tender a causar errores, por ej al refactorizar.

22. 
Pageable es una interfaz que capsula información y operaciones para objetos paginados.

