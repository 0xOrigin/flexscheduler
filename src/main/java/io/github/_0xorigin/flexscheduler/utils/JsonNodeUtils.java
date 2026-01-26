package io.github._0xorigin.flexscheduler.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.UUID;

public class JsonNodeUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonNodeUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }

    public static <T> T deserializeArguments(JsonNode node, Class<T> clazz) {
        if (node == null || node.isNull())
            return null;
        try {
            return objectMapper.treeToValue(node, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse task arguments", e);
        }
    }

    public static <E extends Enum<E>> Optional<E> getOptionalEnum(JsonNode node, String fieldName, Class<E> enumClass) {
        if (node == null) return Optional.empty();
        JsonNode field = node.get(fieldName);
        if (field == null) return Optional.empty();
        try {
            return Optional.of(Enum.valueOf(enumClass, field.asText()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Optional<String> getOptionalString(JsonNode node, String fieldName) {
        if (node == null) return Optional.empty();
        JsonNode field = node.get(fieldName);
        return field != null ? Optional.of(field.asText()) : Optional.empty();
    }

    public static Optional<UUID> getOptionalUUID(JsonNode node, String fieldName) {
        if (node == null) return Optional.empty();
        JsonNode field = node.get(fieldName);
        if (field == null) return Optional.empty();
        try {
            return Optional.of(UUID.fromString(field.asText()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Optional<Boolean> getOptionalBoolean(JsonNode node, String fieldName) {
        if (node == null) return Optional.empty();
        JsonNode field = node.get(fieldName);
        return field != null ? Optional.of(field.asBoolean()) : Optional.empty();
    }

    public static Optional<Integer> getOptionalInteger(JsonNode node, String fieldName) {
        if (node == null) return Optional.empty();
        JsonNode field = node.get(fieldName);
        return field != null ? Optional.of(field.asInt()) : Optional.empty();
    }

    public static Optional<Long> getOptionalLong(JsonNode node, String fieldName) {
        if (node == null) return Optional.empty();
        JsonNode field = node.get(fieldName);
        return field != null ? Optional.of(field.asLong()) : Optional.empty();
    }

    public static Optional<Double> getOptionalDouble(JsonNode node, String fieldName) {
        if (node == null) return Optional.empty();
        JsonNode field = node.get(fieldName);
        return field != null ? Optional.of(field.asDouble()) : Optional.empty();
    }

    public static Optional<BigInteger> getOptionalBigInteger(JsonNode node, String fieldName) {
        if (node == null) return Optional.empty();
        JsonNode field = node.get(fieldName);
        if (field == null) return Optional.empty();
        try {
            return Optional.of(new BigInteger(field.asText()));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static Optional<BigDecimal> getOptionalBigDecimal(JsonNode node, String fieldName) {
        if (node == null) return Optional.empty();
        JsonNode field = node.get(fieldName);
        if (field == null) return Optional.empty();
        try {
            return Optional.of(new BigDecimal(field.asText()));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static Optional<LocalDate> getOptionalLocalDate(JsonNode node, String fieldName) {
        if (node == null) return Optional.empty();
        JsonNode field = node.get(fieldName);
        if (field == null) return Optional.empty();
        try {
            return Optional.of(LocalDate.parse(field.asText()));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    public static Optional<LocalTime> getOptionalLocalTime(JsonNode node, String fieldName) {
        if (node == null) return Optional.empty();
        JsonNode field = node.get(fieldName);
        if (field == null) return Optional.empty();
        try {
            return Optional.of(LocalTime.parse(field.asText()));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    public static Optional<LocalDateTime> getOptionalLocalDateTime(JsonNode node, String fieldName) {
        if (node == null) return Optional.empty();
        JsonNode field = node.get(fieldName);
        if (field == null) return Optional.empty();
        try {
            return Optional.of(LocalDateTime.parse(field.asText()));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    public static Optional<OffsetTime> getOptionalOffsetTime(JsonNode node, String fieldName) {
        if (node == null) return Optional.empty();
        JsonNode field = node.get(fieldName);
        if (field == null) return Optional.empty();
        try {
            return Optional.of(OffsetTime.parse(field.asText()));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    public static Optional<OffsetDateTime> getOptionalOffsetDateTime(JsonNode node, String fieldName) {
        if (node == null) return Optional.empty();
        JsonNode field = node.get(fieldName);
        if (field == null) return Optional.empty();
        try {
            return Optional.of(OffsetDateTime.parse(field.asText()));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    public static Optional<Instant> getOptionalInstant(JsonNode node, String fieldName) {
        if (node == null) return Optional.empty();
        JsonNode field = node.get(fieldName);
        if (field == null) return Optional.empty();
        try {
            return Optional.of(Instant.parse(field.asText()));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    public static Optional<ZonedDateTime> getOptionalZonedDateTime(JsonNode node, String fieldName) {
        if (node == null) return Optional.empty();
        JsonNode field = node.get(fieldName);
        if (field == null) return Optional.empty();
        try {
            return Optional.of(ZonedDateTime.parse(field.asText()));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }
}
