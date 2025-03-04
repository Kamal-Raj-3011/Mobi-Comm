const users = [
    { email: "Dummy@dummy.com", phone: "8148074706" },
    { email: "user2@example.com", phone: "9123456789" },
];

let generatedOTP = "";

document.getElementById("sendOtpBtn").addEventListener("click", function () {
    const email = document.getElementById("email").value.trim();
    const phone = document.getElementById("phone").value.trim();
    const otpMethod = document.querySelector('input[name="otpMethod"]:checked').value;

    // Validation
    const userExists = users.some(user => user.email === email || user.phone === phone);
    const validPhone = /^\d{10}$/.test(phone);
    const validEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

    if (otpMethod === "email" && (!validEmail || !users.some(u => u.email === email))) {
        document.getElementById("emailError").classList.remove("d-none");
        return;
    } else {
        document.getElementById("emailError").classList.add("d-none");
    }

    if (otpMethod === "phone" && (!validPhone || !users.some(u => u.phone === phone))) {
        document.getElementById("phoneError").classList.remove("d-none");
        return;
    } else {
        document.getElementById("phoneError").classList.add("d-none");
    }

    if (!userExists) return;

    document.getElementById("sendOtpBtn").addEventListener("click", function () {
        // Generate OTP
        generatedOTP = Math.floor(1000 + Math.random() * 9000).toString();
    
        // Show OTP Modal
        document.getElementById("otpMessage").textContent = `OTP has been sent to your ${otpMethod}`;
        new bootstrap.Modal(document.getElementById("otpModal")).show();
    
        // Update Toast Content & Show it
        const toastBody = document.querySelector("#otpToast .toast-body");
        toastBody.textContent = `Your OTP: ${generatedOTP}`;
        const otpToast = new bootstrap.Toast(document.getElementById("otpToast"));
        otpToast.show();
    });
    
});

// OTP Verification
document.getElementById("verifyOtp").addEventListener("click", function () {
    const enteredOTP = Array.from(document.querySelectorAll(".otp-field"))
        .map(input => input.value)
        .join("");

    if (enteredOTP === generatedOTP) {
        window.location.href = "userdashboard.html";
    } else {
        document.getElementById("otpError").classList.remove("d-none");
    }
});

// Auto focus next OTP field
document.querySelectorAll(".otp-field").forEach((input, index, fields) => {
    input.addEventListener("input", () => {
        if (input.value && index < fields.length - 1) {
            fields[index + 1].focus();
        }
    });
});
