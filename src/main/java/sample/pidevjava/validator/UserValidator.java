package sample.pidevjava.validator;
import sample.pidevjava.model.User;

public class UserValidator {

        public static boolean validate(User user) {
            return  isValidName(user.getFirstname())&&
                    isValidName(user.getLastname())&&
                    isValidEmail(user.getEmail()) &&
                    isValidPhone(user.getPhone())  ;
        }


        public static boolean isValidEmail(String email) {
            String emailvrai = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            return email != null && email.matches(emailvrai);
        }

        public static boolean isValidPhone(String phone) {
            String phonetunis = "^(00216|\\+216)?[0-9]{8}$";
            return phone != null && phone.matches(phonetunis);
        }

    public static boolean isValidName(String name) {
        return name != null && !name.isEmpty();

    }
}

