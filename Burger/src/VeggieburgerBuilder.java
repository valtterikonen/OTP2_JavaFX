public class VeggieburgerBuilder implements BurgerBuilder {
    private Burger burger;

    public VeggieburgerBuilder() { this.burger = new Burger(); }

    @Override public void buildBun() { burger.setBun("Whole grain bun"); }
    @Override public void buildPatty() { burger.setPatty("Vegetable patty"); }
    @Override public void addCheese() { burger.setCheese("Cheddar"); }
    @Override public void addVeggies() { burger.setVeggies("Lettuce, Tomato, Cucumber"); }
    @Override public void setSize(String size) { burger.setSize(size); }
    @Override public Burger deliverBurger() { return burger; }
}
