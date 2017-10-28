package io.openapitools.swagger.example.alternate;

import io.swagger.annotations.SwaggerDefinition;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.config.ReaderListener;
import io.swagger.models.Info;
import io.swagger.models.Swagger;

@SwaggerDefinition
public class ManipulatorListener implements ReaderListener {
    @Override
    public void beforeScan(Reader reader, Swagger swagger) {
        Info info = new Info();
        info.setTitle("Manipulator Title");
        swagger.setInfo(info);
    }

    @Override
    public void afterScan(Reader reader, Swagger swagger) {
        swagger.getInfo().setDescription("Description from ReaderListener");
    }
}
