document.addEventListener("DOMContentLoaded", function () {
  fetchLocationsForDropdown();
  fetchEmployees();

  document
    .getElementById("createEmployeeForm")
    .addEventListener("submit", function (e) {
      e.preventDefault();
      const firstName = document.getElementById("firstName").value;
      const lastName = document.getElementById("lastName").value;
      const email = document.getElementById("email").value;
      const password = document.getElementById("password").value;
      const locationId = document.getElementById("locationId").value;

      fetch("/api/admin/employees", {
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
          fetchEmployees(); // Refresh the table
        })
        .catch((error) => {
          console.error("Error:", error);
        });
    });

  document
    .getElementById("locationFilter")
    .addEventListener("change", function () {
      const locationId = this.value;
      if (locationId) {
        fetchEmployees(locationId);
      } else {
        fetchEmployees();
      }
    });
});

function fetchLocationsForDropdown() {
  fetch("/api/admin/locations")
    .then((response) => response.json())
    .then((data) => {
      const locationDropdown = document.getElementById("locationId");
      const locationFilter = document.getElementById("locationFilter");
      locationDropdown.innerHTML = ""; // Clear existing options
      locationFilter.innerHTML =
        '<option value="" th:text="#{employee.filter.allLocations}"></option>'; // Add default option
      data.forEach((location) => {
        const option = `<option value="${location.id}">${location.name}</option>`;
        locationDropdown.innerHTML += option;
        locationFilter.innerHTML += option;
      });
    });
}

function fetchEmployees(locationId, fallbackAttempted = false) {
  let url = "/api/admin/employees";
  if (locationId) {
    url = `/api/admin/locations/${locationId}/employees`;
  }

  fetch(url)
    .then((response) => {
      if (response.ok) {
        return response.json();
      }

      // If server returns 405 (Method Not Allowed), try fallbacks once
      if (response.status === 405 && !fallbackAttempted) {
        // 1) Try fetching locations and use the first location's employees
        return fetch("/api/admin/locations")
          .then((r) => {
            if (!r.ok) {
              throw new Error("Could not load locations for fallback");
            }
            return r.json();
          })
          .then((locations) => {
            if (Array.isArray(locations) && locations.length > 0) {
              // Kick off fetching employees for the first location and stop this chain
              fetchEmployees(locations[0].id, true);
              return Promise.reject("fallback-initiated");
            }
            // 2) Try a more general endpoint as a second fallback
            return fetch("/api/employees").then((r2) => {
              if (!r2.ok) {
                return r2
                  .text()
                  .then((t) => {
                    throw new Error(`Fallback failed ${r2.status}: ${t}`);
                  });
              }
              return r2.json();
            });
          });
      }

      return response.text().then((text) => {
        throw new Error(`HTTP ${response.status}: ${text}`);
      });
    })
    .then((data) => {
      if (data === undefined || data === null) return; // nothing to render (fallback already initiated)

      const employees = Array.isArray(data)
        ? data
        : data?.content || data?.employees || [];

      const tableBody = document.getElementById("employeesTableBody");
      tableBody.innerHTML = ""; // Clear existing rows

      if (!Array.isArray(employees) || employees.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="6">No employees found</td></tr>';
        return;
      }

      employees.forEach((employee) => {
        const row = `<tr>
                            <td>${employee.id ?? ""}</td>
                            <td>${employee.firstName ?? ""}</td>
                            <td>${employee.lastName ?? ""}</td>
                            <td>${employee.email ?? ""}</td>
                            <td>${employee.role ?? ""}</td>
                            <td>${employee.locationId ?? (employee.location?.id ?? "")}</td>
                        </tr>`;
        tableBody.innerHTML += row;
      });
    })
    .catch((err) => {
      if (err === "fallback-initiated") return;
      console.error("Failed to load employees:", err);
      const tableBody = document.getElementById("employeesTableBody");
      if (tableBody) {
        tableBody.innerHTML = '<tr><td colspan="6">Error loading employees</td></tr>';
      }
    });
}
