package com.prx.mercury.jpa.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationEntityTest {

    @Test
    void getId() {
        ApplicationEntity applicationEntity = new ApplicationEntity();
        assertNull(applicationEntity.getId());
    }

    @Test
    void setId() {
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setId(null);
        assertNull(applicationEntity.getId());
    }

}
