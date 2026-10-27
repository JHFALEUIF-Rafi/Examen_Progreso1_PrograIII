# proyectoPU202710

## Taller evaluado: centro de impresión universitario

**Asignatura:** Programación III — Estructuras de datos.

Un centro de impresión universitario recibe documentos de estudiantes, docentes
y personal administrativo. Los trabajos deben organizarse por su prioridad para
decidir cuál procesar a continuación. La disponibilidad de espacio es limitada:
la interfaz utilizará una capacidad de **10 trabajos pendientes**.

Desarrolla una aplicación Java con una **cola con prioridad** y una interfaz
gráfica Swing que permita registrar, consultar, listar y procesar trabajos.
El procesamiento es una simulación: no se conecta con una impresora ni se abre
el archivo indicado. El nombre del documento se maneja como texto.

## Reglas de prioridad

- Un menor número indica una mayor prioridad: **1 se procesa antes que 3 y 5**.
- La prioridad la introduce el usuario; no se deduce del tipo de solicitante.
- La atención se decide por prioridad, aunque el trabajo haya llegado después.
- Si dos trabajos tienen la misma prioridad, puede procesarse cualquiera de los
  dos primero. No se exige FIFO entre empates.
- Los trabajos pendientes conservan su prioridad hasta ser procesados. No se
  solicita editar códigos ni prioridades de trabajos que ya están en la cola.

Ejemplo:

| Orden de registro | Código | Solicitante | Documento | Prioridad |
| ---: | --- | --- | --- | ---: |
| 1 | IMP-001 | ESTUDIANTE | Informe.pdf | 5 |
| 2 | IMP-002 | DOCENTE | Examen.pdf | 1 |
| 3 | IMP-003 | ADMINISTRATIVO | Acta.pdf | 3 |

El orden esperado al listar y procesar es **IMP-002, IMP-003, IMP-001**.

## Requisitos técnicos

- JDK 17, Maven y JUnit 5.
- Usar `PriorityQueue<TrabajoImpresion>` mediante la interfaz `Queue`.
- Definir el orden mediante `Comparable` o `Comparator`.
- Construir manualmente la interfaz Swing.
- Mantener separadas la lógica, los datos y la interfaz gráfica.

## 1. Modelo de datos

La clase `ec.edu.taller.TrabajoImpresion` ya contiene constructor, getters,
setters y `toString()`. Sus atributos son:

```java
private String codigo;
private String solicitante;
private String documento;
private int prioridad;
private String estado;
```

El constructor recibe código, solicitante, documento y prioridad. Establece
`PENDIENTE` como estado inicial. Al procesar el trabajo, el gestor debe cambiar
su estado a `PROCESADO`.

No es necesario modificar el modelo, salvo que elijas implementar `Comparable`
para establecer el orden de prioridad.

## 2. Gestor de trabajos

Completa la clase `ec.edu.taller.GestorImpresiones`.

### Constructor: `GestorImpresiones(int capacidadMaxima)`

Inicializa la cola y guarda la capacidad máxima recibida. La interfaz debe crear
el gestor con capacidad 10. Las pruebas también utilizan capacidades menores
para comprobar que se respeta el valor configurado.

### `boolean registrarTrabajo(TrabajoImpresion trabajo)`

Registra un trabajo y devuelve `true` si se acepta. Debe devolver `false` si:

- El trabajo es `null`.
- Su código es `null`, vacío o compuesto únicamente por espacios.
- Ya existe un trabajo pendiente con el mismo código, aunque sus otros datos
  sean diferentes.
- La cola alcanzó la capacidad máxima.

Un rechazo no debe reemplazar trabajos existentes, cambiar su prioridad ni
alterar la cantidad de pendientes o los espacios disponibles.

### `boolean existeTrabajo(String codigo)`

Devuelve `true` si el código pertenece a un trabajo pendiente; de lo contrario,
devuelve `false`. Los códigos nulos, vacíos o compuestos únicamente por espacios
devuelven `false`. La consulta no modifica la cola.

La comparación de códigos es exacta. No se solicita convertir mayúsculas ni
eliminar espacios de los extremos de códigos válidos.

### `TrabajoImpresion consultarSiguienteTrabajo()`

Devuelve el trabajo pendiente con el menor número de prioridad, sin retirarlo
ni cambiar su estado. Devuelve `null` si la cola está vacía.

### `TrabajoImpresion procesarSiguienteTrabajo()`

Retira y devuelve el trabajo con el menor número de prioridad y cambia su estado
a `PROCESADO`. Los demás pendientes conservan su estado. Si la cola está vacía,
devuelve `null`, sin alterar contadores ni espacios.

### `int contarTrabajosPendientes()`

Devuelve el número de trabajos que permanecen en la cola. Debe aumentar con
cada registro aceptado y disminuir al procesar un trabajo.

### `int consultarEspaciosDisponibles()`

Devuelve `capacidadMaxima - cantidadDePendientes`. Por ejemplo, con capacidad
10 y dos trabajos registrados, devuelve 8. Procesar un trabajo libera un
espacio que puede utilizarse para un nuevo registro.

### `List<TrabajoImpresion> listarTrabajos()`

Devuelve todos los trabajos pendientes ordenados de menor a mayor número de
prioridad. Si no hay pendientes, devuelve una lista vacía, nunca `null`.

Listar no debe retirar trabajos ni cambiar sus estados, contadores u orden de
procesamiento. La lista debe ser estructuralmente independiente de la cola:
vaciarla no puede vaciar la cola original. También se acepta una lista
inmodificable. No se exige copiar cada objeto de manera profunda.

**Atención:** el recorrido directo de una `PriorityQueue` no garantiza que todos
sus elementos aparezcan ordenados por prioridad.

## 3. Interfaz gráfica

Completa `ec.edu.taller.InterfazImpresiones` y utiliza los siguientes componentes:

| Componente | Uso sugerido |
| --- | --- |
| `JLabel` | Etiquetas y contadores de pendientes y espacios. |
| `JTextField` | Código del trabajo y nombre del documento. |
| `JComboBox` | Solicitante: ESTUDIANTE, DOCENTE o ADMINISTRATIVO. |
| `JSpinner` | Prioridad numérica, por ejemplo de 1 a 10. |
| `JTextArea` | Listado de trabajos y resultados de las operaciones. |
| `JButton` | Registrar, consultar siguiente, procesar siguiente y listar. |

La ventana debe utilizar un único gestor durante toda la ejecución y mostrar
mensajes comprensibles cuando se rechaza un registro o la cola está vacía.
Actualiza los contadores después de registrar y procesar trabajos.

## 4. Verificación y entrega

1. Abre la carpeta `proyectoPU202710` como proyecto Maven con JDK 17.
2. Completa la lógica del gestor y el criterio de prioridad.
3. Ejecuta las pruebas y corrige la implementación hasta que todas pasen.
4. Construye la interfaz e intégrala con el gestor.
5. Comprueba manualmente el ejemplo de prioridades, los duplicados, la cola llena
   y la cola vacía desde la ventana.
6. Sube el proyecto a GitHub, entrega su enlace en el aula virtual y llama al
   docente para la revisión y explicación del código.

Desde la raíz del proyecto:

```bash
mvn test
mvn exec:java
```

La aplicación Swing necesita un entorno con pantalla. El comando de ejecución
solo mostrará la ventana después de que implementes la interfaz.

**No modifiques las pruebas para hacerlas pasar.** La plantilla compila, pero
sus métodos de lógica están pendientes y por eso inicialmente fallarán pruebas.
Los casos y las observaciones de validación están en `src/test/README_PRUEBAS.md`.

## Calificación sugerida

| Criterio | Puntos |
| --- | ---: |
| Lógica y estructuras de datos: validaciones, prioridad, capacidad, estados y resultados correctos, con explicación del código. | 4 |
| Interfaz Swing manual integrada con el gestor y demostración de las operaciones. | 4 |
| Proyecto en GitHub y entrega del enlace en el aula virtual. | 2 |
| **Total** | **10** |

Se incluye un flujo opcional de GitHub Classroom con las siete categorías de
pruebas. Su máximo automático es 40 puntos y corresponde únicamente a la lógica:
divide ese resultado para 10 para obtener los 4 puntos de ese criterio.
La interfaz, la explicación y la entrega requieren revisión del docente.
