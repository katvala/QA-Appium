# language: es

@AddItems
Característica: Validar funcionalidad del carrito de compras

  Esquema del escenario: Validar producto agregado al carrito
    Dado ingreso al aplicativo "SauceLabs"
    Y valido que carguen correctamente los productos en la galeria
    Cuando agrego <UNIDADES> del siguiente producto "<PRODUCTO>"
    Entonces valido el carrito de compra actualice correctamente

    Ejemplos:
      | PRODUCTO                  | UNIDADES |
      | Sauce Labs Backpack       | 1        |
      | Sauce Labs Bolt T-Shirt   | 1        |
      | Sauce Labs Bike Light     | 2        |