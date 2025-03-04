document.addEventListener("DOMContentLoaded", function () {
    const tabContainer = document.getElementById("plan-tabs");
    const scrollLeftBtn = document.getElementById("scrollLeft");
    const scrollRightBtn = document.getElementById("scrollRight");

    // Function to scroll left
    scrollLeftBtn.addEventListener("click", () => {
        tabContainer.scrollBy({ left: -200, behavior: "smooth" });
    });

    // Function to scroll right
    scrollRightBtn.addEventListener("click", () => {
        tabContainer.scrollBy({ left: 200, behavior: "smooth" });
    });
});





document.addEventListener("DOMContentLoaded", () => {
    fetch("http://localhost:3000/plans") // Replace with your JSON server URL
        .then(response => response.json())
        .then(data => renderPlans(data))
        .catch(error => console.error("Error fetching plans:", error));
});

function renderPlans(plans) {
    const tabsContainer = document.getElementById("plan-tabs");
    const contentContainer = document.getElementById("plan-content");
    tabsContainer.innerHTML = "";
    contentContainer.innerHTML = "";

    let firstCategory = true;

    // Loop through each category
    Object.entries(plans).forEach(([category, plans]) => {
        const categoryId = category.replace(/\s+/g, "-").toLowerCase(); // Unique ID for each tab

        // Create Tabs
        const tab = document.createElement("li");
        tab.classList.add("nav-item");
        tab.innerHTML = `
            <button class="nav-link text-black fw-medium  ${firstCategory ? "active" : ""}" id="${categoryId}-tab" data-bs-toggle="tab" data-bs-target="#${categoryId}" type="button" role="tab">
                 ${category}
            </button>
        `;
        tabsContainer.appendChild(tab);

        // Create Content Sections
        const section = document.createElement("div");
        section.classList.add("tab-pane", "fade");
        if (firstCategory) {
            section.classList.add("show", "active");
        }
        section.id = categoryId;
        section.setAttribute("role", "tabpanel");

        section.innerHTML = `
            <h4 class="fw-bold text-black"><i class="fa-solid fa-signal me-2"></i>${category}</h4>
            <div class="row g-4" id="${categoryId}-container">
            </div>
        `;
        contentContainer.appendChild(section);

        const categoryContainer = document.getElementById(`${categoryId}-container`);

        // Loop through plans inside category
        Object.entries(plans).forEach(([planId, plan]) => {
            const card = document.createElement("div");
            card.classList.add("col-md-4");

            card.innerHTML = `
                <div class="card p-4 shadow-sm border rounded-4 bg-white">
                    <div class="d-flex justify-content-between align-items-center">
                        <h2 class="fw-bold text-dark">${plan.title}</h2>
                        <span class="fs-4 text-warning"><i class="fa-solid fa-certificate"></i></span>
                    </div>

                    <hr class="border-secondary opacity-50">

                    <div class="mb-3">
                        <p class="mb-2 text-muted"><i class="fa-solid fa-calendar-alt me-2 text-primary"></i> ${plan.validity}</p>
                        <p class="mb-2 text-muted"><i class="fa-solid fa-wifi me-2 text-success"></i> ${plan.data}</p>
                        <p class="mb-2 text-muted"><i class="fa-solid fa-phone me-2 text-danger"></i> ${plan.calls}</p>
                        <p class="mb-2 text-muted"><i class="fa-solid fa-comment-dots me-2 text-info"></i> ${plan.sms}</p>
                    </div>

                    <!-- Buy Now Button (Triggers Toast) -->
                    <a href="#" class="btn text-white w-100 py-2 fw-bold rounded-5 mb-2" 
                        style="background: linear-gradient(135deg, #17a2b8, #007bff); border: none;" onclick="showToast()">
                        <i class="fa-solid fa-bolt"></i> Buy Now
                    </a>

                    <!-- More Details (Opens Modal) -->
                    <a href="#" class="text-center text-primary fw-bold d-block" data-bs-toggle="modal" data-bs-target="#planModal-${planId}">
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

            categoryContainer.appendChild(card);
        });

        firstCategory = false;
    });
}

// Toast Notification Function
function showToast() {
    const toastElement = document.getElementById("validationToast");
    const toast = new bootstrap.Toast(toastElement);
    toast.show();
}
