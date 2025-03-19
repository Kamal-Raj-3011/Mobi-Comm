// Your original code starts here (without changes)

// document.addEventListener("DOMContentLoaded", function () {
//     const tabContainer = document.getElementById("plan-tabs");
//     const scrollLeftBtn = document.getElementById("scrollLeft");
//     const scrollRightBtn = document.getElementById("scrollRight");

//     // Function to scroll left
//     scrollLeftBtn.addEventListener("click", () => {
//         tabContainer.scrollBy({ left: -200, behavior: "smooth" });
//     });

//     // Function to scroll right
//     scrollRightBtn.addEventListener("click", () => {
//         tabContainer.scrollBy({ left: 200, behavior: "smooth" });
//     });
// });



document.addEventListener("DOMContentLoaded", function () {
    // Your existing code for fetching categories and plans
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
                    // Create category tab
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

                    // Create content section for category
                    const contentSection = document.createElement("div");
                    contentSection.classList.add("tab-pane", "fade");

                    if (index === 0) {
                        contentSection.classList.add("show", "active");
                    }

                    contentSection.id = `category-content-${category.categoryId}`;
                    contentSection.setAttribute("role", "tabpanel");
                    planContent.appendChild(contentSection);

                    // Fetch and display plans for the first category by default
                    if (index === 0) {
                        setTimeout(() => fetchPlansForCategory(category.categoryId, contentSection), 100);
                    }

                    // Fetch plans when tab is clicked
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
                                    <p class="mb-2 text-muted"><i class="fa-solid fa-phone me-2 text-black"></i> ${plan.calls}</p>
                                    <p class="mb-2 text-muted"><i class="fa-solid fa-comment-dots me-2 text-black"></i> ${plan.message}</p>
                                </div>

                                <!-- Buy Now Button -->
                               <!-- Buy Now Button (Direct Redirect) -->
        <a href="recharge.html" class="btn text-white w-100 py-2 fw-bold rounded-5 mb-2 btn-buy-now" 
            style="background: linear-gradient(135deg, #17a2b8, #007bff);">
            <i class="fa-solid fa-bolt"></i> Buy Now
        </a>

                                <a href="#" class="text-center text-primary fw-bold d-block" data-bs-toggle="modal" data-bs-target="#planModal-${plan.planId}">
                                    More Details
                                </a>

                                <!-- Plan Modal -->
                                <div class="modal fade" id="planModal-${plan.planId}" tabindex="-1" aria-labelledby="planModalLabel-${plan.planId}" aria-hidden="true">
                                    <div class="modal-dialog modal-dialog-centered">
                                        <div class="modal-content border-0 shadow-lg rounded-4">
                                            <div class="modal-header bg-warning text-dark">
                                                <h5 class="modal-title fw-bold"><i class="fa-solid fa-file-invoice-dollar me-2"></i> Plan Details</h5>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                            </div>
                                            <div class="modal-body p-4">
                                                <div class="card border-0 shadow-sm rounded-4 p-3">
                                                    <div class="card-body">
                                                        <h4 class="fw-bold text-dark">${plan.tag} - ${plan.validity} Days</h4>
                                                        <hr>
                                                        <p class="mb-2 text-muted"><i class="fa-solid fa-wifi me-2 text-black"></i> ${plan.data}</p>
                                                        <p class="mb-2 text-muted"><i class="fa-solid fa-phone me-2 text-black"></i> ${plan.calls}</p>
                                                        <p class="mb-2 text-muted"><i class="fa-solid fa-comment-dots me-2 text-black"></i> ${plan.message}</p>
                                                    </div>
                                                    <div class="mt-3">
                                                    <h6 class="fw-bold text-dark">Additional Benefits</h6>
                                                    <ul class="list-group">
                                                        ${plan.additionalFeature ? plan.additionalFeature.split(',').map(benefit => `
                                                            <li class="list-group-item"><i class="fa-solid fa-check-circle text-success me-2"></i> ${benefit.trim()}</li>
                                                        `).join("") : '<li class="list-group-item text-muted">No Additional Benefits</li>'}
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        `;
                        planRow.appendChild(planCard);
                    });
                    contentSection.appendChild(planRow);
                })
                .catch(error => console.error('Error fetching plans:', error));
        }

    fetchCategories();
    setupBuyNowButtons();  // Setup the "Buy Now" buttons after page load
});
