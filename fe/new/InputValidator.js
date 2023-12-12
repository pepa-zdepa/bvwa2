const InputValidator = {
    isEmailValid: (email) => {
      // Add email validation logic
      return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    },
    isPhoneNumberValid: (phoneNumber) => {
        // Phone number should allow 10 to 12 digits and may start with a plus sign
        // It should not contain spaces
        return /^(\+\d{1,2})?\d{10,12}$/g.test(phoneNumber);
      },
    isPasswordValid: (password) => {
      // Add password validation logic
      const hasCapitalLetter = /[A-Z]/.test(password);
      const hasNumber = /\d/.test(password);
      const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);
      const isLengthValid = password.length >= 10;
      return hasCapitalLetter && hasNumber && hasSpecialChar && isLengthValid;
    },
  };
  
  export default InputValidator;
  