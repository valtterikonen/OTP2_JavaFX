public class BurgerDirector {
    private BurgerBuilder builder;

    public BurgerDirector(BurgerBuilder builder) {
        this.builder = builder;
    }

    public void makeSingleBurger() {
        builder.buildBun();
        builder.buildPatty();
        builder.addCheese();
        builder.addVeggies();
        builder.setSize("Single");
    }

    public void makeDoubleBurger() {
        builder.buildBun();
        builder.buildPatty();
        builder.addCheese();
        builder.addVeggies();
        builder.setSize("Double");
    }

    public Burger getBurger() {
        return builder.deliverBurger();
    }
}
