class Bike extends Vehicle {

    public Bike(String name,double price){
        super(name,price);
    }

    @Override
    void showDetails(){
        System.out.println("Bike: " + vehicleName +
                " | Per Day Price: " + pricePerDay + " Tk");
    }
}