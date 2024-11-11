public interface BurgerBuilder {
    void buildBun();
    void buildPatty();
    void addCheese();
    void addVeggies();
    void setSize(String size);
    Burger deliverBurger();
}
