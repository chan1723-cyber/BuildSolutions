package com.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.json.*;

public class App {

    // 🔹 1. Leer el archivo JSON desde un .txt
    public static String leerArchivo(String rutaArchivo) {
        try {
            return new String(Files.readAllBytes(Paths.get(rutaArchivo)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    

    // 🔹 2. Obtener transacciones de un usuario específico
    public static List<JSONObject> obtenerTransacciones(String jsonData, String usuario) {
        List<JSONObject> transacciones = new ArrayList<>();
        
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if (jsonObject.has(usuario)) {
                JSONArray array = jsonObject.getJSONArray(usuario);
                for (int i = 0; i < array.length(); i++) {
                    transacciones.add(array.getJSONObject(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    
        return transacciones;
    }
    

    // 🔹 3. Generar extracto bancario en un archivo .txt
    public static void generarExtracto(String usuario, List<JSONObject> transacciones) {
        String carpetaDestino = "target/extractos";
        File directorio = new File(carpetaDestino);
    
        if (!directorio.exists() && !directorio.mkdirs()) {
            throw new RuntimeException("❌ No se pudo crear el directorio: " + carpetaDestino);
        }
    
        String nombreArchivo = carpetaDestino + "/extracto_" + usuario.replace("@", "_").replace(".", "_") + ".txt";
        File archivo = new File(nombreArchivo);
    
        System.out.println("📂 Intentando escribir en: " + archivo.getAbsolutePath());
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            writer.write("Extracto Bancario - " + usuario + "\n");
            writer.write("====================================\n");
    
            for (JSONObject transaccion : transacciones) {
                writer.write("Tipo: " + transaccion.getString("type") + "\n");
                writer.write("Monto: " + transaccion.getString("balance") + "\n");
                writer.write("Fecha: " + transaccion.getString("timestamp") + "\n");
                writer.write("------------------------------------\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("❌ Error al escribir el extracto bancario", e);
        }
    
        // ✅ Verificación final
        if (!archivo.exists()) {
            throw new RuntimeException("❌ Error: El archivo no se generó correctamente en " + archivo.getAbsolutePath());
        }
    
        System.out.println("✅ Extracto generado en: " + archivo.getAbsolutePath());
    }    
    
    public static void main(String[] args) {
        String rutaArchivo = "src/resources/transactions.txt";
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese su correo electrónico: ");
        String usuario = scanner.nextLine();
        scanner.close();
    
        String jsonData = leerArchivo(rutaArchivo);
        if (jsonData != null) {
            List<JSONObject> transacciones = obtenerTransacciones(jsonData, usuario);
            if (!transacciones.isEmpty()) {
                generarExtracto(usuario, transacciones);
            } else {
                System.out.println("⚠ No se encontraron transacciones para este usuario.");
            }
        } else {
            System.out.println("❌ Error al leer el archivo de transacciones.");
        }
    }
    
}