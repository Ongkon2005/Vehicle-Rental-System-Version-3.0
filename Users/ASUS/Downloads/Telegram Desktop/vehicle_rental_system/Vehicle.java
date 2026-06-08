abstract class Vehicle {

    protected String vehicleName;
    protected double pricePerDay;

    public Vehicle(String name,double price){
        vehicleName = name;
        pricePerDay = price;
    }

    public double getPricePerDay(){
        return pricePerDay;
    }

    abstract void showDetails();
}