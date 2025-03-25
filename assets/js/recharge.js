// Number Validation
function validateNumber(event) {
    event.preventDefault();  

    const mobileNumber = document.getElementById("mobileNumber").value;
    const errorMessage = document.getElementById("error-message");

    // Regular expression for a valid mobile number
    const mobilePattern = /^[6-9]\d{9}$/;

    if (!mobilePattern.test(mobileNumber)) {
        errorMessage.textContent = "Please enter a valid mobile number.";
        return false;
    }

    // API Call to Validate Mobile Number
    fetch(`http://localhost:9090/api/users/validateMobile/${mobileNumber}`)
    .then(response => response.json())
    .then(data => {
        if (data.exists) {
            // ✅ Store mobile number in localStorage
            localStorage.setItem("mobileNumber", mobileNumber);

            // ✅ Fetch User ID and User Name
            return fetch(`http://localhost:9090/api/users/details/${mobileNumber}`);
        } else {
            throw new Error("Invalid Mobi-Comm Number");
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to fetch user details");
        }
        return response.json();
    })
    .then(userData => {
        // ✅ Store User ID and Name in localStorage
        localStorage.setItem("userID", userData.userId);
        localStorage.setItem("userName", userData.name);

        // ✅ Redirect to Payment Gateway page
        window.location.href = "assets/paymentgateway/paymentgateway.html";
    })
    .catch(error => {
        console.error("Error:", error);
        errorMessage.textContent = "Enter Valid Mobi-Comm Number.";
    });
}