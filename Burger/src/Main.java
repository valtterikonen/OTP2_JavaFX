public class Main {
    public static void main(String[] args) {

        BurgerBuilder veggieBuilder = new VeggieburgerBuilder();
        BurgerDirector director = new BurgerDirector(veggieBuilder);
        director.makeSingleBurger();
        Burger veggieBurger = director.getBurger();
        System.out.println("Veggie Burger: " + veggieBurger);

        BurgerBuilder cheeseBuilder = new CheeseburgerBuilder();
        director = new BurgerDirector(cheeseBuilder);
        director.makeDoubleBurger();
        Burger cheeseBurger = director.getBurger();
        System.out.println("Cheese Burger: " + cheeseBurger);
    }
}
