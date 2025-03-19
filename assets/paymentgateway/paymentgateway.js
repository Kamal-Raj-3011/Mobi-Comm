async function buyPlan(planId, planName, planPrice, planValidity) {
    // Check if user is logged in
    const mobileNumber = localStorage.getItem("mobileNumber");
    if (!mobileNumber) {
        alert("Login Necessary!");
        window.location.href = "../../index.html";
        return;
    }

    const userId = localStorage.getItem("guestId") || localStorage.getItem("userID") || null;
    if (!userId) {
        alert("User ID not found!");
        return;
    }

    const userName = localStorage.getItem("userName") || "User Name"; 
    const userEmail = localStorage.getItem("userEmail") || "user@example.com";

    // ✅ Ensure `planValidity` is a valid number
    const validityDays = parseInt(planValidity);
    if (isNaN(validityDays) || validityDays <= 0) {
        alert("Invalid plan validity!");
        return;
    }

    // ✅ Fix: Ensure paymentDate is valid
    const paymentDate = new Date();
    if (isNaN(paymentDate.getTime())) {
        alert("Invalid payment date!");
        return;
    }

    // ✅ Fix: Ensure expiryDate is valid
    const expiryDate = new Date(paymentDate);
    expiryDate.setDate(expiryDate.getDate() + validityDays);
    if (isNaN(expiryDate.getTime())) {
        alert("Invalid expiry date!");
        return;
    }

    var options = {
        "key": "rzp_test_SQCsjYxxhe6Mix", 
        "amount": planPrice * 100, 
        "currency": "INR",
        "name": "MobiComm Pvt Ltd",
        "description": `Payment for ${planName}`,
        "image": "Assets/Images/logo.png", 
        "handler": async function (response) {
            // ✅ Store Payment ID in localStorage
            localStorage.setItem("razorpayPaymentId", response.razorpay_payment_id);

            // ✅ Prepare payment data
            const paymentData = {
                payMethod: "Razorpay",
                rechargePhNo: mobileNumber,
                userId: userId,
                amount: planPrice,
                planId: planId,
                transactionId: response.razorpay_payment_id, 
                paymentDate: paymentDate.toISOString(),  // ✅ Fixed
                expiryDate: expiryDate.toISOString()  // ✅ Fixed
            };

            try {
                const fetchResponse = await fetch(`http://localhost:9090/api/payment-history/${userId}/${planId}`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(paymentData)
                });

                if (!fetchResponse.ok) {
                    throw new Error("Failed to create payment history");
                }

                const data = await fetchResponse.json();
                console.log('Success:', data);

                localStorage.removeItem("selectedPlan");
                localStorage.removeItem("guestMobile");
                localStorage.removeItem("guestId");
                localStorage.removeItem("razorpayPaymentId");

                window.location.href = `../../index.html`;
            } catch (error) {
                console.error('Error:', error);
                alert('Failed to create Payment History.');
            }
        },
        "prefill": {
            "name": userName,
            "email": userEmail,
            "contact": mobileNumber
        },
        "theme": {
            "color": "#3399cc"
        }
    };

    var rzp1 = new Razorpay(options);
    rzp1.open();
}





document.addEventListener("DOMContentLoaded", function () {
    const API_URL_CATEGORIES = "http://127.0.0.1:9090/api/categories/";
    const API_URL_PLANS = "http://127.0.0.1:9090/api/plans/category/";
    const planTabs = document.getElementById("plan-tabs");
    const planContent = document.getElementById("plan-content");

    function fetchCategories() {
        fetch(API_URL_CATEGORIES)
            .then(response => response.json())
            .then(categories => {
                planTabs.innerHTML = ""; 
                planContent.innerHTML = "";

                categories.forEach((category, index) => {
                    const tab = document.createElement("li");
                    tab.classList.add("nav-item");

                    tab.innerHTML = `
                        <a class="nav-link ${index === 0 ? 'active' : ''}" id="category-tab-${category.categoryId}" 
                            data-bs-toggle="pill" href="#category-content-${category.categoryId}" 
                            role="tab" aria-controls="category-content-${category.categoryId}" 
                            aria-selected="${index === 0}">
                            ${category.categoryName}
                        </a>
                    `;
                    planTabs.appendChild(tab);

                    const contentSection = document.createElement("div");
                    contentSection.classList.add("tab-pane", "fade");
                    if (index === 0) contentSection.classList.add("show", "active");

                    contentSection.id = `category-content-${category.categoryId}`;
                    contentSection.setAttribute("role", "tabpanel");
                    planContent.appendChild(contentSection);

                    if (index === 0) {
                        setTimeout(() => fetchPlansForCategory(category.categoryId, contentSection), 100);
                    }

                    tab.querySelector("a").addEventListener("click", function () {
                        setTimeout(() => fetchPlansForCategory(category.categoryId, contentSection), 100);
                    });
                });
            })
            .catch(error => console.error('Error fetching categories:', error));
    }

    function fetchPlansForCategory(categoryId, contentSection) {
        fetch(`${API_URL_PLANS}${categoryId}`)
            .then(response => response.json())
            .then(plans => {
                contentSection.innerHTML = ""; 

                const planRow = document.createElement("div");
                planRow.classList.add("row", "g-4");

                plans.forEach(plan => {
                    const planCard = document.createElement("div");
                    planCard.classList.add("col-md-4", "mb-4");

                    planCard.innerHTML = `
                        <div class="card p-4 shadow-sm border rounded-4 bg-white">
                            <div class="d-flex justify-content-between align-items-center">
                                <h2 class="fw-bold text-dark">₹${plan.price}</h2>
                                <span class="fs-4 text-warning"><i class="fa-solid fa-certificate"></i></span>
                            </div>

                            <hr class="border-secondary opacity-50">

                            <div class="mb-3">
                                <p class="mb-2 text-muted"><i class="fa-solid fa-calendar-alt me-2 text-black"></i> ${plan.validity} Days</p>
                                <p class="mb-2 text-muted"><i class="fa-solid fa-wifi me-2 text-black"></i> ${plan.data}</p>
                                <p class="mb-2 text-muted"><i class="fa-solid fa-phone me-2 text-black"></i> ${plan.calls} Calls</p>
                                <p class="mb-2 text-muted"><i class="fa-solid fa-comment-dots me-2 text-black"></i> ${plan.message} Messages</p>
                            </div>

                            <button onclick="buyPlan('${plan.planId}', '${plan.tag}', ${plan.price}, ${plan.validity})" 
                                class="btn text-white w-100 py-2 fw-bold rounded-5 mb-2" 
                                style="background: linear-gradient(135deg, #17a2b8, #007bff);">
                                Buy Now
                            </button>
                        </div>
                    `;
                    planRow.appendChild(planCard);
                });

                contentSection.appendChild(planRow);
            })
            .catch(error => console.error('Error fetching plans:', error));
    }

    fetchCategories();
});
