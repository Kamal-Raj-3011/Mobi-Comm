
document.addEventListener("DOMContentLoaded", () => {
    fetch("http://localhost:3000/plans")
        .then(response => response.json())
        .then(data => renderPlans(data))
        .catch(error => console.error("Error fetching plans:", error));
});

function renderPlans(plans) {
    const navContainer = document.getElementById("plan-scrollspy");
    const contentContainer = document.getElementById("plan-content");
    
    navContainer.innerHTML = "";
    contentContainer.innerHTML = "";

    // Loop through categories
    Object.entries(plans).forEach(([category, plans]) => {
        const categoryId = category.replace(/\s+/g, "-").toLowerCase();

        // Add category to Scrollspy Navigation
        const navItem = document.createElement("a");
        navItem.classList.add("nav-link", "text-dark", "fw-bold");
        navItem.href = `#${categoryId}`;
        navItem.innerHTML = `<i class="fa-solid fa-tags me-1"></i> ${category}`;
        navContainer.appendChild(navItem);

        // Create category section
        const section = document.createElement("div");
        section.classList.add("pt-5");
        section.id = categoryId;
        section.innerHTML = `<h4 class="fw-bold text-primary mb-3">${category}</h4><div class="row g-4"></div>`;
        contentContainer.appendChild(section);

        const categoryRow = section.querySelector(".row");

        // Loop through plans inside category
        Object.entries(plans).forEach(([planId, plan]) => {
            const card = document.createElement("div");
            card.classList.add("col-md-6", "col-lg-4");

            card.innerHTML = `
                <div class="card p-4 shadow-sm border rounded-4 bg-white">
                    <div class="d-flex justify-content-between align-items-center">
                        <h2 class="fw-bold text-dark">${plan.title}</h2>
                        <span class="fs-4 text-warning"><i class="fa-solid fa-certificate"></i></span>
                    </div>
                    <hr class="border-secondary opacity-50">
                    <p class="text-muted"><i class="fa-solid fa-calendar-alt me-2 text-primary"></i> ${plan.validity}</p>
                    <p class="text-muted"><i class="fa-solid fa-wifi me-2 text-success"></i> ${plan.data}</p>
                    <p class="text-muted"><i class="fa-solid fa-phone me-2 text-danger"></i> ${plan.calls}</p>
                    <p class="text-muted"><i class="fa-solid fa-comment-dots me-2 text-info"></i> ${plan.sms}</p>

                    <!-- Buy Now Button (Opens Modal) -->
                    <button class="btn text-white w-100 py-2 fw-bold rounded-5" 
                    style="background: #ffc600; border: none;" 
                    onclick="redirectToPayment('${planId}')">
                    <i class="fa-solid fa-bolt"></i> Buy Now
                    </button>
                    <!-- More Details (Opens Modal) -->
                    <a href="#" class="text-center mt-1 text-primary fw-bold d-block" data-bs-toggle="modal" data-bs-target="#planModal-${planId}">
                        More Details
                    </a>

                    <!-- Modal -->
                    <div class="modal fade" id="planModal-${planId}" tabindex="-1" aria-labelledby="planModalLabel-${planId}" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered">
                            <div class="modal-content border-0 shadow-lg rounded-4">
                                <div class="modal-header bg-warning text-dark">
                                    <h5 class="modal-title fw-bold"><i class="fa-solid fa-file-invoice-dollar me-2"></i> Plan Details</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body p-4">
                                    <!-- Plan Card -->
                                    <div class="card border-0 shadow-sm rounded-4 p-3">
                                        <div class="card-body">
                                            <h4 class="fw-bold text-dark">${plan.title} - ${plan.validity}</h4>
                                            <hr>
                                            <p class="mb-2 text-muted"><i class="fa-solid fa-wifi me-2 text-success"></i> ${plan.data}</p>
                                            <p class="mb-2 text-muted"><i class="fa-solid fa-phone me-2 text-danger"></i> ${plan.calls}</p>
                                            <p class="mb-2 text-muted"><i class="fa-solid fa-comment-dots me-2 text-info"></i> ${plan.sms}</p>
                                        </div>
                                    </div>

                                    <!-- Additional Benefits Section -->
                                    <div class="mt-3">
                                        <h6 class="fw-bold text-dark">Additional Benefits</h6>
                                        <ul class="list-group">
                                            ${plan.additional_benefits.map(benefit => `
                                                <li class="list-group-item"><i class="fa-solid fa-check-circle text-success me-2"></i> ${benefit}</li>
                                            `).join("")}
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            `;
            categoryRow.appendChild(card);
        });
    });
    
}

function redirectToPayment(planId) {
    window.location.href = `../../payment.html?plan=${planId}`;
}



