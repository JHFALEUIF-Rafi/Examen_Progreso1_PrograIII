# Casos de prueba — proyectoPU202710

La batería contiene **34 pruebas JUnit 5** y conserva siete categorías para la
evaluación automática. Los métodos de prueba tienen nombres que describen el
comportamiento esperado.

## Cobertura

| Clase | Casos | Comportamientos principales |
| --- | ---: | --- |
| `GestorImpresionesRegistrarTest` | 7 | Registro válido; rechazo de null; códigos nulos/vacíos/espacios; duplicados con datos diferentes; décimo aceptado y undécimo rechazado; capacidad configurable; reutilización del espacio. |
| `GestorImpresionesExisteTest` | 5 | Búsqueda de pendientes con distintas prioridades; cola vacía; código inexistente; consultas inválidas; exclusión del procesado. |
| `GestorImpresionesConsultarTest` | 4 | Menor número primero aunque llegue después; consultas repetidas sin retirar ni cambiar estado; cola vacía; actualización del siguiente tras registro y procesamiento. |
| `GestorImpresionesProcesarTest` | 4 | Orden 1, 3, 5 y estado PROCESADO; empates sin exigir desempate; preservación del estado de los restantes; cola vacía sin alterar contadores. |
| `GestorImpresionesContarTest` | 4 | Cero inicial; incremento con registros; estabilidad ante consultas/listados/rechazos; decremento hasta cero. |
| `GestorImpresionesEspaciosTest` | 4 | Capacidad inicial; reducción hasta cero; recuperación y reutilización; dos pendientes de diez dejan ocho espacios. |
| `GestorImpresionesListarTest` | 6 | Lista vacía no nula; listado completo ordenado; empates; independencia estructural de la lista; conservación de estados; exclusión de procesados. |
| **Total** | **34** | |

Los empates permiten cualquier orden entre trabajos de igual prioridad. No se
exige una implementación concreta del comparador ni una copia profunda de los
objetos. Se admite una lista de resultados inmodificable.

El listado con prioridades de entrada `5, 1, 4, 2, 3` ayuda a detectar el error
de copiar directamente los elementos de una `PriorityQueue` sin ordenarlos.

## Ejecución

Desde la raíz del proyecto, con JDK 17 y Maven:

```bash
mvn test
```

Para ejecutar una categoría:

```bash
mvn -Dtest=GestorImpresionesProcesarTest test
```

## Verificación de la plantilla

La plantilla y sus 34 pruebas **compilan con Java 17**. Los getters del modelo
están incluidos, por lo que el estudiante puede ejecutar las pruebas desde el
inicio. Los métodos del gestor contienen TODO y valores de retorno provisionales:
los fallos iniciales de comportamiento son esperados y deben resolverse
implementando la lógica, sin modificar las pruebas.

La batería fue ejecutada con JUnit Jupiter 5.10.2 sobre una implementación de
referencia temporal separada: **34 pruebas aprobadas, 0 fallidas**. Esa referencia
no forma parte del proyecto entregado. La comprobación local utilizó el
compilador de Java y JUnit Console; Maven no estaba instalado en el entorno de
preparación. El flujo de GitHub Classroom no se ejecutó en GitHub durante esta
revisión y la interfaz Swing queda pendiente de implementación y revisión manual.

## Límites

Las pruebas se concentran en el contrato de los siete métodos del gestor. No
sustituyen la revisión docente de `PriorityQueue`, el criterio de comparación,
la claridad del código ni la interfaz gráfica. No se agregan requisitos para
capacidades inválidas, archivos reales o una impresora física.
