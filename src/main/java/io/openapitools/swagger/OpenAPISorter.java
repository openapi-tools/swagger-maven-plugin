package io.openapitools.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Sorter for the contents of an OpenAPI specification.
 * <p>
 * Sorting the OpenAPI specification solves problems when the output of this plugin is
 * committed in a version control system.
 * <p>
 * The swagger-core library generates non-deterministic output, because reflection operations on
 * scanned Resource classes are non-deterministic in the order of methods and fields.
 * <p>
 * This class and its functionality may be removed if the generation of deterministic output is
 * solved in swagger-core.
 * <p>
 * See https://github.com/swagger-api/swagger-core/issues/3475
 * See https://github.com/swagger-api/swagger-core/issues/2775
 * See https://github.com/swagger-api/swagger-core/issues/2828
 */
public class OpenAPISorter {

    /**
     * Sort all the paths and components of the OpenAPI specification, in place.
     */
    public void sort(OpenAPI swagger) {
        sortPaths(swagger.getPaths());
        sortComponents(swagger.getComponents());
    }

    /**
     * Sort all the elements of Paths.
     */
    private void sortPaths(io.swagger.v3.oas.models.Paths paths) {
        sortMap(paths);
    }

    /**
     * Sort all the elements of Components.
     */
    private void sortComponents(Components components) {
        if (components == null) {
            return;
        }

        sortSchemas(components.getSchemas());
        sortMap(components.getResponses());
        sortMap(components.getParameters());
        sortMap(components.getExamples());
        sortMap(components.getRequestBodies());
        sortMap(components.getHeaders());
        sortMap(components.getSecuritySchemes());
        sortMap(components.getLinks());
        sortMap(components.getCallbacks());
        sortMap(components.getExtensions());
    }

    /**
     * Recursively sort all the schemas in the Map.
     */
    private void sortSchemas(Map<String, Schema> schemas) {
        if (schemas == null) {
            return;
        }

        sortMap(schemas);
        for (Schema<?> schema : schemas.values()) {
            sortSchemas(schema.getProperties());
        }
    }

    /**
     * Sort a map by its natural Key order. This method assumes that the Map
     * will store its entries in insertion order (HashMap, LinkedHashMap, etc.).
     * <p>
     * This will not work for a SortedMap which has an inherent ordering (TreeMap, etc.).
     */
    private <T> void sortMap(Map<String, T> map) {
        if (map == null) {
            return;
        }

        Map<String, T> sortedMapItems = map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (item1, item2) -> item2, TreeMap::new));
        map.clear();
        map.putAll(sortedMapItems);
    }
}
