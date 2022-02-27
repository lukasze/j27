package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnginierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Enginier.class);
        Enginier enginier1 = new Enginier();
        enginier1.setId(1L);
        Enginier enginier2 = new Enginier();
        enginier2.setId(enginier1.getId());
        assertThat(enginier1).isEqualTo(enginier2);
        enginier2.setId(2L);
        assertThat(enginier1).isNotEqualTo(enginier2);
        enginier1.setId(null);
        assertThat(enginier1).isNotEqualTo(enginier2);
    }
}
