public class CheeseburgerBuilder implements BurgerBuilder {
    private Burger burger;

    public CheeseburgerBuilder() { this.burger = new Burger(); }

    @Override public void buildBun() { burger.setBun("Sesame bun"); }
    @Override public void buildPatty() { burger.setPatty("Beef patty"); }
    @Override public void addCheese() { burger.setCheese("Swiss"); }
    @Override public void addVeggies() { burger.setVeggies("Lettuce, Tomato"); }
    @Override public void setSize(String size) { burger.setSize(size); }
    @Override public Burger deliverBurger() { return burger; }
}
