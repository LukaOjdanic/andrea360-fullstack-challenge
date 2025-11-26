document.addEventListener("DOMContentLoaded", function () {
  fetchLocationsForDropdown();

  document
    .getElementById("createMemberForm")
    .addEventListener("submit", function (e) {
      e.preventDefault();
      const firstName = document.getElementById("firstName").value;
      const lastName = document.getElementById("lastName").value;
      const email = document.getElementById("email").value;
      const password = document.getElementById("password").value;
      const locationId = document.getElementById("locationId").value;

      fetch("/api/employee/members", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          firstName: firstName,
          lastName: lastName,
          email: email,
          password: password,
          locationId: locationId,
        }),
      })
        .then((response) => response.json())
        .then((data) => {
          console.log("Success:", data);
          alert("Member created successfully!");
          // Optionally, you can clear the form or redirect
        })
        .catch((error) => {
          console.error("Error:", error);
          alert("Error creating member.");
        });
    });
});

function fetchLocationsForDropdown() {
  // Assuming there's an endpoint to get all locations, similar to what was used for admin pages
  fetch("/api/public/locations") // Using a public endpoint for locations
    .then((response) => response.json())
    .then((data) => {
      const locationDropdown = document.getElementById("locationId");
      locationDropdown.innerHTML = ""; // Clear existing options
      data.forEach((location) => {
        const option = `<option value="${location.id}">${location.name}</option>`;
        locationDropdown.innerHTML += option;
      });
    });
}
