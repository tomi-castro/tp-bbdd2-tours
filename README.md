
1. ¿Qué tipo de base de datos es Redis? ¿En qué se diferencia de una base de datos
relacional y de otras bases de datos NoSQL como MongoDB?
2. ¿Dónde almacena los datos Redis? ¿Qué implicancias tiene esto en términos de
velocidad y de persistencia?
3. ¿Qué tipos de datos soporta Redis? Listar y describir brevemente cada uno.
4. Enunciar las características principales de Redis.
5. Comparar Redis con los RDBMS: ¿en qué casos conviene  usar Redis en lugar de una base de datos relacional y en cuáles no?
6. ¿Redis tiene soporte para transacciones? ¿Cómo funcionan? ¿Qué garantías ofrecen y
qué limitaciones tienen respecto de las transacciones ACID?
7. ¿Redis tiene persistencia? Describir los mecanismos disponibles (RDB y AOF) e indicar
las diferencias entre ellos.
8. ¿Cuáles son los principales casos de uso de Redis en aplicaciones reales?

1. Redis es una base de datos de tipo NoSql, a diferencia de las otras bases de datos, Redis solo opera en RAM, haciendola muy rápida. Diferencia con MongoDb es que redis guarda en clave-valor, y mongodb en json?

2. Almacena los datos en Memoria RAM, haciendo las operaciones muy rápidas pero no persistentes.

3. Redis Open Source implements the following data types:

Strings: Secuencia de bytes
- String Bitmaps: String que permite hacer operaciones de bits
- String Bitfields: Es lo que sería un entero
Arrays: Secuencias de strings indexables.
Geospatial indexes: Coordenadas
Hashes: Es similar a un diccionario de Python.
JSON: estructuras jerárquicas que se asimilan al tipo JSON popular
Lists: Colecciones de strings ordenadas en orden de entrada,
Probabilistic data types: Tipo de datos que permiten calcular probabilidades aproximadas eficientemente.
Sets: Colecciones de strings sin repeticiones.
Sorted sets: Sets que se ordenan por un criterio, un "score" que tiene cada string, desempatando lexicográficamente.
Streams: estructura de datos que actúa como un registro append-only.
Time series: Marcas de tiempo.
Vector sets: Tipo de datos especializado diseñado para gestionar datos vectoriales de alta dimensión, lo que permite una búsqueda rápida y eficiente de similitudes vectoriales dentro de Redis

4. 
- Velocidad extrema
- Estructuras de datos nativas y ricas: No solo texto
- Persistencia opcional: A través de Redis Database Backup y Append Only File.
- Modelo monohilo
- TTL y políticas de reemplazo.
- Permite replicación, sharding, y un sistema de monitoreo automático para cambiar de master.
- Mensajería en tiempo real (Pub/Sub): Incluye un sistema nativo de Publicación/Suscripción (Publish/Subscribe). Los clientes pueden suscribirse a canales y recibir instantáneamente los mensajes que otros publican, lo que facilita el desarrollo de chats, notificaciones push o arquitecturas orientadas a eventos.

5. Es el estándar de la industria para guardar sesiones de usuario, almacenar respuestas de APIs pesadas (caché), gestionar carritos de compras, procesar colas de tareas en segundo plano o construir contadores en tiempo real (como las visitas de un sitio o likes de una publicación). También se usa mucho como sistema de caché de alto rendimiento.
No se debería usar cuando se necesitan consultas con muchos datos relacionados (joins) o complejas.
Tampoco cuando se necesita cumplimiento estricto de ACID o con volúmenes de datos gigantescos.
Ni cuando se requieren Consultas Ad-hoc o por Atributos Dinámicos

6. Las transacciones en Redis son bloques de instrucciones que se ejecutan secuencialmente. Redis permite usar esto a través de varios comandos:
MULTI: Abre el bloque de instrucciones.
EXEC: Bloque la BD y ejecuta esas instrucciones secuencialmente.
DISCARD: Descarta las instrucciones.
WATCH: Permite monitorear las claves antes de entrar a un bloque MULTI, asi si fueron modificadas la transacción fallará.
Sobre las propiedades ACID.
  - Atomicidad: No hay rollback, asi que no hay atomicidad.
  - Consistencia: Las estructuras de datos son consistentes pero al no haber rollback no se puede garantizar que la bd quede consistente según la reglas del negocio.
  - Aislamiento: Si porque es monohilo.
  - Durabilidad: Depende si usas alguna de las técnicas de persistencia. Si usas AOF con la configuración appendfsync always, cada transacción se escribirá en disco inmediatamente antes de responder al cliente, ofreciendo durabilidad real. Pero esto reduce notablemente el rendimiento.
7. RDB (Redis Database): Redis saca capturas de los datos en intervalos. Ventajas: El archivo generado es compacto y liviano, ideal para backups, además es eficiente. Desventaja: Pueden ocurrir perdidas si redis se apaga inesperadamente entre capturas.

AOF: Todas las operaciones de escritura se guardan en un registro para poder ser reconstruidas despues. Opcion de compactar, rescribe el archivo AOF con el mínimo de comandos necesarios para llegar al estado final.
Ventajas: mas seguridad y durabilidad, se puede configurar para que se escriba en el archivo cada segundo o incluso depues de cada comando.
Deventajas: Más lento y pesado.

Se recomienda usar los dos juntos para máxima seguridad y durabilidad, y eficiencia, ya que usar RDB junto con AOF ayuda para los backups, reinicios o bugs con AOF.

8. Es el estándar de la industria para guardar sesiones de usuario, almacenar respuestas de APIs pesadas (caché), gestionar carritos de compras, procesar colas de tareas en segundo plano o construir contadores en tiempo real (como las visitas de un sitio o likes de una publicación). También se usa mucho como sistema de caché de alto rendimiento.

9. Agregar una clave package con el valor "Bariloche 3 days".
10. Agregar una clave user con el valor "Turismo BD2". Obtener el valor de la clave user.
11. Obtener todas las claves almacenadas actualmente.
12. Agregar una clave user con el valor "Cronos Turismo". ¿Cuál es el valor actual de la clave
user?
13. Concatenar " S.A." a la clave user. ¿Cuál es el valor actual de la clave user?
14. Eliminar la clave user. ¿Qué valor retorna si se intenta obtener la clave user luego de
eliminarla?

9. SET package "Bariloche 3 days"
10. 
SET user "Turismo BD2" 
GET user
11. MGET user package o SCAN 0 para todas las claves, de forma paginada, o key * pero no es buena idea supuestamente.
12. Cronos Turismo
13. append user " S.A"
14. del user, devuelve la cantidad de claves afectadas, no devuelve nada si hago un get

15. Verificar si existe la clave visits.
16. Agregar una clave visits con el valor 0.
17. Incrementar en 1 la clave visits. ¿Cuál es el valor actual?
18. Incrementar en 5 la clave visits. ¿Cuál es el valor actual?
19. Decrementar en 1 la clave visits. ¿Cuál es el valor actual?
20. Incrementar en 2 la clave visits. ¿Cuál es el valor actual?
21. Agregar una clave "value package" con el valor 539789.32.
22. Incrementar en 20000 la clave "value package". ¿Cuál es el valor actual?
23. ¿Cual es el tipo de datos de "value package", visits y user?

15. exists visits.
16. set visits 0
17. incr visits. valor : 1
18. incrby visits 5
19. decr visits
20. decrby visits 2
21. set "value package" 539789.32
22. incrbyfloat "value package" 20000. 
el valor ahora es 559789.31999999999999318
23. son todos strings, segun el comando type

24. Obtener todas las claves que empiecen con "v".
25. Obtener todas las claves que contengan la letra "t".
26. Obtener todas las claves que terminan con "age".
27. Renombrar la clave "package" por "bariloche package".
28. ¿Qué comando se utiliza para renombrar una clave solo si el nombre destino no existe
aún?
29. Eliminar todas las claves.

24. keys v*, o de forma paginada: scan 0 match v*
23. scan 0 match *t*
26. scan 0 match age*
27. rename package "bariloche package"
28. renamenx
29. flushdb para la bd actual o flushall para todas


30. Agregar una clave agency con el valor "Cronos Tours".
31. ¿Cuál es el tiempo de vida (TTL) de la clave agency?
32. Agregar una expiración de 30 segundos a la clave agency.
33. ¿Cuál es el tiempo de vida de la clave agency luego de agregar la expiración?
34. Pasados los 30 segundos: ¿cuál es el TTL de agency? ¿Que retorna si se solicita el valor
de agency?
35. Agregar una clave agency con el valor "Cronos Tours" que expire en 20 segundos desde
su creación

30. set agency "Cronos Tours"
31. es -1, no tiene un tiempo de expiración
32. expire agency 30
33. 30
34. -2, ya no existe la clave.
35. SET agency "Cronos Tours" EX 20

36. Insertar una lista llamada pets con el valor "dog".
37. ¿Qué sucede si se ejecuta el comando GET sobre pets? ¿Cómo se obtienen los valores
de una lista?
38. Agregar a la lista pets el valor "cat" por la izquierda.
39. Agregar a la lista pets el valor "fish" por la derecha.
40. ¿Qué tipo de dato es el valor de pets?
41. Eliminar el valor del extremo izquierdo de la lista.
42. Eliminar el valor del extremo derecho de la lista.
43. Agregar a una clave "vuelo:ar389" los valores: aep, mdz, brc, nqn y mdq.
44. Ordenar los valores de la lista "vuelo:ar389". ¿Qué sucede si se solicitan todos los
valores de la lista luego de ordenarla?
45. Insertar el valor "fte" inmediatamente después de "brc".
46. Insertar el valor "ush" inmediatamente antes de "fte".
47. Modificar el último elemento de la lista por "sla".
48. Obtener la cantidad de elementos de "vuelo:ar389".
49. Obtener el tercer valor de "vuelo:ar389".
50. Eliminar el valor "aep" de "vuelo:ar389".
51. Quedarse únicamente con los valores de las posiciones 3 a 5 de "vuelo:ar389".
52. Agregar en "vuelo:ar389" el valor "fte". ¿Cuántas veces aparece ahora en la lista?

36. lpush pets "dog"
37. WRONGTYPE Operation against a key holding the wrong kind of value. Se debe usar lpop o lrange
38. lpush pets "cat"
39. Rpush pets "fish"
40. list
41. lpop pets
42. rpop pets
43. lpush vuelo:ar389 aep mdz brc nqn mdq
44. sort vuelo:ar389 alpha, no se guarda en la lista
45. linsert vuelo:ar389 after brc fte
46. linsert vuelo:ar389 before fte ush
47. lset vuelo:ar389 -1 sla
48. llen vuelo:ar389
49. lindex vuelo:ar389 2
50. lrem "vuelo:ar389" aep
51. ltrim vuelo:ar389 3 5
52. lpush vuelo:ar389 fte, aparece 2 veces

53. Agregar un conjunto llamado airports con los siguientes valores:
eze aep nqn mdz mdq ush fte sla aep nqn brc cpc juj aep tuc eqs
54. ¿Cuántos valores tiene el conjunto? ¿Por qué puede diferir de la cantidad de valores
ingresados?
55. Listar los valores del conjunto airports.
56. Quitar el valor "cpc" del conjunto airports.
57. Quitar un valor aleatorio del conjunto airports.
58. ¿Qué cantidad de valores tiene airports ahora?
59. Comprobar si "cpc" es miembro del conjunto airports.
60. Mover los valores "sla" y "juj" a un nuevo conjunto denominado noa_airports.
61. Obtener la unión de los conjuntos airports y noa_airports. ¿Modifica los conjuntos
originales?
62. Realizar la unión de airports y noa_airports y almacenar el resultado en un nuevo
conjunto llamado total_airports.
63. Realizar la intersección entre total_airports y noa_airports.
64. Realizar la diferencia entre total_airports y noa_airports.

53. sadd airports eze aep nqn mdz mdq ush fte sla aep nqn brc cpc juj aep tuc eqs
54. Tiene 14 valores y puede diferir porque no admite repetidos.
55. smembers airports, igual conviene usar sscan airports para hacerlo de forma paginada y no traer todo de una, bloqueando la bd por largo tiempo
56. srem airports cpc
57. spop airports
58. scard airports, 5
59. sismember airports cpc
60. smove airports noa_airports sla
smove airports noa_airports juj
61. sunion airports noa_airports
62. sunionstore airports noa_airports tptal_airports
63. sinter airports noa_airports
64. sdiffer airports noa_airports


65.  Agregar a un conjunto ordenado llamado passengers los siguientes datos (score
nombre):
2.5 federico 4 alejandra 3 julian 1 ivan 2 tomas 2 luciana 2.4
natalia
66. Obtener los valores del conjunto passengers.
67. Actualizar el score de luciana a 2.7.
68. Agregar al conjunto passengers a silvia con score 5.1.
69. Incrementar en 2 el score de alejandra.
70. Obtener los valores del conjunto passengers con sus scores.
71. Obtener los valores del conjunto passengers con sus scores en orden inverso.
72. Obtener la cantidad de elementos del conjunto passengers.
73. Obtener la cantidad de elementos que tienen scores entre 2 y 3.
74. Obtener el ranking de julian en el conjunto passengers.
75. Obtener el score de tomas en el conjunto passengers.
76. Extraer el elemento con menor score del conjunto passengers.
77. Extraer el elemento con mayor score del conjunto passengers.
78. Eliminar del conjunto passengers al valor silvia.

65. ZADD passengers 2.5 federico
ZADD passengers 4 alejandra
ZADD passengers 3 julian
ZADD passengers 1 ivan
ZADD passengers 2 tomas
ZADD passengers 2 luciana
ZADD passengers 2.4 natalia
66. ZRANGE passengers 0 -1
67. ZADD passengers 2 alejandra 
68. ZADD passengers 5.1 silvia
69. ZINCRBY passengers 2 alejandra
70.