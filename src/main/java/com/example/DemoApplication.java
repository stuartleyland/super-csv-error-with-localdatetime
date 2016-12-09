package com.example;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.time.FmtLocalDateTime;
import org.supercsv.cellprocessor.time.ParseLocalDateTime;
import org.supercsv.io.dozer.CsvDozerBeanReader;
import org.supercsv.io.dozer.CsvDozerBeanWriter;
import org.supercsv.io.dozer.ICsvDozerBeanReader;
import org.supercsv.io.dozer.ICsvDozerBeanWriter;
import org.supercsv.prefs.CsvPreference;

@SpringBootApplication
public class DemoApplication {

    private static final String[] FIELD_MAPPING = { "dateTime" };
    private static final String[] HEADERS = { "Date" };
    private static final CellProcessor[] CELL_PROCESSORS_WRITE = { new FmtLocalDateTime() };

    public static void main(final String[] args) {
        SpringApplication.run(DemoApplication.class, args);

        final List<Entity> entities = createEntitiesToWrite();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writeEntities(entities, outputStream);

        readEntities(outputStream);
    }

    private static void readEntities(final ByteArrayOutputStream outputStream) {
        final Reader reader = new InputStreamReader(new ByteArrayInputStream(outputStream.toByteArray()));
        try (ICsvDozerBeanReader beanReader = new CsvDozerBeanReader(reader, CsvPreference.STANDARD_PREFERENCE)) {
            beanReader.configureBeanMapping(Entity.class, FIELD_MAPPING);
            beanReader.getHeader(true);

            final CellProcessor[] cellProcessorsForReading = { new ParseLocalDateTime() };
            final List<Entity> readEntities = new ArrayList<>();
            Entity readEntity;
            while ((readEntity = beanReader.read(Entity.class, cellProcessorsForReading)) != null) {
                readEntities.add(readEntity);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeEntities(final List<Entity> entities, final ByteArrayOutputStream outputStream) {
        final Writer writer = new OutputStreamWriter(outputStream);
        try (ICsvDozerBeanWriter beanWriter = new CsvDozerBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);) {
            beanWriter.configureBeanMapping(Entity.class, FIELD_MAPPING);

            beanWriter.writeHeader(HEADERS);

            for (final Entity entity : entities) {
                beanWriter.write(entity, CELL_PROCESSORS_WRITE);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Entity> createEntitiesToWrite() {
        final List<Entity> entities = new ArrayList<>();
        final Entity entity = new Entity();
        entity.setDateTime(LocalDateTime.now());
        entities.add(entity);
        return entities;
    }
}
