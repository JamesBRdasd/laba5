package ru.itmo.lab5.io;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import ru.itmo.lab5.app.exception.IoAppException;
import ru.itmo.lab5.app.exception.ValidationException;
import ru.itmo.lab5.model.*;
import ru.itmo.lab5.util.Validators;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Hashtable;

/**
 * Загрузка/сохранение коллекции в XML.
 * <p>
 * Чтение выполняется через {@link java.io.InputStreamReader},
 * запись — через {@link java.io.FileWriter}.
 */
public final class XmlCollectionIO {

    /**
     * Загружает коллекцию из файла.
     *
     * @param filePath путь к XML-файлу
     * @return коллекция {@link Hashtable}
     * @throws IoAppException ошибка чтения/парсинга файла
     */
    public Hashtable<Long, Flat> load(String filePath) throws IoAppException {
        Hashtable<Long, Flat> table = new Hashtable<>();
        File file = new File(filePath);

        if (!file.exists()) return table;
        if (!file.isFile()) throw new IoAppException("Путь не является файлом: " + filePath);
        if (!file.canRead()) throw new IoAppException("Нет прав на чтение файла: " + filePath);

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(false);
            dbf.setExpandEntityReferences(false);
            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource src = new InputSource(reader);
            src.setEncoding("UTF-8");
            Document doc = db.parse(src);

            Element root = doc.getDocumentElement();
            if (root == null || !"flats".equals(root.getTagName())) {
                throw new IoAppException("Некорректный XML: корневой элемент должен быть <flats>");
            }

            NodeList entries = root.getElementsByTagName("entry");
            for (int i = 0; i < entries.getLength(); i++) {
                Element e = (Element) entries.item(i);
                String keyStr = e.getAttribute("key");
                if (keyStr == null || keyStr.isBlank()) continue;

                Long key;
                try { key = Long.parseLong(keyStr); }
                catch (NumberFormatException ex) { continue; }

                Element flatEl = (Element) e.getElementsByTagName("flat").item(0);
                if (flatEl == null) continue;

                try {
                    Flat flat = parseFlat(flatEl);
                    Validators.validateFlat(flat);
                    table.put(key, flat);
                } catch (ValidationException ex) {
                }
            }
            return table;
        } catch (IoAppException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IoAppException("Ошибка чтения/парсинга XML: " + ex.getMessage(), ex);
        }
    }

    /**
     * Сохраняет коллекцию в файл.
     *
     * @param filePath  путь к XML-файлу
     * @param table     коллекция
     * @throws IoAppException ошибка записи
     */
    public void save(String filePath, Hashtable<Long, Flat> table) throws IoAppException {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IoAppException("Не удалось создать директорию: " + parent);
        }
        if (file.exists() && !file.canWrite()) {
            throw new IoAppException("Нет прав на запись в файл: " + filePath);
        }

        String xml = toXml(table);

        try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8)) {
            fw.write(xml);
            fw.flush();
        } catch (IOException ex) {
            throw new IoAppException("Ошибка записи файла: " + ex.getMessage(), ex);
        }
    }

    private static String toXml(Hashtable<Long, Flat> table) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<flats>\n");
        for (var entry : table.entrySet()) {
            Long key = entry.getKey();
            Flat f = entry.getValue();
            sb.append("  <entry key=\"").append(key).append("\">\n");
            sb.append("    <flat>\n");
            tag(sb, "id", String.valueOf(f.getId()), 6);
            tag(sb, "name", escape(f.getName()), 6);

            sb.append("      <coordinates>\n");
            tag(sb, "x", String.valueOf(f.getCoordinates().getX()), 8);
            tag(sb, "y", String.valueOf(f.getCoordinates().getY()), 8);
            sb.append("      </coordinates>\n");

            tag(sb, "creationDate", String.valueOf(f.getCreationDate().getTime()), 6);
            tag(sb, "area", f.getArea() == null ? "" : String.valueOf(f.getArea()), 6);
            tag(sb, "numberOfRooms", String.valueOf(f.getNumberOfRooms()), 6);
            tag(sb, "furnish", f.getFurnish() == null ? "" : f.getFurnish().name(), 6);
            tag(sb, "view", f.getView().name(), 6);
            tag(sb, "transport", f.getTransport().name(), 6);

            sb.append("      <house>\n");
            tag(sb, "name", f.getHouse().getName() == null ? "" : escape(f.getHouse().getName()), 8);
            tag(sb, "year", String.valueOf(f.getHouse().getYear()), 8);
            tag(sb, "numberOfLifts", String.valueOf(f.getHouse().getNumberOfLifts()), 8);
            sb.append("      </house>\n");

            sb.append("    </flat>\n");
            sb.append("  </entry>\n");
        }
        sb.append("</flats>\n");
        return sb.toString();
    }

    private static void tag(StringBuilder sb, String name, String value, int indent) {
        sb.append(" ".repeat(indent)).append("<").append(name).append(">");
        sb.append(value == null ? "" : value);
        sb.append("</").append(name).append(">\n");
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    private static Flat parseFlat(Element flat) throws ValidationException {
        Long id = parseLong(getText(flat, "id"), "id");
        String name = getText(flat, "name");
        if (name != null) name = name.trim();

        Element coordEl = (Element) flat.getElementsByTagName("coordinates").item(0);
        if (coordEl == null) throw new ValidationException("coordinates отсутствует");
        Double x = parseDouble(getText(coordEl, "x"), "coordinates.x");
        Long y = parseLong(getText(coordEl, "y"), "coordinates.y");
        Coordinates coordinates = new Coordinates(x, y);

        Long cd = parseLong(getText(flat, "creationDate"), "creationDate");
        Date creationDate = new Date(cd);

        String areaText = getText(flat, "area");
        Float area = null;
        if (areaText != null && !areaText.isBlank()) area = parseFloat(areaText, "area");

        Long numberOfRooms = parseLong(getText(flat, "numberOfRooms"), "numberOfRooms");

        String furnishText = getText(flat, "furnish");
        Furnish furnish = null;
        if (furnishText != null && !furnishText.isBlank()) {
            try { furnish = Furnish.valueOf(furnishText.trim()); }
            catch (IllegalArgumentException ex) { throw new ValidationException("Некорректный furnish: " + furnishText); }
        }

        View view;
        try { view = View.valueOf(getText(flat, "view").trim()); }
        catch (Exception ex) { throw new ValidationException("Некорректный view"); }

        Transport transport;
        try { transport = Transport.valueOf(getText(flat, "transport").trim()); }
        catch (Exception ex) { throw new ValidationException("Некорректный transport"); }

        Element houseEl = (Element) flat.getElementsByTagName("house").item(0);
        if (houseEl == null) throw new ValidationException("house отсутствует");
        String houseName = getText(houseEl, "name");
        if (houseName != null && houseName.isBlank()) houseName = null;

        int year = parseInt(getText(houseEl, "year"), "house.year");
        long lifts = parseLong(getText(houseEl, "numberOfLifts"), "house.numberOfLifts");
        House house = new House(houseName, year, lifts);

        Flat f = new Flat(id, name, coordinates, creationDate, area, numberOfRooms, furnish, view, transport, house);
        Validators.validateFlat(f);
        return f;
    }

    private static String getText(Element parent, String tag) {
        NodeList nl = parent.getElementsByTagName(tag);
        if (nl.getLength() == 0) return null;
        String text = nl.item(0).getTextContent();
        return text == null ? null : text.trim();
    }

    private static Long parseLong(String s, String field) throws ValidationException {
        if (s == null || s.isBlank()) throw new ValidationException(field + " отсутствует");
        try { return Long.parseLong(s.trim()); }
        catch (NumberFormatException ex) { throw new ValidationException("Некорректный long в поле " + field + ": " + s); }
    }

    private static long parseLongPrimitive(String s, String field) throws ValidationException {
        return parseLong(s, field);
    }

    private static int parseInt(String s, String field) throws ValidationException {
        if (s == null || s.isBlank()) throw new ValidationException(field + " отсутствует");
        try { return Integer.parseInt(s.trim()); }
        catch (NumberFormatException ex) { throw new ValidationException("Некорректный int в поле " + field + ": " + s); }
    }

    private static Double parseDouble(String s, String field) throws ValidationException {
        if (s == null || s.isBlank()) throw new ValidationException(field + " отсутствует");
        try { return Double.parseDouble(s.trim()); }
        catch (NumberFormatException ex) { throw new ValidationException("Некорректный double в поле " + field + ": " + s); }
    }

    private static Float parseFloat(String s, String field) throws ValidationException {
        if (s == null || s.isBlank()) throw new ValidationException(field + " отсутствует");
        try { return Float.parseFloat(s.trim()); }
        catch (NumberFormatException ex) { throw new ValidationException("Некорректный float в поле " + field + ": " + s); }
    }
}
