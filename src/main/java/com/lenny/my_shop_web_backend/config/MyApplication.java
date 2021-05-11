/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.config;

import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author lenny
 */
public class MyApplication extends ResourceConfig {

    public MyApplication() {
        register(JerseyObjectMapperProvider.class);
        register(org.glassfish.jersey.media.multipart.MultiPartFeature.class);

        packages("com.lenny.my_shop_web_backend.api", "com.fasterxml.jackson.jaxrs");

        property("jersey.config.jsonFeature", "JacksonFeature");
        property("jersey.config.disableMoxyJson.server", true);
    }
}
