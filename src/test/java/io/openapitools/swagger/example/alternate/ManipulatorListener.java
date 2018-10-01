package io.openapitools.swagger.example.alternate;

import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.jaxrs2.ReaderListener;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@OpenAPIDefinition
public class ManipulatorListener implements ReaderListener {
    @Override
    public void beforeScan(Reader reader, OpenAPI swagger) {
        Info info = new Info();
        info.setTitle("Manipulator Title");
        swagger.setInfo(info);
    }

    @Override
    public void afterScan(Reader reader, OpenAPI swagger) {
        swagger.getInfo().setDescription("Description from ReaderListener");
    }
}
