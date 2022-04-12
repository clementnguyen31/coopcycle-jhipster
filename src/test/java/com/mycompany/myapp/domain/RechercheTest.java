package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RechercheTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recherche.class);
        Recherche recherche1 = new Recherche();
        recherche1.setId(1L);
        Recherche recherche2 = new Recherche();
        recherche2.setId(recherche1.getId());
        assertThat(recherche1).isEqualTo(recherche2);
        recherche2.setId(2L);
        assertThat(recherche1).isNotEqualTo(recherche2);
        recherche1.setId(null);
        assertThat(recherche1).isNotEqualTo(recherche2);
    }
}
