public class User {

    private String emailOrPhone;
    private String password;

    public User(String emailOrPhone, String password){
        this.emailOrPhone = emailOrPhone;
        this.password = password;
    }

    public String getEmailOrPhone(){
        return emailOrPhone;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
}