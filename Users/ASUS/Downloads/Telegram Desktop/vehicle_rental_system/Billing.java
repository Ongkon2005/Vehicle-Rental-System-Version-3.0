class Billing {

    double calculateBill(double price,int days){
        return price * days;
    }

    double calculateBill(double price,int days,double discount){
        return (price * days) - discount;
    }
}