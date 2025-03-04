
// HOME PAGE NUMBER VALIDATION
const validNumbers = ["8148074706", "9876543210", "9123456780"];

function validateNumber(event) {
    event.preventDefault();

    const mobileInput = document.getElementById("mobileNumber");
    const number = mobileInput.value.trim();
    const errorMessage = document.getElementById("error-message");

    if (!/^[0-9]{10}$/.test(number)) {
        errorMessage.innerText = "Enter a valid 10-digit number.";
        return false;
    }

    if (!validNumbers.includes(number)) {
        errorMessage.innerText = "Enter a valid mobi-comm number.";
        return false;
    }

    errorMessage.innerText = "";
    window.location.href = "assets/otherhtml/prepaidplans.html";
}

document.addEventListener("DOMContentLoaded", () => {
    const counter = document.getElementById("userCount");
    const target = parseInt(counter.getAttribute("data-target"));
    let count = 0;
    const duration = 2000; // Animation duration in milliseconds (2 seconds)
    const increment = target / (duration / 16); // ~60fps

    function updateCounter() {
        count += increment;
        if (count >= target) {
            count = target;
            counter.textContent = count.toLocaleString(); // Adds commas (e.g., 3,882,734)
        } else {
            counter.textContent = Math.floor(count).toLocaleString();
            requestAnimationFrame(updateCounter); // Smooth animation
        }
    }

    // Start the counter when the element is in view (optional)
    const observer = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting) {
            requestAnimationFrame(updateCounter);
            observer.disconnect(); // Run only once
        }
    }, { threshold: 0.5 });

    observer.observe(counter);
});