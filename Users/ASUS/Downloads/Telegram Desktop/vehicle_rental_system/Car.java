class Car extends Vehicle {

    public Car(String name,double price){
        super(name,price);
    }

    @Override
    void showDetails(){
        System.out.println("Car: " + vehicleName +
                " | Per Day Price: " + pricePerDay + " Tk");
    }
}