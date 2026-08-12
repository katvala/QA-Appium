package com.nttdata.steps;

import com.nttdata.screens.SauceLabsScreen;

public class SauceLabsSteps {
    SauceLabsScreen sauceLabsScreen = new SauceLabsScreen();

    public boolean validarProductos(){
        System.out.println("[SauceLabsSteps] validarProductos");
        return sauceLabsScreen.validarProductos();
    }

    public void ingresarDetalle(String producto) {
        System.out.println("[SauceLabsSteps] ingresarDetalle: " + producto);
        sauceLabsScreen.ingresarDetalle(producto);
    }

    public void agregarProducto(int unidades) {
        System.out.println("[SauceLabsSteps] agregarProducto: " + unidades);
        sauceLabsScreen.agregarProducto(unidades);
    }

    public boolean validarCarrito(int unidadesAgregadas){
        System.out.println("[SauceLabsSteps] validarCarrito");
        return sauceLabsScreen.validarCarrito(unidadesAgregadas);
    }
}
