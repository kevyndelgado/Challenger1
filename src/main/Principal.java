package main;

import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Principal {
    public static void main(String[] args) {
        File input = new File("resources/entrada.json");
        String fechaCreacion, fechaFin;
        int id;
        JsonArray fechas, fechasFaltantes;

        try {
            JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
            JsonObject fileObject = fileElement.getAsJsonObject();
            JsonObject fileResult = new JsonObject();

            id = fileObject.get("id").getAsInt();
            fechaCreacion = fileObject.get("fechaCreacion").getAsString();
            fechaFin = fileObject.get("fechaFin").getAsString();
            fechas = fileObject.get("fechas").getAsJsonArray();

            fechasFaltantes = getFechasFaltantes(fechaCreacion, fechaFin, fechas);

            fileResult.addProperty("id", id);
            fileResult.addProperty("fechaCreacion", fechaCreacion);
            fileResult.addProperty("fechaFin", fechaFin);
            fileResult.add("fechasFaltantes", fechasFaltantes);

            try {
                FileWriter file = new FileWriter("resources/salida.json");
                file.write(fileResult.toString().replace("\\", ""));
                file.flush();
                file.close();
            } catch (Exception ex) {
                System.out.println("Error al escribir en el archivo de sálida: " + ex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JsonArray getFechasFaltantes(String fechaCreacion, String fechaFin, JsonArray fechas) {
        JsonArray result = new JsonArray();

        LocalDate fechaEval = LocalDate.parse(fechaCreacion);
        new JsonPrimitive(fechaEval.toString());
        JsonElement date;
        int cont = 0;

        while (fechaEval.isBefore(LocalDate.parse(fechaFin))) {
            date = new JsonPrimitive(fechaEval.toString());
            if (!fechas.contains(date)) {
                result.add(date);
            }
            fechaEval = fechaEval.plusDays(31);
            fechaEval = fechaEval.parse(fechaEval.withDayOfMonth(1).toString());
        }
        return result;
    }
}