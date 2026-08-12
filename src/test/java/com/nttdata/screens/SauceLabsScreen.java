package com.nttdata.screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class SauceLabsScreen {
    private static final Duration TIMEOUT = Duration.ofSeconds(20);

    private static final By GALERIA_PRODUCTOS = AppiumBy.id("com.saucelabs.mydemoapp.android:id/productRV");
    private static final By NOMBRE_PRODUCTO = AppiumBy.id("com.saucelabs.mydemoapp.android:id/titleTV");
    private static final By BOTON_AUMENTAR_CANTIDAD = AppiumBy.id("com.saucelabs.mydemoapp.android:id/plusIV");
    private static final By CANTIDAD_PRODUCTO = AppiumBy.id("com.saucelabs.mydemoapp.android:id/noTV");
    private static final By BOTON_AGREGAR_CARRITO = AppiumBy.id("com.saucelabs.mydemoapp.android:id/cartBt");
    private static final By CANTIDAD_CARRITO = AppiumBy.id("com.saucelabs.mydemoapp.android:id/cartTV");

    public boolean validarProductos() {
        AndroidDriver driver = AppConfigScreen.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(GALERIA_PRODUCTOS)
        );

        return !driver.findElements(NOMBRE_PRODUCTO).isEmpty();
    }

    public void ingresarDetalle(String producto) {
        AndroidDriver driver = AppConfigScreen.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        By productoSeleccionado = AppiumBy.accessibilityId(producto);

        wait.until(ExpectedConditions.elementToBeClickable(productoSeleccionado)).click();
    }
    public void agregarProducto(int unidades) {
        AndroidDriver driver = AppConfigScreen.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        deslizarHastaBotonAgregar();

        try{
                wait.until(ExpectedConditions.visibilityOfElementLocated(CANTIDAD_PRODUCTO));

        for (int i = 1; i < unidades; i++) {
            wait.until(
                    ExpectedConditions.elementToBeClickable(BOTON_AUMENTAR_CANTIDAD)
            ).click();
        }

        wait.until(
                ExpectedConditions.textToBe(CANTIDAD_PRODUCTO, String.valueOf(unidades))
        );

        wait.until(
                ExpectedConditions.elementToBeClickable(BOTON_AGREGAR_CARRITO)
        ).click();
        } catch (TimeoutException e) {

                System.err.println(
                        "[ERROR TECNICO] " + e.getMessage()
                );

                throw new AssertionError(
                        "No fue posible completar la operación con el producto. " +
                        "La aplicación dejó de responder durante la interacción."
                );
         }
    }

    private void deslizarHastaBotonAgregar() {
        AndroidDriver driver = AppConfigScreen.getDriver();

        driver.executeScript(
                "mobile: swipeGesture",
                java.util.Map.of(
                        "left", 100,
                        "top", 600,
                        "width", 900,
                        "height", 1200,
                        "direction", "up",
                        "percent", 0.75
                )
        );
    }

    public boolean validarCarrito(int unidades) {
    AndroidDriver driver = AppConfigScreen.getDriver();
    WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

    try {
        return wait.until(
                ExpectedConditions.textToBe(
                        CANTIDAD_CARRITO,
                        String.valueOf(unidades)
                )
        );

    } catch (TimeoutException e) {

        String cantidadActual = driver
                .findElement(CANTIDAD_CARRITO)
                .getText();

        throw new AssertionError(
                "Validación de carrito fallida. " +
                "Cantidad esperada: " + unidades +
                ". Cantidad obtenida: " + cantidadActual
        );
    }
}

}
