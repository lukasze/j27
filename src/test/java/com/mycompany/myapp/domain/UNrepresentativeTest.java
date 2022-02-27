package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UNrepresentativeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UNrepresentative.class);
        UNrepresentative uNrepresentative1 = new UNrepresentative();
        uNrepresentative1.setId(1L);
        UNrepresentative uNrepresentative2 = new UNrepresentative();
        uNrepresentative2.setId(uNrepresentative1.getId());
        assertThat(uNrepresentative1).isEqualTo(uNrepresentative2);
        uNrepresentative2.setId(2L);
        assertThat(uNrepresentative1).isNotEqualTo(uNrepresentative2);
        uNrepresentative1.setId(null);
        assertThat(uNrepresentative1).isNotEqualTo(uNrepresentative2);
    }
}
