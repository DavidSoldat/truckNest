package com.trucknest.backend;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

public class ModulithTests {

    @Test
    void verifyModularStructure(){
        ApplicationModules.of(BackendApplication.class).verify();
    }
}
