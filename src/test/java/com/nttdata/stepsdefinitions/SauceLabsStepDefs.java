package com.nttdata.stepsdefinitions;

import com.nttdata.steps.SauceLabsSteps;
import com.nttdata.support.ScreenshotAttacher;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertTrue;

/**
 * Step definitions especificos del login de Sauce Labs.
 * <p>
 * NO contiene el step "Given ingreso al aplicativo...": ese step es
 * generico y vive en {@link AppConfigStepsDefs}, reutilizable para
 * cualquier app.
 */
public class SauceLabsStepDefs {

    private final SauceLabsSteps sauceLabsSteps = new SauceLabsSteps();

    private Scenario scenario;

    private int unidadesAgregadas;

    @Before
    public void configurarScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    @And("valido que carguen correctamente los productos en la galeria")
    public void valido_productos_en_la_galeria() {
        assertTrue("No se cargaron productos en la galeria", sauceLabsSteps.validarProductos());
        ScreenshotAttacher.attach(scenario, "Productos cargados");
    }

    @When("agrego {int} del siguiente producto {string}")
    public void agrego_del_siguiente_producto(int unidades, String producto) {

        this.unidadesAgregadas = unidades;
        sauceLabsSteps.ingresarDetalle(producto);
        ScreenshotAttacher.attach(scenario, "Detalle del producto: " + producto);

        sauceLabsSteps.agregarProducto(unidades);
        ScreenshotAttacher.attach(scenario, "Producto agregado al carrito");
    }

    @Then("valido el carrito de compra actualice correctamente")
    public void valido_el_carrito_compra_actualice_correctamente() {
        assertTrue("El carrito no muestra la cantidad esperada", sauceLabsSteps.validarCarrito(unidadesAgregadas)
        );
        ScreenshotAttacher.attach(scenario, "Carrito de compra");
    }
}
