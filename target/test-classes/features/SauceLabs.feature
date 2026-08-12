# language: es

@AddItems
Característica: Validar funcionalidad del carrito de compras

  Escenario: Agregar una mochila al carrito
    Dado ingreso al aplicativo "SauceLabs"
    Y valido que carguen correctamente los productos en la galeria
    Cuando agrego 1 del siguiente producto "Sauce Labs Backpack"
    Entonces valido el carrito de compra actualice correctamente
