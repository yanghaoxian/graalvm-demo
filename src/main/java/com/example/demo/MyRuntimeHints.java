package com.example.demo;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

/**
 * @author: yanghaoxian
 * @created: 2023/05/05
 * @description:
 */
public class MyRuntimeHints implements RuntimeHintsRegistrar{


        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            // Register method for reflection
            //Method method = ReflectionUtils.findMethod(Data.class, "<init>");
            //hints.reflection().registerMethod(method, ExecutableMode.INVOKE);

            // Register resources
            //hints.resources().registerPattern("my-resource.txt");

            // Register serialization
            hints.serialization()
//                    .registerType(EndpointReadEntity.class)
//                    .registerType(EndpointEntity.class)
//                    .registerType(DeviceReadEntity.class)
//                    .registerType(EndpointNameEntity.class)
                    .registerType(Data.class);

            // Register proxy
            //hints.proxies().registerJdkProxy(MyInterface.class);
        }

    }


