
// Number Validation
function validateNumber(event) {
    event.preventDefault();  // Prevent form submission

    const mobileNumber = document.getElementById("mobileNumber").value;
    const errorMessage = document.getElementById("error-message");

    // Regular expression to match 10-digit mobile number
    const mobilePattern = /^[6-9]\d{9}$/;

    if (!mobilePattern.test(mobileNumber)) {
        errorMessage.textContent = "Please enter a valid mobile number.";
        return false;
    }

    // If the mobile number format is valid, make an API call to check if it exists in the backend
    fetch(`http://localhost:9090/api/users/validateMobile/${mobileNumber}`, {
        method: "GET",
    })
    .then(response => response.json())  // Parse JSON response
    .then(data => {
        if (data.exists) {
            // If the number exists in the database, redirect to Prepaid Plans page
            window.location.href = "assets/loggedusershtmlpage/recharge.html";  // Adjust the URL based on your routing
        } else {
            // If the number does not exist, display an error message
            errorMessage.textContent = "Enter A Valid Mobi-Comm Number";
        }
    })
    .catch(error => {
        console.error("Error during mobile validation:", error);
        errorMessage.textContent = "An error occurred. Please try again later.";
    });

    return false;  // Prevent the form from submitting
}



// Number Animation
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

// Display Popular Plans Card
    // Function to fetch and display multiple plan details by Plan IDs
    function fetchMultiplePlans() {
        const planIds = ['MC_PLAN-001', 'MC_PLAN-002', 'MC_PLAN-003'];  // Add the Plan IDs you want to fetch
        const planPromises = planIds.map(planId => {
            const planURL = `http://localhost:9090/api/plans/${planId}`;
            return fetch(planURL).then(response => {
                if (!response.ok) {
                    throw new Error(`Plan ${planId} not found`);
                }
                return response.json();
            });
        });

        // Wait for all plan details to be fetched
        Promise.all(planPromises)
            .then(plans => {
                const detailsContainer = document.getElementById('plans-container');
                detailsContainer.innerHTML = `
                    <div class="container">
                        <div class="row g-4 justify-content-center">
                            <!-- Dynamically create a card for each plan -->
                            ${plans.map(plan => `
                                <div class="col-12 col-md-4">
                                    <div class="card p-4 shadow-lg rounded-4 border-0 text-white h-100" style="background: linear-gradient(135deg, #1e1e1e, #343a40);">
                                        <div class="d-flex justify-content-between align-items-center mb-3">
                                            <h6 class="text-uppercase fw-bold" style="color: #ffc600;">⭐ Best Seller</h6>
                                            <span class="fs-4" style="color: #fff7d1;">✨</span>
                                        </div>
                                        <h2 class="fw-bold mb-3" style="color: #f8f9fa;">${plan.price}</h2>
                                        <hr style="border-top: 2px solid rgba(255, 255, 255, 0.2); margin: 1rem 0;">
                                        <div class="mb-4">
                                            <p class="mb-2"><i class="fa-solid fa-calendar-alt me-2"></i> ${plan.validity}</p>
                                            <p class="mb-2"><i class="fa-solid fa-wifi me-2"></i> ${plan.data}</p>
                                            <p class="mb-2"><i class="fa-solid fa-phone me-2"></i> ${plan.calls}</p>
                                            <p class="mb-2"><i class="fa-solid fa-comment-dots me-2"></i> ${plan.sms}</p>
                                        </div>
                                        <a href="prepaidplans.html" class="btn text-white w-100 py-2 fw-bold rounded-pill" style="background: linear-gradient(135deg, #ff6b6b, #ffc600); box-shadow: 0 4px 10px rgba(255, 198, 0, 0.5); transition: transform 0.3s ease;" onmouseover="this.style.transform='scale(1.05)';" onmouseout="this.style.transform='scale(1)';">
                                            Get This Plan <i class="fa-solid fa-rocket ms-2"></i>
                                        </a>
                                    </div>
                                </div>
                            `).join('')}
                        </div>
                    </div>
                `;
            })
            .catch(error => {
                const detailsContainer = document.getElementById('plans-container');
                detailsContainer.innerHTML = `<p class="text-danger">Error: ${error.message}</p>`;
            });
    }

    // Call the function to fetch and display multiple plans
    fetchMultiplePlans();
