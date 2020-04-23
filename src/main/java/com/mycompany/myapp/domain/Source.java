package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Source.
 */
@Entity
@Table(name = "source")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Source implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_name")
    private String sourceName;

    @Column(name = "cost_usd")
    private Long costUSD;

    @Column(name = "cost_ars")
    private Long costARS;

    @Column(name = "cost_date")
    private Instant costDate;

    @OneToMany(mappedBy = "source")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceName() {
        return sourceName;
    }

    public Source sourceName(String sourceName) {
        this.sourceName = sourceName;
        return this;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Long getCostUSD() {
        return costUSD;
    }

    public Source costUSD(Long costUSD) {
        this.costUSD = costUSD;
        return this;
    }

    public void setCostUSD(Long costUSD) {
        this.costUSD = costUSD;
    }

    public Long getCostARS() {
        return costARS;
    }

    public Source costARS(Long costARS) {
        this.costARS = costARS;
        return this;
    }

    public void setCostARS(Long costARS) {
        this.costARS = costARS;
    }

    public Instant getCostDate() {
        return costDate;
    }

    public Source costDate(Instant costDate) {
        this.costDate = costDate;
        return this;
    }

    public void setCostDate(Instant costDate) {
        this.costDate = costDate;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Source products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Source addProduct(Product product) {
        this.products.add(product);
        product.setSource(this);
        return this;
    }

    public Source removeProduct(Product product) {
        this.products.remove(product);
        product.setSource(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Source)) {
            return false;
        }
        return id != null && id.equals(((Source) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Source{" +
            "id=" + getId() +
            ", sourceName='" + getSourceName() + "'" +
            ", costUSD=" + getCostUSD() +
            ", costARS=" + getCostARS() +
            ", costDate='" + getCostDate() + "'" +
            "}";
    }
}
