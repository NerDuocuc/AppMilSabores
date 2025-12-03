package com.service.pasteleriamilsabores.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.Period;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * NOTE: This generator uses Spring Security's BCryptPasswordEncoder which
 * is available because the project depends on Spring Security. Running
 * this class via `mvn -DskipTests package` will compile it with the
 * project's classpath.
 */

public class SqlSeedGenerator {
    private static final ObjectMapper M = new ObjectMapper();
    private static final BCryptPasswordEncoder BCRYPT = new BCryptPasswordEncoder();

    public static void main(String[] args) throws Exception {
        File out = new File("target/seed.sql");
        out.getParentFile().mkdirs();
        try (Writer w = new OutputStreamWriter(new FileOutputStream(out), StandardCharsets.UTF_8)) {
            w.write("-- SQL seed generated from JSON\n");
            w.write("SET NAMES utf8mb4;\n");
            w.write("USE pasteleria;\n\n");

            writeBlogs(w);
            writeRecetas(w);
            writeRegiones(w);
            writeCategoriasAndProductos(w);
            writeUsersAndAddresses(w);

            w.flush();
        }
        System.out.println("Seed SQL generated to " + out.getAbsolutePath());
    }

    private static void writeBlogs(Writer w) throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/service/data/blogs.json");
        if (is == null) return;
        JsonNode arr = M.readTree(is);
        for (JsonNode n : arr) {
            String id = esc(n.path("id").asText(""));
            String titulo = esc(n.path("titulo").asText(""));
            String resumen = esc(n.path("resumen").asText(""));
            String imagen = esc(n.path("imagen").asText(""));
            String fecha = esc(n.path("fecha").asText(""));
            String autor = esc(n.path("autor").asText(""));
            String link = esc(n.path("link").asText(""));
            w.write(String.format("INSERT INTO blogs (id,titulo,resumen,imagen,fecha,autor,link) VALUES ('%s','%s','%s','%s','%s','%s','%s');\n",
                    id,titulo,resumen,imagen,fecha,autor,link));
        }
        w.write("\n");
    }

    private static void writeRecetas(Writer w) throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/service/data/recetas.json");
        if (is == null) return;
        JsonNode arr = M.readTree(is);
        for (JsonNode n : arr) {
            String id = esc(n.path("id").asText(""));
            String titulo = esc(n.path("titulo").asText(""));
            String resumen = esc(n.path("resumen").asText(""));
            String ingredientes = esc(n.path("ingredientes").asText(""));
            String preparacion = esc(n.path("preparacion").asText(""));
            String imagen = esc(n.path("imagen").asText(""));
            String badge = esc(n.path("badge").asText(""));
            String badgeClass = esc(n.path("badgeClass").asText(""));
            w.write(String.format("INSERT INTO recetas (id,titulo,resumen,ingredientes,preparacion,imagen,badge,badge_class) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s');\n",
                    id,titulo,resumen,ingredientes,preparacion,imagen,badge,badgeClass));
        }
        w.write("\n");
    }

    private static void writeRegiones(Writer w) throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/service/data/regionesComunas/regionesComunas.json");
        if (is == null) return;
        JsonNode arr = M.readTree(is);
        for (JsonNode n : arr) {
            String id = esc(n.path("id").asText(""));
            String region = esc(n.path("region").asText(""));
            w.write(String.format("INSERT INTO regiones_comunas (id,region) VALUES ('%s','%s');\n", id, region));
            JsonNode cArr = n.path("comunas");
            if (cArr.isArray()) {
                for (JsonNode c : cArr) {
                    String comuna = esc(c.asText(""));
                    w.write(String.format("INSERT INTO regiones_comunas_comunas (region_id,comuna) VALUES ('%s','%s');\n", id, comuna));
                }
            }
        }
        w.write("\n");
    }

    private static void writeCategoriasAndProductos(Writer w) throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/service/data/products/productos.json");
        if (is == null) return;
        JsonNode root = M.readTree(is);
        JsonNode categorias = root.path("categorias");
        if (!categorias.isArray()) return;
        int tempCatId = 1000;
        for (JsonNode catNode : categorias) {
            String nombreCat = esc(catNode.path("nombre_categoria").asText(""));
            int catId = tempCatId++;
            w.write(String.format("INSERT INTO categorias (id_categoria,nombre_categoria) VALUES (%d,'%s');\n", catId, nombreCat));
            JsonNode productos = catNode.path("productos");
            if (productos.isArray()) {
                for (JsonNode pnode : productos) {
                    String codigo = esc(pnode.path("codigo_producto").asText(""));
                    String nombre = esc(pnode.path("nombre_producto").asText(""));
                    String precio = pnode.has("precio_producto") && !pnode.path("precio_producto").isNull() ? pnode.path("precio_producto").asText() : "NULL";
                    String descripcion = esc(pnode.path("descripción_producto").asText(""));
                    String imagen = esc(pnode.path("imagen_producto").asText(""));
                    String stock = pnode.has("stock") && !pnode.path("stock").isNull() ? pnode.path("stock").asText() : "NULL";
                    String stockCrit = pnode.has("stock_critico") && !pnode.path("stock_critico").isNull() ? pnode.path("stock_critico").asText() : "NULL";
                    w.write(String.format("INSERT INTO productos (codigo_producto,nombre_producto,precio_producto,descripcion_producto,imagen_producto,stock,stock_critico,categoria_id) VALUES ('%s','%s',%s,'%s','%s',%s,%s,%d);\n",
                            codigo,nombre,precio,descripcion,imagen,stock,stockCrit,catId));
                }
            }
        }
        w.write("\n");
    }

    private static void writeUsersAndAddresses(Writer w) throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/service/data/users/users.json");
        if (is == null) return;
        JsonNode arr = M.readTree(is);
        for (JsonNode n : arr) {
            String run = esc(n.path("run").asText(""));
            String nombre = esc(n.path("nombre").asText(""));
            String apellidos = esc(n.path("apellidos").asText(""));
            String correo = esc(n.path("correo").asText(""));
            String telefonoRaw = n.has("telefono") && !n.path("telefono").isNull() ? n.path("telefono").asText(null) : null;
            String telefonoSql = (telefonoRaw == null || telefonoRaw.isEmpty()) ? "NULL" : "'" + esc(telefonoRaw) + "'";
            String fechaNacimiento = esc(n.path("fechaNacimiento").asText(""));
            String tipoUsuario = esc(n.path("tipoUsuario").asText(""));
            String rawPassword = n.path("password").asText("");
            String hashedPassword;
            if (rawPassword != null && !rawPassword.isEmpty()) {
                try {
                    hashedPassword = BCRYPT.encode(rawPassword);
                } catch (Exception ex) {
                    // fallback to plain if encoder not available (very unlikely)
                    hashedPassword = rawPassword;
                }
            } else {
                hashedPassword = BCRYPT.encode("changeme");
            }
            // compute or take provided values
            Integer discountVal = null;
            if (n.has("discount_percent") && !n.path("discount_percent").isNull()) {
                try { discountVal = n.path("discount_percent").asInt(); } catch (Exception ex) { }
            }
            Integer lifetimeVal = null;
            if (n.has("lifetime_discount_percent") && !n.path("lifetime_discount_percent").isNull()) {
                try { lifetimeVal = n.path("lifetime_discount_percent").asInt(); } catch (Exception ex) { }
            }
            Boolean freeCakeVal = null;
            if (n.has("free_cake_eligible") && !n.path("free_cake_eligible").isNull()) {
                freeCakeVal = n.path("free_cake_eligible").asBoolean();
            }

            String registrationCode = n.has("registration_code") && !n.path("registration_code").isNull() ? n.path("registration_code").asText(null) : null;
            String registrationDate = n.has("registration_date") && !n.path("registration_date").isNull() ? n.path("registration_date").asText(null) : null;

            // Auto-fill derived fields when missing
            if (discountVal == null && fechaNacimiento != null && !fechaNacimiento.isEmpty()) {
                try {
                    LocalDate dob = LocalDate.parse(fechaNacimiento);
                    int age = Period.between(dob, LocalDate.now()).getYears();
                    if (age >= 50) discountVal = 50;
                } catch (DateTimeParseException ex) {
                    // ignore
                }
            }
            if (freeCakeVal == null && correo != null && correo.toLowerCase().contains("duoc.cl")) {
                freeCakeVal = Boolean.TRUE;
            }
            if (lifetimeVal == null && registrationCode != null && "FELICES50".equalsIgnoreCase(registrationCode.trim())) {
                lifetimeVal = 10;
            }
            if (registrationCode != null && (registrationDate == null || registrationDate.isEmpty())) {
                registrationDate = LocalDate.now().toString();
            }

                String discountSql = discountVal == null ? "NULL" : discountVal.toString();
                String lifetimeSql = lifetimeVal == null ? "NULL" : lifetimeVal.toString();
                String freeCakeSql = freeCakeVal == null ? "NULL" : (freeCakeVal ? "1" : "0");
                String regCodeSql = registrationCode == null ? "NULL" : "'" + esc(registrationCode) + "'";
                String regDateSql = (registrationDate == null || registrationDate.isEmpty()) ? "NULL" : "'" + esc(registrationDate) + "'";

                w.write(String.format("INSERT INTO usuarios (run,nombre,apellidos,correo,telefono,fecha_nacimiento,tipo_usuario,password,porcentaje_descuento,porcentaje_descuento_permanente,torta_gratis_elegible,codigo_registro,fecha_registro) VALUES ('%s','%s','%s','%s',%s,'%s','%s','%s',%s,%s,%s,%s,%s);\n",
                    run,nombre,apellidos,correo,telefonoSql,fechaNacimiento,tipoUsuario, esc(hashedPassword),discountSql,lifetimeSql,freeCakeSql,regCodeSql,regDateSql));

            JsonNode addresses = n.path("addresses");
            if (addresses.isArray()) {
                for (JsonNode a : addresses) {
                    String id = esc(a.path("id").asText(""));
                    String address = esc(a.path("address").asText(""));
                    String region = esc(a.path("region").asText(""));
                    String comuna = esc(a.path("comuna").asText(""));
                    w.write(String.format("INSERT INTO direcciones (id,direccion,region,comuna,usuario_run) VALUES ('%s','%s','%s','%s','%s');\n",
                            id,address,region,comuna,run));
                }
            }
            JsonNode cards = n.path("cards");
            if (cards.isArray()) {
                for (JsonNode c : cards) {
                    String cardId = esc(c.path("id").asText(""));
                    String cardNumber = esc(c.path("numero_tarjeta").asText(""));
                    String cardMes = c.has("mes") && !c.path("mes").isNull() ? c.path("mes").asText() : "NULL";
                    String cardAnio = c.has("anio") && !c.path("anio").isNull() ? c.path("anio").asText() : "NULL";
                    String cardNombre = esc(c.path("nombre").asText(""));
                    w.write(String.format("INSERT INTO tarjetas (id,numero_tarjeta,mes,anio,nombre,usuario_run) VALUES ('%s','%s',%s,%s,'%s','%s');\n",
                            cardId,cardNumber,cardMes,cardAnio,cardNombre,run));
                }
            }
        }
        w.write("\n");
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("'","''");
    }
}
