package com.example;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void testLeerArchivo() {
        String rutaArchivo = "src/resources/transactions.txt";
        String contenido = App.leerArchivo(rutaArchivo);

        assertNotNull(contenido, "El contenido del archivo no debe ser nulo");
        assertFalse(contenido.isEmpty(), "El archivo no debe estar vacío");
        assertTrue(contenido.contains("juan.jose@urosario.edu.co"), "Debe contener el correo del usuario");
    }

    @Test
    public void testObtenerTransacciones() {
        String jsonData = "{ \"juan.jose@urosario.edu.co\": [ " +
                        "{\"balance\": \"50\", \"type\": \"Deposit\", \"timestamp\": \"2025-02-11 14:17:21.921536\"}, " +
                        "{\"balance\": \"-20\", \"type\": \"Withdrawal\", \"timestamp\": \"2025-02-15 10:30:15.123456\"} " +
                        "] }";

        List<JSONObject> transacciones = App.obtenerTransacciones(jsonData, "juan.jose@urosario.edu.co");

        assertNotNull(transacciones, "La lista de transacciones no debe ser nula");
        assertEquals(2, transacciones.size(), "Debe haber 2 transacciones");
        assertEquals("50", transacciones.get(0).getString("balance"), "El balance de la primera transacción debe ser 50");
        assertEquals("Deposit", transacciones.get(0).getString("type"), "El tipo de la primera transacción debe ser Deposit");
    }

    @Test
    public void testGenerarExtracto() {
        String usuario = "juan.jose@urosario.edu.co";
        List<JSONObject> transacciones = List.of(
            new JSONObject().put("balance", "50").put("type", "Deposit").put("timestamp", "2025-02-11 14:17:21.921536"),
            new JSONObject().put("balance", "-20").put("type", "Withdrawal").put("timestamp", "2025-02-15 10:30:15.123456")
        );

        // Generamos el extracto
        App.generarExtracto(usuario, transacciones);

        // Ruta correcta del archivo esperado
        String carpetaDestino = "target/extractos";
        String nombreArchivo = carpetaDestino + "/extracto_juan_jose_urosario_edu_co.txt";
        File archivo = new File(nombreArchivo);

        // Asegurar que el archivo se genera
        assertTrue(archivo.exists(), "El archivo de extracto debe generarse en la ubicación correcta");
        assertTrue(archivo.length() > 0, "El archivo no debe estar vacío");

        // Limpiar después de la prueba
        archivo.delete();
    }

}