package com.service.pasteleriamilsabores.config;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.pasteleriamilsabores.models.Address;
import com.service.pasteleriamilsabores.models.Blog;
import com.service.pasteleriamilsabores.models.Categoria;
import com.service.pasteleriamilsabores.models.Producto;
import com.service.pasteleriamilsabores.models.Receta;
import com.service.pasteleriamilsabores.models.RegionComuna;
import com.service.pasteleriamilsabores.models.User;
import com.service.pasteleriamilsabores.models.Card;
import com.service.pasteleriamilsabores.models.UserType;
import com.service.pasteleriamilsabores.repository.BlogRepository;
import com.service.pasteleriamilsabores.repository.CategoriaRepository;
import com.service.pasteleriamilsabores.repository.ProductoRepository;
import com.service.pasteleriamilsabores.repository.RecetaRepository;
import com.service.pasteleriamilsabores.repository.RegionComunaRepository;
import com.service.pasteleriamilsabores.repository.UserRepository;
import com.service.pasteleriamilsabores.repository.CardRepository;

@Component
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true", matchIfMissing = true)
public class DataLoader implements CommandLineRunner {

    @org.springframework.beans.factory.annotation.Autowired
    private org.springframework.core.env.Environment env;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private RegionComunaRepository regionComunaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    @Override
    public void run(String... args) throws Exception {
        String seedProp = env != null ? env.getProperty("app.seed.enabled") : null;
        log.info("DataLoader starting (app.seed.enabled={})", seedProp);
        try {
            loadBlogs();
            loadRecetas();
            loadRegiones();
            loadProductsAndCategorias();
            loadUsers();
            log.info("DataLoader finished successfully");
        } catch (Exception e) {
            log.error("DataLoader failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    private InputStream resource(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    private void loadBlogs() throws Exception {
        InputStream is = resource("com/service/data/blogs.json");
        if (is == null) return;
        JsonNode arr = mapper.readTree(is);
        for (JsonNode n : arr) {
            String id = n.path("id").asText(null);
            if (id == null || blogRepository.existsById(id)) continue;
            Blog b = new Blog();
            b.setId(id);
            b.setTitulo(n.path("titulo").asText(null));
            b.setResumen(n.path("resumen").asText(null));
            b.setImagen(n.path("imagen").asText(null));
            b.setFecha(n.path("fecha").asText(null));
            b.setAutor(n.path("autor").asText(null));
            b.setLink(n.path("link").asText(null));
            blogRepository.save(b);
        }
    }

    private void loadRecetas() throws Exception {
        InputStream is = resource("com/service/data/recetas.json");
        if (is == null) return;
        JsonNode arr = mapper.readTree(is);
        for (JsonNode n : arr) {
            String id = n.path("id").asText(null);
            if (id == null || recetaRepository.existsById(id)) continue;
            Receta r = new Receta();
            r.setId(id);
            r.setTitulo(n.path("titulo").asText(null));
            r.setResumen(n.path("resumen").asText(null));
            r.setIngredientes(n.path("ingredientes").asText(null));
            r.setPreparacion(n.path("preparacion").asText(null));
            r.setImagen(n.path("imagen").asText(null));
            r.setBadge(n.path("badge").asText(null));
            r.setBadgeClass(n.path("badgeClass").asText(null));
            recetaRepository.save(r);
        }
    }

    private void loadRegiones() throws Exception {
        InputStream is = resource("com/service/data/regionesComunas/regionesComunas.json");
        if (is == null) return;
        JsonNode arr = mapper.readTree(is);
        for (JsonNode n : arr) {
            String id = n.path("id").asText(null);
            if (id == null || regionComunaRepository.existsById(id)) continue;
            RegionComuna rc = new RegionComuna();
            rc.setId(id);
            rc.setRegion(n.path("region").asText(null));
            List<String> comunas = new ArrayList<>();
            JsonNode cArr = n.path("comunas");
            if (cArr.isArray()) {
                for (JsonNode c : cArr) comunas.add(c.asText());
            }
            rc.setComunas(comunas);
            regionComunaRepository.save(rc);
        }
    }

    private void loadProductsAndCategorias() throws Exception {
        InputStream is = resource("com/service/data/products/productos.json");
        if (is == null) return;
        JsonNode root = mapper.readTree(is);
        JsonNode categorias = root.path("categorias");
        if (!categorias.isArray()) return;
        for (JsonNode catNode : categorias) {
            String nombreCat = catNode.path("nombre_categoria").asText(null);
            if (nombreCat == null) continue;
            // try find existing category by name
            List<Categoria> found = categoriaRepository.findAll();
            Categoria categoria = found.stream().filter(c -> nombreCat.equals(c.getNombreCategoria())).findFirst().orElse(null);
            if (categoria == null) {
                categoria = new Categoria();
                categoria.setNombreCategoria(nombreCat);
                categoria = categoriaRepository.save(categoria);
            }

            JsonNode productos = catNode.path("productos");
            if (productos.isArray()) {
                for (JsonNode pnode : productos) {
                    String codigo = pnode.path("codigo_producto").asText(null);
                    if (codigo == null || productoRepository.existsById(codigo)) continue;
                    Producto p = new Producto();
                    p.setCodigoProducto(codigo);
                    p.setNombreProducto(pnode.path("nombre_producto").asText(null));
                    p.setPrecioProducto(pnode.path("precio_producto").isInt() ? pnode.path("precio_producto").asInt() : null);
                    // description key has accent in JSON
                    p.setDescripcionProducto(pnode.path("descripción_producto").asText(null));
                    p.setImagenProducto(pnode.path("imagen_producto").asText(null));
                    p.setStock(pnode.path("stock").isInt() ? pnode.path("stock").asInt() : null);
                    p.setStockCritico(pnode.path("stock_critico").isInt() ? pnode.path("stock_critico").asInt() : null);
                    p.setCategoria(categoria);
                    productoRepository.save(p);
                }
            }
        }
    }

    private void loadUsers() throws Exception {
        InputStream is = resource("com/service/data/users/users.json");
        if (is == null) return;
        JsonNode arr = mapper.readTree(is);
        for (JsonNode n : arr) {
            String run = n.path("run").asText(null);
            if (run == null) continue;
            JsonNode cardsNode = n.path("cards");
            if (userRepository.existsById(run)) {
                ensureCardsForExistingUser(userRepository.getReferenceById(run), cardsNode);
                continue;
            }
            User u = new User();
            u.setRun(run);
            u.setNombre(n.path("nombre").asText(null));
            u.setApellidos(n.path("apellidos").asText(null));
            u.setCorreo(n.path("correo").asText(null));
            String telefonoTexto = n.has("telefono") ? n.path("telefono").asText(null) : null;
            if (telefonoTexto != null && telefonoTexto.isEmpty()) {
                telefonoTexto = null;
            }
            u.setTelefono(telefonoTexto);
            u.setFechaNacimiento(n.path("fechaNacimiento").asText(null));
            u.setTipoUsuario(UserType.fromString(n.path("tipoUsuario").asText(null)));
            u.setPassword(n.path("password").asText(null));

            // optional extra fields present in enhanced JSON
            // discount percent
            if (n.has("discount_percent") && !n.path("discount_percent").isNull()) {
                try {
                    u.setDiscountPercent(n.path("discount_percent").isInt() ? n.path("discount_percent").asInt() : Integer.valueOf(n.path("discount_percent").asText()));
                } catch (NumberFormatException ex) {
                    // ignore malformed number
                }
            }
            if (n.has("lifetime_discount_percent") && !n.path("lifetime_discount_percent").isNull()) {
                try {
                    u.setLifetimeDiscountPercent(n.path("lifetime_discount_percent").isInt() ? n.path("lifetime_discount_percent").asInt() : Integer.valueOf(n.path("lifetime_discount_percent").asText()));
                } catch (NumberFormatException ex) {
                    // ignore malformed number
                }
            }
            if (n.has("free_cake_eligible") && !n.path("free_cake_eligible").isNull()) {
                u.setFreeCakeEligible(n.path("free_cake_eligible").asBoolean(false));
            }
            if (n.has("registration_code") && !n.path("registration_code").isNull()) {
                String rc = n.path("registration_code").asText(null);
                if (rc != null && !rc.isEmpty()) u.setRegistrationCode(rc);
            }
            if (n.has("registration_date") && !n.path("registration_date").isNull()) {
                String rd = n.path("registration_date").asText(null);
                if (rd != null && !rd.isEmpty()) {
                    try {
                        u.setRegistrationDate(LocalDate.parse(rd));
                    } catch (java.time.format.DateTimeParseException ex) {
                        // ignore parse errors
                    }
                }
            }

            // Auto-fill computed fields when missing
            try {
                // compute age-based discount if not present
                if (u.getDiscountPercent() == null && u.getFechaNacimiento() != null) {
                    try {
                        LocalDate dob = LocalDate.parse(u.getFechaNacimiento());
                        int age = java.time.Period.between(dob, LocalDate.now()).getYears();
                        if (age >= 50) u.setDiscountPercent(50);
                    } catch (Exception ex) {
                        // ignore parse errors
                    }
                }

                // Duoc students are eligible for free cake flag
                if (u.getFreeCakeEligible() == null && u.getCorreo() != null) {
                    if (u.getCorreo().toLowerCase().contains("duoc.cl")) {
                        u.setFreeCakeEligible(Boolean.TRUE);
                    }
                }

                // lifetime discount from registration code
                if (u.getLifetimeDiscountPercent() == null && u.getRegistrationCode() != null) {
                    if ("FELICES50".equalsIgnoreCase(u.getRegistrationCode().trim())) {
                        u.setLifetimeDiscountPercent(10);
                    }
                }

                // if registration code present but registration date missing, set now
                if (u.getRegistrationCode() != null && u.getRegistrationDate() == null) {
                    u.setRegistrationDate(LocalDate.now());
                }
            } catch (RuntimeException ex) {
                // defensive: don't fail the loader for any calculation issue
                log.debug("Non-fatal error computing derived user fields for run {}: {}", run, ex.getMessage());
            }

            // addresses
            JsonNode addresses = n.path("addresses");
            if (addresses.isArray()) {
                List<Address> list = new ArrayList<>();
                for (JsonNode a : addresses) {
                    Address addr = new Address();
                    addr.setId(a.path("id").asText(null));
                    addr.setAddress(a.path("address").asText(null));
                    addr.setRegion(a.path("region").asText(null));
                    addr.setComuna(a.path("comuna").asText(null));
                    list.add(addr);
                }
                u.setAddresses(list);
            }

            if (cardsNode.isArray()) {
                List<Card> cardList = new ArrayList<>();
                for (JsonNode c : cardsNode) {
                    Card card = new Card();
                    card.setId(c.path("id").asText(null));
                    card.setCardNumber(c.path("numero_tarjeta").asText(null));
                    card.setMonth(c.path("mes").isInt() ? c.path("mes").asInt() : null);
                    card.setYear(c.path("anio").isInt() ? c.path("anio").asInt() : null);
                    card.setCardholderName(c.path("nombre").asText(null));
                    card.setUser(u);
                    cardList.add(card);
                }
                u.setCards(cardList);
            }

            userRepository.save(u);
        }
    }

    private void ensureCardsForExistingUser(User existing, JsonNode cardsNode) {
        if (cardsNode == null || !cardsNode.isArray()) {
            return;
        }
        List<Card> currentCards = cardRepository.findByUserRun(existing.getRun());
        java.util.Set<String> existingIds = new java.util.HashSet<>();
        for (Card card : currentCards) {
            existingIds.add(card.getId());
        }
        List<Card> toSave = new ArrayList<>();
        for (JsonNode c : cardsNode) {
            String cardId = c.path("id").asText(null);
            if (cardId == null || existingIds.contains(cardId)) {
                continue;
            }
            Card card = new Card();
            card.setId(cardId);
            card.setCardNumber(c.path("numero_tarjeta").asText(null));
            card.setMonth(c.path("mes").isInt() ? c.path("mes").asInt() : null);
            card.setYear(c.path("anio").isInt() ? c.path("anio").asInt() : null);
            card.setCardholderName(c.path("nombre").asText(null));
            card.setUser(existing);
            toSave.add(card);
        }
        if (!toSave.isEmpty()) {
            cardRepository.saveAll(toSave);
            log.info("Added {} new tarjetas for existing user {}", toSave.size(), existing.getRun());
        }
    }
}
