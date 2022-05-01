package br.com.fit.apigateway.crud.config;

import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.stereotype.Component;

@Component
public class OpenApiTagsCustomiser extends SpecFilter implements OpenApiCustomiser {

    public Parameter getAuthorizationParameter() {
        return new Parameter()
                .in(ParameterIn.HEADER.toString())
                .name("Authorization")
                .description("Authorization token")
                .schema(new StringSchema()).required(true);
    }

    @Override
    public void customise(OpenAPI openApi) {
        // rename the operation tags
        openApi.getPaths().values().stream().flatMap(pathItem -> pathItem.readOperations().stream())
                .forEach(operation -> {
                    String tagName = operation.getTags().get(0);

                    if (tagName.startsWith("cliente")) {
                        operation.addParametersItem(new Parameter()
                                .in(ParameterIn.HEADER.toString())
                                .name("X-Authorization")
                                .description("Authorization token")
                                .schema(new StringSchema()).required(true));
                    }
                });
    }
}
