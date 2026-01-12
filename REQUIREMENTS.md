# Enunciado
Se requiere implementar una aplicación java que exponga un api por http para cumplir los siguientes casos de uso:

## 1 Caché de puntos de venta
La aplicación deberá contener un cache en memoria de "puntos de venta", de los que se conoce su identificador numérico y el nombre de este.
Información inicial de puntos de venta:
Id, Nombre
1, CABA
2, GBA_1
3, GBA_2
4, Santa Fe
5, Córdoba
6, Misiones
7, Salta
8, Chubut
9, Santa Cruz
10, Catamarca

Para este cache de puntos de venta se requerirán endpoints HTTP para
(1) Recuperar todos los puntos de venta presentes en el cache
(2) Ingresar un nuevo punto de venta
(3) Actualizar un punto de venta
(4) Borrar un punto de venta

## 2 Caché de costos entre los puntos de venta
También se deberá poseer un caché en memoria que guarde el costo (numérico) de ir de un punto de venta a otro. Se comprende que
- el costo nunca podría ser menor a cero
- el costo de ir a un punto de venta a si mismo es irrisorio, 0
- el costo de ir del punto de venta A al punto de venta B siempre será igual a hacer el camino inverso.
- no todos los puntos de venta están directamente conectados, y puede que un punto de venta B sea inalcanzable desde un punto A
- si un punto de venta A está comunicado directamente con un punto de venta B ese camino es único y no se posee un camino directo paralelo
- el camino más directo entre dos puntos puede no ser el menos costoso

Información inicial de costos entre puntos de venta:
IdA, IdB, Costo
1,2,2
1,3,3
2,3,5
2,4,10
1,4,11
4,5,5
2,5,14
6,7,32
8,9,11
10,7,5
3,8,10
5,8,30
10,5,5
4,6,6

Para este cache de costos entre puntos de venta se requerirán endpoints HTTP para
(1) Cargar un nuevo costo entre un punto de venta A y un punto de venta B (crearía un camino directo entre A y B con el costo indicado)
(2) Remover un costo entre un punto de venta A y un punto de venta B (removería en caso de existir un camino directo entre A y B)
(3) Consultar los puntos de venta directamente a un punto de venta A, y los costos que implica llegar a ellos
(4) Consultar el camino con costo mínimo entre dos puntos de venta A y B. (indicar el costo mínimo, y el camino realizado, aprovechando los nombres de los puntos de venta del caché del punto anterior)

## 3 Acreditaciones
Además del control de cache de los puntos de venta se requiere otro end point http que reciba "acreditaciones".
La información relevante de la acreditación que se recibirá de forma externa es un importe y un identificador de punto de venta.
Con esa información provista debemos enriquecer esta acreditación con
(1) la fecha en la que se recibió el pedido
(2) el nombre del punto de venta que le corresponde consultando el cache en memoria, o fallando si el punto de venta no existe

Esta información enriquecida deberá ser persistida en una BBDD (preferentemente no una en memoria, y que sea externa a la aplicación) los atributos que deben persistirse son
- importe
- identificador de punto de venta
- fecha de recepción
- nombre del punto de venta

Esta misma información deberá poder ser consultable a través de otro end point http.


Tópicos Técnicos:

-	Java 21/24
-	Spring 6 / SpringBoot 3 – APIs Restfull – CRUD
-	Testing / Coverage
-	Swagger 3
-	Arquitectura de Micro Servicios
-	Principales Patrones y componentes del ecosistema: Resiliencia, Tracing, Observabilidad, etc
-	Comunicación Sincrona y Asincrona
-	Caching
-	Docker