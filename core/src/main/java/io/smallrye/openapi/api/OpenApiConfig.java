package io.smallrye.openapi.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import io.smallrye.openapi.api.constants.JsonbConstants;
import io.smallrye.openapi.api.constants.OpenApiConstants;

/**
 * Accessor to OpenAPI configuration options.
 *
 * Reference:
 * https://github.com/eclipse/microprofile-open-api/blob/master/spec/src/main/asciidoc/microprofile-openapi-spec.asciidoc#list-of-configurable-items
 *
 * @author eric.wittmann@gmail.com
 */
public interface OpenApiConfig {

    DuplicateOperationIdBehavior DUPLICATE_OPERATION_ID_BEHAVIOR_DEFAULT = DuplicateOperationIdBehavior.WARN;

    default String modelReader() {
        return null;
    }

    default String filter() {
        return null;
    }

    default boolean scanDisable() {
        return false;
    }

    default Set<String> scanPackages() {
        return null;
    }

    default Set<String> scanClasses() {
        return null;
    }

    default Set<String> scanExcludePackages() {
        return OpenApiConstants.NEVER_SCAN_PACKAGES;
    }

    default Set<String> scanExcludeClasses() {
        return OpenApiConstants.NEVER_SCAN_CLASSES;
    }

    default boolean scanBeanValidation() {
        return true;
    }

    default List<String> servers() {
        return new ArrayList<>();
    }

    default List<String> pathServers(String path) {
        return new ArrayList<>();
    }

    default List<String> operationServers(String operationId) {
        return new ArrayList<>();
    }

    default boolean scanDependenciesDisable() {
        return false;
    }

    default Set<String> scanDependenciesJars() {
        return new HashSet<>();
    }

    default boolean arrayReferencesEnable() {
        return true;
    }

    default String customSchemaRegistryClass() {
        return null;
    }

    default boolean applicationPathDisable() {
        return false;
    }

    default boolean privatePropertiesEnable() {
        return true;
    }

    default String propertyNamingStrategy() {
        return JsonbConstants.IDENTITY;
    }

    default boolean sortedPropertiesEnable() {
        return false;
    }

    default Map<String, String> getSchemas() {
        return new HashMap<>();
    }

    // Here we extend this in SmallRye with some more configure options (mp.openapi.extensions)
    default String getOpenApiVersion() {
        return null;
    }

    default String getInfoTitle() {
        return null;
    }

    default String getInfoVersion() {
        return null;
    }

    default String getInfoDescription() {
        return null;
    }

    default String getInfoTermsOfService() {
        return null;
    }

    default String getInfoContactEmail() {
        return null;
    }

    default String getInfoContactName() {
        return null;
    }

    default String getInfoContactUrl() {
        return null;
    }

    default String getInfoLicenseName() {
        return null;
    }

    default String getInfoLicenseUrl() {
        return null;
    }

    default OperationIdStrategy getOperationIdStrategy() {
        return null;
    }

    default DuplicateOperationIdBehavior getDuplicateOperationIdBehavior() {
        return DUPLICATE_OPERATION_ID_BEHAVIOR_DEFAULT;
    }

    default Optional<String[]> getDefaultProduces() {
        return Optional.empty();
    }

    default Optional<String[]> getDefaultConsumes() {
        return Optional.empty();
    }

    default Optional<Boolean> allowNakedPathParameter() {
        return Optional.empty();
    }

    default Set<String> getScanProfiles() {
        return new HashSet<>();
    }

    default Set<String> getScanExcludeProfiles() {
        return new HashSet<>();
    }

    default boolean removeUnusedSchemas() {
        return false;
    }

    default void doAllowNakedPathParameter() {
    }

    enum OperationIdStrategy {
        METHOD,
        CLASS_METHOD,
        PACKAGE_CLASS_METHOD
    }

    enum DuplicateOperationIdBehavior {
        FAIL,
        WARN
    }

    default Pattern patternOf(String configValue) {
        return patternOf(configValue, null);
    }

    default Pattern patternOf(String configValue, Set<String> buildIn) {
        Pattern pattern = null;

        if (configValue != null && (configValue.startsWith("^") || configValue.endsWith("$"))) {
            pattern = Pattern.compile(configValue);
        } else {
            Set<String> literals = asCsvSet(configValue);
            if (buildIn != null && !buildIn.isEmpty()) {
                literals.addAll(buildIn);
            }
            if (literals.isEmpty()) {
                return Pattern.compile("", Pattern.LITERAL);
            } else {
                pattern = Pattern.compile("(" + literals.stream().map(Pattern::quote).collect(Collectors.joining("|")) + ")");
            }
        }

        return pattern;
    }

    default Set<String> asCsvSet(String items) {
        Set<String> rval = new HashSet<>();
        if (items != null) {
            String[] split = items.split(",");
            for (String item : split) {
                rval.add(item.trim());
            }
        }
        return rval;
    }

    default List<String> asCsvList(String items) {
        List<String> rval = new ArrayList<>();
        if (items != null) {
            String[] split = items.split(",");
            for (String item : split) {
                rval.add(item.trim());
            }
        }
        return rval;
    }
}
