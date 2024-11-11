public class Burger {
    private String bun;
    private String patty;
    private String cheese;
    private String veggies;
    private String size;

    public void setBun(String bun) { this.bun = bun; }
    public void setPatty(String patty) { this.patty = patty; }
    public void setCheese(String cheese) { this.cheese = cheese; }
    public void setVeggies(String veggies) { this.veggies = veggies; }
    public void setSize(String size) { this.size = size; }

    @Override
    public String toString() {
        return "Burger [bun=" + bun + ", patty=" + patty + ", cheese=" + cheese + ", veggies=" + veggies + ", size=" + size + "]";
    }
}
