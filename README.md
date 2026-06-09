1. En MongoDB, los conceptos de los sistemas de bases de datos relacionales (RDBMS) que mencionas se manejan de la siguiente manera:
Base de Datos: Este concepto sí existe tal cual. MongoDB utiliza bases de datos para organizar la información
Tabla / Relación: Este concepto no existe con ese nombre. La alternativa en MongoDB es la Colección (Collection), la cual se utiliza junto con las bases de datos para organizar los conjuntos de datos

Fila / Tupla: No existen como tal. La alternativa en MongoDB es el Documento (Document). Los documentos son la unidad básica de almacenamiento de datos en este sistema y se estructuran en formato BSON

Columna:  El equivalente de una columna en un documento de MongoDB es un Campo (Field), conformado por pares clave-valor dentro de la estructura BSON.

2. MongoDB no existen las claves foráneas con las restricciones estrictas de integridad referencial propias de las bases de datos relacionales. En su lugar, los enlaces entre entidades relacionadas se manejan de dos maneras: incrustando datos directamente dentro de un documento (embedding) o utilizando referencias (references), donde un documento guarda el identificador de otro, actuando de forma similar a una clave foránea pero sin la validación automática a nivel de la base de datos


3.
Índice de un solo campo (Single Field Index): Es el tipo más básico. MongoDB crea automáticamente un índice en el campo _id de cada colección, pero puedes crear índices adicionales en cualquier otro campo individual de un documento para acelerar las consultas por ese campo específico.
Índice compuesto (Compound Index): Permite indexar múltiples campos en un solo índice. Son muy útiles para consultas que filtran o evalúan varios campos a la vez (por ejemplo, buscar por "nombre" y ordenar por "fecha").
Índice multiclave (Multikey Index): Se utiliza para indexar campos que contienen arreglos (arrays). MongoDB crea una entrada de índice para cada elemento del arreglo, lo que permite realizar búsquedas rápidas de documentos basándose en los elementos de sus listas internas.
Índices geoespaciales (Geospatial Indexes): Soportan las consultas de datos basados en coordenadas espaciales. Existen los índices 2dsphere (para cálculos de geometría esférica, como la Tierra) y 2d (para distancias en un plano bidimensional).
Índice de texto (Text Index): Soportan la búsqueda de texto completo (full-text search) en campos de cadenas de caracteres, permitiendo buscar palabras o frases dentro de bloques de texto, ignorando palabras vacías (stop words) y utilizando derivación de palabras (stemming).
Índice Hash (Hashed Index): Indexa el valor hash del campo. Se utilizan principalmente para soportar el particionamiento horizontal de datos (sharding) distribuyendo los documentos de forma equilibrada a lo largo del clúster.
Índice de vectores (Vector Search Index): Una de las adiciones más modernas (orientada a la Inteligencia Artificial) que permite realizar búsquedas semánticas indexando representaciones numéricas de datos no estructurados (vectores)


4. 
- Vistas Estándar (Standard Views) Tal y como señala la fuente sobre las "vistas de solo lectura"
, estas no almacenan datos en el disco. Se definen mediante un pipeline de agregación (una serie de operaciones de procesamiento de datos) y se calculan en memoria bajo demanda cada vez que un cliente realiza una consulta.
Diferencia clave: No ocupan espacio de almacenamiento extra, pero consumen recursos de procesamiento en cada consulta.
Casos de uso: Son excelentes para la seguridad y privacidad. Puedes crear una vista que excluya campos con información confidencial y dar acceso a los usuarios solo a esa vista. También sirven para simplificar el desarrollo, permitiendo a las aplicaciones consultar una estructura compleja como si fuera una colección plana tradicional.
- Vistas Materializadas Bajo Demanda (On-Demand Materialized Views) A diferencia de las estándar, las vistas materializadas sí escriben y almacenan los resultados en el disco como si fueran una colección normal. Se llaman "bajo demanda" porque no se actualizan en tiempo real automáticamente con cada cambio en los datos base, sino que deben actualizarse ejecutando la agregación nuevamente de forma manual o programada.
Diferencia clave: Consumen espacio en disco y la información puede no estar 100% actualizada en tiempo real, pero las consultas sobre ellas son extremadamente rápidas.
Casos de uso: Son ideales para optimizar el rendimiento de consultas analíticas pesadas y repetitivas. Por ejemplo, en tableros de métricas o reportes mensuales que procesan millones de registros; en lugar de hacer el cálculo cada vez que alguien abre el reporte, la aplicación simplemente lee los resultados pre-calculados desde el disco.

5. A pesar de tener un esquema dinámico, MongoDB permite definir reglas de validación a nivel de colección. Puedes configurar una colección para que valide los documentos contra un estándar específico antes de permitir su inserción o actualización.  

Campos Requeridos: Puedes obligar a que ciertos campos (como un ID o un email) estén siempre presentes.

Tipos de Datos: Puedes restringir que un campo sea estrictamente un string, int, date, etc.

Rango de Valores: Puedes definir valores mínimos, máximos o expresiones regulares para validar el contenido.

6. Hay transacciones a nivel de documento, se puede hacer transacciones tradicionales, o sea entre documentos distintos, pero pierde las ventajas que tiene las bd NoSQL.

7.
Los documentos embebidos almacenan información relacionada en un solo documento, duplicando la información. Como consecuencia, tiene las siguientes ventajas:
- Mayor performance para operaciones de lectura
- La capacidad de leer y actualizar información relacionada en una sola operación
Se debería usar cuando los documentos embebidos no son muy grandes.

También se puede almacenar información relacionada en otros documentos y unirlos por links.
Se debería usar cuando
- Se suele acceder a la información relacionada por separado
- Los datos relacionados se actualizan frecuentemente.
- Cuando hay relaciones many to many complejas


11. El id?
12. lo hice copiando todo y usnado un insertmany, q otra forma hay?



13. db.recorridos.updateOne(
   { "nombre": "Cultural Odyssey" },
   { $set: { "totalKm": 5 } }
)

14. db.recorridos.updateOne(
   { nombre: "Delta Tour" }, // Filtro: el ID del recorrido
   { $push: { "stops": "Tigre" } }      // Acción: agrega la parada al array "stops"
)

15. db.recorridos.updateMany(
   {},                     // 1. Filtro vacío para seleccionar TODOS los documentos
   { $mul: { precio: 1.1 } } // 2. Multiplica el campo "precio" por 1.1
)

16. db.recorridos.deleteOne({nombre:"Temporal Route"})

17. db.recorridos.updateOne(
   { nombre: "Urban Exploration" },
   { $push: { tags: "Gastronomia" } }
)

18. db.recorridos.findOne({nombre:"Museum Tour"})

19. db.recorridos.find({precio: {$gt : 60000 }})

20. db.recorridos.find({precio: {$gt : 50000 }, totalKm: 10})

21. db.recorridos.find({ stops: "San Telmo" })

22. db.recorridos.find({ 
   stops: "Recoleta", 
   stops: { $ne: "Plaza Italia" } 
})

23. db.recorridos.find(
   { 
     stops: "Delta", 
     precio: { $lt: 50000 } 
   },
   { 
     nombre: 1, 
     totalKm: 1, 
     _id: 0 
   }
)

24  db.recorridos.find({ stops: {$all : ["San Telmo", "Recoleta", "Avenida de Mayo"] }})

25. db.recorridos.find(
   { 
     $expr: { $gt: [ { $size: "$stops" }, 5 ] } 
   },
   { 
     nombre: 1, 
     _id: 0 
   }
)

26. db.recorridos.find({totalKm: {$exists : false}})

27. db.recorridos.find(
   { 
     stops: /museo/i
   },
   { 
     nombre: 1, 
     stops: 1, 
     _id: 0 
   }
)

28. db.recorridos.countDocuments({})

29. db.route.aggregate([
   { $sample: { size: 5 } }
])

30.db.route.aggregate([
  // 1. Elegimos las 5 rutas al azar
  { $sample: { size: 5 } },


  // 2. Traemos la información de la colección 'stop'
  {
    $lookup: {
      from: "stop",          // Colección de origen con la que nos unimos
      localField: "stops",   // Campo en 'route' (el array de números)
      foreignField: "code",  // Campo equivalente en la colección 'stop'
      as: "stopsInfo"        // Cómo se llamará el nuevo array con los resultados completos
    }
  },

  // 3. (Opcional) Limpiamos la salida para reemplazar el viejo array de números por el detallado
  {
    $project: {
      name: 1,
      price: 1,
      totalKm: 1,
      stops: "$stopsInfo"    // Sobrescribimos 'stops' con los objetos completos de 'stopsInfo'
    }
  }
])

31. db.route.aggregate([
  { $match:{ price: {$gte: 90000}}},


  {
    $lookup: {
      from: "stop",          // Colección de origen con la que nos unimos
      localField: "stops",   // Campo en 'route' (el array de números)
      foreignField: "code",  // Campo equivalente en la colección 'stop'
      as: "stopsInfo"        // Cómo se llamará el nuevo array con los resultados completos
    }
  },

  {
    $project: {
      name: 1,
      price: 1,
      totalKm: 1,
      stops: "$stopsInfo"    // Sobrescribimos 'stops' con los objetos completos de 'stopsInfo'
    }
  }
])

32. db.route.aggregate([
  { $match:{ price: {$gte: 900000}}},


  {
    $lookup: {
      from: "stop",          // Colección de origen con la que nos unimos
      localField: "stops",   // Campo en 'route' (el array de números)
      foreignField: "code",  // Campo equivalente en la colección 'stop'
      as: "stopsInfo"        // Cómo se llamará el nuevo array con los resultados completos
    }
  },

  {
    $project: {
      name: 1,
      price: 1,
      totalKm: 1,
      stops: "$stopsInfo"    // Sobrescribimos 'stops' con los objetos completos de 'stopsInfo'
    }
  }
])

33.
db.route.aggregate([
  // 1. Filtramos las rutas que tengan 5 o más paradas
  {
    $match: {
      $expr: { $gte: [ { $size: "$stops" }, 5 ] }
    }
  },

  // 2. Opcional: Si quieres ver también los nombres de las paradas (como en el punto 31)
  {
    $lookup: {
      from: "stop",
      localField: "stops",
      foreignField: "code",
      as: "stops"
    }
  }
])


34. db.route.aggregate([
  {
    $match: {
       name: /111/
      
    }
  },

  // 2. Opcional: Si quieres ver también los nombres de las paradas (como en el punto 31)
  {
    $lookup: {
      from: "stop",
      localField: "stops",
      foreignField: "code",
      as: "stops"
    }
  }
])

35. db.route.aggregate([
  { $match:{ name: "route100"}},


  {
    $lookup: {
      from: "stop",          // Colección de origen con la que nos unimos
      localField: "stops",   // Campo en 'route' (el array de números)
      foreignField: "code",  // Campo equivalente en la colección 'stop'
      as: "stops"        // Cómo se llamará el nuevo array con los resultados completos
    }
  },
  {
    $project :
    {
        stops : 1,
        _id : 0
    }
  }]
)

36. db.route.aggregate([
  // 1. Descomponer el array de paradas en un documento por cada parada
  { $unwind: "$stops" },
  
  // 2. Agrupar por el código de parada y contar las ocurrencias
  { 
    $group: { 
      _id: "$stops", 
      totalApariciones: { $sum: 1 } 
    } 
  },
  
  // 3. Ordenar de mayor a menor según el conteo
  { $sort: { totalApariciones: -1 } },
  
  // 4. Limitar el resultado al primero (el que más aparece)
  { $limit: 1 },
  
  // 5. Cruzar con la colección 'stop' para traer los datos legibles (nombre, descripción)
  {
    $lookup: {
      from: "stop",
      localField: "_id",
      foreignField: "code",
      as: "datosParada"
    }
  },
  
  // 6. Limpiar el formato para que quede más ordenado (opcional)
  { $unwind: "$datosParada" }, 
  {
    $project: {
      _id: 0,
      codigoParada: "$_id",
      totalApariciones: 1,
      nombreParada: "$datosParada.name",
      descripcion: "$datosParada.descipcion" // Respetando el typo de tu script original
    }
  }
])

37. db.route.aggregate([
  { $match:{ price: {$lt: 15000}}},
  {
    $project: {
      name: 1,
      totalKm: 1,
      cantStops: {$size: "$stops"},
      price: 1,
    }
  },
  { $out: "rutas_economicas" }
])

38. db.route.aggregate([
  // 1. Descomponer el array de paradas en un documento por cada parada
  { $unwind: "$stops" },
  
  // 2. Agrupar por el código de parada y contar las ocurrencias
  { 
    $group: { 
      _id: "$stops", 
      promedio: { $avg: "$price" } 
    } 
  },
  {
    $lookup: {
      from: "stop",
      localField: "_id",
      foreignField: "code",
      as: "datosParada"
    }
  },
  
  // 6. Limpiar el formato para que quede más ordenado (opcional)
   { $unwind: "$datosParada" }, 
  {
    $project: {
      _id: 0,
      codigoParada: "$_id",
      nombreParada: "$datosParada.name",
      descripcion: "$datosParada.descipcion",
      promedio : 1
    }
  }
  

])