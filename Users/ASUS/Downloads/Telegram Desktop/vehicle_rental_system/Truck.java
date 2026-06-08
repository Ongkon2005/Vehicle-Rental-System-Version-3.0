class Truck extends Vehicle {

    public Truck(String name,double price){
        super(name,price);
    }

    @Override
    void showDetails(){
        System.out.println("Truck: " + vehicleName +
                " | Per Day Price: " + pricePerDay + " Tk");
    }
}
