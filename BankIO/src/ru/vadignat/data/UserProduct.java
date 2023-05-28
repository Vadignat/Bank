package ru.vadignat.data;

import java.io.Serializable;

public class UserProduct implements Serializable {
        private User user;
        private Product product;

        public User getUser() {
            return user;
        }

        public Product getProduct() {
            return product;
        }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

