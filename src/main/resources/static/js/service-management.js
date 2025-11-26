document.addEventListener("DOMContentLoaded", function () {
  fetchServices();

  document
    .getElementById("createServiceForm")
    .addEventListener("submit", function (e) {
      e.preventDefault();
      const name = document.getElementById("name").value;
      const price = document.getElementById("price").value;

      fetch("/api/employee/services", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ name: name, price: price }),
      })
        .then((response) => response.json())
        .then((data) => {
          console.log("Success:", data);
          fetchServices(); // Refresh the table
        })
        .catch((error) => {
          console.error("Error:", error);
        });
    });
});

function fetchServices() {
  fetch("/api/employee/services")
    .then((response) => response.json())
    .then((data) => {
      const tableBody = document.getElementById("servicesTableBody");
      tableBody.innerHTML = ""; // Clear existing rows
      data.forEach((service) => {
        const row = `<tr>
                            <td>${service.id}</td>
                            <td>${service.name}</td>
                            <td>${service.price}</td>
                        </tr>`;
        tableBody.innerHTML += row;
      });
    });
}
